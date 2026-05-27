'use client';

import React, { useState, useEffect } from 'react';
import { GoogleMap, useJsApiLoader, Marker, Polyline } from '@react-google-maps/api';
import { Search, MapPin, Navigation, AlertCircle } from 'lucide-react';

const containerStyle = {
  width: '100%',
  height: '600px'
};

const center = {
  lat: 4.4389, // Coordenadas por defecto (Ibagué, Colombia)
  lng: -75.2322
};

interface Trayecto {
  id: number;
  ubicacion: string;
  ordenParada: number;
  latitud: number;
  longitud: number;
  conductor: {
    nombres: string;
    apellidos: string;
  };
}

export default function RouteMap() {
  const [codigoRuta, setCodigoRuta] = useState('');
  const [trayectos, setTrayectos] = useState<Trayecto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY || ''
  });

  const buscarRuta = async () => {
    if (!codigoRuta) return;
    setLoading(true);
    setError('');
    try {
      // Nota: En un entorno real, aquí deberíamos obtener el token del auth context/localStorage
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/trayectos/ruta/${codigoRuta}`, {
        headers: {
          // 'Authorization': `Bearer ${token}` 
        }
      });
      
      if (!response.ok) throw new Error('Ruta no encontrada o error de conexión');
      
      const data = await response.json();
      setTrayectos(data.sort((a: Trayecto, b: Trayecto) => a.ordenParada - b.ordenParada));
    } catch (err: any) {
      setError(err.message);
      setTrayectos([]);
    } finally {
      setLoading(false);
    }
  };

  const path = trayectos
    .filter(t => t.latitud && t.longitud)
    .map(t => ({ lat: t.latitud, lng: t.longitud }));

  return (
    <div className="flex flex-col gap-6 p-6 max-w-6xl mx-auto">
      <header className="flex flex-col gap-2">
        <h1 className="text-3xl font-bold text-slate-900 flex items-center gap-2">
          <Navigation className="text-blue-600" />
          Sistema de Gestión de Rutas
        </h1>
        <p className="text-slate-500">Visualización secuencial de trayectos y conductores.</p>
      </header>

      <div className="bg-white p-4 rounded-xl shadow-sm border border-slate-200 flex gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 size-5" />
          <input
            type="text"
            placeholder="Ingrese el Código de Ruta (ej. RUTA-001)"
            className="w-full pl-10 pr-4 py-2 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
            value={codigoRuta}
            onChange={(e) => setCodigoRuta(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && buscarRuta()}
          />
        </div>
        <button
          onClick={buscarRuta}
          disabled={loading}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-medium transition-colors disabled:opacity-50"
        >
          {loading ? 'Buscando...' : 'Consultar'}
        </button>
      </div>

      {error && (
        <div className="bg-red-50 text-red-700 p-4 rounded-lg flex items-center gap-2 border border-red-100">
          <AlertCircle size={20} />
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 bg-white p-2 rounded-xl shadow-md border border-slate-200 overflow-hidden min-h-[600px]">
          {isLoaded ? (
            <GoogleMap
              mapContainerStyle={containerStyle}
              center={path.length > 0 ? path[0] : center}
              zoom={14}
            >
              {trayectos.map((t, index) => (
                t.latitud && t.longitud && (
                  <Marker
                    key={t.id}
                    position={{ lat: t.latitud, lng: t.longitud }}
                    label={(index + 1).toString()}
                    title={t.ubicacion}
                  />
                )
              ))}
              {path.length > 1 && (
                <Polyline
                  path={path}
                  options={{
                    strokeColor: "#2563eb",
                    strokeOpacity: 0.8,
                    strokeWeight: 4,
                  }}
                />
              )}
            </GoogleMap>
          ) : (
            <div className="w-full h-full flex items-center justify-center bg-slate-50 text-slate-400">
              Cargando Mapa...
            </div>
          )}
        </div>

        <div className="flex flex-col gap-4">
          <div className="bg-white p-6 rounded-xl shadow-md border border-slate-200">
            <h2 className="text-xl font-semibold mb-4 text-slate-800 border-bottom pb-2">Detalle de la Ruta</h2>
            {trayectos.length > 0 ? (
              <div className="space-y-6">
                {trayectos.map((t, index) => (
                  <div key={t.id} className="flex gap-4 relative">
                    {index !== trayectos.length - 1 && (
                      <div className="absolute left-3 top-7 bottom-0 w-0.5 bg-slate-200" />
                    )}
                    <div className={`z-10 size-6 rounded-full flex items-center justify-center text-xs font-bold text-white shadow-sm ${
                      index === 0 ? 'bg-green-500' : index === trayectos.length - 1 ? 'bg-red-500' : 'bg-blue-500'
                    }`}>
                      {index + 1}
                    </div>
                    <div>
                      <p className="font-medium text-slate-900">{t.ubicacion}</p>
                      <p className="text-sm text-slate-500 flex items-center gap-1 mt-1">
                        <MapPin size={14} /> 
                        {t.latitud ? `${t.latitud.toFixed(4)}, ${t.longitud.toFixed(4)}` : 'Pendiente de geocodificación'}
                      </p>
                      <p className="text-xs text-blue-600 font-semibold mt-2 uppercase tracking-wider">
                        Conductor: {t.conductor.nombres} {t.conductor.apellidos}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-slate-400 italic">No hay resultados para mostrar.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

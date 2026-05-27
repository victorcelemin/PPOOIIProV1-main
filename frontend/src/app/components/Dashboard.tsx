'use client';

import React, { useState, useEffect } from 'react';
import { GoogleMap, useJsApiLoader, Marker, Polyline, InfoWindow } from '@react-google-maps/api';
import { 
  Search, MapPin, Navigation, AlertCircle, 
  Car, User, Calendar, ShieldCheck, 
  LayoutDashboard, History, Settings, Bell,
  ChevronRight, ArrowRight, Info
} from 'lucide-react';

// --- Types ---
interface Conductor {
  idPersona: number;
  nombres: string;
  apellidos: string;
  estadoConductor: string;
}

interface Vehiculo {
  id: number;
  placa: string;
  marca: string;
  linea: string;
}

interface Trayecto {
  id: number;
  ubicacion: string;
  ordenParada: number;
  latitud: number;
  longitud: number;
  codigoRuta: string;
  conductor: Conductor;
  vehiculo: Vehiculo;
}

// --- Styles ---
const mapContainerStyle = {
  width: '100%',
  height: '100%'
};

const mapOptions = {
  disableDefaultUI: false,
  zoomControl: true,
  styles: [
    {
      featureType: "administrative",
      elementType: "geometry",
      stylers: [{ visibility: "off" }]
    }
  ]
};

export default function Dashboard() {
  const [codigoRuta, setCodigoRuta] = useState('');
  const [trayectos, setTrayectos] = useState<Trayecto[]>([]);
  const [excepciones, setExcepciones] = useState<Trayecto[]>([]);
  const [selectedPoint, setSelectedPoint] = useState<Trayecto | null>(null);
  const [view, setView] = useState<'map' | 'list' | 'exceptions'>('map');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY || ''
  });

  // Fetch initial data
  useEffect(() => {
    fetchExcepciones();
  }, []);

  const fetchExcepciones = async () => {
    try {
      const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/trayectos/excepciones`);
      if (res.ok) setExcepciones(await res.json());
    } catch (e) { console.error("Error fetching exceptions", e); }
  };

  const buscarRuta = async (codigo?: string) => {
    const query = codigo || codigoRuta;
    if (!query) return;
    setLoading(true);
    setError('');
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/trayectos/ruta/${query}`);
      if (!response.ok) throw new Error('Ruta no encontrada');
      const data = await response.json();
      setTrayectos(data.sort((a: Trayecto, b: Trayecto) => a.ordenParada - b.ordenParada));
      setView('map');
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

  const center = path.length > 0 ? path[0] : { lat: 4.4389, lng: -75.2322 };

  return (
    <div className="flex h-screen bg-slate-50 text-slate-900 font-sans overflow-hidden">
      
      {/* --- Sidebar --- */}
      <aside className="w-64 bg-white border-r border-slate-200 flex flex-col z-20 shadow-sm">
        <div className="p-6 flex items-center gap-3">
          <div className="bg-blue-600 p-2 rounded-lg text-white">
            <Navigation size={24} />
          </div>
          <span className="font-bold text-xl tracking-tight">RutaMaster</span>
        </div>

        <nav className="flex-1 px-4 space-y-1">
          <button 
            onClick={() => setView('map')}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all ${view === 'map' ? 'bg-blue-50 text-blue-600 font-semibold' : 'text-slate-500 hover:bg-slate-100'}`}
          >
            <LayoutDashboard size={20} /> Dashboard
          </button>
          <button 
            onClick={() => { setView('exceptions'); fetchExcepciones(); }}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all relative ${view === 'exceptions' ? 'bg-red-50 text-red-600 font-semibold' : 'text-slate-500 hover:bg-slate-100'}`}
          >
            <AlertCircle size={20} /> Excepciones
            {excepciones.length > 0 && (
              <span className="absolute right-4 top-1/2 -translate-y-1/2 bg-red-500 text-white text-[10px] px-1.5 py-0.5 rounded-full">
                {excepciones.length}
              </span>
            )}
          </button>
          <button className="w-full flex items-center gap-3 px-4 py-3 rounded-xl text-slate-500 hover:bg-slate-100 transition-all">
            <History size={20} /> Historial
          </button>
          <div className="pt-4 pb-2 px-4 text-[10px] font-bold text-slate-400 uppercase tracking-widest">Favoritos</div>
          {['RUTA-001', 'RUTA-IB-BOG'].map(r => (
            <button 
              key={r}
              onClick={() => buscarRuta(r)}
              className="w-full flex items-center justify-between px-4 py-2 text-sm text-slate-600 hover:bg-slate-50 rounded-lg group"
            >
              <div className="flex items-center gap-2">
                <div className="size-1.5 rounded-full bg-blue-400"></div> {r}
              </div>
              <ChevronRight size={14} className="opacity-0 group-hover:opacity-100 transition-opacity" />
            </button>
          ))}
        </nav>

        <div className="p-4 border-t border-slate-100">
          <div className="bg-slate-900 rounded-2xl p-4 text-white">
            <p className="text-xs text-slate-400">Usuario Activo</p>
            <p className="font-semibold truncate">Administrador Sistema</p>
            <button className="mt-3 text-xs text-blue-400 hover:text-blue-300 font-medium">Cerrar Sesión</button>
          </div>
        </div>
      </aside>

      {/* --- Main Content --- */}
      <main className="flex-1 flex flex-col relative overflow-hidden">
        
        {/* Header */}
        <header className="h-20 bg-white border-b border-slate-200 px-8 flex items-center justify-between">
          <div className="flex items-center gap-4 flex-1 max-w-xl">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
              <input 
                type="text" 
                placeholder="Buscar código de ruta..." 
                className="w-full pl-10 pr-4 py-2.5 bg-slate-100 border-none rounded-2xl focus:ring-2 focus:ring-blue-500 transition-all outline-none"
                value={codigoRuta}
                onChange={(e) => setCodigoRuta(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && buscarRuta()}
              />
            </div>
          </div>
          <div className="flex items-center gap-4">
            <button className="relative p-2 text-slate-500 hover:bg-slate-100 rounded-full transition-all">
              <Bell size={20} />
              <div className="absolute top-2 right-2 size-2 bg-red-500 rounded-full border-2 border-white"></div>
            </button>
            <div className="size-10 bg-gradient-to-tr from-blue-600 to-indigo-600 rounded-full flex items-center justify-center text-white font-bold shadow-md">
              AD
            </div>
          </div>
        </header>

        {/* View Content */}
        <div className="flex-1 p-8 overflow-y-auto">
          {view === 'map' && (
            <div className="h-full flex flex-col gap-6">
              
              {/* Stats Bar */}
              <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                {[
                  { label: 'Ruta Activa', val: trayectos[0]?.codigoRuta || '---', icon: Navigation, color: 'text-blue-600', bg: 'bg-blue-100' },
                  { label: 'Paradas', val: trayectos.length, icon: MapPin, color: 'text-green-600', bg: 'bg-green-100' },
                  { label: 'Vehículo', val: trayectos[0]?.vehiculo.placa || '---', icon: Car, color: 'text-indigo-600', bg: 'bg-indigo-100' },
                  { label: 'Alertas', val: excepciones.length, icon: AlertCircle, color: 'text-red-600', bg: 'bg-red-100' },
                ].map((s, i) => (
                  <div key={i} className="bg-white p-5 rounded-3xl shadow-sm border border-slate-200 flex items-center gap-4">
                    <div className={`${s.bg} p-3 rounded-2xl ${s.color}`}>
                      <s.icon size={24} />
                    </div>
                    <div>
                      <p className="text-xs text-slate-500 font-medium uppercase tracking-wider">{s.label}</p>
                      <p className="text-xl font-bold">{s.val}</p>
                    </div>
                  </div>
                ))}
              </div>

              {/* Map + Detail Area */}
              <div className="flex-1 grid grid-cols-1 lg:grid-cols-3 gap-8 min-h-[500px]">
                {/* Map */}
                <div className="lg:col-span-2 bg-white rounded-3xl shadow-xl border border-slate-200 overflow-hidden relative group">
                  {isLoaded ? (
                    <GoogleMap
                      mapContainerStyle={mapContainerStyle}
                      center={center}
                      zoom={14}
                      options={mapOptions}
                    >
                      {trayectos.map((t, idx) => (
                        t.latitud && (
                          <Marker
                            key={t.id}
                            position={{ lat: t.latitud, lng: t.longitud }}
                            onClick={() => setSelectedPoint(t)}
                            label={{ text: (idx + 1).toString(), color: 'white', fontWeight: 'bold' }}
                            icon={{
                              path: window.google.maps.SymbolPath.CIRCLE,
                              fillColor: idx === 0 ? '#10b981' : idx === trayectos.length -1 ? '#ef4444' : '#2563eb',
                              fillOpacity: 1,
                              strokeWeight: 2,
                              strokeColor: '#fff',
                              scale: 16,
                            }}
                          />
                        )
                      ))}
                      {path.length > 1 && (
                        <Polyline
                          path={path}
                          options={{
                            strokeColor: "#2563eb",
                            strokeOpacity: 0.6,
                            strokeWeight: 5,
                            geodesic: true,
                          }}
                        />
                      )}
                      {selectedPoint && (
                        <InfoWindow
                          position={{ lat: selectedPoint.latitud, lng: selectedPoint.longitud }}
                          onCloseClick={() => setSelectedPoint(null)}
                        >
                          <div className="p-2 min-w-[150px]">
                            <p className="font-bold text-slate-900 mb-1">{selectedPoint.ubicacion}</p>
                            <p className="text-xs text-slate-500">Parada #{selectedPoint.ordenParada + 1}</p>
                            <div className="mt-2 pt-2 border-t border-slate-100 flex items-center gap-2">
                              <User size={12} className="text-blue-500" />
                              <span className="text-[10px] font-semibold">{selectedPoint.conductor.nombres}</span>
                            </div>
                          </div>
                        </InfoWindow>
                      )}
                    </GoogleMap>
                  ) : <div className="w-full h-full bg-slate-100 animate-pulse flex items-center justify-center">Cargando Mapa...</div>}
                </div>

                {/* Vertical Timeline/Details */}
                <div className="bg-white rounded-3xl shadow-xl border border-slate-200 p-6 flex flex-col">
                  <h3 className="text-lg font-bold mb-6 flex items-center gap-2">
                    <History className="text-blue-600" size={20} /> Secuencia de Ruta
                  </h3>
                  
                  {trayectos.length > 0 ? (
                    <div className="flex-1 overflow-y-auto pr-2 space-y-2">
                      {trayectos.map((t, idx) => (
                        <div 
                          key={t.id} 
                          onClick={() => { setSelectedPoint(t); }}
                          className={`group p-4 rounded-2xl transition-all cursor-pointer border-2 ${selectedPoint?.id === t.id ? 'border-blue-500 bg-blue-50' : 'border-transparent hover:bg-slate-50'}`}
                        >
                          <div className="flex gap-4">
                            <div className="flex flex-col items-center">
                              <div className={`size-8 rounded-full flex items-center justify-center text-xs font-bold text-white shadow-md transition-transform group-hover:scale-110 ${
                                idx === 0 ? 'bg-green-500' : idx === trayectos.length - 1 ? 'bg-red-500' : 'bg-blue-600'
                              }`}>
                                {idx + 1}
                              </div>
                              {idx !== trayectos.length - 1 && <div className="flex-1 w-0.5 bg-slate-200 my-2"></div>}
                            </div>
                            <div className="flex-1 pb-4">
                              <p className="font-bold text-sm text-slate-800 line-clamp-1">{t.ubicacion}</p>
                              <div className="mt-2 flex items-center gap-3">
                                <span className="text-[10px] bg-slate-100 px-2 py-0.5 rounded-full font-bold text-slate-500 uppercase tracking-tighter">
                                  {t.vehiculo.placa}
                                </span>
                                <span className="text-[10px] text-blue-500 font-semibold italic">
                                  {t.conductor.nombres}
                                </span>
                              </div>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div className="flex-1 flex flex-col items-center justify-center text-center p-8 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
                      <div className="bg-white p-4 rounded-full shadow-sm mb-4">
                        <Navigation size={32} className="text-slate-300" />
                      </div>
                      <p className="text-slate-400 text-sm font-medium">No hay una ruta cargada</p>
                      <p className="text-[10px] text-slate-300 mt-1 uppercase">Usa el buscador superior</p>
                    </div>
                  )}

                  {trayectos.length > 0 && (
                    <div className="mt-6 pt-6 border-t border-slate-100">
                      <div className="flex items-center justify-between mb-4">
                        <span className="text-xs font-bold text-slate-400 uppercase tracking-widest">Estado General</span>
                        <div className="flex items-center gap-1 text-green-500 text-xs font-bold">
                          <ShieldCheck size={14} /> Operativo
                        </div>
                      </div>
                      <button className="w-full bg-slate-900 text-white py-3 rounded-2xl font-bold text-sm hover:bg-slate-800 transition-colors flex items-center justify-center gap-2 group">
                        Generar Reporte PDF <ArrowRight size={16} className="group-hover:translate-x-1 transition-transform" />
                      </button>
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}

          {view === 'exceptions' && (
            <div className="space-y-6 animate-in fade-in duration-500">
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-2xl font-bold text-slate-900 flex items-center gap-3">
                    <AlertCircle className="text-red-500" /> Monitor de Excepciones
                  </h2>
                  <p className="text-slate-500">Rutas con vehículos restringidos o conductores bloqueados.</p>
                </div>
                <button 
                  onClick={fetchExcepciones}
                  className="bg-white border border-slate-200 px-4 py-2 rounded-xl text-sm font-bold hover:bg-slate-50"
                >
                  Refrescar
                </button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {excepciones.length > 0 ? excepciones.map(e => (
                  <div key={e.id} className="bg-white p-6 rounded-3xl border border-red-100 shadow-lg shadow-red-500/5 hover:-translate-y-1 transition-all">
                    <div className="flex justify-between items-start mb-4">
                      <div className="bg-red-50 px-3 py-1 rounded-full text-[10px] font-black text-red-600 uppercase tracking-widest">
                        Alerta de Seguridad
                      </div>
                      <span className="text-xs font-bold text-slate-400">#{e.id}</span>
                    </div>
                    <h3 className="text-lg font-bold mb-2">{e.codigoRuta}</h3>
                    <p className="text-sm text-slate-600 mb-6 flex items-start gap-2">
                      <MapPin size={16} className="text-slate-400 shrink-0 mt-0.5" /> {e.ubicacion}
                    </p>
                    
                    <div className="space-y-3 bg-slate-50 p-4 rounded-2xl">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-2">
                          <Car size={14} className="text-slate-400" />
                          <span className="text-xs font-medium">{e.vehiculo.placa}</span>
                        </div>
                        <span className="text-[10px] font-bold text-red-500">DOC VENCIDO</span>
                      </div>
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-2">
                          <User size={14} className="text-slate-400" />
                          <span className="text-xs font-medium">{e.conductor.nombres}</span>
                        </div>
                        <span className="text-[10px] font-bold text-orange-500">ESTADO: {e.conductor.estadoConductor}</span>
                      </div>
                    </div>

                    <button className="w-full mt-6 flex items-center justify-center gap-2 py-3 bg-red-600 text-white rounded-2xl text-xs font-bold hover:bg-red-700 transition-colors">
                      <Info size={14} /> Gestionar Incidencia
                    </button>
                  </div>
                )) : (
                  <div className="col-span-full h-64 flex flex-col items-center justify-center bg-white rounded-3xl border-2 border-dashed border-slate-200 text-slate-400 font-medium italic">
                    <ShieldCheck size={48} className="text-green-200 mb-4" />
                    No se han detectado irregularidades en el sistema.
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      </main>

      {/* --- Error Toast --- */}
      {error && (
        <div className="fixed bottom-8 right-8 bg-slate-900 text-white px-6 py-4 rounded-2xl shadow-2xl flex items-center gap-4 animate-in slide-in-from-right duration-300 z-50">
          <div className="bg-red-500 p-2 rounded-xl">
            <AlertCircle size={20} />
          </div>
          <div>
            <p className="text-xs text-slate-400 font-bold uppercase tracking-widest">Error del Sistema</p>
            <p className="font-semibold">{error}</p>
          </div>
          <button onClick={() => setError('')} className="ml-4 text-slate-500 hover:text-white">×</button>
        </div>
      )}
    </div>
  );
}

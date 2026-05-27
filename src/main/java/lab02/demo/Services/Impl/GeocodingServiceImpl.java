package lab02.demo.Services.Impl;

import lab02.demo.Services.GeocodingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.List;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private final RestTemplate restTemplate;

    @Value("${google.maps.api.key:YOUR_API_KEY}")
    private String apiKey;

    public GeocodingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double[] obtenerCoordenadas(String ubicacion) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", ubicacion)
                .queryParam("key", apiKey)
                .toUriString();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "OK".equals(response.get("status"))) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                if (!results.isEmpty()) {
                    Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
                    Map<String, Object> location = (Map<String, Object>) geometry.get("location");
                    
                    // Manejar lat/lng que pueden venir como Double o Integer
                    double lat = Double.parseDouble(location.get("lat").toString());
                    double lng = Double.parseDouble(location.get("lng").toString());
                    
                    return new double[]{lat, lng};
                }
            }
        } catch (Exception e) {
            System.err.println("Error geocodificando: " + e.getMessage());
        }
        return null;
    }
}

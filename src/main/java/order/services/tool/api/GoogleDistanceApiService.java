package order.services.tool.api;

import static order.services.tool.utils.Constants.ACCEPT;
import static order.services.tool.utils.Constants.DESTINATIONS;
import static order.services.tool.utils.Constants.KEY;
import static order.services.tool.utils.Constants.ORIGINS;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import order.services.tool.config.ConnectionConfig;

@Service
public class GoogleDistanceApiService
{
    private RestTemplate restTemplate;
    private ConnectionConfig connectionConfig;

    public GoogleDistanceApiService(RestTemplate restTemplate, ConnectionConfig connectionConfig)
    {
        this.restTemplate = restTemplate;
        this.connectionConfig = connectionConfig;
    }
    
    public GoogleDistanceResponse connectToApi(String origin, String destination) throws IOException
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity< ? > requestEntity = new HttpEntity<>(headers);

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(connectionConfig.getUrl())
                .path(connectionConfig.getContextPath())
                .queryParam(ORIGINS, origin)
                .queryParam(DESTINATIONS, destination)
                .queryParam(KEY, connectionConfig.getKey());

        URI uri = builder.build(true).toUri();
        
        final ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        if (response.getStatusCode().equals(HttpStatus.OK))
        {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), GoogleDistanceResponse.class);
        }
        return null;
    }

}

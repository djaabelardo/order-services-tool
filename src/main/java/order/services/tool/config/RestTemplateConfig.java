package order.services.tool.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import order.services.tool.api.ClientErrorHandler;
import order.services.tool.api.GoogleDistanceApiService;

@Configuration
@ConfigurationProperties(prefix = "google-distance.api")
public class RestTemplateConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, ConnectionConfig connectionConfig)
    {
        LOGGER.debug("Configuring rest template");
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Integer.valueOf(connectionConfig.getConnectionTimeout()))
                .setReadTimeout(Integer.valueOf(connectionConfig.getReadTimeout()))
                .build();
        restTemplate.setErrorHandler(new ClientErrorHandler());
        return restTemplate;
    }

    @Bean
    @Autowired
    public GoogleDistanceApiService distanceApiService(RestTemplate restTemplate, ConnectionConfig connectionConfig)
    {
        return new GoogleDistanceApiService(restTemplate, connectionConfig);
    }

}

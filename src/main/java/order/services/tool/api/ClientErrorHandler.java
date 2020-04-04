package order.services.tool.api;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;

public class ClientErrorHandler implements ResponseErrorHandler
{
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException
    {
        return ((response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR)
                || (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR));
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException
    {

        if (HttpStatus.BAD_GATEWAY.equals(response.getStatusCode()) ||
                HttpStatus.SERVICE_UNAVAILABLE.equals(response.getStatusCode()) ||
                HttpStatus.GATEWAY_TIMEOUT.equals(response.getStatusCode()))
        {
            throw new ResourceAccessException("Unable to connect to Google Distance API.");
        }
    }
}

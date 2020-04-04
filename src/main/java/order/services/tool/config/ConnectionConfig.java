package order.services.tool.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google-distance.api")
public class ConnectionConfig
{
    private String url;
    private String contextPath;
    private String key;
    private String readTimeout;
    private String connectionTimeout;

    public String getReadTimeout()
    {
        return readTimeout;
    }

    public void setReadTimeout(String readTimeout)
    {
        this.readTimeout = readTimeout;
    }

    public String getConnectionTimeout()
    {
        return connectionTimeout;
    }

    public void setConnectionTimeout(String connectionTimeout)
    {
        this.connectionTimeout = connectionTimeout;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public void setContextPath(String contextPath)
    {
        this.contextPath = contextPath;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}

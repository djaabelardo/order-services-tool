package order.services.tool.exception;

public class ApiClientException extends RuntimeException
{
    private static final long serialVersionUID = 2074196847468820060L;

    public ApiClientException(String message)
    {
        super(message);
    }
}

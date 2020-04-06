package order.services.tool.exception;

public class DataNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = -1415222240548305646L;

    public DataNotFoundException(String message)
    {
        super(message);
    }
}

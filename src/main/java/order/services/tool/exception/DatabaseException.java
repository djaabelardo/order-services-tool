package order.services.tool.exception;

public class DatabaseException extends RuntimeException
{
    private static final long serialVersionUID = -6092340176648934755L;

    public DatabaseException(String message)
    {
        super(message);
    }

    public DatabaseException(String msg, Throwable t)
    {
        super(msg, t);
    }
}

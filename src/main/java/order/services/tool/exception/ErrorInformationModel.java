package order.services.tool.exception;

import java.io.Serializable;

public class ErrorInformationModel implements Serializable
{
    private static final long serialVersionUID = 8386986296077639326L;

    private String error;

    public ErrorInformationModel(String error)
    {
        this.setError(error);
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

}

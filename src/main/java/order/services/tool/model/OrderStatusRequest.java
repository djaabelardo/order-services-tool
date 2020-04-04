package order.services.tool.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class OrderStatusRequest implements Serializable {

    private static final long serialVersionUID = 5117325544924979724L;

    @NotNull
    private String status;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

}

package order.services.tool.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class OrderLocationRequest implements Serializable {

    private static final long serialVersionUID = 5117325544924979724L;

    @NotEmpty
    @Size(min=2, max=2)
    private List<String> origin;
    
    @NotEmpty
    @Size(min=2, max=2)
    private List<String> destination;

    public List<String> getOrigin()
    {
        return origin;
    }

    public void setOrigin(List<String> origin)
    {
        this.origin = origin;
    }

    public List<String> getDestination()
    {
        return destination;
    }

    public void setDestination(List<String> destination)
    {
        this.destination = destination;
    }

}

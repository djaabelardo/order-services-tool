package order.services.tool.api.model;

import java.io.Serializable;
import java.util.List;

public class GoogleDistanceResponse implements Serializable {

    private static final long serialVersionUID = 5117325544924979724L;
    
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<Row> rows;
    private String status;
    
    public List<String> getDestination_addresses()
    {
        return destination_addresses;
    }
    public void setDestination_addresses(List<String> destination_addresses)
    {
        this.destination_addresses = destination_addresses;
    }
    public List<String> getOrigin_addresses()
    {
        return origin_addresses;
    }
    public void setOrigin_addresses(List<String> origin_addresses)
    {
        this.origin_addresses = origin_addresses;
    }
    public List<Row> getRows()
    {
        return rows;
    }
    public void setRows(List<Row> rows)
    {
        this.rows = rows;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
}

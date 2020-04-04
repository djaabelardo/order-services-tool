package order.services.tool.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "TBL_ORDER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetail implements Serializable
{

    private static final long serialVersionUID = -7039713787520750000L;

    @Id
    private String id;

    @Column(name = "distance", nullable = false)
    private Integer distance;

    @Column(name = "status", nullable = false)
    private String status;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Integer getDistance()
    {
        return distance;
    }

    public void setDistance(Integer distance)
    {
        this.distance = distance;
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

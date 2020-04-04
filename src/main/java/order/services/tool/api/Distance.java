package order.services.tool.api;

import java.io.Serializable;

public class Distance implements Serializable {

    private static final long serialVersionUID = 5117325544924979724L;

    private String text;
    private Integer value;
    
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        this.text = text;
    }
    public Integer getValue()
    {
        return value;
    }
    public void setValue(Integer value)
    {
        this.value = value;
    }
}

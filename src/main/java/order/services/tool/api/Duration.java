package order.services.tool.api;

import java.io.Serializable;

public class Duration implements Serializable {

    private static final long serialVersionUID = 5190584668680054337L;
    
    private String text;
    private Long value;
    
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        this.text = text;
    }
    public Long getValue()
    {
        return value;
    }
    public void setValue(Long value)
    {
        this.value = value;
    }
}

package order.services.tool.api.model;

import java.io.Serializable;
import java.util.List;

public class Row implements Serializable {

    private static final long serialVersionUID = 5117325544924979724L;

    private List<Element> elements;

    public List<Element> getElements()
    {
        return elements;
    }

    public void setElements(List<Element> elements)
    {
        this.elements = elements;
    }

}

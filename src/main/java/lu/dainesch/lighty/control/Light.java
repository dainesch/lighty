package lu.dainesch.lighty.control;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Light implements Serializable {

    private int id;
    private String name;
    private int r;
    private int g;
    private int b;

    public Light() {
    }

    public Light(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "Light{" + "id=" + id + ", name=" + name + ", r=" + r + ", g=" + g + ", b=" + b + '}';
    }
    
    

}

import java.io.Serializable;

public class Message implements Serializable {

    String type;

    public void setType(String type) {
        this.type= type;
    }

    public String getType() {
        return this.type;
    }
}

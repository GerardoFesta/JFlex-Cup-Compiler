package tables.entries;

public class Param {
    String name;
    String type;
    boolean out;

    public Param(String name, String type, boolean out) {
        this.name = name;
        this.type = type;
        this.out = out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }
}

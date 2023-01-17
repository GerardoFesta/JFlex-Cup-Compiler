package tables.entries;

public class VarEntry extends TabEntry{
    private boolean out;
    private boolean declared;
    private boolean assigned;

    public VarEntry(String entrySpec, String entryType, String entryName, boolean out) {
        super(entrySpec, entryType, entryName);
        this.out = out;
        this.declared = false;
        this.assigned = false;
    }

    public void setDeclared(boolean bool){declared = bool;}
    public boolean isDeclared(){return declared;}

    public void setAssigned(boolean bool){assigned = bool;}
    public boolean isAssigned(){return assigned;}
    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }
}

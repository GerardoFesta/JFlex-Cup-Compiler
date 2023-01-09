package tables.entries;

public class VarEntry extends TabEntry{
    private boolean out;

    public VarEntry(String entrySpec, String entryType, String entryName, boolean out) {
        super(entrySpec, entryType, entryName);
        this.out = out;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }
}

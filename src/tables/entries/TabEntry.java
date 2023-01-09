package tables.entries;

public abstract class TabEntry {
    private String entrySpec; //è una variabile o un metodo
    private String entryType;//tipo effettivo della entry (int, bool ecc.)

    private String entryName; //identificativo


    public TabEntry(String entrySpec, String entryType, String entryName) {
        this.entrySpec = entrySpec;
        this.entryType = entryType;
        this.entryName = entryName;
    }

    public String getEntrySpec() {
        return entrySpec;
    }

    public void setEntrySpec(String entrySpec) {
        this.entrySpec = entrySpec;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}

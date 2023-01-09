package tables.stacktables;

import tables.entries.TabEntry;

import java.util.HashMap;

public class SymbolTable {
    HashMap<String, TabEntry> table;
    public String tempLabel;

    public SymbolTable(HashMap<String, TabEntry> table) {
        this.table = table;
    }

    public SymbolTable(String label) {
        this.tempLabel = label;
    }

    public SymbolTable() {
        table= new HashMap<String, TabEntry>();
    }

    public void add(String str, TabEntry entry){
        table.put(str, entry);
    }

    public TabEntry lookup(String str){
        return table.get(str);
    }

}

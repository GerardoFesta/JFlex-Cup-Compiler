package tables.stacktables;

import java.util.HashMap;

public class TablesContainer {
    HashMap<String, SymbolTable> tablemap;

    public TablesContainer(HashMap<String, SymbolTable> tablemap) {
        this.tablemap = tablemap;
    }

    public TablesContainer() {
        tablemap = new HashMap<String, SymbolTable>();
    }

    public void addTable(String key, SymbolTable symbolTable){
        tablemap.put(key, symbolTable);
    }

    public SymbolTable getTable(String key){
        return tablemap.get(key);
    }
}

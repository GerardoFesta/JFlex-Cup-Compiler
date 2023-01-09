package tables.entries;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class MethodEntry extends TabEntry{
    ArrayList<Param> parameters;
    boolean main;
    ArrayList<String> paramTypes;

    public MethodEntry(String entrySpec, String entryType, String entryName, ArrayList<Param> parameters, boolean main) {
        super(entrySpec, entryType, entryName);
        this.parameters = parameters;
        this.main = main;
        paramTypes = new ArrayList<String>();
        for(Param param:parameters){
            paramTypes.add(param.type);
        }

    }

    public MethodEntry(String entrySpec, String entryType, String entryName, boolean main) {
        super(entrySpec, entryType, entryName);
        this.main = main;
        this.parameters = new ArrayList<Param>();
        this.paramTypes = new ArrayList<String>();

    }

    public void addParameter(Param p){
        parameters.add(p);
        paramTypes.add(p.type);
    }
    public ArrayList<Param> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Param> parameters) {
        this.parameters = parameters;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}

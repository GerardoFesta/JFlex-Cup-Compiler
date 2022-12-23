package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class VarDecl extends Declaration {
    public VarDecl(Type t, ArrayList<IdInit> ai) {
        super("VarDecl");
        this.tipo=t;
        this.idInitList=ai;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }
    public void setTipo(Type tipo) {
        this.tipo = tipo;
    }
    public Type getType(){ return this.tipo; }
    public ArrayList<IdInit> getIdInitList() {
        return idInitList;
    }

    public void setIdInitList(ArrayList<IdInit> idInitList) {
        this.idInitList = idInitList;
    }

    Type tipo;
    ArrayList<IdInit> idInitList;
}

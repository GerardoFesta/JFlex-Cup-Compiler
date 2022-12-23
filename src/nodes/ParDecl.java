package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class ParDecl {
    public ParDecl(Type t, ArrayList<IDLeaf> ai, boolean out) {
     this.tipo=t;
     this.idList=ai;
     this.isOut=out;
    }

    public Type getTipo() {
        return tipo;
    }

    public void setTipo(Type tipo) {
        this.tipo = tipo;
    }

    public ArrayList<IDLeaf> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<IDLeaf> idList) {
        this.idList = idList;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    Type tipo;
    ArrayList<IDLeaf> idList;
    boolean isOut;
}

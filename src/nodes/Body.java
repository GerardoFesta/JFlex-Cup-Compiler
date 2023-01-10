package nodes;

import tables.stacktables.SymbolTable;
import visitors.Visitor;

import java.util.ArrayList;

public class Body {
    public Body(ArrayList<VarDecl> av, ArrayList<Stat> as) {
        this.varDeclList=av;
        this.statList=as;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    public ArrayList<VarDecl> getVarDeclList() {
        return varDeclList;
    }

    public void setVarDeclList(ArrayList<VarDecl> varDeclList) {
        this.varDeclList = varDeclList;
    }

    public ArrayList<Stat> getStatList() {
        return statList;
    }

    public void setStatList(ArrayList<Stat> statList) {
        this.statList = statList;
    }

    ArrayList<VarDecl> varDeclList;
    ArrayList<Stat> statList;

    SymbolTable symtable = null;

    public void setSymtable(SymbolTable table){symtable=table;}
    public SymbolTable getSymtable(){return symtable;}

}

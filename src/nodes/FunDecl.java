package nodes;

import tables.stacktables.SymbolTable;
import visitors.Visitor;

import java.util.ArrayList;

public class FunDecl extends Declaration {
    public FunDecl(IDLeaf id, ArrayList<ParDecl> ap, Type t, Body b) {
        super("FunDecl");
        this.id=id;
        this.parDeclList=ap;
        this.type=t;
        this.body=b;
        this.main=false;
    }

    public IDLeaf getId() {
        return id;
    }

    public void setId(IDLeaf id) {
        this.id = id;
    }

    public ArrayList<ParDecl> getParDeclList() {
        return parDeclList;
    }

    public void setParDeclList(ArrayList<ParDecl> parDeclList) {
        this.parDeclList = parDeclList;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    IDLeaf id;
    ArrayList<ParDecl> parDeclList;
    Type type;
    Body body;
    boolean main;

    SymbolTable symtable = null;


    public Object accept(Visitor v){
        return v.visit(this);
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public boolean isMain() {
        return main;
    }


    public void setSymtable(SymbolTable table){symtable=table;}
}

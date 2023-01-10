package nodes;

import tables.stacktables.SymbolTable;
import visitors.Visitor;

import java.util.ArrayList;

public class Program {
    public Program(ArrayList<Declaration> ad1, FunDecl mf, ArrayList<Declaration> ad2 ) {
        this.declList1=ad1;
        if(mf.isMain()) this.mainFunDecl=mf;
        this.declList2=ad2;
    }

    public ArrayList<Declaration> getDeclList1() {
        return declList1;
    }

    public void setDeclList1(ArrayList<Declaration> declList1) {
        this.declList1 = declList1;
    }

    public ArrayList<Declaration> getDeclList2() {
        return declList2;
    }

    public void setDeclList2(ArrayList<Declaration> declList2) {
        this.declList2 = declList2;
    }

    public FunDecl getMainFunDecl() {
        return mainFunDecl;
    }

    public void setMainFunDecl(FunDecl mainFunDecl) {
        this.mainFunDecl = mainFunDecl;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    ArrayList<Declaration> declList1;
    ArrayList<Declaration> declList2;
    FunDecl mainFunDecl;

    SymbolTable symtable = null;

    public void setSymtable(SymbolTable table){symtable=table;}
}

package nodes;

import tables.stacktables.SymbolTable;
import visitors.Visitor;

import java.util.ArrayList;

public class LetStat extends Stat{
    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public LetStat(VarDecl letDecl, Expr expr1, ArrayList<Stat> statList1, ArrayList<SingleWhen> whenList, ArrayList<Stat> otherStatList) {
        super("LetStat");
        this.letDecl = letDecl;
        this.expr1 = expr1;
        this.statList1 = statList1;
        this.whenList = whenList;
        this.otherStatList = otherStatList;
    }

    VarDecl letDecl;
    Expr expr1;
    ArrayList<Stat> statList1;
    ArrayList<SingleWhen> whenList;
    ArrayList<Stat> otherStatList;
    SymbolTable symtable = null;

    public VarDecl getLetDecl() {
        return letDecl;
    }

    public void setLetDecl(VarDecl letDecl) {
        this.letDecl = letDecl;
    }

    public Expr getExpr1() {
        return expr1;
    }

    public void setExpr1(Expr expr1) {
        this.expr1 = expr1;
    }

    public ArrayList<Stat> getStatList1() {
        return statList1;
    }

    public void setStatList1(ArrayList<Stat> statList1) {
        this.statList1 = statList1;
    }

    public ArrayList<SingleWhen> getWhenList() {
        return whenList;
    }

    public void setWhenList(ArrayList<SingleWhen> whenList) {
        this.whenList = whenList;
    }

    public ArrayList<Stat> getOtherStatList() {
        return otherStatList;
    }

    public void setOtherStatList(ArrayList<Stat> otherStatList) {
        this.otherStatList = otherStatList;
    }

    public void setSymtable(SymbolTable table){symtable=table;}
    public SymbolTable getSymtable(){return symtable;}
}

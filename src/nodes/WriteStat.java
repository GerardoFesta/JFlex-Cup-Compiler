package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class WriteStat extends Stat{
    public WriteStat(ArrayList<Expr> ae, int m) {
        super("WriteStat");
        this.exprList=ae;
        this.mode=m;
    }

    public ArrayList<Expr> getExprList() {
        return exprList;
    }

    public void setExprList(ArrayList<Expr> exprList) {
        this.exprList = exprList;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }
    ArrayList<Expr> exprList;
    int mode;
}

package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class FunCall extends Expr{
    public FunCall(IDLeaf id, ArrayList<Expr> ae) {
        super("FunCall");
        this.id=id;
        this.exprList=ae;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }
    IDLeaf id;
    ArrayList<Expr> exprList;

    public IDLeaf getId() {
        return id;
    }

    public void setId(IDLeaf id) {
        this.id = id;
    }

    public ArrayList<Expr> getExprList() {
        return exprList;
    }

    public void setExprList(ArrayList<Expr> exprList) {
        this.exprList = exprList;
    }
}

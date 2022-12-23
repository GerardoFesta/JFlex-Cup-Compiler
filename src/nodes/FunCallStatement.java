package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class FunCallStatement extends Stat{
    public FunCallStatement(FunCall f) {
        super("FunCallStatement");
        this.id = f.getId();
        this.exprList= f.getExprList();
    }

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

    public Object accept(Visitor v){
        return v.visit(this);
    }

    IDLeaf id;
    ArrayList<Expr> exprList;

}

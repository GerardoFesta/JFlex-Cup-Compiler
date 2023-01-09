package nodes;

import java_cup.runtime.SyntaxTreeDFS;
import visitors.Visitor;

import java.util.ArrayList;

public class AssignStat extends Stat{
    public AssignStat(ArrayList<IDLeaf> ai, ArrayList<Expr> ae) {
        super("AssignStat");
        this.idList=ai;
        this.exprList=ae;
    }

    public ArrayList<IDLeaf> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<IDLeaf> idList) {
        this.idList = idList;
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


    ArrayList<IDLeaf> idList;
    ArrayList<Expr> exprList;

}

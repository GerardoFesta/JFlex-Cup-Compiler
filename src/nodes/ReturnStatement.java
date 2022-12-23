package nodes;


import visitors.Visitor;

public class ReturnStatement extends Stat{
    public ReturnStatement(Expr e) {
        super("Return");
        this.expr=e;
    }
    public ReturnStatement() {
        super("EmptyReturn");
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    Expr expr;
}

package nodes;

import visitors.Visitor;

public class IfStat extends Stat{
    public IfStat(Expr e, Body b1, Body elseBody) {
        super("IfStat");
        this.expr=e;
        this.body=b1;
        this.elseBody=elseBody;

    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getElseBody() {
        return elseBody;
    }

    public void setElseBody(Body elseBody) {
        this.elseBody = elseBody;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    Expr expr;
    Body body, elseBody;
}

package nodes;

import visitors.Visitor;

public class WhileStat extends Stat{
    public WhileStat(Expr e, Body b) {
        super("WhileStat");
        this.expr=e;
        this.body=b;
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

    public Object accept(Visitor v){
        return v.visit(this);
    }

    Expr expr;
    Body body;
}

package nodes;

import visitors.Visitor;

public class IdInit {
    public IdInit(IDLeaf id) {
        this.id = id;
    }
    public IdInit(IDLeaf id, Expr e) {
        this.id = id;
        this.expr=e;
    }

    public IDLeaf getId() {
        return id;
    }

    public void setId(IDLeaf id) {
        this.id = id;
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

    IDLeaf id;
    Expr expr;
}

package nodes;

import visitors.Visitor;

public class UnaryOperation extends Expr{
    public UnaryOperation(String ot, Expr v) {
        super("UnaryOperation");
        this.opType=ot;
        this.value=v;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Expr getValue() {
        return value;
    }

    public void setValue(Expr value) {
        this.value = value;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    String opType;
    Expr value;
}

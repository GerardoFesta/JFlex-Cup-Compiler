package nodes;

import visitors.Visitor;

public class BinaryOperation extends Expr{
    public BinaryOperation(Expr v1, String ot, Expr v2) {
        super("BinaryOperation");
        this.opType=ot;
        this.value1=v1;
        this.value2=v2;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Expr getValue1() {
        return value1;
    }

    public void setValue1(Expr value1) {
        this.value1 = value1;
    }

    public Expr getValue2() {
        return value2;
    }

    public void setValue2(Expr value2) {
        this.value2 = value2;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    String opType;
    Expr value1, value2;
}

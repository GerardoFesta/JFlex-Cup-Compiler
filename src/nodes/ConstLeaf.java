package nodes;

import visitors.Visitor;

public class ConstLeaf extends Expr{
    public ConstLeaf(String ct, String v) {
        super("ConstLeaf");
        this.constType=ct;
        this.value=v;
    }

    public String getConstType() {
        return constType;
    }

    public void setConstType(String constType) {
        this.constType = constType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    String constType;
    String value;
}

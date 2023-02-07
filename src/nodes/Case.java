package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class Case {
    public ConstLeaf constLeaf;
    public ArrayList<Stat> statList;

    public String caseExpectedType;

    public String getCaseExpectedType() {
        return caseExpectedType;
    }

    public void setCaseExpectedType(String caseExpectedType) {
        this.caseExpectedType = caseExpectedType;
    }

    public ConstLeaf getConstLeaf() {
        return constLeaf;
    }

    public void setConstLeaf(ConstLeaf constLeaf) {
        this.constLeaf = constLeaf;
    }

    public ArrayList<Stat> getStatList() {
        return statList;
    }

    public void setStatList(ArrayList<Stat> statList) {
        this.statList = statList;
    }

    public Case(ConstLeaf constLeaf, ArrayList<Stat> statList) {
        this.constLeaf = constLeaf;
        this.statList = statList;
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }
}

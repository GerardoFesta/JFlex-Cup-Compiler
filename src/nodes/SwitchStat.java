package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class SwitchStat extends Stat{
    public IDLeaf switchArg;
    public ArrayList<Case> caseList;

    public IDLeaf getSwitchArg() {
        return switchArg;
    }

    public void setSwitchArg(IDLeaf switchArg) {
        this.switchArg = switchArg;
    }

    public ArrayList<Case> getCaseList() {
        return caseList;
    }

    public void setCaseList(ArrayList<Case> caseList) {
        this.caseList = caseList;
    }

    public SwitchStat(IDLeaf switchArg, ArrayList<Case> caseList) {
        super("SwitchStat");
        this.switchArg = switchArg;
        this.caseList = caseList;
    }

    public SwitchStat(String tipo) {
        super("SwitchStat");
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}

package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class ReadStat extends Stat{
    public ReadStat(ArrayList<IDLeaf> ai, String ic) {
        super("ReadStatWithText");
        this.idList=ai;
        this.str_con=ic;
    }
    public ReadStat(ArrayList<IDLeaf> ai) {
        super("ReadStat");
        this.idList=ai;
    }

    public ArrayList<IDLeaf> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<IDLeaf> idList) {
        this.idList = idList;
    }

    public String getStr_con() {
        return str_con;
    }

    public void setStr_con(String str_con) {
        this.str_con = str_con;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    ArrayList<IDLeaf> idList;
    String str_con;
}

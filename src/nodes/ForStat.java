package nodes;

import tables.stacktables.SymbolTable;
import visitors.Visitor;

public class ForStat extends Stat{
    public ForStat(IDLeaf id, String ic1, String ic2, Body b) {
        super("ForStat");
        this.id = id;
        this.int_con1=ic1;
        this.int_con2=ic2;
        this.body=b;
    }

    public IDLeaf getId() {
        return id;
    }

    public void setId(IDLeaf id) {
        this.id = id;
    }

    public String getInt_con1() {
        return int_con1;
    }

    public void setInt_con1(String int_con1) {
        this.int_con1 = int_con1;
    }

    public String getInt_con2() {
        return int_con2;
    }

    public void setInt_con2(String int_con2) {
        this.int_con2 = int_con2;
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

    IDLeaf id;
    String int_con1,int_con2;
    Body body;

    SymbolTable symtable = null;

    public void setSymtable(SymbolTable table){symtable=table;}
    public SymbolTable getSymtable(){return symtable;}

}

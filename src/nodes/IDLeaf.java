package nodes;

import visitors.Visitor;

public class IDLeaf extends Expr{
    public IDLeaf(String id) {
        super("IDLeaf");
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }



    String id;
}

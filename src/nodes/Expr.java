package nodes;

import visitors.Visitor;

public abstract class Expr extends Object {
    public Expr(String tipoexpr) {
        this.tipoexpr = tipoexpr;
    }

    public String getTipoexpr() {
        return tipoexpr;
    }

    public void setTipoexpr(String tipoexpr) {
        this.tipoexpr = tipoexpr;
    }
    public abstract Object accept(Visitor v);

    public void setType(String type){ this.type = type;}
    public String getType(){return type;}
    String tipoexpr;
    String type;
}

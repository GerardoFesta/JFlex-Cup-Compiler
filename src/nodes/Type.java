package nodes;

import visitors.Visitor;

public class Type {
    public Type(String t) {
        this.tipo=t;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    String tipo;
}

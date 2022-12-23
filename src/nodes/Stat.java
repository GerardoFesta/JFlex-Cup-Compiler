package nodes;

import visitors.Visitor;

public abstract class Stat {
    public Stat(String tipo) {
       this.tipo=tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public abstract Object accept(Visitor v);

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    String tipo;
}

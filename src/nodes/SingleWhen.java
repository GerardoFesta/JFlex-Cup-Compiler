package nodes;

import visitors.Visitor;

import java.util.ArrayList;

public class SingleWhen {
    public SingleWhen(Expr espressione, ArrayList<Stat> statList) {
        this.espressione = espressione;
        this.statList = statList;
    }

    public Expr getEspressione() {
        return espressione;
    }

    public void setEspressione(Expr espressione) {
        this.espressione = espressione;
    }

    public ArrayList<Stat> getStatList() {
        return statList;
    }

    public void setStatList(ArrayList<Stat> statList) {
        this.statList = statList;
    }

    Expr espressione;
    ArrayList<Stat> statList;
}

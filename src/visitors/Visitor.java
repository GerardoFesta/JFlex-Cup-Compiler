package visitors;

import nodes.*;

public interface Visitor {
    public Object visit(AssignStat nodo);
    public Object visit(BinaryOperation nodo);
    public Object visit(Body nodo);
    public Object visit(ConstLeaf nodo);

    public Object visit(ForStat nodo);
    public Object visit(FunCall nodo);
    public Object visit(FunCallStatement nodo);
    public Object visit(FunDecl nodo);
    public Object visit(IdInit nodo);
    public Object visit(IDLeaf nodo);
    public Object visit(IfStat nodo);
    public Object visit(ParDecl nodo);
    public Object visit(Program nodo);
    public Object visit(ReadStat nodo);
    public Object visit(ReturnStatement nodo);
    public Object visit(Type nodo);
    public Object visit(UnaryOperation nodo);
    public Object visit(VarDecl nodo);
    public Object visit(WhileStat nodo);
    public Object visit(WriteStat nodo);

    public Object visit(SwitchStat nodo);
    public Object visit(Case nodo);



}

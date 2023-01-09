package visitors;

import nodes.*;
import tables.entries.TabEntry;
import tables.entries.VarEntry;
import tables.stacktables.SymbolTable;
import tables.stacktables.TablesContainer;

import java.util.ArrayList;
import java.util.Stack;

public class ScopingVisitor implements Visitor{
    private int tableCounter = 0;
    Stack<SymbolTable> stack;
    TablesContainer tablesContainer;



    public SymbolTable enterScope(){
        tableCounter++;
        SymbolTable table = new SymbolTable(""+tableCounter);
        stack.push(table);
        return table;
    }

    public SymbolTable exitScope(){
        SymbolTable table = stack.pop();
        tablesContainer.addTable(table.tempLabel, table);
        return table;
    }

    public void addVarId(VarEntry entry){
        SymbolTable currentScope = stack.pop();

        if(currentScope.lookup(entry.getEntryName())!=null)
            throw new Error("Variabile " + entry.getEntryName() + " già dichiarata");
        else
            currentScope.add(entry.getEntryName(), entry);

        stack.push(currentScope);

    }

    @Override
    public Object visit(Program programNode) {
        enterScope();

        for(Declaration d: programNode.getDeclList1()){
            d.accept(this);
        }

        programNode.getMainFunDecl().accept(this);

        for(Declaration d: programNode.getDeclList2()){
            d.accept(this);
        }
        exitScope();

        //DA SOSTITUIRE CON SECONDA PASSATA
        return null;

    }

    @Override
    public Object visit(VarDecl nodo) {
        String tipo= (String) nodo.getType().accept(this);
        VarEntry entry= null;
        if(tipo.equals("var")){}//Non so se qui o dopo, penso dopo. se ho var x = a; e poi ho int a = 10, va fatta type inference?
        ArrayList<IdInit> idInitList= nodo.getIdInitList();
        //qui si potrebbe scendere a fare la visita di ogni idInit, ma il tipo come me lo tengo?
        for(IdInit idInit:idInitList) {
            String simbolo = (String) idInit.getId().accept(this);
            entry = new VarEntry("variable",tipo,simbolo,false);
            addVarId(entry);
        }

        //temporaneo
        return null;
    }

    @Override
    public Object visit(Type nodo) {return nodo.getTipo();}


    @Override
    public Object visit(IDLeaf nodo) {return nodo.getId();}


    @Override
    public Object visit(FunDecl nodo) {
        return null;
    }


    @Override
    public Object visit(AssignStat nodo) {
        return null;
    }

    @Override
    public Object visit(BinaryOperation nodo) {
        return null;
    }

    @Override
    public Object visit(Body nodo) {
        return null;
    }

    @Override
    public Object visit(ConstLeaf nodo) {
        return null;
    }

    @Override
    public Object visit(ForStat nodo) {
        return null;
    }

    @Override
    public Object visit(FunCall nodo) {
        return null;
    }

    @Override
    public Object visit(FunCallStatement nodo) {
        return null;
    }



    @Override
    public Object visit(IdInit nodo) {
        return null;
    }


    @Override
    public Object visit(IfStat nodo) {
        return null;
    }

    @Override
    public Object visit(ParDecl nodo) {
        return null;
    }



    @Override
    public Object visit(ReadStat nodo) {
        return null;
    }

    @Override
    public Object visit(ReturnStatement nodo) {
        return null;
    }


    @Override
    public Object visit(UnaryOperation nodo) {
        return null;
    }



    @Override
    public Object visit(WhileStat nodo) {
        return null;
    }

    @Override
    public Object visit(WriteStat nodo) {
        return null;
    }
}

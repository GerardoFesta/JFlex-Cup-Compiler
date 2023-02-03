package visitors;

import nodes.*;
import tables.entries.MethodEntry;
import tables.entries.Param;
import tables.entries.TabEntry;
import tables.entries.VarEntry;
import tables.stacktables.SymbolTable;
import tables.stacktables.TablesContainer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;
import tables.OperationRules;

public class ScopingVisitor implements Visitor{
    private int tableCounter = 0;
    Stack<SymbolTable> stack;
    TablesContainer tablesContainer;
    String savepath;


    public ScopingVisitor(String savepath) {
        this.stack = new Stack<SymbolTable>();
        tablesContainer = new TablesContainer();
        this.savepath=savepath;
    }

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

    public SymbolTable getCurrentSymTable(){
        SymbolTable currentScope = stack.pop();
        stack.push(currentScope);
        return currentScope;
    }

    public void addVarId(VarEntry entry){
        SymbolTable currentScope = stack.pop();

        if(currentScope.lookup(entry.getEntryName())!=null)
            throw new Error("Variable with name " + entry.getEntryName() + " already declared in the scope");
        else
            currentScope.add(entry.getEntryName(), entry);

        stack.push(currentScope);

    }

    public void addFunId(MethodEntry entry){
        SymbolTable currentScope = stack.pop();
        if(entry.getEntryName().equals("main"))
            throw new Error("Error, cannot name a function \"main\"");
        if(currentScope.lookup(entry.getEntryName())!=null)
            throw new Error("Function with name " + entry.getEntryName() + " was already declared");
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
        SymbolTable tabella_prodotta = exitScope();
        programNode.setSymtable(tabella_prodotta);

        TypeCheckerVisitor secondavisita = new TypeCheckerVisitor(savepath);
        secondavisita.visit(programNode);
        return null;

    }

    @Override
    public Object visit(VarDecl nodo) {
        String tipo= (String) nodo.getType().accept(this);
        VarEntry entry= null;

        ArrayList<IdInit> idInitList = nodo.getIdInitList();
        for (IdInit idInit : idInitList) {
            entry = (VarEntry) idInit.accept(this);
            if(!tipo.equals("var")) {
                entry.setEntryType(tipo);
            }
            addVarId(entry);
        }

        //temporaneo
        return null;
    }


    @Override
    public Object visit(Type nodo) {return nodo.getTipo();}

    @Override
    public Object visit(IdInit nodo) {
        String simbolo = (String) nodo.getId().accept(this);
        if(nodo.isObbl()){
            String tipo_const = (String) nodo.getExpr().accept(this); //sicuramente è una costante, perché il tipo è var
            return new VarEntry("variable", tipo_const, simbolo,false);
        }
        return new VarEntry("variable", "", simbolo,false);
    }

    @Override
    public Object visit(ConstLeaf nodo) {
        return OperationRules.getConstType(nodo.getConstType());
    }


    @Override
    public Object visit(IDLeaf nodo) {return nodo.getId();}


    @Override
    public Object visit(FunDecl nodo) {
        MethodEntry entry = null;
        ArrayList<Param> paramlist;
        String nome_f = (String) nodo.getId().accept(this);
        String tipo_f = (String) nodo.getType().accept(this);
        boolean isMain = nodo.isMain();
        ArrayList<ParDecl> pardeclList = nodo.getParDeclList();
        entry = new MethodEntry("fun", tipo_f, nome_f, isMain);
        for(ParDecl pardecl:pardeclList){
            paramlist = (ArrayList<Param>) pardecl.accept(this);
            for(Param parametro: paramlist){
                entry.addParameter(parametro);
            }

        }
        addFunId(entry);

        //creazione tabella della funzione, che poi passo a body e che ha i parametri all'interno
        enterScope();
        VarEntry varEntry= null;
        for(ParDecl pardecl:pardeclList){
            paramlist = (ArrayList<Param>) pardecl.accept(this);
            for(Param parametro:paramlist){
                varEntry=new VarEntry("variable", parametro.getType(), parametro.getName(), parametro.isOut());
                varEntry.setDeclared(true);
                varEntry.setAssigned(true);
                addVarId(varEntry);
            }
            //ATTENZIONE A GESTIONE OUT

        }
        Body body = nodo.getBody();
        body.setSymtable(getCurrentSymTable());
        SymbolTable symtab_aggiornata = (SymbolTable) body.accept(this);
        nodo.setSymtable(symtab_aggiornata);

        return null;
    }

    @Override
    public Object visit(ParDecl nodo) {
        String tipo = (String) nodo.getTipo().accept(this);
        ArrayList<IDLeaf> idlist= nodo.getIdList();
        Param parametro;
        ArrayList<Param> paramList = new ArrayList<Param>();
        for(IDLeaf id:idlist){
            String nome= (String) id.accept(this);
            id.setType(tipo);
            parametro = new Param(nome,tipo, nodo.isOut());
            paramList.add(parametro);
        }
        return paramList;
    }

    @Override
    public Object visit(Body nodo) {
        if(nodo.getSymtable()==null){
            enterScope();
        }
        ArrayList<VarDecl> varDecls= nodo.getVarDeclList();
        for (VarDecl decl:varDecls) {decl.accept(this);}
        ArrayList<Stat> stats= nodo.getStatList();
        for (Stat stat:stats){stat.accept(this);}


        SymbolTable bodySymTable = exitScope();
        nodo.setSymtable(bodySymTable);
        return bodySymTable;
    }

    @Override
    public Object visit(ForStat nodo) {
        String iteratore = (String) nodo.getId().accept(this);
        VarEntry entry = new VarEntry("variable", "int", iteratore, false);
        enterScope();
        addVarId(entry);
        SymbolTable currentSymTable = getCurrentSymTable();
        Body body = nodo.getBody();
        body.setSymtable(currentSymTable);
        SymbolTable symtab_aggiornata = (SymbolTable) body.accept(this);
        nodo.setSymtable(symtab_aggiornata);
        return null;
    }

    @Override
    public Object visit(IfStat nodo) {
        nodo.getBody().accept(this);
        if(nodo.getElseBody()!=null) nodo.getElseBody().accept(this);
        return null;
    }

    @Override
    public Object visit(WhileStat nodo) {
        nodo.getBody().accept(this);
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
    public Object visit(FunCall nodo) {
        return null;
    }

    @Override
    public Object visit(FunCallStatement nodo) {
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
    public Object visit(WriteStat nodo) {
        return null;
    }
}

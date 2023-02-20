package visitors;

import nodes.*;
import tables.OperationRules;
import tables.entries.MethodEntry;
import tables.entries.Param;
import tables.entries.TabEntry;
import tables.entries.VarEntry;
import tables.stacktables.SymbolTable;
import tables.stacktables.TablesContainer;

import java.util.ArrayList;
import java.util.Stack;

public class TypeCheckerVisitor implements Visitor{
    private int tableCounter = 0;
    Stack<SymbolTable> stack;
    TablesContainer tablesContainer;
    MethodEntry current_function;
    boolean function_has_returned;

    String savePath;


    public TypeCheckerVisitor(String savePath) {
        this.stack = new Stack<SymbolTable>();
        tablesContainer = new TablesContainer();
        this.savePath=savePath;
    }

    private void loadScope(SymbolTable tab){
        stack.push(tab);
    }

    private void exitScope(){
        stack.pop();
    }

    private TabEntry lookup (String simbolo){
        Stack<SymbolTable> clone = (Stack<SymbolTable>) stack.clone();
        SymbolTable tab;
        TabEntry result;
        while(!clone.empty()){
            tab = clone.pop();
            result = tab.lookup(simbolo);
            if(result!= null) return result;
        }
        throw new Error(""+simbolo+" has not been declared");

    }

    @Override
    public Object visit(Program nodo) {
        loadScope(nodo.getSymtable());
        ArrayList<Declaration> dichiarazioni1 = nodo.getDeclList1();
        for(Declaration d:dichiarazioni1){
            d.accept(this);
        }

        nodo.getMainFunDecl().accept(this);

        ArrayList<Declaration> dichiarazioni2 = nodo.getDeclList2();
        for(Declaration d:dichiarazioni2){
            d.accept(this);
        }


        CTranslatorVisitor cvisitor = new CTranslatorVisitor(savePath);
        cvisitor.visit(nodo);
        return null;
    }

    @Override
    public Object visit(VarDecl nodo) {
        ArrayList<IdInit> idInits = nodo.getIdInitList();
        //non ho bisogno di aggiungere tipo agli id, perché già fatto nella prima passata
        for(IdInit idInit:idInits){
            idInit.accept(this);
        }
        //se non ho errori nelle idinit, allora il nodo è ok
        return null;
    }

    @Override
    public Object visit(IdInit nodo) {
        Expr expr = nodo.getExpr();
        if(expr!=null) {
            String tipo_simbolo = (String) nodo.getId().accept(this);
            String tipo_expr = (String) expr.accept(this);
            if(tipo_simbolo.equals("float") && tipo_expr.equals("int"))
                tipo_expr="float";
            if(!tipo_expr.equals(tipo_simbolo))
                throw new Error("Type mismatch. Initializing "+nodo.getId().getId()+" with "+tipo_expr+" while expected "+tipo_simbolo);

        }

        return null;
    }

    //EXPR
    @Override
    public Object visit(IDLeaf nodo) {
        if(lookup(nodo.getId()).getEntrySpec().equals("fun")){
            throw new Error("Expected a variable but got a function: "+nodo.getId());
        }
        VarEntry entry = (VarEntry) lookup(nodo.getId());
        nodo.setType(entry.getEntryType());
        return entry.getEntryType();
    }

    @Override
    public Object visit(ConstLeaf nodo) {
        nodo.setType(OperationRules.getConstType(nodo.getConstType()));
        return OperationRules.getConstType(nodo.getConstType());
    }

    @Override
    public Object visit(BinaryOperation nodo) {
        String tipo1 = (String) nodo.getValue1().accept(this);
        String tipo2 = (String) nodo.getValue2().accept(this);
        if(OperationRules.getOpType(nodo.getOpType(), tipo1, tipo2) == null){
            throw new Error ("Cannot execute operation "+nodo.getOpType()+ " with types: "+tipo1+" and "+tipo2);
        }
        nodo.setType(OperationRules.getOpType(nodo.getOpType(), tipo1, tipo2));
        return OperationRules.getOpType(nodo.getOpType(), tipo1, tipo2);
    }

    @Override
    public Object visit(UnaryOperation nodo) {
        String tipo_expr = (String) nodo.getValue().accept(this);
        if(!nodo.getOpType().equals("par")) {
            if (OperationRules.getOpType(nodo.getOpType(), tipo_expr) == null) {
                throw new Error("Cannot execute operation " + nodo.getOpType() + " with type: " + tipo_expr);
            }
            nodo.setType(OperationRules.getOpType(nodo.getOpType(), tipo_expr));
            return OperationRules.getOpType(nodo.getOpType(), tipo_expr);
        }else{
            nodo.setType(tipo_expr);
            return tipo_expr;
        }

    }

    @Override
    public Object visit(FunCall nodo) {
        String nome_fun= nodo.getId().getId();
        TabEntry tab = lookup(nome_fun);
        if(!tab.getEntrySpec().equals("fun"))
            throw new Error(nome_fun+" is not a function");
        MethodEntry fun = (MethodEntry) tab;
        ArrayList<Param> formal_params = fun.getParameters();
        ArrayList<Expr> actual_params = nodo.getExprList();
        if(actual_params == null && formal_params.isEmpty()){
            nodo.setType(fun.getEntryType());
            return fun.getEntryType();
        }
        if(formal_params.size()!= actual_params.size())
            throw new Error(nome_fun+": expected "+ formal_params.size()+" parameters, got "+actual_params.size());

        int i=0;
        Param param;
        String tipo_param;
        Expr expr;
        String tipo_expr;
        while(i< formal_params.size()){
            param = formal_params.get(i);
            tipo_param = param.getType();
            expr = actual_params.get(i);
            tipo_expr = (String) expr.accept(this);
            if(param.isOut() && !expr.getTipoexpr().equals("IDLeaf"))
                throw new Error(nome_fun+": parameter "+param.getName()+" must be a variable");
            if(!tipo_expr.equals(tipo_param))
                //Posso passare un iintero a un float
                if(! (tipo_expr.equals("int") && tipo_param.equals("float")))
                    throw new Error(nome_fun+": parameter "+param.getName()+"-"+tipo_param+" cannot be used with "+tipo_expr);
            i++;
        }
        nodo.setType(fun.getEntryType());
        return fun.getEntryType();
    }
    //FINE EXPR



    @Override
    public Object visit(FunDecl nodo) {
        String nome_fun = nodo.getId().getId();
        current_function = (MethodEntry) lookup(nome_fun);
        function_has_returned = false;
        nodo.getBody().accept(this);
        if(!(function_has_returned || current_function.getEntryType().equals("void"))){
            throw new Error("Function "+nome_fun+" has not returned. Expected " +current_function.getEntryType()+ " return");
        }
        current_function = null;
        return null;
    }

    @Override
    public Object visit(Body nodo) {
        loadScope(nodo.getSymtable());
        ArrayList<VarDecl> varDecls = nodo.getVarDeclList();
        for (VarDecl varDecl:varDecls){
            varDecl.accept(this);
        }

        ArrayList<Stat> statlist = nodo.getStatList();
        for(Stat stat:statlist){
            stat.accept(this);
        }

        exitScope();
        return null;
    }

    @Override
    public Object visit(ReturnStatement nodo) {
        if(nodo.getExpr()!=null) {
            String tipo_ritorno = (String) nodo.getExpr().accept(this);
            if(current_function.getEntryType().equals("float") && tipo_ritorno.equals("int"))
                tipo_ritorno="float";
            if (!tipo_ritorno.equals(current_function.getEntryType()))
                throw new Error("Returned "+tipo_ritorno+" in function "+current_function.getEntryName()+" while "+current_function.getEntryType()+" was expected");
            function_has_returned = true;
        }else{
            if(!current_function.getEntryType().equals("void"))
                throw new Error("Returned void  in function "+current_function.getEntryName()+" while "+current_function.getEntryType()+" was expected");
        }
        return null;
    }


    @Override
    public Object visit(AssignStat nodo) {
        ArrayList<IDLeaf> idlist = nodo.getIdList();
        ArrayList<Expr> exprList = nodo.getExprList();
        if(idlist.size()!= exprList.size())
            throw new Error("Sizes of the assignments don't match");
        int i=0;
        TabEntry t;
        String tipo_expr;
        while(i< idlist.size()){
            t = lookup(idlist.get(i).getId());
            if(!t.getEntrySpec().equals("variable"))
                throw new Error(t.getEntryName()+" is not a variable");
            tipo_expr= (String) exprList.get(i).accept(this);
            if(t.getEntryType().equals("float") && tipo_expr.equals("int"))
                tipo_expr="float";
            if(!tipo_expr.equals(t.getEntryType()))
                throw new Error("Type mismatch in assignment. Cannot assign to "+t.getEntryName()+" of type "+t.getEntryType()+" an expression of type "+tipo_expr);
            i++;
        }
        return null;
    }

    @Override
    public Object visit(ForStat nodo) {
        return nodo.getBody().accept(this);
    }

    @Override
    public Object visit(IfStat nodo) {
        String tipo_expr = (String) nodo.getExpr().accept(this);
        if(!tipo_expr.equals("bool"))
            throw new Error("Expression in If statement must be a boolean. Instaed got "+tipo_expr);

        nodo.getBody().accept(this);
        if(nodo.getElseBody()!=null)
            nodo.getElseBody().accept(this);
        return null;
    }


    @Override
    public Object visit(WhileStat nodo) {
        String tipo_expr = (String) nodo.getExpr().accept(this);
        if(!tipo_expr.equals("bool"))
            throw new Error("Expression in While statement must be a boolean. Instaed got "+tipo_expr);

        nodo.getBody().accept(this);
        return null;
    }



    @Override
    public Object visit(FunCallStatement nodo) {
        String nome_fun= nodo.getId().getId();
        TabEntry tab = lookup(nome_fun);
        if(!tab.getEntrySpec().equals("fun"))
            throw new Error(nome_fun+" is not a function");
        MethodEntry fun = (MethodEntry) tab;
        ArrayList<Param> formal_params = fun.getParameters();
        ArrayList<Expr> actual_params = nodo.getExprList();
        if(actual_params == null && formal_params.isEmpty()){
            return null;
        }
        if(formal_params.size()!= actual_params.size())
            throw new Error(nome_fun+": expected "+ formal_params.size()+" parameters, got "+actual_params.size());

        int i=0;
        Param param;
        String tipo_param;
        Expr expr;
        String tipo_expr;
        while(i< formal_params.size()){
            param = formal_params.get(i);
            tipo_param = param.getType();
            expr = actual_params.get(i);
            tipo_expr = (String) expr.accept(this);
            if(tipo_expr.equals("int") && tipo_param.equals("float"))
                tipo_expr="float";
            if(param.isOut() && !expr.getTipoexpr().equals("IDLeaf"))
                throw new Error(nome_fun+": parameter "+param.getName()+" must be a variable, cannot be const");
            if(!tipo_expr.equals(tipo_param))
                throw new Error(nome_fun+": parameter "+param.getName()+"-"+tipo_param+" cannot be used with "+tipo_expr);
            i++;
        }

        return null;
    }







    @Override
    public Object visit(ParDecl nodo) {
        //Non dovrebbe esserci niente da fare, c'è tutto nella prima passata

        return null;
    }



    @Override
    public Object visit(ReadStat nodo) {
        //Controllo solo se questi id sono stati dichiarati
        for(IDLeaf id: nodo.getIdList())
            id.accept(this);
        return null;
    }



    @Override
    public Object visit(Type nodo) {
        return nodo.getTipo();
    }





    @Override
    public Object visit(WriteStat nodo) {
        //Controllo solo se queste expr sono corrette, e assegno anche i tipi
        ArrayList<Expr> exprList = nodo.getExprList();
        for(Expr e:exprList)
            e.accept(this);
        return null;
    }

    @Override
    public Object visit(LetStat nodo) {
        loadScope(nodo.getSymtable());
        VarDecl letdecl = nodo.getLetDecl();
        Expr expr1 = nodo.getExpr1();
        ArrayList<Stat> statList1 = nodo.getStatList1();
        ArrayList<SingleWhen> whenList = nodo.getWhenList();
        ArrayList<Stat> otherList = nodo.getOtherStatList();

        letdecl.accept(this);
        String tipo_primobool = (String) expr1.accept(this);
        if(!tipo_primobool.equals("bool")){throw  new Error ("tipo 1 must be bool");}
        for(Stat s1 : statList1){
            s1.accept(this);
        }
        if(whenList.isEmpty()){throw  new Error ("dotwhile  at least 1");}
        for(SingleWhen sw : whenList){
            if(sw != null ) {
                String tipo_bool = (String) sw.getEspressione().accept(this);
                if (!tipo_bool.equals("bool")) {
                    throw new Error("tipo  must be bool");
                }
                for (Stat s : sw.getStatList()) {
                    s.accept(this);
                }
            }
        }
        for(Stat s3 : otherList){
            s3.accept(this);
        }
        exitScope();

        return  null;
    }



}

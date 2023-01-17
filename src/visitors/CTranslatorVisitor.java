package visitors;

import nodes.*;
import tables.entries.MethodEntry;
import tables.entries.Param;
import tables.entries.TabEntry;
import tables.entries.VarEntry;
import tables.stacktables.SymbolTable;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class CTranslatorVisitor implements Visitor{
    PrintWriter writer;
    Stack<SymbolTable> stack;


    public CTranslatorVisitor(String nome_file){
        try{
            writer = new PrintWriter(nome_file, "UTF-8");
            printInitials();
            stack = new Stack<>();
        }catch(IOException e){
            throw new Error("Error opening the file");
        }
    }

    private void loadScope(SymbolTable tab){
        stack.push(tab);
    }

    private void exitScope(){
        stack.pop();
    }
    private TabEntry lookup (String simbolo) {
        Stack<SymbolTable> clone = (Stack<SymbolTable>) stack.clone();
        SymbolTable tab;
        TabEntry result = null;
        while (!clone.empty()) {
            tab = clone.pop();
            result = tab.lookup(simbolo);
            if (result != null) return result;
        }
        return result;
    }

    private void updateSymbolTable(TabEntry tabEntry){
        SymbolTable curr_sym_table = stack.pop();
        curr_sym_table.table.replace(tabEntry.getEntryName(), tabEntry);
        stack.push(curr_sym_table);
    }

    private String getTypeInC(String type) {
        if(type.equals("string")) return "char*";
        return type;
    }

    public void printInitials(){
        writer.println("#include <stdio.h> ");
        writer.println("#include <string.h>");
        writer.println("#include <stdlib.h>");
        writer.println("#include <math.h>");
        writer.println("#include <unistd.h>");

        writer.println("typedef enum { false, true } bool;");

        writer.println("char * str_concat(char* str1 , char* str2) {" +
                "   char *buffer = malloc(sizeof(char) * 9999999);" +
                "   *buffer = '\\0';" +
                "   strcat(buffer , str1);" +
                "   strcat(buffer , str2);" +
                "   return buffer; " +
                "}\n");
        writer.println("char * castIntToString(int num) {" +
                "   char *buffer = malloc(sizeof(char) * 9999999);" +
                "   *buffer = '\\0';" +
                "   sprintf(buffer , \"%d\" ,num);" +
                "   return buffer; " +
                "}\n");

        writer.println("char * castFloatToString(float num) {" +
                "   char *buffer = malloc(sizeof(char) * 1000000);" +
                "   *buffer = '\\0';" +
                "   sprintf(buffer , \"%f\" ,num);" +
                "   return buffer; " +
                "}\n");

        writer.println("char * castBoolToString(int num) {" +
                "   char *buffer = \"true\";" +
                "   if(num == 0) { buffer = \"false\"; }" +
                "   return buffer; " +
                "}\n");

        writer.println("int castStringToint(char* num) {" +
                " int ritorno;"+
                " sscanf (num,\"%d\",&ritorno);" +
                "   return ritorno; " +
                "}\n");

        writer.println("int castStringTofloat(char* num) {" +
                " float ritorno;"+
                " sscanf (num,\"%f\",&ritorno);" +
                "   return ritorno; " +
                "}\n");

        writer.println("int castStringTochar(char* num) {" +
                " char ritorno;"+
                " sscanf (num,\"%c\",&ritorno);" +
                "   return ritorno; " +
                "}\n");

        writer.println("int castStringTobool(char* num) {" +
                " bool ritorno;"+
                " sscanf (num,\"%d\",&ritorno);" +
                "   return ritorno; " +
                "}\n");

        writer.println("char* castStringTostring(char* num) {" +
                " return num; " +
                "}\n");

        writer.println("char * leggiStringa() { char *buffer = malloc(sizeof(char) * 1000); scanf(\"%s\" ,buffer);   return buffer; }");

    }


    public void printFunctionSignature(MethodEntry m){
        System.out.println("PRINTING SIG");
        writer.print("\n"+getTypeInC(m.getEntryType())+" "+m.getEntryName()+"(");
        String out="";
        int i=0;
        for(Param p:m.getParameters()){
            if(p.isOut())
                out="*";
            else
                out="";
            writer.print(getTypeInC(p.getType())+" "+out+p.getName());
            i=i+1;
            if(m.getParameters().size()-i>=1)
                writer.print(", ");
        }
        writer.print(");");
    }

    @Override
    public Object visit(Program nodo) {
        nodo.getSymtable().table.forEach((key, entry) -> {
            System.out.println("PRINTING");
            if(entry.getEntrySpec().equals("fun"))
                printFunctionSignature((MethodEntry) entry);
            else{
                writer.println("\n"+getTypeInC(entry.getEntryType())+" "+ entry.getEntryName()+";");
                VarEntry v_entry =(VarEntry)entry;
                v_entry.setDeclared(true);
            }
        });

        loadScope(nodo.getSymtable());

        ArrayList<Declaration> decls1 = nodo.getDeclList1();
        ArrayList<Declaration> decl_support = (ArrayList<Declaration>) decls1.clone();
        boolean result;
        int completati = 0;
        while(!(decl_support.size() ==completati)){
            for (Declaration d: decl_support) {
                result = (boolean) d.accept(this);
                if(result)
                    completati++;
            }

        }

        //STAMPA MAIN CON FUNZIONE DI START
        FunDecl mainFun = nodo.getMainFunDecl();
        MethodEntry mainEntry = (MethodEntry) lookup(mainFun.getId().getId());
        writer.println("\n\nint main(int argc, char* argv[]){");
        String tipo_param;
        String out="";
        int i = 0;
        for(Param p: mainEntry.getParameters()){
            tipo_param = getTypeInC(p.getType());
            writer.println("\t"+tipo_param+" t"+i+" = castStringTo"+tipo_param+"(argv["+i+"]);");
            i=i+1;
        }
        writer.print("\n\t"+mainEntry.getEntryName()+"(");
        i = 0;
        for(Param p: mainEntry.getParameters()){
            if(p.isOut())
                out="*";
            else
                out="";
            writer.print(out+"t"+i);
            i=i+1;
            if(mainEntry.getParameters().size()-i>=1)
                writer.print(", ");
        }
        writer.print(");");
        writer.println("\n}");


        //DECL LIST 2
        ArrayList<Declaration> decls2 = nodo.getDeclList2();
        decl_support = (ArrayList<Declaration>) decls2.clone();
        result = false;
        completati = 0;
        while(!(decl_support.size() ==completati)){
            for (Declaration d: decl_support) {
                result = (boolean) d.accept(this);
                if(result)
                    completati++;
            }

        }

        writer.close();

        return null;
    }

    @Override
    public Object visit(FunDecl nodo) {
        MethodEntry m = (MethodEntry) lookup(nodo.getId().getId());
        writer.print("\n"+getTypeInC(m.getEntryType())+" "+m.getEntryName()+"(");
        String out="";
        int i=0;
        for(Param p:m.getParameters()){
            if(p.isOut())
                out="*";
            else
                out="";
            writer.print(getTypeInC(p.getType())+" "+out+p.getName());
            i=i+1;
            if(m.getParameters().size()-i>=1)
                writer.print(", ");
        }
        writer.print(")");

        nodo.getBody().accept(this);
        return true;
    }


    @Override
    public Object visit(VarDecl nodo) {
        boolean terminata = true;
        boolean id_init_finito;
        for(IdInit idInit: nodo.getIdInitList()){
            id_init_finito = (boolean) idInit.accept(this);
            if(!id_init_finito)
                terminata = false;
        }
        return terminata;
    }

    @Override
    public Object visit(IdInit nodo) {
        VarEntry entry = (VarEntry) lookup(nodo.getId().getId());
        String prefisso_tipo="";
        if(entry.isAssigned())
            return true;
        if(nodo.getExpr() == null){
            if(!entry.isDeclared())
                writer.println(getTypeInC(entry.getEntryType())+" "+entry.getEntryName());
                entry.setDeclared(true);
                updateSymbolTable(entry);
            return true;
        }else{
            Expr expr= nodo.getExpr();
            if((boolean) expr.accept(this)){
                if(!entry.isDeclared())
                    prefisso_tipo=getTypeInC(entry.getEntryType());
                writer.println(prefisso_tipo+" "+entry.getEntryName()+" = ");
                printExpr(expr);
                writer.print(";");
                entry.setDeclared(true);
                entry.setAssigned(true);
                updateSymbolTable(entry);
                return true;
            }
        }
        return false;
    }

    private void printExpr(Expr e){
        if(e instanceof IDLeaf) {
            IDLeaf id = (IDLeaf) e;
            writer.print(" "+id.getId());
        } else if (e instanceof ConstLeaf) {
            ConstLeaf constleaf = (ConstLeaf) e;
            writer.print(constleaf.getValue());
        } else if (e instanceof UnaryOperation) {
            UnaryOperation unaryOperation = (UnaryOperation) e;
            String op="";
            if(unaryOperation.getOpType().equals("minus"))
                op="-";
            else if(unaryOperation.getOpType().equals("not"))
                op="!";
            else
                op="par";
            if(!op.equals("par")) {
                writer.print(op);
                printExpr(unaryOperation.getValue());
            }else{
                writer.print("(");
                printExpr(unaryOperation.getValue());
                writer.print(")");
            }
        } else if (e instanceof BinaryOperation) {
            BinaryOperation binaryOperation = (BinaryOperation) e;
            String op="";
            String tipo_op = binaryOperation.getOpType();
            String op_tradotta="";
            switch(tipo_op){
                case "plus": op_tradotta="+"; break;
                case "minus": op_tradotta="-"; break;
                case "times": op_tradotta="*"; break;
                case "div": op_tradotta="/"; break;
                case "pow": op_tradotta="pow"; break;
                case "str_concat": op_tradotta="str_concat"; break;
                case "or": op_tradotta="||"; break;
                case "and": op_tradotta="&&"; break;
                case "gt": op_tradotta=">"; break;
                case "ge": op_tradotta=">="; break;
                case "lt": op_tradotta="<"; break;
                case "le": op_tradotta="<="; break;
                case "eq": op_tradotta="=="; break;
                case "ne": op_tradotta="!="; break;
            }
            if(op_tradotta.equals("str_concat")){
                writer.print("str_concat(");
                printExpr(binaryOperation.getValue1());
                writer.print(", ");
                printExpr(binaryOperation.getValue2());
                writer.print(")");
            }else if(op_tradotta.equals("pow")){
                writer.print("pow(");
                printExpr(binaryOperation.getValue1());
                writer.print(", ");
                printExpr(binaryOperation.getValue2());
                writer.print(")");
            }else{
                printExpr(binaryOperation.getValue1());
                writer.print(op_tradotta);
                printExpr(binaryOperation.getValue2());
            }

        }else{ //funcall
            FunCall funcall= (FunCall) e;
            writer.print(funcall.getId().getId()+"(");
            MethodEntry firma = (MethodEntry) lookup(funcall.getId().getId());
            ArrayList<Param> parametri_formali = firma.getParameters();
            int i=0;
            for(Expr expr: funcall.getExprList()){
                if(parametri_formali.get(i).isOut())
                    writer.print("&");
                printExpr(expr);
                i++;
                if(parametri_formali.size()-i>=1)
                    writer.print(", ");
            }
            writer.print(")");
        }
    }


    @Override
    public Object visit(BinaryOperation nodo) {
        boolean b = (boolean) (nodo.getValue1().accept(this)) && (boolean)(nodo.getValue2().accept(this));
        return b;
    }

    @Override
    public Object visit(Body nodo) {
        loadScope(nodo.getSymtable());
        nodo.getVarDeclList();

        writer.print("{\n");
        ArrayList<VarDecl> decls = nodo.getVarDeclList();
        ArrayList<VarDecl> decl_support = (ArrayList<VarDecl>) decls.clone();
        boolean result;
        int completati = 0;
        while(!(decl_support.size() ==completati)){
            for (Declaration d: decl_support) {
                result = (boolean) d.accept(this);
                if(result)
                    completati++;
            }

        }
        for(Stat s: nodo.getStatList())
            s.accept(this);

        writer.print("\n}\n");
        exitScope();
        return null;
    }

    @Override
    public Object visit(ConstLeaf nodo) {
        return true;
    }

    @Override
    public Object visit(FunCall nodo) {
        return true;
    }
    @Override
    public Object visit(IDLeaf nodo) {
        VarEntry entry = (VarEntry) lookup(nodo.getId());
        return entry.isDeclared();
    }

    @Override
    public Object visit(Type nodo) {
        return getTypeInC(nodo.getTipo());
    }

    @Override
    public Object visit(UnaryOperation nodo) {
        return nodo.accept(this);
    }



    @Override
    public Object visit(AssignStat nodo) {
        ArrayList<IDLeaf> idlist = nodo.getIdList();
        ArrayList<Expr> exprList = nodo.getExprList();
        int i=0;
        IDLeaf id;
        Expr expr;
        while(i<idlist.size()){
            id=idlist.get(i);
            expr=exprList.get(i);
            id.accept(this);
            writer.println("\n");
            printExpr(id);
            writer.println(" = ");
            expr.accept(this);
            printExpr(expr);
            writer.println(";");
            i++;
        }
        return true;
    }

    @Override
    public Object visit(ForStat nodo) {
        IDLeaf id = nodo.getId();
        writer.print("\nfor(int "+id.getId()+" = "+nodo.getInt_con1()+"; "+id.getId()+" < "+nodo.getInt_con2()+"; "+id.getId()+"++)");
        nodo.getBody().accept(this);
        return true;
    }


    @Override
    public Object visit(IfStat nodo) {

        Expr e = nodo.getExpr();
        writer.print("\nif(");
        e.accept(this);
        printExpr(e);
        writer.write(")");
        nodo.getBody().accept(this);
        if(nodo.getElseBody()!=null){
            writer.write("else");
            nodo.getElseBody().accept(this);
        }

        return true;
    }

    @Override
    public Object visit(WhileStat nodo) {
        Expr e = nodo.getExpr();
        e.accept(this);
        writer.print("\nwhile(");
        printExpr(e);
        writer.print(")");
        nodo.getBody().accept(this);
        return true;
    }

    @Override
    public Object visit(FunCallStatement nodo) {
        writer.write("\n"+nodo.getId().getId()+"(");
        MethodEntry firma = (MethodEntry) lookup(nodo.getId().getId());
        ArrayList<Param> parametri_formali = firma.getParameters();
        int i=0;
        for(Expr expr: nodo.getExprList()){
            if(parametri_formali.get(i).isOut())
                writer.print("&");
            printExpr(expr);
            i++;
            if(parametri_formali.size()-i>=1)
                writer.print(", ");
        }
        writer.print(")");

        return true;
    }


    @Override
    public Object visit(ReadStat nodo) {

        if(nodo.getStr_con()!=null)
            writer.write("\nprintf(\"%s\", "+nodo.getStr_con());
        ArrayList<IDLeaf> ids = nodo.getIdList();
        String tipo_id="";
        String tipo_read="";
        for(IDLeaf id:ids){
            tipo_id = id.getType();
            if(tipo_id.equals("string")){
               writer.print("\n"+id.getId()+" = leggiStringa();");
            }else{
                switch(tipo_id){
                    case "int":tipo_read="%d"; break;
                    case "float":tipo_read="%f"; break;
                    case "boolean":tipo_read="%d"; break;
                }
                writer.print("\nscanf("+tipo_read+", "+id.getId()+");");
            }
        }
        return true;
    }

    @Override
    public Object visit(ReturnStatement nodo) {
        writer.print("\nreturn ");
        if(nodo.getExpr()!=null)
            printExpr(nodo.getExpr());
        writer.print(";");
        return true;
    }



    @Override
    public Object visit(WriteStat nodo) {
        ArrayList<Expr> exprlist = nodo.getExprList();
        String tipo_expr="";
        String tipo_print="";
        for(Expr e:exprlist){
            tipo_expr= e.getType();
            switch(tipo_expr){
                case "int":tipo_print=tipo_print+"%d"; break;
                case "float":tipo_print=tipo_print+"%f"; break;
                case "string":tipo_print=tipo_print+"%s"; break;
                case "boolean":tipo_print=tipo_print+"%s"; break;
            }
        }
        int i = 0;
        for(Expr e:exprlist){
            writer.print("\nprintf(\""+tipo_print+"\", ");
            if(!e.getType().equals("bool"))
                printExpr(e);
            else
                writer.print("castBoolToString(");
                printExpr(e);
                writer.print(")");
            i++;
            if(exprlist.size()-i>1)
                writer.write(", ");
        }
        writer.print(");");
        return true;
    }



    @Override
    public Object visit(ParDecl nodo) {
        return null;
    }
}

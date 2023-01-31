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
    String tabulation="";

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
        writer.println("char * castintToString(int num) {" +
                "   char *buffer = malloc(sizeof(char) * 9999999);" +
                "   *buffer = '\\0';" +
                "   sprintf(buffer , \"%d\" ,num);" +
                "   return buffer; " +
                "}\n");

        writer.println("char * castfloatToString(float num) {" +
                "   char *buffer = malloc(sizeof(char) * 1000000);" +
                "   *buffer = '\\0';" +
                "   sprintf(buffer , \"%f\" ,num);" +
                "   return buffer; " +
                "}\n");

        writer.println("char * castboolToString(int num) {" +
                "   char *buffer = \"true\";" +
                "   if(num == 0) { buffer = \"false\"; }" +
                "   return buffer; " +
                "}\n");

        writer.println("int castStringToint(char* num) {" +
                " int ritorno;"+
                " sscanf (num,\"%d\",&ritorno);" +
                "   return ritorno; " +
                "}\n");

        writer.println("float castStringTofloat(char* num) {" +
                " float ritorno;"+
                " sscanf (num,\"%f\",&ritorno);" +
                "   return ritorno; " +
                "}\n");

        writer.println("char castStringTochar(char* num) {" +
                " char ritorno;"+
                " sscanf (num,\"%c\",&ritorno);" +
                "   return ritorno; " +
                "}\n");

        writer.println("bool castStringTobool(bool* b, char*str) {" +
                " if(strcmp(str,\"1\")==0 || strcmp(str,\"true\")==0){"+
                "   *b = true;"+
                "   return 1;"+
                "}"+
                "if(strcmp(str,\"0\")==0 || strcmp(str,\"false\")==0){"+
                "   *b = false;"+
                "   return 0;"+
                "}"+
                "return -1;"+
                "}");

        writer.println("char* caststringTostring(char* num) {" +
                " return num; " +
                "}\n");

        writer.println("char * leggiStringa() { char *buffer = malloc(sizeof(char) * 1000); scanf(\"%s\" ,buffer);   return buffer; }");
        writer.println("bool leggibool(bool* b){"+
            "char* str = leggiStringa();"+
            "if(strcmp(str,\"1\")==0 || strcmp(str,\"true\")==0){"+
             "   *b = true;"+
             "   return 1;"+
            "}"+
            "if(strcmp(str,\"0\")==0 || strcmp(str,\"false\")==0){"+
                "*b = false;"+
                "return 0;"+
            "}"+
            "return -1;"+
        "}");
    }


    public void printFunctionSignature(MethodEntry m){
        //System.out.println("PRINTING SIG");
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
        //System.out.println("Entrato in"+ nodo.getClass());
        nodo.getSymtable().table.forEach((key, entry) -> {
            //System.out.println("PRINTING");
            if(entry.getEntrySpec().equals("fun"))
                printFunctionSignature((MethodEntry) entry);
        });

        loadScope(nodo.getSymtable());

        ArrayList<Declaration> decls1 = nodo.getDeclList1();
        ArrayList<Declaration> decls2 = nodo.getDeclList2();
        ArrayList<Declaration>decl_support = new ArrayList<Declaration>();
        boolean result = false;
        ArrayList<VarDecl> globalvardecl = new ArrayList<>();

        //STAMPA VARIABILI GLOBALI
        decls1.forEach(d -> {
            if(d instanceof VarDecl)
                globalvardecl.add((VarDecl) d);
        });
        decls2.forEach(d -> {
            if(d instanceof VarDecl)
                globalvardecl.add((VarDecl) d);

        });

        globalvardecl.forEach(varDecl -> {
            if(decls1.contains(varDecl))
                decls1.remove(varDecl);
            if(decls2.contains(varDecl))
                decls2.remove(varDecl);
        });

        globalvardecl.forEach(d -> d.accept(this));

        //STAMPA FUNZIONI E VARIABILI
        while(!(decl_support.size() == decls1.size())){
            for (Declaration d: decls1) {
                if(!decl_support.contains(d)){
                    result = (boolean) d.accept(this);
                    if(result)
                        decl_support.add(d);

                }
            }

        }

        //STAMPA MAIN CON FUNZIONE DI START
        FunDecl mainFun = nodo.getMainFunDecl();
        MethodEntry mainEntry = (MethodEntry) lookup(mainFun.getId().getId());
        writer.println("\n\nint main(int argc, char* argv[]){");
        String tipo_param;
        String out="";
        int i = 1;
        for(Param p: mainEntry.getParameters()){
            if(p.getType().equals("bool")){
                tipo_param = getTypeInC(p.getType());
                writer.print("\n\tbool"+" t"+ i+";");
                writer.println("\n\t" + "int" + " ts" + i + " = castStringTobool(&t"+i+", argv[" + i + "]);");
                writer.println("\t"+"if(ts"+i+"==-1){");
                writer.println("\t\t printf(\"%s\",\"Errore, il parametro booleano dato in input non è ben formattato\");");
                writer.println("\t\t return 1;");
                writer.println("\t}");
            }else if(!p.getType().equals("string")) {
                tipo_param = getTypeInC(p.getType());
                writer.println("\t" + tipo_param + " t" + i + " = castStringTo" + tipo_param + "(argv[" + i + "]);");
            }else{
                writer.println("\tchar *t" + i + " = argv[" + i + "];");
            }
            i=i+1;
        }
        writer.print("\n\t"+mainEntry.getEntryName()+"(");
        i = 1;
        for(Param p: mainEntry.getParameters()){
            if(p.isOut())
                out="&";
            else
                out="";
            writer.print(out+"t"+i);
            i=i+1;
            if(mainEntry.getParameters().size()-i>=1)
                writer.print(", ");
        }
        writer.print(");");
        writer.println("\n}");
        mainFun.accept(this);


        //DECL LIST 2
        decl_support = new ArrayList<Declaration>();
        result = false;

        while(!(decl_support.size() == decls2.size())){
            for (Declaration d: decls2) {
                if(!decl_support.contains(d)){
                    result = (boolean) d.accept(this);
                    if(result)
                        decl_support.add(d);

                }
            }

        }

        writer.close();

        return null;
    }

    @Override
    public Object visit(FunDecl nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());
        MethodEntry m = (MethodEntry) lookup(nodo.getId().getId());
        writer.print("\n\n"+getTypeInC(m.getEntryType())+" "+m.getEntryName()+"(");
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
        //System.out.println("Entrato in"+ nodo.getClass());
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
        //System.out.println("Entrato in"+ nodo.getClass());
        VarEntry entry = (VarEntry) lookup(nodo.getId().getId());
        String prefisso_tipo="";
        if(entry.isAssigned())
            return true;
        if(nodo.getExpr() == null){
            if(!entry.isDeclared())
                writer.print("\n"+tabulation+getTypeInC(entry.getEntryType())+" "+entry.getEntryName()+";");
                entry.setDeclared(true);
                updateSymbolTable(entry);
            return true;
        }else{
            Expr expr= nodo.getExpr();
            if((boolean) expr.accept(this)){
                if(!entry.isDeclared())
                    prefisso_tipo=getTypeInC(entry.getEntryType());
                writer.print("\n"+tabulation+prefisso_tipo+" "+entry.getEntryName()+" = ");
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
        //System.out.println("Entrato in PrintExpr");

        if(e instanceof IDLeaf) {
            IDLeaf id = (IDLeaf) e;
            VarEntry entry = (VarEntry) lookup(id.getId());
            String out="";
            if(entry.isOut())
                out="*";
            writer.print(out+id.getId());
        } else if (e instanceof ConstLeaf) {
            ConstLeaf constleaf = (ConstLeaf) e;
            if(constleaf.getConstType().equals("string_const"))
                writer.print("\""+constleaf.getValue()+"\"");
            else
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
                String tipo_expr1 = binaryOperation.getValue1().getType();
                String extraPar="";
                if(!tipo_expr1.equals("string")){
                    writer.print("str_concat("+"cast"+tipo_expr1+"ToString(");
                    extraPar=")";
                }else
                    writer.print("str_concat(");
                printExpr(binaryOperation.getValue1());
                writer.print(extraPar+", ");
                extraPar="";
                String tipo_expr2 = binaryOperation.getValue2().getType();
                if(!tipo_expr2.equals("string")) {
                    writer.print("cast" + tipo_expr2 + "ToString(");
                    extraPar = ")";
                }
                printExpr(binaryOperation.getValue2());
                writer.print(extraPar+")");
            }else if(op_tradotta.equals("pow")){
                writer.print("pow(");
                printExpr(binaryOperation.getValue1());
                writer.print(", ");
                printExpr(binaryOperation.getValue2());
                writer.print(")");
            }else if(op_tradotta.equals("==") && binaryOperation.getValue1().getType().equals("string")){
                writer.print("strcmp(");
                printExpr(binaryOperation.getValue1());
                writer.print(", ");
                printExpr(binaryOperation.getValue2());
                writer.print(")==0");
            } else{
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
            if(funcall.getExprList()!=null) { //serve perché se è null non ci sono params
                for (Expr expr : funcall.getExprList()) {
                    if (parametri_formali.get(i).isOut())
                        writer.print("&");
                    printExpr(expr);
                    i++;
                    if (parametri_formali.size() - i >= 1)
                        writer.print(", ");
                }
            }
            writer.print(")");
        }
    }


    @Override
    public Object visit(BinaryOperation nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());
        boolean b = (boolean) (nodo.getValue1().accept(this)) && (boolean)(nodo.getValue2().accept(this));
        return b;
    }

    @Override
    public Object visit(Body nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());

        loadScope(nodo.getSymtable());
        nodo.getVarDeclList();

        writer.print("{\n");
        tabulation=tabulation+"\t";
        ArrayList<VarDecl> vardecls = nodo.getVarDeclList();
        ArrayList<VarDecl> decl_support = new ArrayList<VarDecl>();
        boolean result = false;

        while(!(decl_support.size() == vardecls.size())){
            for (VarDecl d: vardecls) {
                if(!decl_support.contains(d)){
                    result = (boolean) d.accept(this);
                    if(result)
                        decl_support.add(d);

                }
            }

        }
        for(Stat s: nodo.getStatList())
            s.accept(this);

        exitScope();
        tabulation = tabulation.substring(0,tabulation.length()-1);
        writer.print("\n"+tabulation+"}");

        return null;
    }

    @Override
    public Object visit(ConstLeaf nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());
        return true;
    }

    @Override
    public Object visit(FunCall nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());

        return true;
    }
    @Override
    public Object visit(IDLeaf nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());
        VarEntry entry = (VarEntry) lookup(nodo.getId());
        return entry.isDeclared();
    }

    @Override
    public Object visit(Type nodo) {

        //System.out.println("Entrato in"+ nodo.getClass());
        return getTypeInC(nodo.getTipo());
    }

    @Override
    public Object visit(UnaryOperation nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());
        return nodo.getValue().accept(this);
    }



    @Override
    public Object visit(AssignStat nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());

        ArrayList<IDLeaf> idlist = nodo.getIdList();
        ArrayList<Expr> exprList = nodo.getExprList();
        int i=0;
        IDLeaf id;
        Expr expr;
        while(i<idlist.size()){
            id=idlist.get(i);
            expr=exprList.get(i);
            id.accept(this);
            writer.print("\n"+tabulation);
            printExpr(id);
            writer.print(" = ");
            expr.accept(this);
            printExpr(expr);
            writer.print(";");
            i++;
        }
        return true;
    }

    @Override
    public Object visit(ForStat nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());

        IDLeaf id = nodo.getId();
        int val1 = Integer.parseInt(nodo.getInt_con1());
        int val2 = Integer.parseInt(nodo.getInt_con2());
        if(val1<=val2)
            writer.print("\n"+tabulation+"for(int "+id.getId()+" = "+nodo.getInt_con1()+"; "+id.getId()+" <= "+nodo.getInt_con2()+"; "+id.getId()+"++)");
        else
            writer.print("\n"+tabulation+"for(int "+id.getId()+" = "+nodo.getInt_con1()+"; "+id.getId()+" >= "+nodo.getInt_con2()+"; "+id.getId()+"--)");
        nodo.getBody().accept(this);
        return true;
    }


    @Override
    public Object visit(IfStat nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());


        Expr e = nodo.getExpr();
        writer.print("\n"+tabulation+"if(");
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
        //System.out.println("Entrato in"+ nodo.getClass());

        Expr e = nodo.getExpr();
        e.accept(this);
        writer.print("\n"+tabulation+"while(");
        printExpr(e);
        writer.print(")");
        nodo.getBody().accept(this);
        return true;
    }

    @Override
    public Object visit(FunCallStatement nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());

        writer.write("\n"+tabulation+nodo.getId().getId()+"(");
        MethodEntry firma = (MethodEntry) lookup(nodo.getId().getId());
        ArrayList<Param> parametri_formali = firma.getParameters();
        int i=0;
        if(nodo.getExprList()!=null) {
            for (Expr expr : nodo.getExprList()) {
                if (parametri_formali.get(i).isOut())
                    writer.print("&");
                printExpr(expr);
                i++;
                if (parametri_formali.size() - i >= 1)
                    writer.print(", ");
            }
        }
        writer.print(");");

        return true;
    }


    @Override
    public Object visit(ReadStat nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());


        if(nodo.getStr_con()!=null)
            writer.write("\n"+tabulation+"printf(\"%s\", \""+nodo.getStr_con()+"\");");
        ArrayList<IDLeaf> ids = nodo.getIdList();
        String tipo_id="";
        String tipo_read="";
        String out="";
        for(IDLeaf id:ids){
            tipo_id = id.getType();
            if(tipo_id.equals("string")){
               writer.print("\n"+tabulation);
               printExpr(id);
               writer.print(" = leggiStringa();");
            }else if(tipo_id.equals("bool")) {
                writer.print("\n"+tabulation+"if(leggibool(&");
                printExpr(id);
                writer.print(")==-1){");
                writer.print("\n"+tabulation+"\tprintf(\"%s\",\"Errore lettura booleano (inserisci true/1, false/0)\");");
                writer.print("\n"+tabulation+"\treturn 1;");
                writer.print("\n"+tabulation+"}");
            }else{

                switch(tipo_id){
                    case "int":tipo_read="%d"; break;
                    case "float":tipo_read="%f"; break;
                }
                writer.print("\n"+tabulation+"scanf(\""+tipo_read+"\", &");
                printExpr(id);
                writer.print(");");
            }
        }
        return true;
    }

    @Override
    public Object visit(ReturnStatement nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());

        writer.print("\n"+tabulation+"return ");
        if(nodo.getExpr()!=null)
            printExpr(nodo.getExpr());
        writer.print(";");
        return true;
    }



    @Override
    public Object visit(WriteStat nodo) {
        //System.out.println("Entrato in "+ nodo.getClass());

        ArrayList<Expr> exprlist = nodo.getExprList();
        String tipo_expr="";
        String tipo_print="";
        for(Expr e:exprlist){
            tipo_expr= e.getType();
            switch(tipo_expr){
                case "int":tipo_print=tipo_print+"%d"; break;
                case "float":tipo_print=tipo_print+"%f"; break;
                case "string":tipo_print=tipo_print+"%s"; break;
                case "bool":tipo_print=tipo_print+"%s"; break;
            }
        }
        if(nodo.getMode()==1)
            tipo_print+="\\n";
        int i = 0;
        writer.print("\n"+tabulation+"printf(\""+tipo_print+"\", ");
        for(Expr e:exprlist){

            if(!e.getType().equals("bool"))
                    printExpr(e);
                else {
                    writer.print("castboolToString(");
                    printExpr(e);
                    writer.print(")");
                }
            i++;
            if(exprlist.size()-i>=1)
                writer.write(", ");
        }
        writer.print(");");

        return true;
    }



    @Override
    public Object visit(ParDecl nodo) {
        //System.out.println("Entrato in"+ nodo.getClass());

        return null;
    }
}

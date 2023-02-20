/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


/**

*/

package de.jflex.example;
import esercitazione5.sym;
import java_cup.runtime.*;
import java.util.HashMap;

%% //OPTIONS AND DECLARATIONS

%public
%class Lexer
%cup
%line
%column

%unicode

%{

    StringBuffer string = new StringBuffer();
    StringBuilder stringBuilder = new StringBuilder();

        private Symbol symbol(int type) {
            return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private static HashMap<String, Symbol> stringTable = new HashMap<>();
    private Symbol installID(String lessema){

        if(stringTable.containsKey(lessema)){
             Symbol letto = stringTable.get(lessema);
             Symbol nuovo = new Symbol(letto.sym , letto.value);
             return nuovo;
        } else {
            Symbol s = symbol(sym.ID,lessema);
            stringTable.put(lessema, s);
            return s;
        }

    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

ID = [:jletter:] [:jletterdigit:]*
Digit = [0-9]
NoZeroDigit = [1-9]
INTEGER_CONST = ({NoZeroDigit}{Digit}*) | 0
REAL_CONST = {INTEGER_CONST}\.{Digit}+
CHAR_CONST = \'[^"'"]\'




%init{
    stringTable = new HashMap<>();

    stringTable.put("let", symbol(sym.LET));
    stringTable.put("godotwhen", symbol(sym.GODOTWHEN));
    stringTable.put("dotloop", symbol(sym.DOTLOOP));
    stringTable.put("dotwhen", symbol(sym.DOTWHEN));
    stringTable.put("dototherwisedotdo", symbol(sym.OTHERWISEDO));

    stringTable.put("if", symbol(sym.IF));   // inserimento delle parole chiavi nella stringTable per evitare di scrivere un diagramma di transizione per ciascuna di esse (le parole chiavi verranno "catturate" dal diagramma di transizione e gestite e di conseguenza). IF poteva anche essere associato ad una costante numerica
    stringTable.put("var", symbol(sym.VAR));
    stringTable.put("else", symbol(sym.ELSE));
    stringTable.put("while", symbol(sym.WHILE));
    stringTable.put("integer", symbol(sym.INT));
    stringTable.put("float", symbol(sym.FLOAT));
    stringTable.put("string", symbol(sym.STRING));
    stringTable.put("boolean", symbol(sym.BOOL));
    stringTable.put("char", symbol(sym.CHAR));
    stringTable.put("void", symbol(sym.VOID));
    stringTable.put("def", symbol(sym.DEF));
    stringTable.put("out", symbol(sym.OUT));
    stringTable.put("for", symbol(sym.FOR));
    stringTable.put("to", symbol(sym.TO));
    stringTable.put("loop", symbol(sym.LOOP));
    stringTable.put("true", symbol(sym.TRUE));
    stringTable.put("false", symbol(sym.FALSE));
    stringTable.put("and", symbol(sym.AND));
    stringTable.put("or", symbol(sym.OR));
    stringTable.put("not", symbol(sym.NOT));
    stringTable.put("return", symbol(sym.RETURN));
    stringTable.put("then", symbol(sym.THEN));


%init}

%state InLineCOMMENT
%state MultiLineCOMMENT
%state STR_CONST


%% //Lexical Rules
<YYINITIAL>{
    {WhiteSpace}        {}
    "("                 { return symbol(sym.LPAR); }
    ")"                 { return symbol(sym.RPAR); }
    "{"                 { return symbol(sym.LBRAC); }
    "}"                 { return symbol(sym.RBRAC); }
    ":"                 { return symbol(sym.COLON); }
    ","                 { return symbol(sym.COMMA); }
    ";"                 { return symbol(sym.SEMI); }
    "|"                 { return symbol(sym.PIPE); }
    "<--"                 { return symbol(sym.READ); }
    "-->"                 { return symbol(sym.WRITE); }
    "-->!"                 { return symbol(sym.WRITELN); }
    "<"                 { return symbol(sym.LT); }
    ">"                 { return symbol(sym.GT); }
    "<>"             { return symbol(sym.NE); }
    "!="             { return symbol(sym.NE); }
    "<="                { return symbol(sym.LE); }
    ">="                { return symbol(sym.GE); }
    "="                 { return symbol(sym.EQ); }
    "<<"                  {return symbol(sym.ASSIGN);}
    "+"                  {return symbol(sym.PLUS);}
    "-"                  {return symbol(sym.MINUS);}
    "*"                {return symbol(sym.TIMES);}
    "/"                  {return symbol(sym.DIV);}
    "^"                  {return symbol(sym.POW);}
    "&"                  {return symbol(sym.STR_CONCAT);}
    "start:"           {return symbol(sym.MAIN);}
     "\."                {return symbol(sym.DOT);}


    {INTEGER_CONST}         {return symbol(sym.INTEGER_CONST, yytext());}
    {REAL_CONST}         {return symbol(sym.REAL_CONST, yytext());}
    {ID}        { return installID(yytext());}
    {CHAR_CONST} {return symbol(sym.CHAR_CONST, yytext());}

    //COMMENTI

    "||" { yybegin(InLineCOMMENT);}
    "|*" {yybegin(MultiLineCOMMENT);}

    //STRINGA

    \" {stringBuilder.setLength(0);; yybegin(STR_CONST);}

    /* error fallback */
    [^] { throw new Error("Illegal character <"+yytext()+">"); }
        <<EOF>>                 { return symbol(sym.EOF); }
}

<InLineCOMMENT>{
    {LineTerminator}    {yybegin(YYINITIAL);}
    [^[\r|\n|\r\n]] {}
}

<MultiLineCOMMENT>{
    "|"    {yybegin(YYINITIAL);}
    [^"|"] {/* do nothing */}
}

<STR_CONST>{
    \" { yybegin(YYINITIAL); return symbol(sym.STRING_CONST,stringBuilder.toString()); }
    [^\n\r\"\\]+ { stringBuilder.append( yytext() ); }
    \\t { stringBuilder.append('\t'); }
    \\n { stringBuilder.append('\n'); }
    \\r { stringBuilder.append('\r'); }
    \\\" { stringBuilder.append('\"'); }
    \\ { stringBuilder.append('\\'); }
    <<EOF>>         { throw new Error("STR_CONST NON CHIUSA"); }
}







package tables;

import java.util.HashMap;

public class OperationRules {
    public static HashMap<String, String> inferenceTable;

    static{
        inferenceTable = new HashMap<>();
        inferenceTable.put("plus-int-int", "int");
        inferenceTable.put("plus-int-float", "float");
        inferenceTable.put("plus-float-int", "float");
        inferenceTable.put("plus-float-float", "float");
        inferenceTable.put("times-int-int", "int");
        inferenceTable.put("times-float-int", "float");
        inferenceTable.put("times-int-float", "float");
        inferenceTable.put("times-float-float", "float");
        inferenceTable.put("minus-int-int", "int");
        inferenceTable.put("minus-float-int", "float");
        inferenceTable.put("minus-int-float", "float");
        inferenceTable.put("minus-float-float", "float");
        inferenceTable.put("div-int-int", "int");
        inferenceTable.put("div-float-int", "float");
        inferenceTable.put("div-int-float", "float");
        inferenceTable.put("div-float-float", "float");
        inferenceTable.put("pow-int-int", "int");
        inferenceTable.put("pow-float-int", "float");
        inferenceTable.put("pow-int-float", "float");
        inferenceTable.put("pow-float-float", "float");
        inferenceTable.put("str_concat-string-string", "string");
        inferenceTable.put("str_concat-string-int", "string");
        inferenceTable.put("str_concat-string-float", "string");
        inferenceTable.put("str_concat-string-bool", "string");
        inferenceTable.put("str_concat-int-string", "string");
        inferenceTable.put("str_concat-float-string", "string");
        inferenceTable.put("str_concat-bool-string", "string");
        inferenceTable.put("str_concat-int-int", "string");
        inferenceTable.put("str_concat-float-int", "string");
        inferenceTable.put("str_concat-bool-int", "string");
        inferenceTable.put("str_concat-int-float", "string");
        inferenceTable.put("str_concat-float-float", "string");
        inferenceTable.put("str_concat-bool-float", "string");
        inferenceTable.put("str_concat-int-bool", "string");
        inferenceTable.put("str_concat-float-bool", "string");
        inferenceTable.put("str_concat-bool-bool", "string");
        inferenceTable.put("and-bool-bool", "bool");
        inferenceTable.put("or-bool-bool", "bool");
        inferenceTable.put("gt-int-int", "bool");
        inferenceTable.put("gt-int-float", "bool");
        inferenceTable.put("gt-float-int", "bool");
        inferenceTable.put("gt-float-float", "bool");
        inferenceTable.put("ge-int-int", "bool");
        inferenceTable.put("ge-int-float", "bool");
        inferenceTable.put("ge-float-int", "bool");
        inferenceTable.put("ge-float-float", "bool");
        inferenceTable.put("lt-int-int", "bool");
        inferenceTable.put("lt-int-float", "bool");
        inferenceTable.put("lt-float-int", "bool");
        inferenceTable.put("lt-float-float", "bool");
        inferenceTable.put("le-int-int", "bool");
        inferenceTable.put("le-int-float", "bool");
        inferenceTable.put("le-float-int", "bool");
        inferenceTable.put("le-float-float", "bool");
        inferenceTable.put("eq-int-int", "bool");
        inferenceTable.put("eq-int-float", "bool");
        inferenceTable.put("eq-float-int", "bool");
        inferenceTable.put("eq-float-float", "bool");
        inferenceTable.put("eq-string-string", "bool");
        inferenceTable.put("eq-bool-bool", "bool");
        inferenceTable.put("ne-int-int", "bool");
        inferenceTable.put("ne-int-float", "bool");
        inferenceTable.put("ne-float-int", "bool");
        inferenceTable.put("ne-float-float", "bool");
        inferenceTable.put("ne-bool-bool", "bool");
        inferenceTable.put("minus-int", "int");
        inferenceTable.put("minus-float", "float");
        inferenceTable.put("not-bool", "bool");
        inferenceTable.put("integer_const", "int");
        inferenceTable.put("real_const", "float");
        inferenceTable.put("boolean_const", "bool");
        inferenceTable.put("string_const", "string");
        inferenceTable.put("char_const", "char");


    }

    public static String getOpType(String operation, String type1, String type2){
        return inferenceTable.get(operation+"-"+type1+"-"+type2);
    }

    public static String getOpType(String operation, String type1){
        return inferenceTable.get(operation+"-"+type1);
    }

    public static String getConstType(String c){
        return inferenceTable.get(c);
    }

}

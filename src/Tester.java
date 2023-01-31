import de.jflex.example.Lexer;
import esercitazione5.parser;
import nodes.Program;
import visitors.ScopingVisitor;
import visitors.XmlVisitor;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class Tester {
    public static void main(String[] args) throws Exception {

        FileInputStream str = new FileInputStream(args[0]);
        Reader reader = new InputStreamReader(str);
        parser p = new parser(new Lexer(reader));
        String nomefile= args[0].split("/")[args[0].split("/").length-1];
        String savePath ="./test_files/c_out/"+nomefile.split("\\.")[0]+".c";
        ScopingVisitor scopingVisitor = new ScopingVisitor(savePath);
        Program program = (Program) p.parse().value;

        program.accept(scopingVisitor);
        //Lo scoping visitor, al termine della visita, richiamerà
        //il typecheckerVisitor che a sua volta richiamerà il CTranslator

    }
}

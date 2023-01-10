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


        ScopingVisitor scopingVisitor = new ScopingVisitor();
        Program program = (Program) p.parse().value;

        program.accept(scopingVisitor);

    }
}

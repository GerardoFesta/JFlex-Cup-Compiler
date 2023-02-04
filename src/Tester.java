import de.jflex.example.Lexer;
import esercitazione5.parser;
import nodes.Program;
import visitors.ScopingVisitor;
import visitors.XmlVisitor;

import java.io.*;

public class Tester {
    public static void main(String[] args) throws Exception {

        FileInputStream str = new FileInputStream(args[0]);
        Reader reader = new InputStreamReader(str);
        parser p = new parser(new Lexer(reader));
        String nomefile = args[0].split("/")[args[0].split("/").length - 1];
        String savePath = "./test_files/c_out/" + nomefile.split("\\.")[0] + ".c";
        ScopingVisitor scopingVisitor = new ScopingVisitor(savePath);
        Program program = (Program) p.parse().value;

        program.accept(scopingVisitor);
        //Lo scoping visitor, al termine della visita, richiamerà
        //il typecheckerVisitor che a sua volta richiamerà il CTranslator

        /* QUESTA PARTE GENERA IN C_OUT IL COMPILATO DI C

        String nomefile_no_ext = nomefile.split("\\.")[0];
        ProcessBuilder builder = new ProcessBuilder(
                "cmd", "/c", "cd test_files/c_out && gcc  -o " + nomefile_no_ext+".out "+ nomefile_no_ext+ ".c");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
         */


        /*LA PARTE SUCCESSIVA SERVE PER L'ESECUZIONE DEL COMPILATO

        builder = new ProcessBuilder("cmd", "/c", "cd test_files/c_out && start "+nomefile_no_ext+".out");
        builder.redirectErrorStream(true);
        process = builder.start();
        r = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }

         */
    }
}

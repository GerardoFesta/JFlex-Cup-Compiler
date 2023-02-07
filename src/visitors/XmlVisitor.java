package visitors;

import nodes.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class XmlVisitor implements Visitor{

    public Document document;


    public XmlVisitor() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
    }

    public void save(String filepath) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(new File(filepath)));
    }




    public Object visit(IDLeaf idLeaf) {
        Element idLeafel = document.createElement(idLeaf.getTipoexpr());
        Text e1 = document.createTextNode(idLeaf.getId());
        idLeafel.appendChild(e1);
        return idLeafel;
    }

    @Override
    public Object visit(IfStat ifStat) {
        Element ifEl = document.createElement(ifStat.getTipo());
        Expr expr = ifStat.getExpr();
        Element exEl = (Element) expr.accept(this);
        ifEl.appendChild(exEl);
        Body body = ifStat.getBody();
        Element bodyEl = (Element) body.accept(this);
        ifEl.appendChild(bodyEl);
        if(ifStat.getElseBody() != null){
            Body elseBody = ifStat.getElseBody();
            Element elseBodyEl = (Element) elseBody.accept(this);
            ifEl.appendChild(elseBodyEl);
        }
        return ifEl;

    }

    @Override
    public Object visit(ParDecl parDecl) {
        Element parDeclEl = document.createElement("ParDecl");
        Type tipo = parDecl.getTipo();
        parDeclEl.appendChild((Element) tipo.accept(this));
        ArrayList<IDLeaf> idList = parDecl.getIdList();
        boolean isOut = parDecl.isOut();
        if(isOut)
            parDeclEl.appendChild(document.createElement("out"));
        for(IDLeaf id:idList)
            parDeclEl.appendChild((Element) id.accept(this));

        return parDeclEl;
    }

    @Override
    public Object visit(Program program) {
        Element programEl =document.createElement("Program");
        ArrayList<Declaration> declList1 = program.getDeclList1();
        for(Declaration d:declList1) programEl.appendChild((Element) d.accept(this));
        FunDecl main_f = program.getMainFunDecl();
        /*
        programEl.appendChild(document.createElement("START"));
        programEl.appendChild((Element) main_f.accept(this));
         */
        Element main_f_el= (Element) main_f.accept(this);
        main_f_el.appendChild(document.createElement("START"));
        programEl.appendChild(main_f_el);

        ArrayList<Declaration> declList2 = program.getDeclList2();
        for(Declaration d:declList2) programEl.appendChild((Element) d.accept(this));
        document.appendChild(programEl);
        return document;
    }

    @Override
    public Object visit(ReadStat readStat) {
        Element readStatEl =document.createElement(readStat.getTipo());

        ArrayList<IDLeaf> idList= readStat.getIdList();
        for(IDLeaf id:idList)
            readStatEl.appendChild((Element) id.accept(this));
        if(readStat.getStr_con()!=null){
            String strConst=readStat.getStr_con();
            readStatEl.appendChild(document.createTextNode(strConst));
        }
        return readStatEl;
    }

    @Override
    public Object visit(ReturnStatement returnStatement) {
        Element returnEl = document.createElement(returnStatement.getTipo());
        if(returnStatement.getExpr()!=null){
            Expr e = returnStatement.getExpr();
            returnEl.appendChild((Element) e.accept(this));
        }
        return returnEl;
    }


    @Override
    public Object visit(Type type) {
        return document.createElement(type.getTipo());
    }

    @Override
    public Object visit(UnaryOperation unaryOp) {
        Element unaryOpEl = document.createElement(unaryOp.getTipoexpr());
        unaryOpEl.appendChild(document.createElement(unaryOp.getOpType()));
        Expr expr = unaryOp.getValue();
        unaryOpEl.appendChild((Element)expr.accept(this));
        return unaryOpEl;
    }

    @Override
    public Object visit(VarDecl varDecl) {
        Element varDeclEl = document.createElement(varDecl.getTipo());
        Type type = varDecl.getType();
        varDeclEl.appendChild((Element) type.accept(this));
        ArrayList<IdInit> idInitList = varDecl.getIdInitList();
        for(IdInit init:idInitList)
            varDeclEl.appendChild((Element) init.accept(this));

        return varDeclEl;
    }

    @Override
    public Object visit(WhileStat whileStat) {
        Element element = document.createElement(whileStat.getTipo());
        Expr expr = whileStat.getExpr();
        Body body = whileStat.getBody();
        element.appendChild((Element)expr.accept(this));
        element.appendChild((Element)body.accept(this));
        return element;
    }
    @Override
    public Object visit(WriteStat writeStat) {
        ArrayList<Expr> exprList = writeStat.getExprList();
        int mode = writeStat.getMode();
        Element element = document.createElement(mode==0?"WriteStat":"WriteStatLN");
        for(Expr e : exprList) {
            element.appendChild((Element) e.accept(this));
        }

        return element;
    }

    @Override
    public Object visit(SwitchStat nodo) {
        return null;
    }

    @Override
    public Object visit(Case nodo) {
        return null;
    }

    @Override
    public Object visit(AssignStat assignStat) {
        Element element = document.createElement(assignStat.getTipo());
        ArrayList<IDLeaf> idList = assignStat.getIdList();
        ArrayList<Expr> exprList = assignStat.getExprList();
        Element idl = document.createElement("idList");
        Element exl = document.createElement("exprList");
        for (IDLeaf i : idList){
            Element elemento = (Element) i.accept(this);
            idl.appendChild(elemento);
        }
        System.out.println(idl.hasChildNodes());
        System.out.println(exl);
        if(exl!=null) {
            for (Expr e : exprList) {
                Element elemento = (Element) e.accept(this);
                exl.appendChild(elemento);
            }
        }
        element.appendChild(idl);
        element.appendChild(exl);
        return element;
    }

    @Override
    public Object visit(BinaryOperation binaryOp) {
        Element binaryOpEl = document.createElement(binaryOp.getOpType());
        Expr expr1 = binaryOp.getValue1();
        binaryOpEl.appendChild((Element) expr1.accept(this));
        Expr expr2 = binaryOp.getValue2();
        binaryOpEl.appendChild((Element) expr2.accept(this));
        return  binaryOpEl;
    }

    @Override
    public Object visit(Body body) {
        Element element = document.createElement("Body");
        ArrayList<VarDecl> varDecls = body.getVarDeclList();
        ArrayList<Stat> stats = body.getStatList();
        for (VarDecl v: varDecls) {
            Element e = (Element) v.accept(this);
            element.appendChild(e);
        }

        if(!stats.isEmpty()){
            for (Stat s: stats) {
                Element e = (Element) s.accept(this);
                element.appendChild(e);
            }
        }
        return element;
    }

    @Override
    public Object visit(ConstLeaf constLeaf) {
        Element constLeafEl = document.createElement(constLeaf.getTipoexpr());
        String valore = constLeaf.getValue();
        Text e1 = document.createTextNode(valore);

        constLeafEl.appendChild(e1);
        return constLeafEl;
    }

    @Override
    public Object visit(ForStat forStat) {
        Element element = document.createElement(forStat.getTipo());
        IDLeaf id = forStat.getId();
        String int_con1 = forStat.getInt_con1();
        String int_con2 = forStat.getInt_con2();
        Body body = forStat.getBody();
        element.appendChild((Element) id.accept(this));
        element.appendChild(document.createElement(int_con1));
        element.appendChild(document.createElement(int_con2));
        element.appendChild((Element)body.accept(this));
        return element;
    }

    @Override
    public Object visit(FunCall funCall) {
        //nella grammatica funcall ci va IDLeaf anche nella classe funcall
        //nella classe di funcall statemant
        Element element = document.createElement(funCall.getTipoexpr());
        IDLeaf id = funCall.getId();
        ArrayList<Expr> exprList = funCall.getExprList();
        element.appendChild((Element)id.accept(this));
        if (exprList != null){
            for(Expr e : exprList){
                element.appendChild((Element)e.accept(this));
            }
        }

        return element;
    }

    @Override
    public Object visit(FunCallStatement funCallStatement) {
        Element element = document.createElement(funCallStatement.getTipo());
        IDLeaf id = funCallStatement.getId();
        ArrayList<Expr> exprList = funCallStatement.getExprList();
        element.appendChild((Element)id.accept(this));
        if (exprList != null){
            for(Expr e : exprList){
                element.appendChild((Element)e.accept(this));
            }
        }

        return element;
    }

    @Override
    public Object visit(FunDecl funDecl) {
        Element funDeclEl = document.createElement("FunDecl");
        IDLeaf id =  funDecl.getId();
        funDeclEl.appendChild((Element) id.accept(this));
        ArrayList<ParDecl> paramDeclList = funDecl.getParDeclList();
        for (ParDecl p:paramDeclList)
            funDeclEl.appendChild((Element) p.accept(this));
        Type tipo = funDecl.getType();
        funDeclEl.appendChild((Element)tipo.accept(this));
        Body b = funDecl.getBody();
        funDeclEl.appendChild((Element) b.accept(this));

        return funDeclEl;
    }

    @Override
    public Object visit(IdInit idInit) {
        Element idInitEl = document.createElement("idInit");
        IDLeaf idLeaf = idInit.getId();
        idInitEl.appendChild((Element) idLeaf.accept(this));

        Expr expr = idInit.getExpr();
        if(expr != null)
            idInitEl.appendChild((Element) expr.accept(this));
        return idInitEl;
    }

}

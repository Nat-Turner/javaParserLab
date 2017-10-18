import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class sequenceDiagram {


    public static HashMap<String, List<String>> hmap = new HashMap<String, List<String>>();
    public static String currentClass = "";
    public static List<String> classNames = new ArrayList<>();
    public static List<String> varList = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        File folder = new File("testcode/");

        visitFiles(new classNameCVisitor(),folder);
        visitFiles(new ClassDiagramVisitor(),folder);
        visitFiles( new interaction(),folder);

    }


    public static List<File> getAllFiles(final File folder) {
        List<File> files = new ArrayList<>();

        for (final File f : folder.listFiles()) {

            if (f.isFile()) {
                 files.add(f);

            } else {
                files.addAll(getAllFiles(f));
            }
        }
        return files;
    }

    public static void visitFiles(VoidVisitorAdapter v ,File folder ){
        FileInputStream in=null;
        CompilationUnit cu = null;
        List<File> allFiles = getAllFiles(folder);
        for (final File javaFile :allFiles) {

            try {
                in = new FileInputStream(javaFile.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                cu = JavaParser.parse(in);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            v.visit(cu, null);
            hmap.put(currentClass, varList);

        }
    }
    private static class ClassDiagramVisitor extends VoidVisitorAdapter {
        public void visit(ClassOrInterfaceDeclaration c, Object a){
            currentClass=c.getNameAsString();
            super.visit(c, a);
        }

        public void visit(VariableDeclarator n, Object a) {
            for (String S : classNames) {

                if (n.getType().asString().equals(S)) {
                    addTomap(currentClass+"-"+n.getNameAsString() + "-" + n.getType().asString());
                }

            }
            super.visit(n, a);
        }

        public void visit(VariableDeclarationExpr n, Object a) {

            n.getParentNode();

            for (String S : classNames) {

                if (n.getElementType().asString().equals(S)) {
                    addTomap(currentClass+"-"+n.getVariable(0).getNameAsString()+"-"+S);

                }

            }

            super.visit(n, a);
        }



    }
    public static void addTomap(String varName){
        varList.add(varName);

    }
    private static class classNameCVisitor extends VoidVisitorAdapter {

        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            classNames.add(n.getNameAsString());
        }
    }

    private static class interaction extends VoidVisitorAdapter {

        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            System.out.println("");
           System.out.println("In Class: " + n.getName());
            currentClass=n.getNameAsString();
            super.visit(n, arg);
        }

        public void visit(MethodCallExpr n, Object a) {



            Set set = hmap.entrySet();
            Iterator iterator = set.iterator();

            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();

                    List<String> temp = (List<String>) mentry.getValue();


                    for (String var : temp){
                        String[] tokens = var.split("-");
                        // if (the variable name is the same as the variable the methodCall is being performed on && if the currentClass is the same as the class the method call is in && that currentClass != the class we calling)
                        if(tokens[1].equals(n.getChildNodes().get(0).toString()) && (tokens[0].equals(currentClass)) && (!tokens[2].equals(currentClass))){

                            System.out.println(  "Line: "+n.getBegin().get().line+" MethodCall: "+n+" ----> " + tokens[2]+".java  "  );
                            return;

                        }
                    }


            }

           super.visit(n, a);
        }


    }
}

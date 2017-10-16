import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class sequenceDiagram {


    public static HashMap<String, List<String>> hmap = new HashMap<String, List<String>>();
    public static List<ArrayList<String>> table = new ArrayList<>();
    public static String currentClass = "";
    public static List<String> classNames = new ArrayList<>();
    public static List<String> varList = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        File folder = new File("testing/");
        FileInputStream in;
        CompilationUnit cu=null;
        int classIndex = 0;
        // List<String> classNames = new ArrayList<>();
        for (final File javaFile : folder.listFiles()) {

            in = new FileInputStream(javaFile.getPath());
            try {
                cu = JavaParser.parse(in);
            } finally {
                in.close();
            }
            new classNameCVisitor().visit(cu, null);


        }

        for (final File javaFile : folder.listFiles()) {
            currentClass=javaFile.getName();
            in = new FileInputStream(javaFile.getPath());
            try {
                cu = JavaParser.parse(in);
            } finally {
                in.close();
            }


            new ClassDiagramVisitor().visit(cu, null);
            hmap.put(currentClass, varList);
            varList= new ArrayList<>();
        }

        int index= 0;
        for (final File javaFile : folder.listFiles()) {

            in = new FileInputStream(javaFile.getPath());
            try {
                cu = JavaParser.parse(in);
            } finally {
                in.close();
            }

           // System.out.println(hmap);
            new interaction().visit(cu,null);

            varList= new ArrayList<>();
          //  System.out.println(index);
            ++index;
        }

    }


    private static class ClassDiagramVisitor extends VoidVisitorAdapter {
        public void visit(VariableDeclarator n, Object a) {
            addTomap(n.getNameAsString()+"-"+ n.getType().asString());
        }

        public void visit(VariableDeclarationExpr n, Object a) {
            String tempClass =  currentClass;
            ArrayList<String> tempVar = new ArrayList<>();

            for (String S : classNames) {

                if (n.getElementType().asString().equals(S)) {
                 //   System.out.println("Relationship with " + S + ".java class");
                  //  System.out.println("  ---   "+ n.getVariable(0).getNameAsString());
                    addTomap(n.getVariable(0).getNameAsString()+"-"+S);

                    // System.out.println( n );
                }

            }

           // hmap.put(currentClass,tempVar);

        }


    }
    public static void addTomap(String varName){
        varList.add(varName);

    }
    private static class classNameCVisitor extends VoidVisitorAdapter {


        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
           // System.out.println(" names of classes" + n.getNameAsString());
            classNames.add(n.getNameAsString());
        }
    }

    private static class interaction extends VoidVisitorAdapter {

        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            System.out.println("");
           System.out.println("Class Name: " + n.getName());
            super.visit(n, arg);
        }

        public void visit(MethodCallExpr n, Object a) {



            Set set = hmap.entrySet();
            Iterator iterator = set.iterator();

            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();

                    List<String> temp = (List<String>) mentry.getValue();
                  //  System.out.println(temp);

                    for (String var : temp){
                        String[] tokens = var.split("-");
                        if(tokens[0].equals(n.getChildNodes().get(0).toString())){


                            System.out.println(  "Line: "+n.getBegin().get().line+" MethodCall: "+n+" ----> " + tokens[1]+".java  "  );
                            n.getBegin();
                        }
                    }


            }
           // System.out.println("here " +n);
            //System.out.println(n.asMethodCallExpr());
           // System.out.println(n.getScope());

        }
    }
}

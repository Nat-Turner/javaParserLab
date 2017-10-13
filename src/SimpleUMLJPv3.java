import java.io.File;
import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class SimpleUMLJPv3 {

    public static void main(String[] args) throws Exception {
        File folder = new File("testing/");
        FileInputStream in;
        for (final File javaFile : folder.listFiles()) {
                 in = new FileInputStream(javaFile.getPath());

        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        System.out.println("/*******************/");
        new ClassDiagramVisitor().visit(cu, null);
        System.out.println("");
        System.out.println("/*******************/");
        new methodVisitor().visit(cu, null);
        System.out.println("");
    }


}

    private static class methodVisitor extends VoidVisitorAdapter  {
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            if(n.isPublic()){
                System.out.print(" + ");
            }
            if(n.isPrivate()) {
                System.out.print(" - ");
            }
            if(n.isProtected()){
                System.out.print(" # ");
            }

            System.out.print(" "+ n.getName());
            System.out.println(" : "+ n.getType());
            System.out.println(" Method parameters: ");
            for (Parameter p : n.getParameters()) {
                System.out.print(" Type: " + p.getType());
                System.out.println(" Name: " + p.getName());
            }
            System.out.println("");

        }  }

    private static class ClassDiagramVisitor extends VoidVisitorAdapter {

        public void visit(ClassOrInterfaceDeclaration n, Object arg){
            System.out.println("Class Name: " + n.getName());
            System.out.println("");
            System.out.print("Class Implements: ");
            for (ClassOrInterfaceType coi : n.getImplementedTypes()) {
                System.out.println(coi.getName());
            }
            System.out.println("");
            System.out.print("Class Extends: ");
            for (ClassOrInterfaceType coi : n.getExtendedTypes()) {
                System.out.println(coi.getName());
            }
            super.visit(n, arg);
        }
    }
}

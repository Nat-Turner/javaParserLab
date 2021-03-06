import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class printClassD {

    public static List<String> classNames = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a file Path :");
        String n = input.next();
        input.close();
        System.out.println("");
        File folder = new File(n);
        FileInputStream in;
        CompilationUnit cu;

        List<File> allFiles = getAllFiles(folder);
            for ( File javaFile :allFiles) {
                in = new FileInputStream(javaFile.getPath());
                try {
                    cu = JavaParser.parse(in);
                } finally {
                    in.close();
                }
                new classNameCVisitor().visit(cu, null);
            }

            for (final File javaFile : allFiles) {
                in = new FileInputStream(javaFile.getPath());
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

    private static class methodVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(MethodDeclaration n, Object arg) {

            if (n.isPublic()) {
                System.out.print(" + ");
            }
            if (n.isPrivate()) {
                System.out.print(" - ");
            }
            if (n.isProtected()) {
                System.out.print(" # ");
            }

            System.out.print(" " + n.getName());
            System.out.println(" : " + n.getType());
            System.out.println(" Method parameters: ");
            for (Parameter p : n.getParameters()) {
                System.out.print(" Type: " + p.getType());
                System.out.println(" Name: " + p.getName());
            }
            System.out.println("");


        }
    }

    private static class classNameCVisitor extends VoidVisitorAdapter {
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {

            classNames.add(n.getNameAsString());
        }
    }

    private static class ClassDiagramVisitor extends VoidVisitorAdapter {

        public void visit(ClassOrInterfaceDeclaration n, Object arg) {

            if(n.isInterface()){
                System.out.println("<<Interface>>");
            }
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

        public void visit(VariableDeclarationExpr n, Object a) {


            for (String S : classNames) {

                if (n.getElementType().asString().equals(S)) {
                    System.out.println("");
                    System.out.println("Relationship with " + S + ".java class");
                   // System.out.println( n );
                    new interaction().visit(n, null);
                }

            }
        }

    }
    private static class interaction extends VoidVisitorAdapter {
        public void visit(MethodCallExpr n, Object a) {
            System.out.println(n.getName());
            System.out.println(n.asMethodCallExpr());
            System.out.println(n.getScope());

        }
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
}


import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;

public class MethodPrinter {
     static int methods;
     static int depth;
    public static void main(String[] args) throws Exception {
        File folder = new File("testing/");
        FileInputStream in;
        for (final File javaFile : folder.listFiles()) {
            in = new FileInputStream(javaFile.getPath());
              methods=0;
              depth=0;

            CompilationUnit cu;
            try {
                cu = JavaParser.parse(in);
            } finally {
                in.close();
            }

            // visit and print the methods names
            cu.accept(new MVisitor(), null);
            cu.accept(new ClassVisitor(), null);
            System.out.println("WMC (number of methods in class): " + methods);
            System.out.println("DIT  maximum inheritance path from the class to the root class : " + depth);

        }
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
           // System.out.println(n.getName());
            ++methods;
            super.visit(n, arg);
        }
    }


    private static class ClassVisitor extends VoidVisitorAdapter {

        public void visit(ClassOrInterfaceDeclaration n, Object arg){
           Class c= n.getClass();
                  //  System.out.println( " "+ n.getExtendedTypes().getClass());
                  //  System.out.println(n.getClass());
            if(n.getParentNode().get() instanceof ClassOrInterfaceDeclaration){
               // System.out.println(n.getParentNode().get());
            }
            while(c.getSuperclass()!=null){
               // System.out.println(c.getSuperclass());
                c = c.getSuperclass();
                ++depth;

            }
            super.visit(n, arg);
        }
    }
}




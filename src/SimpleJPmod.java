import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleJPmod {

    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream("Stego.java");


        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        new MethodModdsVisitor().visit(cu, null);
        // write the modified cu back...
        System.out.println(cu.toString());

        // Write modified AST to a file
        byte[] modfile = cu.toString().getBytes();
        Path file = Paths.get("Stego.java");
        Files.write(file, modfile);
    }

    /**
     * Example of how to modify code and insert instrumentation
     * Just print out name of class after execution
     *
     * Also need to modify the class name - easy to do and an exercise for the reader!
     */
    private static class MethodModdsVisitor extends VoidVisitorAdapter  {
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // Step 1 - create a new node (Various options for doing this)
            // NameExpr systemOut = NameExpr.name("System.out");
            // MethodCallExpr call = new MethodCallExpr(systemOut, "println");
            // MethodCallExpr call = new MethodCallExpr(NameExpr.name("System.out"), "println");
            MethodCallExpr call = new MethodCallExpr(new NameExpr("System.out"), "println");
            // Add in the argument - name of method visited
            call.addArgument(new StringLiteralExpr(n.getName().asString()));

            // Step 2 - Add this statement to the method
            n.getBody().get().addStatement(call);  // n.getBody() returns type optional so need get() to obtain the object (if it is there)
//            n.getBody().ifPresent(l -> l.addStatement(call));  // alternatively using lambda expression

            // Step 3 - Modify block
            BlockStmt block = new BlockStmt(); // create a new empty block statement
            block.addStatement(call);  // add in the call just created
            block.addStatement(n.getBody().get()); // add in the body of the method that was originally there
            n.setBody(block); // and then replace the contents of the method body with the newly created one
        }
    }
}


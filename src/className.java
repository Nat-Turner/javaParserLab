import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class className extends VoidVisitorAdapter {
    String className=null;

    public void visit(ClassOrInterfaceDeclaration c, Object a){
        className=c.getName().asString();
    }

    public String getClassName(){
        return className;
    }
}

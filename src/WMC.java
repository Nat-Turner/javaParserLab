import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public  class WMC extends VoidVisitorAdapter<Void> {
    static int methods;

    public WMC(){
        methods=0;
    }
    @Override
    public void visit(ConstructorDeclaration n, Void arg) {

        ++methods;
        super.visit(n, arg);
    }
    @Override
    public void visit(MethodDeclaration n, Void arg) {

        ++methods;
        super.visit(n, arg);
    }

    public int methodCount(){

        return methods;
    }
}
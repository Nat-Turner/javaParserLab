import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public  class WMC extends VoidVisitorAdapter<Void> {
    static int methods=0;


    @Override
    public void visit(MethodDeclaration n, Void arg) {

        ++methods;
        super.visit(n, arg);
    }

    public int methodCount(){
        int tempCount= methods;
        methods=0;
        return tempCount;
    }
}
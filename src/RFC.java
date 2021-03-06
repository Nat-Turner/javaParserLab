import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class RFC extends VoidVisitorAdapter {
    int M=0;
    int R=0;
    public RFC(){
    }
    @Override
    public void visit(MethodCallExpr n, Object arg) {

       if(n.asMethodCallExpr().getScope().isPresent()){
           //there is a remote method call so we increment
            ++R;
       }

        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        ++M;
        super.visit(n,arg);
    }

    public int getRFC(){
        return R+M;
    }
}


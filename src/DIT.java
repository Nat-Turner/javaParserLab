import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class DIT extends VoidVisitorAdapter<Void> {
    static int depth=0;
    String currentDir="";
    List<File> allFiles;

    public DIT (String cur, List<File> Files){
        currentDir=cur;
        allFiles= Files;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {

        depth=1;// becuase every class inherits directly from System.Object
        if(n.getExtendedTypes().isNonEmpty()){
            int i = 0;
           while( i<n.getExtendedTypes().size()){
               File f = new File(currentDir+"/"+n.getExtendedTypes().get(i).asString()+".java");
               if(!(allFiles.contains(f))){

                   depth=2; // unrecognised extended class but will also inherit from System.object
                   return;
               }
               FileInputStream in=null;
               CompilationUnit cu = null;
               try {
                   in = new FileInputStream( f.getPath());
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
               cu = JavaParser.parse(in);
               visit(cu,null);
               ++i;
           }
           ++depth;
        }
        super.visit(n, arg);
    }

    public int getDepth(){
        return depth;
    }
}

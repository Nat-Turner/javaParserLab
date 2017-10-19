import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NOC extends  VoidVisitorAdapter<Void> {
    List<String> allFileNames=new ArrayList<>();
    public List<String> extendsList= new ArrayList<>();
    public List<File> allFiles=new ArrayList<>();
    public String currentClass="";

    public NOC(List<File> allf, String className) {
        FileInputStream in = null;
        CompilationUnit cu = null;
        allFiles = allf;
        currentClass=className;
        for (File f : allf) {
            allFileNames.add(f.getName().toString());

        }
        for (final File javaFile : allFiles) {

            try {
                in = new FileInputStream(javaFile.getPath());
                cu = JavaParser.parse(in);
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

                visit(cu,null);
        }

    }
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) { //broken

           if (n.getExtendedTypes().isNonEmpty()) {
               int i = 0;
               while (i < n.getExtendedTypes().size()) {

                   extendsList.add(n.getExtendedTypes().get(i).asString());
                   ++i;
               }

           }
       super.visit(n, arg);
    }

    public int getNoC(){
        int children=0;
         for(String child : extendsList){
           if(child.equals(currentClass)){
               ++children;
           }
         }
        return children;
    }

}

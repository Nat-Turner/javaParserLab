import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class main {


    public static void main(String[] args)  {
        File folder = new File("testingCase1.1/");
        List<File> allFiles = getAllFiles(folder);

        FileInputStream in;
        CompilationUnit cu = null;
        className name = new className();
        for (final File javaFile :allFiles) {

            WMC wmc = new WMC();
            DIT dit= new DIT(folder.getPath(),allFiles);

            try {
                in = new FileInputStream(javaFile.getPath());
                cu = JavaParser.parse(in);
                in.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            name.visit(cu,null);
            System.out.println("Class Name :" + name.getClassName());
            wmc.visit(cu, null);
            System.out.println("WMC :" +wmc.methodCount());
            dit.visit(cu,null);
            System.out.println("DIT :"+dit.getDepth());

            NOC noc = new NOC(allFiles,name.getClassName());
            System.out.println("NoC : "+ noc.getNoC());

            RFC rfc = new RFC(wmc.methodCount());
            rfc.visit(cu,null);
            System.out.println("RFC : " + rfc.getRFC());
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

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

        FileInputStream in=null;
        CompilationUnit cu = null;
        className name = new className();
        WMC wmc = new WMC();
        DIT dit= new DIT(folder.getPath(),allFiles);
        NOC noc = new NOC(allFiles);
        for (final File javaFile :allFiles) {

            try {
                in = new FileInputStream(javaFile.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                cu = JavaParser.parse(in);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            name.visit(cu,null);
            System.out.println("Class Name :" + name.getClassName());
            wmc.visit(cu, null);
            System.out.println("WMC :" +wmc.methodCount());
            dit.visit(cu,null);
            System.out.println("DIT :"+dit.getDepth());
            noc.visit(cu,null);
            System.out.println("NoC : "+ noc.getNoC());
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

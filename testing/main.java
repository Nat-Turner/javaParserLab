import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.BitSet;

public class main {

    public static void main(String args[]){
        Stego s = new Stego();

       System.out.println(s.extractString(s.hideString("Michael Chan 1995 on the first day.", "Koala.bmp")));
       s.extractFile(s.hideFile("Small.gif","Koala.bmp"));

            }



    }




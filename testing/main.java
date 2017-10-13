import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.BitSet;

public class main {

    public static void main(String args[]){
        Stego s = new Stego();

       s.extractString(s.hideString("Michael Chan 1995 y.", "Koala.bmp"));
       s.extractFile(s.hideFile("Small.gif","Koala.bmp"));

            }



    }




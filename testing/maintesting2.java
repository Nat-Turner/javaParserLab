import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.BitSet;

public class maintesing2 {

    public static void main(String args[]){
        Stego s = new Stego();
    if(true) {
        s.extractString(s.hideString("Michael Chan 1995 y.", "Koala.bmp"));
        s.extractFile(s.hideFile("Small.gif", "Koala.bmp"));
    }
            }



    }




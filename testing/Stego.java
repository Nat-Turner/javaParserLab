import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

class Stego {

    /**
     * A constant to hold the number of bits per byte
     */
    private final int byteLength = 8;

    /**
     * A constant to hold the number of bits used to store the size of the file extracted
     */
    protected final int sizeBitsLength = 32;

    /**
     * A constant to hold the number of bits used to store the extension of the file extracted
     */
    protected final int extBitsLength = 64;

    /**
     * A constant to hold the number of bits used to store the bmp file format information
     */
    protected int offset = 54;

    public Stego() {
    }

    /**
    A method for hiding a string in an uncompressed image file such as a .bmp or .png
    You can assume a .bmp will be used
    @param cover_filename - the filename of the cover image as a string including the extension
    @param payload - the string which should be hidden in the cover image.
    @return a string which either contains 'Fail' or the name of the stego image (including the extension) which has been
    written out as a result of the successful hiding operation.
    You can assume that the images are all in the same directory as the java files
    */
    //TODO you must write this method
    public String hideString(String payload, String cover_filename) {

            File bmpFile = new File(cover_filename);
            BufferedImage image = null;
            byte[] strByte = payload.getBytes();
            //offset of picture info
            int payLoadSize = (strByte.length * 8);
            String alterImage = "hiddenString.bmp";
            try {
                // Bug in JDK8 , so had to update to JDK 9 : https://bugs.openjdk.java.net/browse/JDK-8182461
                image = ImageIO.read(bmpFile);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ImageIO.write(image, "bmp", byteStream);
                byte[] imageInBytes = byteStream.toByteArray();
                byteStream.close();
                if (payLoadSize > (imageInBytes.length - (offset + sizeBitsLength))) {
                    return "fail:Too large to hide in image";
                }
                imageInBytes = storeInfo(strByte, imageInBytes, offset, 4);
                imageInBytes = storePayload(strByte, imageInBytes, offset + sizeBitsLength);
                InputStream hiddenImageBytes = new ByteArrayInputStream(imageInBytes);
                BufferedImage imageWithString = ImageIO.read(hiddenImageBytes);
                ImageIO.write(imageWithString, "bmp", new File(alterImage));
            } catch (IOException e) {
                e.printStackTrace();
                return "Fail";
            }
            return alterImage;

    }

    //TODO you must write this method
    /**
    The extractString method should extract a string which has been hidden in the stegoimage
    @param the name of the stego image including the extension
    @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
    was unsuccessful
    */
    public String extractString(String stego_image) {

            File bmpFile = new File(stego_image);
            BufferedImage image = null;
            String hidenMessage = "failed";
            try {
                image = ImageIO.read(bmpFile);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ImageIO.write(image, "bmp", byteStream);
                byteStream.flush();
                byte[] imageInBytes = byteStream.toByteArray();
                byteStream.close();
                int sizeNo = extractInfo(imageInBytes, offset, 4);
                byte[] text = new byte[sizeNo / byteLength];
                hidenMessage = new String(extractPayload(imageInBytes, text, sizeNo, offset + sizeBitsLength));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hidenMessage;

    }

    //TODO you must write this method
    /**
    The hideFile method hides any file (so long as there's enough capacity in the image file) in a cover image

    @param file_payload - the name of the file to be hidden including the extension, you can assume it is in the same directory as the program
    @param cover_image - the name of the cover image file including the extension, you can assume it is in the same directory as the program
    @return String - either 'Fail' to indicate an error in the hiding process, or the name of the stego image (including the extension) written out as a result of the successful hiding process
    */
    public String hideFile(String file_payload, String cover_image) {

            File bmpFile = new File(cover_image);
            BufferedImage image = null;
            String[] ext1 = file_payload.split("\\.");
            String extension = ext1[ext1.length - 1];
            FileInputStream payloadInput = null;
            byte[] payloadBytes = null;
            byte[] imageInBytes = null;
            try {
                // Bug in JDK8 , so had to update to JDK 9 : https://bugs.openjdk.java.net/browse/JDK-8182461
                image = ImageIO.read(bmpFile);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                payloadInput = new FileInputStream(file_payload);
                ImageIO.write(image, "bmp", byteStream);
                imageInBytes = byteStream.toByteArray();
                byteStream.close();
                payloadBytes = new byte[payloadInput.available()];
                int i = 0;
                while (payloadInput.available() != 0) {
                    payloadBytes[i] = (byte) payloadInput.read();
                    ++i;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if ((payloadBytes.length * byteLength) > (imageInBytes.length - (offset + sizeBitsLength))) {
                return "fail:Too large to hide in image";
            }
            imageInBytes = storeInfo(payloadBytes, imageInBytes, offset, 4);
            imageInBytes = storeInfo(extension.getBytes(), imageInBytes, offset + sizeBitsLength, 8);
            imageInBytes = storePayload(payloadBytes, imageInBytes, offset + sizeBitsLength + extBitsLength);
            FileOutputStream out = null;
            byte[] bytes = imageInBytes;
            String filename = "hiddenFile.bmp";
            try {
                out = new FileOutputStream(filename);
                out.write(bytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filename;

    }

    //TODO you must write this method
    /**
    The extractFile method hides any file (so long as there's enough capacity in the image file) in a cover image

    @param stego_image - name of the cover image (including the extension) from which to extract, you can assume it is in the same directory as the program
    @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file (including the extension) written out as a
    result of the successful extraction process
    */
    public String extractFile(String stego_image) {

            File bmpFile = new File(stego_image);
            String filename = "hidden.";
            BufferedImage image;
            byte[] imageInBytes = null;
            try {
                // Bug in JDK8 , so had to update to JDK 9 : https://bugs.openjdk.java.net/browse/JDK-8182461
                image = ImageIO.read(bmpFile);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ImageIO.write(image, "bmp", byteStream);
                imageInBytes = byteStream.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int payloadSize = extractInfo(imageInBytes, offset, 4);
            byte[] extenChars = new byte[extBitsLength / byteLength];
            String extension = new String(extractPayload(imageInBytes, extenChars, extBitsLength, offset + sizeBitsLength));
            FileOutputStream out = null;
            byte[] fileBytes = new byte[payloadSize / byteLength];
            try {
                filename = filename.concat(extension.trim());
                out = new FileOutputStream(filename);
                fileBytes = extractPayload(imageInBytes, fileBytes, payloadSize, offset + sizeBitsLength + extBitsLength);
                out.write(fileBytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filename;

    }

    //TODO you must write this method
    /**
     * This method swaps the least significant bit of a byte to match the bit passed in
     * @param bitToHide - the bit which is to replace the lsb of the byte
     * @param byt - the current byte
     * @return the altered byte
     */
    public int swapLsb(int bitToHide, int byt) {

            int tempByte = byt;
            if (bitToHide == 0) {
                tempByte &= ~1;
            } else {
                tempByte |= 1;
            }
            return tempByte;


    }

    public byte[] storePayload(byte[] payload, byte[] imageInBytes, int offset) {


            int i = 0;
            int payLoadSize = (payload.length * byteLength);
            byte[] imageWithpayload = imageInBytes;
            while (i < payLoadSize) {
                int bytePos = (i / byteLength);
                int bitPos = (i % byteLength);
                //getting the bit your hiding
                int bit = payload[bytePos] >> (byteLength - (bitPos + 1)) & 1;
                int tempByte = swapLsb(bit, imageWithpayload[i + offset]);
                imageWithpayload[i + offset] = (byte) tempByte;
                ++i;
            }
            return imageWithpayload;

    }

    protected byte[] storeInfo(byte[] payLoadInfo, byte[] imageInBytes, int offset, int len) {

            byte[] image = imageInBytes;
            byte[] infoBits = null;
            if (len == 4) {
                int sizeOfData = payLoadInfo.length * byteLength;
                // for the length of the payload
                infoBits = ByteBuffer.allocate(len).putInt(sizeOfData).array();
            }
            if (len == 8) {
                // for extension
                infoBits = ByteBuffer.allocate(len).put(payLoadInfo).array();
            }
            int i = 0;
            while (i < (len * byteLength)) {
                int bytePos = (i / byteLength);
                int bitPos = (i % byteLength);
                //getting the bit your hiding
                int bit = infoBits[bytePos] >> (byteLength - (bitPos + 1)) & 1;
                int tempByte = image[i + offset];
                if (bit == 0) {
                    tempByte &= ~1;
                } else {
                    tempByte |= 1;
                }
                image[i + offset] = (byte) tempByte;
                ++i;
            }
            return image;


    }

    private int extractInfo(byte[] imageInBytes, int offset, int len) {

            int i = 0;
            int bit = 0;
            List<Integer> stringBits = new ArrayList<>();
            String sizeStr = "";
            while (i < (len * byteLength)) {
                int tempByte = imageInBytes[i + offset];
                bit = (int) tempByte & 1;
                stringBits.add(bit);
                ++i;
            }
            for (Integer b : stringBits) {
                sizeStr += String.valueOf(b);
            }
            return Integer.parseInt(sizeStr, 2);

    }

    private byte[] extractPayload(byte[] imageInBytes, byte[] text, int Size, int offset) {

            ArrayList<Integer> stringBits = new ArrayList<>();
            int test = 0;
            int i = 0;
            while (i < Size) {
                int tempByte = imageInBytes[i + offset];
                test = (int) tempByte & 1;
                stringBits.add(test);
                ++i;
            }
            for (int a = 0; a < (Size / byteLength); ++a) {
                int letter = 0;
                for (int b = a * byteLength; b < (a + 1) * byteLength; ++b) {
                    // shifts to the left by 1
                    letter = letter << 1;
                    //adds to letter to get the int val of the char
                    letter += stringBits.get(b);
                }
                text[a] = (byte) letter;
            }
            return text;

    }
}

package testcode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

class Stego {


    /**
     * A constant to hold the number of bits per byte
     */
    private static final int byteLength = 8;

    /**
     * A constant to hold the number of bits used to store the size of the file extracted
     */
    protected static final int sizeBitsLength = 32;

    /**
     * A constant to hold the number of bits used to store the extension of the file extracted
     */
    protected static final int extBitsLength = 64;

    /**
     * A constant for the index of the first usable byte of an image
     */
    protected static final int imageInfoByteOffset = 54;


    /**
     * Default constructor to create a stego object, doesn't do anything - so we actually don't need to declare it explicitly. Oh well.
     */

    public Stego() {

    }

    /**
     * A method for hiding a string in an uncompressed image file such as a .bmp or .png
     * You can assume a .bmp will be used
     *
     * @param cover_filename - the filename of the cover image as a string including the extension
     * @param payload        - the string which should be hidden in the cover image.
     * @return a string which either contains 'Fail' or the name of the stego image (including the extension) which has been
     * written out as a result of the successful hiding operation.
     * You can assume that the images are all in the same directory as the java files
     */
    public String hideString(String payload, String cover_filename) {
        try {
            BufferedImage image = ImageIO.read(new File(cover_filename));
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ImageIO.write(image, "bmp", byteStream);
            byte[] imageBytes = byteStream.toByteArray();

            if ((payload.length() * byteLength) > (imageBytes.length - (sizeBitsLength + imageInfoByteOffset))) {
                System.out.println("Payload too large for supplied image.");
                return "Fail";
            } else {
                hideBits(ByteBuffer.allocate(4).putInt(payload.length()).array(), imageBytes, imageInfoByteOffset);
                hideBits(payload.getBytes(), imageBytes, imageInfoByteOffset + sizeBitsLength);
                InputStream in = new ByteArrayInputStream(imageBytes);
                BufferedImage stegoImage = ImageIO.read(in);
                String outputFilePath = "stegoImageWithHiddenText.bmp";
                ImageIO.write(stegoImage, "bmp", new File(outputFilePath));
                return outputFilePath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Fail";
    }

    /**
     * The extractString method should extract a string which has been hidden in the stegoimage
     *
     * @param stego_image name of the stego image including the extension
     * @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
     * was unsuccessful
     */
    public String extractString(String stego_image) {
        try {
            BufferedImage image = ImageIO.read(new File(stego_image));
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ImageIO.write(image, "bmp", byteStream);
            byte[] imageBytes = byteStream.toByteArray();

            byte[] payloadSizeBytes = extractBitsFromBytes(imageBytes, 4, imageInfoByteOffset);
            int payloadSize;
            ByteBuffer byteBuffer = ByteBuffer.wrap(payloadSizeBytes);
            payloadSize = byteBuffer.getInt();
            byte[] payloadBytes = extractBitsFromBytes(imageBytes, payloadSize, imageInfoByteOffset + sizeBitsLength);

            String payloadString = "";
            for (int i = 0; i < payloadBytes.length; i++) {
                payloadString = payloadString.concat(String.valueOf((char) payloadBytes[i]));
            }
            return payloadString;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Fail";
    }

    /**
     * The hideFile method hides any file (so long as there's enough capacity in the image file) in a cover image
     *
     * @param file_payload - the name of the file to be hidden including the extension, you can assume it is in the same directory as the program
     * @param cover_image  - the name of the cover image file including the extension, you can assume it is in the same directory as the program
     * @return String - either 'Fail' to indicate an error in the hiding process, or the name of the stego image (including the extension) written out as a result of the successful hiding process
     */
    public String hideFile(String file_payload, String cover_image) {

        try {
            BufferedImage image = ImageIO.read(new File(cover_image));
            ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
            ImageIO.write(image, "bmp", imageByteStream);
            byte[] imageBytes = imageByteStream.toByteArray();

            File file = new File(file_payload);
            byte[] payloadBytes = Files.readAllBytes(file.toPath());

            String fileType = file.getPath().split("\\.")[1];
            byte[] fileTypeBytes = new byte[8];
            System.arraycopy(fileType.getBytes(), 0, fileTypeBytes, 0, fileType.getBytes().length);

            if( ( ( payloadBytes.length + fileTypeBytes.length) * byteLength) > imageBytes.length){
                System.out.println("Payload too large for supplied image.");
                return "Fail";
            }else {
                hideBits(ByteBuffer.allocate(4).putInt(payloadBytes.length).array(), imageBytes, imageInfoByteOffset);
                hideBits(fileTypeBytes, imageBytes, imageInfoByteOffset + sizeBitsLength);
                hideBits(payloadBytes, imageBytes, imageInfoByteOffset + sizeBitsLength + extBitsLength);

                InputStream in = new ByteArrayInputStream(imageBytes);
                BufferedImage stegoImage = ImageIO.read(in);
                ImageIO.write(stegoImage, "bmp", new File("stegoImageWithHiddenFile.bmp"));
                return "stegoImageWithHiddenFile.bmp";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Fail";
    }

    /**
     * The extractFile method hides any file (so long as there's enough capacity in the image file) in a cover image
     *
     * @param stego_image - name of the cover image (including the extension) from which to extract, you can assume it is in the same directory as the program
     * @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file (including the extension) written out as a
     * result of the successful extraction process
     */
    public String extractFile(String stego_image) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(stego_image));
            ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
            ImageIO.write(image, "bmp", imageByteStream);
            byte[] imageBytes = imageByteStream.toByteArray();

            byte[] payloadSizeBytes = extractBitsFromBytes(imageBytes, 4, imageInfoByteOffset);
            int payloadSize = ByteBuffer.wrap(payloadSizeBytes).getInt();

            byte[] extensionBits = extractBitsFromBytes(imageBytes, 8, imageInfoByteOffset + sizeBitsLength);
            String extensionString = "";
            for (int i = 0; i < extensionBits.length; i++) {
                extensionString = extensionString.concat(String.valueOf((char) extensionBits[i]));
            }
            extensionString = extensionString.trim();

            byte[] payload = extractBitsFromBytes(imageBytes, payloadSize, imageInfoByteOffset + sizeBitsLength + extBitsLength);
            FileOutputStream fileOutputStream = new FileOutputStream("extractedFile." + extensionString);
            fileOutputStream.write(payload);
            fileOutputStream.close();
            return "extractedFile." + extensionString;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Fail";
    }

    /**
     * General purpose method to hide a byte[] within another byte[] using a least-significant bit replacement approach
     * @param bytesToHide - array of bytes containing the information to be hidden
     * @param bytesToHideIn - array of bytes in which bytesToHide will be hidden
     * @param startIndexOfUsableBytes - index of the first byte within bytesToHideIn that can be used to hide other bytes
     * @return boolean - true if the bytesToHide have been successfully hidden within bytesToHideIn, false otherwise
     */
    private static boolean hideBits(byte[] bytesToHide, byte[] bytesToHideIn, int startIndexOfUsableBytes) {
        if (bytesToHide.length > bytesToHideIn.length - startIndexOfUsableBytes) {
            System.out.println("Error hiding info - payload too large for supplied cover file.");
            return false;
        }

        int byteToHideInCounter = startIndexOfUsableBytes;
        for (int i = 0; i < bytesToHide.length; i++) {
            byte byteToHide = bytesToHide[i];
            for (int bit = 7; bit >= 0; bit--) {
                int bitToHide = getBit(byteToHide, bit);
                if (bitToHide == 1) {
                    bytesToHideIn[byteToHideInCounter] |= 0x1;
                } else if (bitToHide == 0) {
                    bytesToHideIn[byteToHideInCounter] &= ~0x1;
                } else {
                    System.out.println("Error hiding info - string supplied is not in binary format.");
                    return false;
                }
                byteToHideInCounter++;
            }
        }
        return true;
    }

    /**
     * General purpose method to extract a byte[] from within another byte[] using a least-significant bit extraction approach
     * @param bytesWithHiddenInfo - array of bytes whose LSBs contain information to be extracted
     * @param numOfPayloadBytes - number of bytes to be extracted from bytesWithHiddenInfo
     * @param startIndexOfUsableBytes - index of the first byte within bytesWithHiddenInfo that contains hidden LSB information
     * @return byte[] containing any info successfully extracted fomr bytesWithHiddenInfo
     */
    private static byte[] extractBitsFromBytes(byte[] bytesWithHiddenInfo, int numOfPayloadBytes, int startIndexOfUsableBytes) {
        byte[] extractedBytes = new byte[numOfPayloadBytes];
        int byteCounter = startIndexOfUsableBytes;
        for (int i = 0; i < numOfPayloadBytes; i++) {
            byte currentByte = 0x0;
            for (int j = 0; j < 8; j++) {
                currentByte = (byte) (currentByte << 1);
                int lsb = bytesWithHiddenInfo[byteCounter] & 0x1;
                if (lsb == 0) {
                    currentByte &= ~0x1;
                } else if (lsb == 1) {
                    currentByte |= 0x1;
                }
                byteCounter++;
            }
            extractedBytes[i] = currentByte;
        }
        return extractedBytes;
    }

    /**
     * This method swaps the least significant bit of a byte to match the bit passed in
     *
     * @param bitToHide - the bit which is to replace the lsb of the byte
     * @param byt       - the current byte
     * @return the altered byte
     */
    public int swapLsb(int bitToHide, int byt) {
        if (bitToHide == 1) {
            return byt |= 0x1;
        }
        return byt &= ~0x1;
    }

    public static int getBit(byte input, int position) {
        return (input >> position) & 1;
    }


}
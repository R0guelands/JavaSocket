package br.com.up.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

public class ImageToASCII {

    // Variable to store the image from the link
    private BufferedImage img;

    // Variable to store the pixel value for each pixel 
    private double pixval;

    // Variable to store the ASCII 'image'
    private String image = "\n";

    // Main function to convert image to ASCII
    public String convertToASCII(String imgurl) {

        // Try catch because the url can be wrong or don't work 
        try {

            // Gets the image from url
            URL url = new URL(imgurl);

            // Reads the image
            img = ImageIO.read(url);

            // Resizes the image to a fixed size
            if (img.getHeight() > 100 && img.getWidth() > 45) {

                // Resizes the image 
                img = resizeImage(img, 100, 45);

                // Return error if the image resizing fails
                if (img == null) {
                    return "Erro";
                }

            }

        } catch (IOException e) {

            return "Erro";

        }

        // Main loop to go pixel by pixel getting the pixel sum and placing a coresponding ASCII charactere for that sum
        for (int i = 0; i < img.getHeight(); i++) {

            // Each pixel has x and y, so this is the 'y' part of the loop
            for (int j = 0; j < img.getWidth(); j++) {

                // Reads the color 'RGB' from the pixel
                Color pixcol = new Color(img.getRGB(j, i));

                // Sums the colors
                pixval = (((pixcol.getRed() * 0.30) + (pixcol.getBlue() * 0.59) + (pixcol.getGreen() * 0.11)));

                // Gets an ASCII character for that color
                image += strChar(pixval);

            }

            // Breaks the line
            try {

                image += "\n";

            } catch (Exception e) {
            }

        }

        // Returns the full ASCII 'image'
        return image;

    }

    // For each color 'part' asigns an ASCII character
    public String strChar(double g) {

        String str = " ";

        if (g >= 240) {
            str = " ";
        } else if (g >= 210) {
            str = ".";
        } else if (g >= 190) {
            str = "*";
        } else if (g >= 170) {
            str = "+";
        } else if (g >= 120) {
            str = "^";
        } else if (g >= 110) {
            str = "&";
        } else if (g >= 80) {
            str = "8";
        } else if (g >= 60) {
            str = "#";
        } else {
            str = "@";
        }

        // Returns the character
        return str;

    }

    // Function to resize the image
    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {

        try {

            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            graphics2D.dispose();
            return resizedImage;

        } catch (Exception e) {

            return null;
            
        }

    }
}

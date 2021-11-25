package imagescalerfx.utils;

import imagescalerfx.model.ImageData;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h1>Class File Utils</h1>
 * <p>Class to handle the operations of load, save, resize and scale {@link ImageData}  data objects into the
 * corresponding files</p>
 * @author Francisco David Manzanedo Valle
 * @version 1.0
 */
public class IOUtils
{

    private static void resize(String inputImagePath,
                               String outputImagePath, int scaledWidth, int scaledHeight){
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(inputFile);
        } catch (IOException e) { e.printStackTrace(); }

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

        // writes to output file
        try {
            ImageIO.write(outputImage, formatName, new File(outputImagePath));
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * It scale an input image to a given percentage.
     * @param inputImagePath Image to scale.
     * @param outputImagePath Resulting image, already scaled.
     * @param percent Scaling percentage.
     */
    public static void resize(String inputImagePath,
                              String outputImagePath, double percent) {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }

    /**
     * Recursively deletes the directory referenced by the given path,
     * including every files and folders that it may have.
     * @param path Directory to delete.
     */
    public static void deleteDirectory(Path path) {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries)
                    deleteDirectory(entry);

            } catch (IOException e) { e.printStackTrace(); }
        }

        try {
            Files.delete(path);
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Get the file names and paths of the files from a given folder.
     * @param folderName String name of the folder.
     * @return ArrayList of an {@link ImageData} objects.
     */
    public static List<ImageData>getImages(String folderName){

        try {
            if(new File(folderName).exists()){
                return Files.list(Path.of(folderName))
                        .filter(Files::isRegularFile)
                        .map(p -> new ImageData(p.getFileName().toString(),
                               Path.of (p.toString().replace(".jpg", ""))))
                        .collect(Collectors.toList());

            }else MessageUtils.showError("Error", "File not found");

        }catch (IOException ex){ ex.printStackTrace(); }

        return new ArrayList<>();
    }


    /**
     *  Get the number of files stored in a given folder.
     * @param folderName String name of the folder.
     * @return Integer with the number of files.
     */
    public static int getNumFiles(String folderName){
        if(new File(folderName).exists())
            return Objects.requireNonNull(new File(folderName).list()).length;
        else
            return 1;
    }
}

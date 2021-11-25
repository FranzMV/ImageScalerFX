package imagescalerfx.model;

import java.nio.file.Path;

/**
 * <h1>ImageData Class</h1>
 * <p>Class to define a Image object and ist attributes</p>
 * @author Francisco David Manzanedo Valle.
 * @version 1.0
 */
public class ImageData{

    /**
     * The name of the image and its extension
     */
    private final String fileName;
    /**
     * The Path of the image
     */
    private final Path path;

    /**
     *Constructor with parameters to create a {@link ImageData} object.
     * @param fileName String with name of the image and its extension.
     * @param path  Path with the path of the image.
     */
    public ImageData(String fileName, Path path) {
        this.fileName = fileName;
        this.path = path;
    }

    /**
     *Returns the name of the image and its extension.
     * @return String with the name of the image.
     */
    public String getFileName() { return fileName; }

    /**
     *Returns the path of the image.
     * @return Path corresponding to the image.
     */
    public Path getPath() { return path; }

    /**
     *Returns the name of the image.
     * @return String with the name of the image.
     */
    @Override
    public String toString() { return fileName; }

}

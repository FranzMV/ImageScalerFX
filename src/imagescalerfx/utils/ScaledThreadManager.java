package imagescalerfx.utils;

import imagescalerfx.model.ImageData;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * <h1>ScaledThreadManager</h1>
 * <p>Class to manage the process to scaling an image.</p>
 * @author Francisco David Manzanedo Valle
 * @version 1.0.
 */
public class ScaledThreadManager implements Runnable{

    /**
     * An {@link ImageData} object.
     */
    private final ImageData imageData;
    /**
     * The path of the folder that store the image.
     */
    private final String folderPath;
    /**
     * ListView to update the current list of images in the layout
     */
    private final ListView<ImageData> imageDataListView;
    /**
     * Name of the folder that store the images.
     */
    private static final String FOLDER_NAME = "images";


    /**
     * Initialize a {@link Thread} from the image and the list thaw will be update when the thread finished.
     * @param imageData {@link ImageData} object to be scaled
     * @param imageDataListView ListView that contains the names of the images.
     */
    public ScaledThreadManager(ImageData imageData, ListView<ImageData> imageDataListView){
        this.imageData = imageData;
        this.imageDataListView = imageDataListView;
        folderPath = imageData.getPath().toString()+File.separator+imageData.getFileName();

    }

    /**
     * Search a subfolder inside of the images folder with the same name that an associate image,
     * if the subfolder exists, then its delete an created again.<br>
     * Then, de images are scaling from 10% to 90% of its original size.<br>
     * Each resulting scaled image is stored in its associate subfolder with a prefix indicating scaling factor.<br>
     * Finally, the images filenames are update in a ListView.
     */
    @Override
    public void run() {
        if (new File(folderPath).exists()){
            try {
                IOUtils.deleteDirectory(Path.of(imageData.getPath().toString()));
            } catch (Exception ex) {
                MessageUtils.showError("Error", "An error has occurred while deleting: "+folderPath);
                ex.printStackTrace();
            }
        }

        try {
            Files.createDirectories(Path.of(imageData.getPath().toString()));
        } catch (Exception ex) {
            MessageUtils.showError("Error", "An error has occurred while creating: "+folderPath);
            ex.printStackTrace();
        }

        for (int i = 10; i <= 90; i += 10){
            try {
                IOUtils.resize(FOLDER_NAME+ File.separator+imageData.getFileName(),
                        imageData.getPath().toString() + File.separator + i + "_" + imageData.getFileName(),
                        i / 100.0);

            } catch (Exception ex) {
                MessageUtils.showError("Error", "An error has occurred while resize: "+imageData.getFileName());
                ex.printStackTrace();
            }
        }
        Platform.runLater(()->imageDataListView.getItems().add(imageData));
    }
}

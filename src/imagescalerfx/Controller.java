package imagescalerfx;

import imagescalerfx.model.ImageData;
import imagescalerfx.utils.IOUtils;
import imagescalerfx.utils.MessageUtils;
import imagescalerfx.utils.ScaledThreadManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


/**
 * <h1>Class Controller</h1>
 * <p>Controller in charge of managing the main window</p>
 * @author Francisco David Manzanedo Valle
 * @version 1.0
 */
public class Controller implements Initializable {

    @FXML private Button btnChart;
    @FXML private ImageView imageViewSelectedImage;
    @FXML private ListView<ImageData> listViewScaledInstances;
    @FXML private ListView<ImageData> listViewImages;
    @FXML private Label labelStatus;
    @FXML private Button btnStart;

    private ThreadPoolExecutor executor;
    private ScheduledService<Boolean> scheduledService;
    private List<ScaledThreadManager> scaledThreadManager;
    private List<ScaledThreadManager> threadManagerAux;

    private static final String FOLDER_NAME = "images";

    /**
     * Initialize method
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        threadManagerAux = new ArrayList<>();
        scaledThreadManager  = new ArrayList<>();
        btnChart.setDisable(true);
    }

    /**
     *Click Event to start scaling the images
     * Get the images from a given folder an creates an executor with the number o files stored on it.
     * Creates a {@link ScaledThreadManager} List that stores {@link ImageData} objects.
     * The {@link ImageData} objects are passed to the {@link ScaledThreadManager} constructor
     * that manages the threads to scaling the images.
     */
    @FXML
    private void StartScalingImages() {
        manageControls(true);
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(IOUtils.getNumFiles(FOLDER_NAME));
        threadManagerAux = IOUtils.getImages(FOLDER_NAME)
                .stream()
                .map(imageData -> new ScaledThreadManager(imageData, listViewImages))
                .collect(Collectors.toList());

        if(threadManagerAux.size() > 0) {
            scaledThreadManager = threadManagerAux;
            scaledThreadManager.forEach(executor::execute);
            handleScheduledService(executor);
            executor.shutdown();
            scheduledService.restart();
        }
    }


    /**
     *Additional method to manage the ScheduledService of the pool executor.
     * @param executor ThreadPoolExecutor to scaling the images.
     */
    private void handleScheduledService(ThreadPoolExecutor executor){
        scheduledService = new ScheduledService<>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<>() {
                    @Override
                    protected Boolean call() {
                        Platform.runLater(() ->
                                labelStatus.setText(executor.getCompletedTaskCount()
                                + " of "
                                + executor.getTaskCount()
                                + " tasks finished."));
                        return executor.isTerminated();
                    }
                };
            }
        };
        scheduledService.setDelay(Duration.millis(500));
        scheduledService.setPeriod(Duration.seconds(1));
        scheduledService.setOnSucceeded( s ->{
            if(scheduledService.getValue()){
                scheduledService.cancel();
                manageControls(false);
            }
        });
    }

    /**
     *Click Event to show the corresponding scaled image in the ListView of the scaled images.
     */
    @FXML
    private void OnClickedOriginalImage() {
        ImageData imageSelected = listViewImages.getSelectionModel().getSelectedItem();
        if(imageSelected != null) {
            final String folderPath = imageSelected.getPath().toString().replace(".jpg","");
            listViewScaledInstances.setItems(FXCollections.observableList(IOUtils.getImages(folderPath)));

        }else MessageUtils.showMessage("Information", "Nothing Selected");
    }


    /**
     *Click Event to show the image selected by the user in the imageView
     */
    @FXML
    private void OnClickedScaledImages() {
        ImageData imageDataSelected = listViewScaledInstances.getSelectionModel().getSelectedItem();
        if(imageDataSelected != null)
            imageViewSelectedImage.setImage(new Image(new File(imageDataSelected.getPath().toString()).toURI() +".jpg"));

        else MessageUtils.showMessage("Information", "Nothing Selected");
    }

    /**
     *Helper method to manage the different states of the layout controls
     * @param disable Boolean Set if the controls are enable or disable.
     */
    private void manageControls(boolean disable){
        if(disable){
            btnChart.setDisable(true);
            btnStart.setDisable(true);
            listViewScaledInstances.getItems().clear();
            imageViewSelectedImage.setImage(null);
            listViewImages.getItems().clear();

        }else {
            btnChart.setDisable(false);
            btnStart.setDisable(false);
            listViewScaledInstances.getItems();
        }
    }
}

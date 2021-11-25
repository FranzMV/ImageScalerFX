package imagescalerfx.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
*  <h1>Class MessageUtils</h1>
*  <p>Class to handle some additional context messages</p>
*  @author Francisco David Manzanedo Valle
*  @version 1.0
*/
public class MessageUtils {


    /**
     * Static method to handle a built-in Error dialog.
     * @param header String corresponding to the header of the message
     * @param message String corresponding to the actual error.
     */
    public static void showError(String header,String message){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Static method to handle a built-in Information Message dialog
     * @param header String corresponding to the header of the message
     * @param message String corresponding to the actual message
     */
    public static void showMessage(String header,String message){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Static method to handle a built-in Confirmation dialog
     * @param header String corresponding to the header of the message
     * @param message String corresponding to the actual warning message
     * @return boolean to check the option selected by the user
     */
    public static boolean showConfirmation(String header, String message){

        boolean confirmation = false;

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Confirmation");
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        Optional<ButtonType> result = dialog.showAndWait();

        //If the confirmation is OK, we returns true so that the item can be removed
        if (result.isPresent() && result.get() == ButtonType.OK)
            confirmation = true;

        return confirmation;
    }
}

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class QuantityScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button BackButton;

    @FXML
    private ChoiceBox<Integer> QuantityDropBox;

    @FXML
    private Button ProceedButton;

    @FXML
    void BackButtonPressed(ActionEvent event) throws IOException {

        /*
        Stage stage = null;
        Parent root = null;

        if(event.getSource()==BackButton){
            stage = (Stage) BackButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ProductInfoScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

         */

    }

    @FXML
    void ProceedButtonPressed(ActionEvent event) throws IOException {

        /*

        System.out.println(QuantityDropBox.getSelectionModel().getSelectedItem());
        selectedQuantityDropBox = QuantityDropBox.getSelectionModel().getSelectedItem();


        Stage stage = null;
        Parent root = null;

        if(event.getSource()==ProceedButton){
            stage = (Stage) ProceedButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("CartScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

         */

    }



    @FXML
    void initialize() {
        assert BackButton != null : "fx:id=\"BackButton\" was not injected: check your FXML file 'QuantityScreen.fxml'.";
        assert QuantityDropBox != null : "fx:id=\"QuantityDropBox\" was not injected: check your FXML file 'QuantityScreen.fxml'.";
        assert ProceedButton != null : "fx:id=\"ProceedButton\" was not injected: check your FXML file 'QuantityScreen.fxml'.";

        /*
        for(int j = 0; j < MainMenuScreenController.selectedQuantity; j++){
            QuantityDropBox.getItems().add(j+1);
        }

         */
    }
}

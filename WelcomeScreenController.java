import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WelcomeScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button LoginInButton;

    @FXML
    private Button SignInButton;

    @FXML
    void LoginButtonPressed(ActionEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==LoginInButton){
            stage = (Stage) LoginInButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("LoginInScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void SignInButtonPressed(ActionEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==SignInButton){
            stage = (Stage) SignInButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("SignInScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void initialize() {
        assert LoginInButton != null : "fx:id=\"LoginInButton\" was not injected: check your FXML file 'WelcomeScreen.fxml'.";
        assert SignInButton != null : "fx:id=\"SignInButton\" was not injected: check your FXML file 'WelcomeScreen.fxml'.";

    }
}

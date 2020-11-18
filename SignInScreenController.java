import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignInScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField FNameTextField;

    @FXML
    private TextField MInitTextField;

    @FXML
    private TextField LNameTextField;

    @FXML
    private TextField OrganizationTextField;

    @FXML
    private TextField GenderTextField;

    @FXML
    private TextArea AddressTextField;

    @FXML
    private TextField ZipCodeTextField;

    @FXML
    private TextField EmailAddressTextField;

    @FXML
    private TextField PhoneNumberTextField;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Button RegisterButton;

    @FXML
    private Label UserDetailLabel;

    @FXML
    private Button BackButton;

    @FXML
    void BackButtonPressed(ActionEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==BackButton){
            stage = (Stage) BackButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void RegisterButtonPressed(ActionEvent event) throws IOException {

        String sqlInsertMember = "INSERT INTO MEMBERS (first_name, middle_initial, last_name, organization, gender, address, zip_code, email, phone_num, password) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Stage stage = null;
        Parent root = null;

        try {

            conn = ConnectionDB.dbConnection();
            prepState = conn.prepareStatement(sqlInsertMember);

            prepState.setString(1, FNameTextField.getText());
            prepState.setString(2, MInitTextField.getText());
            prepState.setString(3, LNameTextField.getText());

            prepState.setString(4, OrganizationTextField.getText());
            prepState.setString(5, GenderTextField.getText());

            prepState.setString(6, AddressTextField.getText());
            prepState.setInt(7, Integer.parseInt(ZipCodeTextField.getText()));

            prepState.setString(8, EmailAddressTextField.getText());
            prepState.setInt(9, Integer.parseInt(PhoneNumberTextField.getText()));
            prepState.setString(10, PasswordTextField.getText());

            prepState.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(event.getSource()==RegisterButton){
            stage = (Stage) RegisterButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static PreparedStatement prepState = null;
    public static Connection conn = null;

    @FXML
    void initialize() {
        assert FNameTextField != null : "fx:id=\"FNameTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert MInitTextField != null : "fx:id=\"MInitTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert LNameTextField != null : "fx:id=\"LNameTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert OrganizationTextField != null : "fx:id=\"OrganizationTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert GenderTextField != null : "fx:id=\"GenderTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert AddressTextField != null : "fx:id=\"AddressTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert ZipCodeTextField != null : "fx:id=\"ZipCodeTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert EmailAddressTextField != null : "fx:id=\"EmailAddressTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert PhoneNumberTextField != null : "fx:id=\"PhoneNumberTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert PasswordTextField != null : "fx:id=\"PasswordTextField\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert RegisterButton != null : "fx:id=\"RegisterButton\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert UserDetailLabel != null : "fx:id=\"UserDetailLabel\" was not injected: check your FXML file 'SignInScreen.fxml'.";
        assert BackButton != null : "fx:id=\"BackButton\" was not injected: check your FXML file 'SignInScreen.fxml'.";

    }
}


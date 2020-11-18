import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;


public class LoginInScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label EmailAddressLabel;

    @FXML
    private Label PasswordLabel;

    @FXML
    private TextField EmailAddressTextField;

    @FXML
    private Button LoginInButton;

    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private ImageView RattlerLabelLogin;

    @FXML
    private Button BackButton;

    @FXML
    void LoginInButtonPressed(ActionEvent event) throws SQLException, IOException {

        Window owner = LoginInButton.getScene().getWindow();

        System.out.println(EmailAddressTextField.getText());
        System.out.println(PasswordTextField.getText());

        if (EmailAddressTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email id");
            return;
        }
        if (PasswordTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        EmailAddressString = EmailAddressTextField.getText();
        PasswordString = PasswordTextField.getText();


        String sqlLoginIn = "SELECT * FROM MEMBERS WHERE email = ? AND password = ?";
        String sqlEmail = "";
        String sqlPassword = "";
        conn = ConnectionDB.dbConnection();

        prepState = conn.prepareStatement(sqlLoginIn);
        ResultSet rs;
        prepState.setString(1, EmailAddressString);
        prepState.setString(2, PasswordString);
        rs = prepState.executeQuery();


//        if(rs.next()){
//            MemberID = rs.getInt("member_id");
//            AllowLogin = true;
//        }

        if(rs.next() == false){
            AllowLogin = false;
        }
        else {
            do{
                MemberID = rs.getInt("Member_id");
                AllowLogin = true;
            }while(rs.next());
        }


        System.out.println(AllowLogin);
        if (AllowLogin){
            Stage stage = null;
            Parent root = null;

            if(event.getSource()==LoginInButton){
                stage = (Stage) LoginInButton.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("MainMenuScreen.fxml"));
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            showAlert(Alert.AlertType.ERROR, owner, "Login Error!",
                    "Please enter a valid password or email");
        }


    }

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
    void RattlerLogInClicked(MouseEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource() == RattlerLabelLogin){
            stage = (Stage) RattlerLabelLogin.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private static String EmailAddressString;
    private static String PasswordString;
    public static Boolean AllowLogin = false;
    public static PreparedStatement prepState = null;
    public static Connection conn = null;
    public static int MemberID;

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @FXML
    void initialize() {
        assert EmailAddressLabel != null : "fx:id=\"EmailAddressLabel\" was not injected: check your FXML file 'LoginInScreen.fxml'.";
        assert PasswordLabel != null : "fx:id=\"PasswordLabel\" was not injected: check your FXML file 'LoginInScreen.fxml'.";
        assert EmailAddressTextField != null : "fx:id=\"EmailAddressTextField\" was not injected: check your FXML file 'LoginInScreen.fxml'.";
        assert LoginInButton != null : "fx:id=\"LoginInButton\" was not injected: check your FXML file 'LoginInScreen.fxml'.";
        assert PasswordTextField != null : "fx:id=\"PasswordTextField\" was not injected: check your FXML file 'LoginInScreen.fxml'.";

    }
}

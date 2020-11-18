import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CheckOutScreenController {

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
    private TextField CardNumberTextField;

    @FXML
    private TextField EmailAddressTextField;

    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private TableView<Product> CheckOutTableView;

    @FXML
    private TableColumn<Product, Integer> CheckOutProductIDColumn;

    @FXML
    private TableColumn<Product, String> CheckOutProductNameColumn;

    @FXML
    private TableColumn<Product, Integer> CheckOutQuantityColumn;

    @FXML
    private TableColumn<Product, String> CheckOutCostColumn;

    @FXML
    private TextField TotalAmountTextField;

    @FXML
    private Button ProcessTransactionButton;

    @FXML
    private Button MyCartButton;

    @FXML
    private Button MyProfileButton;

    private static int loggedInUser;
    private static int lastInsertId = 0;
    private static int invoiceNum = 0 ;

    @FXML
    void MyCartButtonPressed(ActionEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==MyCartButton){
            stage = (Stage) MyCartButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("CartScreen.fxml"));
        }

        CartScreenController.CartScreenList.remove(CartScreenController.CartScreenList.size()-1);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void MyProfileButtonPressed(ActionEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==MyProfileButton){
            stage = (Stage) MyProfileButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ProfileScene.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void ProcessTransactionButtonPressed(ActionEvent event) throws IOException, SQLException {

        Window owner = ProcessTransactionButton.getScene().getWindow();

        Fname = FNameTextField.getText();
        MInit = MInitTextField.getText();
        LName = LNameTextField.getText();
        CCard_Number = CardNumberTextField.getText();
        Email = EmailAddressTextField.getText();
        Password = PasswordTextField.getText();

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

        if (CardNumberTextField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a credit card number");
            return;
        }

        if (FNameTextField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter first name");
            return;
        }

        if (LNameTextField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter last name");
            return;
        }

        boolean correctLogin = false;

        correctLogin = checkEmailandPass(Email, Password);
        if(!correctLogin){
            showAlert(Alert.AlertType.ERROR, owner, "Login Error!",
                    "Please enter a valid password or email");
        }
        else {
            createOrder();
            createOrderItems();
            createInvoice();
            savePaymentMethod();
            createPayment();

            CartScreenController.CartScreenList.clear();
            MainMenuScreenController.selectedProductID = 0;

            Stage stage = null;
            Parent root = null;

            if (event.getSource() == ProcessTransactionButton) {
                stage = (Stage) ProcessTransactionButton.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("MainMenuScreen.fxml"));
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }


    }

    private void savePaymentMethod() throws SQLException{
        String sqlInsertPAYMENTMETHOD = "INSERT INTO PAYMENT_METHOD (Customer_ID, Pay_Method, CCard_Number) " +
                "VALUES(?, ?, ?)";

        try {
            conn = ConnectionDB.dbConnection();


            prepState = conn.prepareStatement(sqlInsertPAYMENTMETHOD);
            prepState.setInt(1, LoginInScreenController.MemberID);
            prepState.setString(2, "Credit/Debit");
            prepState.setString(3, CardNumberTextField.getText());
            prepState.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void createPayment() throws SQLException{
        String sqlInsertPAYMENTS = "INSERT INTO PAYMENTS (Invoice, Date, Amount) " +
                "VALUES(?, ?, ?)";

        try {
            conn = ConnectionDB.dbConnection();
            Date date = Date.valueOf(LocalDate.now());

                prepState = conn.prepareStatement(sqlInsertPAYMENTS);
                prepState.setInt(1, invoiceNum);
                prepState.setDate(2, date);
                prepState.setInt(3, Integer.valueOf(TotalAmountTextField.getText()));
                prepState.executeUpdate();

        }

        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void createOrderItems() throws SQLException{
        String sqlInsertORDERITEMS = "INSERT INTO ORDER_ITEMS (Prod_id, Order_ID, Quantity) " +
                "VALUES(?, ?, ?)";

        try {
            conn = ConnectionDB.dbConnection();

            for(int i = 0; i < CartScreenController.CartScreenList.size(); i++){
                prepState = conn.prepareStatement(sqlInsertORDERITEMS);
                prepState.setInt(1, CartScreenController.CartScreenList.get(i).getProduct_ID());
                prepState.setInt(2, lastInsertId);
                prepState.setInt(3, Integer.valueOf(CartScreenController.CartScreenList.get(i).getQuantity()));
                prepState.executeUpdate();

                //debugging the inserts
                System.out.println("Prod id: " + CartScreenController.CartScreenList.get(i).getProduct_ID());
                System.out.println("Order id: " + lastInsertId);
                System.out.println("Quantity: " + Integer.valueOf(CartScreenController.CartScreenList.get(i).getQuantity()));
                System.out.println();

                int decrement = ProductInfoScreenController.selectedQuantityDropBox;
                int currentQuantity = 0;

                String sqlDecrement = "Select Product_Quantity FROM PRODUCTS where product_id = ?";
                prepState = conn.prepareStatement(sqlDecrement);
                prepState.setInt(1, CartScreenController.CartScreenList.get(i).getProduct_ID());
                ResultSet set = prepState.executeQuery();

                while(set.next()){
                    currentQuantity = set.getInt("Product_Quantity");
                }

                currentQuantity -= decrement;

                String update = "UPDATE PRODUCTS " +
                        "SET Product_Quantity = ?" +
                        " WHERE Product_ID=?";

                prepState = conn.prepareStatement(update);
                prepState.setInt(1, currentQuantity);
                prepState.setInt(2, CartScreenController.CartScreenList.get(i).getProduct_ID());
                prepState.executeUpdate();

                if(currentQuantity <= 0){
                    String updateAvailability = "UPDATE PRODUCTS " +
                            "SET Availability_Status = ?, Product_Quantity = ?" +
                            " WHERE Product_ID=?";

                    prepState = conn.prepareStatement(updateAvailability);
                    prepState.setInt(1, 0);
                    prepState.setInt(2,0);
                    prepState.setInt(3, CartScreenController.CartScreenList.get(i).getProduct_ID());
                    prepState.executeUpdate();
                }

            }

            }

         catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void createOrder() throws SQLException{
        String sqlInsertORDERS = "INSERT INTO ORDERS (Customer, Completion_Status) " +
                "VALUES(?, ?)";

        try {
            conn = ConnectionDB.dbConnection();

            prepState = conn.prepareStatement(sqlInsertORDERS, Statement.RETURN_GENERATED_KEYS);
            //ResultSet rs;
            prepState.setInt(1, LoginInScreenController.MemberID);
            prepState.setInt(2, 1);
            prepState.executeUpdate();

            //getting order id of just generated order
            ResultSet rs = prepState.getGeneratedKeys();
            if (rs.next()) {
                lastInsertId = rs.getInt(1);
            }
            //System.out.println(lastInsertId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void createInvoice() throws SQLException{
        String sqlInsertINVOICES = "INSERT INTO INVOICES (Order_id, Invoice_Status, Date, Details) " +
                "VALUES(?, ?, ?, ?)";

       try {
            conn = ConnectionDB.dbConnection();

            Date date = Date.valueOf(LocalDate.now());

            //debugging date
            System.out.println(date);

                prepState = conn.prepareStatement(sqlInsertINVOICES, Statement.RETURN_GENERATED_KEYS);
                prepState.setInt(1, lastInsertId);
                prepState.setString(2, "Complete");
                prepState.setDate(3, date);
                prepState.setString(4, "Transaction for " + lastInsertId);
                prepState.executeUpdate();

                //getting invoice number just generated
           ResultSet rs = prepState.getGeneratedKeys();
           if (rs.next()) {
               invoiceNum = rs.getInt(1);
           }

        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkEmailandPass(String email, String pass) throws SQLException {
        boolean AllowLogin = false;

        String sqlLoginIn = "SELECT email, password FROM MEMBERS WHERE member_id = ?";
        String checkEmail = null, checkPassword= null;

        conn = ConnectionDB.dbConnection();

        prepState = conn.prepareStatement(sqlLoginIn);
        ResultSet rs;
        prepState.setInt(1, LoginInScreenController.MemberID);
        rs = prepState.executeQuery();

        if(rs.next()){
            checkEmail = rs.getString("email");
            checkPassword = rs.getString("password");
        }

        if(email.equals(checkEmail) && pass.equals(checkPassword)){
            AllowLogin = true;
        }
        else{
            AllowLogin = false;
        }

        return AllowLogin;
    }

    private static PreparedStatement prepState = null;
    private static Connection conn = null;
    private static String Fname;
    private static String MInit;
    private static String LName;
    private static String CCard_Number;
    private static String Email;
    private static String Password;
    private static Integer TotalCost;
    public static ObservableList<Product> CheckoutScreenBuyNow = FXCollections.observableArrayList();
    public static String CheckOutBuyNowSQL = "SELECT Product_ID, Product_name, Product_Price, Product_Quantity FROM PRODUCTS WHERE Product_ID = ?";


    private static void infoBox(String infoMessage, String headerText, String title) {
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
    void initialize() throws SQLException {
        assert FNameTextField != null : "fx:id=\"FNameTextField\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert MInitTextField != null : "fx:id=\"MInitTextField\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert LNameTextField != null : "fx:id=\"LNameTextField\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert CardNumberTextField != null : "fx:id=\"CardNumberTextField\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert EmailAddressTextField != null : "fx:id=\"EmailAddressTextField\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert PasswordTextField != null : "fx:id=\"PasswordTextField\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert CheckOutTableView != null : "fx:id=\"CheckOutTableView\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert CheckOutProductIDColumn != null : "fx:id=\"CheckOutProductIDColumn\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert CheckOutProductNameColumn != null : "fx:id=\"CheckOutProductNameColumn\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert CheckOutQuantityColumn != null : "fx:id=\"CheckOutQuantityColumn\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert CheckOutCostColumn != null : "fx:id=\"CheckOutCostColumn\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert TotalAmountTextField != null : "fx:id=\"TotalAmountTextField\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert ProcessTransactionButton != null : "fx:id=\"ProcessTransactionButton\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert MyCartButton != null : "fx:id=\"MyCartButton\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";
        assert MyProfileButton != null : "fx:id=\"MyProfileButton\" was not injected: check your FXML file 'CheckOutScreen.fxml'.";



        //clearing for new screen
        //CheckoutScreenBuyNow.clear();

        //getting the connection
        conn = ConnectionDB.dbConnection();

        //calling method to execute a sql statement and get a observable list of product objects.
        //createListOfProduct(CheckOutBuyNowSQL);

        //Setting the columns of the GUI with the Object and the type it takes for the column
        CheckOutProductIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
        CheckOutProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
        CheckOutCostColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Cost"));
        CheckOutQuantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Quantity"));

        //if(CartScreenController.CartScreenList.contains()
        //CheckOutTableView.setItems(CartScreenController.CartScreenList);
        CheckOutTableView.setItems(CartScreenController.CartScreenList);

        //System.out.println(CheckoutScreenBuyNow.get(0).getCost());

        int total = 0;
        for(int i = 0; i < CartScreenController.CartScreenList.size(); i++){
            total += CartScreenController.CartScreenList.get(i).getCost();
        }
        TotalAmountTextField.setText(Integer.toString(total));

    }

    public static void createListOfProduct(String sqlStatement) throws SQLException {

        try {
            prepState = conn.prepareStatement(sqlStatement);
            prepState.setInt(1, MainMenuScreenController.selectedProductID);
            ResultSet rs;
            rs = prepState.executeQuery();

            while (rs.next()) {

                Integer id = rs.getInt("Product_ID");
                String name = rs.getString("Product_name");
                //String quantity = rs.getString("Product_Quantity");
                String quantity = String.valueOf(ProductInfoScreenController.selectedQuantityDropBox);
                Integer cost = rs.getInt("Product_Price");
                //Integer TotalCost = QuantityScreenController.selectedQuantityDropBox * rs.getInt("Product_price)");
                CheckoutScreenBuyNow.add(new Product(id, name, cost * ProductInfoScreenController.selectedQuantityDropBox, quantity));

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Table");
        }

    }
}

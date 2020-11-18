import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyProfileScreenController {

    @FXML
    private ListView<Integer> productListView;

    @FXML
    private TextField prodIDTextField;

    @FXML
    private TextField ProdNameTextField;

    @FXML
    private TextField ProdPriceTextField;

    @FXML
    private TextField ProdDescriptionTextField;

    @FXML
    private TextField ProdConditionTextField;

    @FXML
    private TextField ProdTypeTextField;


    @FXML
    private TextField ProdQuantityTextField;

    @FXML
    private TextField ProdAvailabilityTextField;

    @FXML
    private Text textNameField;

    @FXML
    private Button AddProductButton;

    @FXML
    private Button UpdateButton;

    @FXML
    private Button DeleteProductButton;

    @FXML
    private Button BackButton;

    @FXML
    private Button invoicesButton;

    @FXML
    private Button logOutButton;


    public PreparedStatement prepState = null;
    public Connection conn =null;
    private final ObservableList<Integer> productsID = FXCollections.observableArrayList();


    @FXML
    void loadInvoices(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;

        if(event.getSource()==invoicesButton){
            stage = (Stage) invoicesButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("InvoicesScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        LoginInScreenController.MemberID = 0;

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==logOutButton){
            stage = (Stage) logOutButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void addProductButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Product");
        alert.setHeaderText("Instructions to Add Product");
        alert.setContentText("Enter product information and hit submit. (Product ID will be auto-generated).");

        alert.showAndWait();

        prodIDTextField.setText("");
        ProdNameTextField.setText("");
        ProdPriceTextField.setText("");
        ProdDescriptionTextField.setText("");
        ProdConditionTextField.setText("");
        ProdTypeTextField.setText("");
        ProdQuantityTextField.setText("");
        ProdAvailabilityTextField.setText("");


    }

    @FXML
    void deleteProductButton(ActionEvent event) {

        //this function got changed from delete to make product unavailable
        //int position = productListView.getSelectionModel().getSelectedIndex();
        int id = productListView.getSelectionModel().getSelectedItem();

       // String deleteItemSQL = "DELETE FROM PRODUCTS where Product_Id = ?";
        String makeUnavailableSQL = "UPDATE PRODUCTS SET Availability_Status = ? " +
                "WHERE Product_Id = ?";

        try{
            prepState = conn.prepareStatement(makeUnavailableSQL);
            prepState.setInt(1, 0);
            prepState.setInt(2, id);
            prepState.executeUpdate();
            //productsID.remove(position);
           // productListView.setItems(productsID);
           // loadListView();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Availability");
            alert.setHeaderText("Product Availability Changed");
            alert.setContentText("Product was made no longer available.");

            alert.showAndWait();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    void updateProductButton(ActionEvent event) {
        String sql1 = "INSERT INTO PRODUCTS (Type_num, Product_name, Product_Price,Product_Condition, Product_Description, Seller_ID," +
                " Availability_Status, Product_Quantity) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // if new product is being created
            if(prodIDTextField.getText().isEmpty()) {
                prepState = conn.prepareStatement(sql1);
                prepState.setInt(1, Integer.valueOf(ProdTypeTextField.getText()));
                prepState.setString(2, ProdNameTextField.getText());
                prepState.setInt(3, Integer.valueOf(ProdPriceTextField.getText()));
                prepState.setString(4, ProdConditionTextField.getText());
                prepState.setString(5, ProdDescriptionTextField.getText());
                prepState.setInt(6, testUser);
                prepState.setInt(7, Integer.valueOf(ProdAvailabilityTextField.getText()));
                prepState.setInt(8, Integer.valueOf(ProdQuantityTextField.getText()));
            }
            else{
                // if updating existing product
                String update = "UPDATE PRODUCTS " +
                        "SET Type_num=?, Product_name=?, Product_Price=?, Product_Condition=?, Product_Description=?, Seller_ID=?, Availability_Status=?, Product_Quantity=?" +
                        " WHERE Product_ID=?";
                prepState = conn.prepareStatement(update);
                prepState.setInt(1, Integer.valueOf(ProdTypeTextField.getText()));
                prepState.setString(2, ProdNameTextField.getText());
                prepState.setInt(3, Integer.valueOf(ProdPriceTextField.getText()));
                prepState.setString(4, ProdConditionTextField.getText());
                prepState.setString(5, ProdDescriptionTextField.getText());
                prepState.setInt(6, testUser);
                prepState.setInt(7, Integer.valueOf(ProdAvailabilityTextField.getText()));
                prepState.setInt(8, Integer.valueOf(ProdQuantityTextField.getText()));
                prepState.setInt(9, Integer.valueOf(prodIDTextField.getText()));
            }
            prepState.executeUpdate();

            loadListView();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ReturnScreen(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;

        if(event.getSource()==BackButton){
            stage = (Stage) BackButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainMenuScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private int testUser = LoginInScreenController.MemberID;

    public void initialize() {
        conn = ConnectionDB.dbConnection();

        loadListView();

        productListView.setItems(productsID);

        String sql2 = "SELECT Product_Name, Product_Price, Product_Description, Product_Condition, Type_num, " +
                "Product_quantity, Availability_Status from PRODUCTS where" +
                " Product_id = ?";

        productListView.getSelectionModel().selectedItemProperty().addListener(
                ((observableValue, integer, t1) -> {
                    try{
                        prepState = conn.prepareStatement(sql2);

                            prepState.setInt(1, t1);
                            ResultSet set = prepState.executeQuery();

                            prodIDTextField.setText(t1.toString());

                            while(set.next()) {
                                ProdNameTextField.setText(set.getString("Product_Name"));
                                ProdPriceTextField.setText(String.valueOf(set.getInt("Product_Price")));
                                ProdDescriptionTextField.setText(set.getString("Product_Description"));
                                ProdConditionTextField.setText(set.getString("Product_Condition"));
                                ProdTypeTextField.setText(String.valueOf(set.getInt("Type_num")));
                                ProdQuantityTextField.setText(String.valueOf(set.getInt("Product_Quantity")));
                                ProdAvailabilityTextField.setText(String.valueOf(set.getInt("Availability_Status")));
                            }
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                }
                ));


    }

    private void loadListView(){
        String sql1 = "SELECT Product_ID from PRODUCTS where Seller_ID = ?";

        productsID.clear();

        try {
            prepState = conn.prepareStatement(sql1);
            prepState.setInt(1, testUser);
            ResultSet rs = prepState.executeQuery();

            while(rs.next()){
                productsID.add(rs.getInt("Product_ID"));
            }

            String sqlName = "SELECT first_name from MEMBERS where Member_id = ?";
            prepState = conn.prepareStatement(sqlName);
            prepState.setInt(1, testUser);
            rs = prepState.executeQuery();

            while(rs.next()){
                textNameField.setText("Welcome " + rs.getString("first_name") + "!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


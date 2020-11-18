import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CartScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private Button buyItemButton;

    @FXML
    private Button clearCartButton;

    @FXML
    private TableView<Product> cartTable;

    @FXML
    private TableColumn<Product, Integer> productIDColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> costColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    void buyAllItems(ActionEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==buyItemButton){
            stage = (Stage) buyItemButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("CheckOutScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();



    }

    @FXML
    void clearCart(ActionEvent event) {
        CartScreenList.clear();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==backButton){
            stage = (Stage) backButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainMenuScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    private static PreparedStatement prepState = null;
    private static Connection conn = null;
    private static String AddToCartSQL = "SELECT Product_ID, Product_name, Product_Price, Product_Quantity FROM PRODUCTS WHERE Product_ID = ?";
    public static ObservableList<Product> CartScreenList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        //PreparedStatement prepState = null;
        //Connection conn = null;
        //int selectedProductID = 1000;

        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'CartScreen.fxml'.";
        assert buyItemButton != null : "fx:id=\"buyItemButton\" was not injected: check your FXML file 'CartScreen.fxml'.";
        assert clearCartButton != null : "fx:id=\"clearCartButton\" was not injected: check your FXML file 'CartScreen.fxml'.";
        assert cartTable != null : "fx:id=\"cartTable\" was not injected: check your FXML file 'CartScreen.fxml'.";
        assert productIDColumn != null : "fx:id=\"productIDColumn\" was not injected: check your FXML file 'CartScreen.fxml'.";
        assert productNameColumn != null : "fx:id=\"productNameColumn\" was not injected: check your FXML file 'CartScreen.fxml'.";
        assert costColumn != null : "fx:id=\"costColumn\" was not injected: check your FXML file 'CartScreen.fxml'.";
        assert quantityColumn != null : "fx:id=\"quantityColumn\" was not injected: check your FXML file 'CartScreen.fxml'.";

        /*
        conn = ConnectionDB.dbConnection();

        String cartTableEntrySql = "SELECT Product_id, Product_Name, Product_Price, Product_Quantity"
                + " FROM PRODUCTS"
                + " WHERE Product_id = ?";
        try {
            prepState = conn.prepareStatement(cartTableEntrySql);
            prepState.setInt(1, selectedProductID);
            ResultSet set = prepState.executeQuery();

            //productIDTextField.setText(String.valueOf(selectedProductID));

            while(set.next()) {
                /*
                productNameTextField.setText(set.getString("Product_Name"));
                productPriceTextField.setText(String.valueOf(set.getInt("Product_Price")));
                productDescriptionTextField.setText(set.getString("Product_Description"));
                productConditionTextField.setText(set.getString("Product_Condition"));
                productTypeTextField.setText(String.valueOf(set.getInt("Type_Description")));

                productQuantityTextField.setText(String.valueOf(set.getInt("Product_Quantity")));

         */
        try{

            //getting the connection
            conn = ConnectionDB.dbConnection();

            //calling method to execute a sql statement and get a observable list of product objects.
            createListOfProduct(AddToCartSQL);


            //Setting the columns of the GUI with the Object and the type it takes for the column
            productIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
            costColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Cost"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Quantity"));

            cartTable.setItems(CartScreenList);

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createListOfProduct(String sqlStatement) throws SQLException {

        try {

            prepState = conn.prepareStatement(sqlStatement);
            prepState.setInt(1, MainMenuScreenController.selectedProductID);
            ResultSet rs;
            rs = prepState.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("Product_ID");
                String name = rs.getString("Product_name");
                //String quantity = rs.getString("Product_Quantity");
                String quantity = String.valueOf(ProductInfoScreenController.selectedQuantityDropBox);
                int cost = rs.getInt("Product_Price");
                //Integer cost = ProductInfoScreenController.selectedQuantityDropBox * rs.getInt("Product_price)");
                CartScreenList.add(new Product(id, name, cost * ProductInfoScreenController.selectedQuantityDropBox, quantity));
                System.out.println(quantity + "  " + cost + "   "  + cost * ProductInfoScreenController.selectedQuantityDropBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Table");
        }

    }
}

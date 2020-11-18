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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductInfoScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField productNameTextField;

    @FXML
    private Button backButton;

    @FXML
    private TextField productTypeTextField;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private TextField productConditionTextField;

    @FXML
    private TextField productDescriptionTextField;

    @FXML
    private TextField productIDTextField;

    @FXML
    private TextField productQuantityTextField;

    @FXML
    private Button buyButton;

    @FXML
    private Button cartButton;

    @FXML
    private ChoiceBox<Integer> ChoiceBoxQuantity;

    @FXML
    void addToCart(ActionEvent event) throws IOException {

        System.out.println(ChoiceBoxQuantity.getSelectionModel().getSelectedItem());

        selectedQuantityDropBox = ChoiceBoxQuantity.getSelectionModel().getSelectedItem();
        System.out.println(ChoiceBoxQuantity.getSelectionModel().getSelectedItem());

        //CartScreenController.CartScreenList.add(MainMenuScreenController.selectedProduct);
        Stage stage = null;
        Parent root = null;

        if(event.getSource()==cartButton){
            stage = (Stage) cartButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("CartScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void buyItem(ActionEvent event) throws IOException {

        System.out.println(ChoiceBoxQuantity.getSelectionModel().getSelectedItem());

        selectedQuantityDropBox = ChoiceBoxQuantity.getSelectionModel().getSelectedItem();
        System.out.println(ChoiceBoxQuantity.getSelectionModel().getSelectedItem());

        Product product = MainMenuScreenController.selectedProduct;
        product.setQuantity(String.valueOf(selectedQuantityDropBox));
        product.setCost(product.getCost() * selectedQuantityDropBox);

        //product.setQuantity(String.valueOf(selectedQuantityDropBox));

        //Integer id = product.getProduct_ID();
        //String name = product.getProduct_Name();
        //String quantity = product.getQuantity();
        //Integer cost = product.getCost() * selectedQuantityDropBox;

        //CartScreenController.CartScreenList.add(new Product(id, name, cost, quantity));
        CartScreenController.CartScreenList.add(product);

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==buyButton){
            stage = (Stage) buyButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("CheckOutScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();



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
    public static ObservableList<Product> cartData = FXCollections.observableArrayList();
    public static int selectedQuantityDropBox;



    @FXML
    void initialize() {

        int selectedProductID = MainMenuScreenController.selectedProductID;

        assert productNameTextField != null : "fx:id=\"productNameTextField\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert productTypeTextField != null : "fx:id=\"productTypeTextField\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert productPriceTextField != null : "fx:id=\"productPriceTextField\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert productConditionTextField != null : "fx:id=\"productConditionTextField\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert productDescriptionTextField != null : "fx:id=\"productDescriptionTextField\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert productIDTextField != null : "fx:id=\"productIDTextField\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert buyButton != null : "fx:id=\"buyButton\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert cartButton != null : "fx:id=\"cartButton\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";
        assert productQuantityTextField != null : "fx:id=\"QuantityTextField\" was not injected: check your FXML file 'ProductInfoScreen.fxml'.";

        conn = ConnectionDB.dbConnection();

        String productInfoSql = "SELECT Product_Name, Type_Description, Product_Price, Product_Condition, Product_Description, Product_Quantity"
                + " FROM PRODUCTS JOIN PRODUCT_TYPE ON Type_num = Type_number"
                + " WHERE Product_id = ?";
        try {
            prepState = conn.prepareStatement(productInfoSql);
            prepState.setInt(1, selectedProductID);
            ResultSet set = prepState.executeQuery();

            productIDTextField.setText(String.valueOf(selectedProductID));

            while(set.next()) {
                productNameTextField.setText(set.getString("Product_Name"));
                productPriceTextField.setText(String.valueOf(set.getInt("Product_Price")));
                productDescriptionTextField.setText(set.getString("Product_Description"));
                productConditionTextField.setText(set.getString("Product_Condition"));
                //productTypeTextField.setText(String.valueOf(set.getInt("Type_Description")));
                productTypeTextField.setText(set.getString("Type_Description"));
                //productQuantityTextField.setText(String.valueOf(set.getInt("Product_Quantity")));
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        for(int j = 0; j < MainMenuScreenController.selectedQuantity; j++){
            ChoiceBoxQuantity.getItems().add(j+1);
        }



    }
}

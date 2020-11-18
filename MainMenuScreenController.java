import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class MainMenuScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label WelcomeLabel;

    @FXML
    private TextField SearchProductBar;

    @FXML
    private Button MyProfileButton;

    @FXML
    private Button MyCartButton;

    @FXML
    private Button ElectronicButton;

    @FXML
    private Button DormDecor;

    @FXML
    private Button BookButton;

    @FXML
    private Button ClothesButton;

    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableColumn<Product, Integer> ProductIDColumn;

    @FXML
    private TableColumn<Product, String> ProductNameColumn;

    @FXML
    private TableColumn<Product, String> DescriptionColumn;

    @FXML
    private TableColumn<Product, Integer> CostColumn;

    @FXML
    private Button SearchButton;

    @FXML
    private Button ViewDetailButton;

    @FXML
    void BookButtonPressed(ActionEvent event) throws SQLException {

        tableView.getItems().clear();

        String ButtonSQL = "SELECT Product_ID, Product_name, Product_Description, Product_Price, Product_Quantity FROM PRODUCTS WHERE Type_num = 1";

        createListOfProduct(ButtonSQL);

        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Description"));
        CostColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Cost"));
        tableView.setItems(data);
    }

    @FXML
    void ClothesButtonPressed(ActionEvent event) throws SQLException {

        tableView.getItems().clear();

        String ClothesSQL = "SELECT Product_ID, Product_name, Product_Description, Product_Price, Product_Quantity FROM PRODUCTS WHERE Type_num = 2";

        createListOfProduct(ClothesSQL);

        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Description"));
        CostColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Cost"));
        tableView.setItems(data);

    }

    @FXML
    void DormDecorButton(ActionEvent event) throws SQLException {

        tableView.getItems().clear();

        String ButtonSQL = "SELECT Product_ID, Product_name, Product_Description, Product_Price, Product_Quantity FROM PRODUCTS WHERE Type_num = 3";

        createListOfProduct(ButtonSQL);

        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Description"));
        CostColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Cost"));
        tableView.setItems(data);

    }

    @FXML
    void ElectronicButtonPressed(ActionEvent event) throws SQLException {

        tableView.getItems().clear();

        String ButtonSQL = "SELECT Product_ID, Product_name, Product_Description, Product_Price, Product_Quantity FROM PRODUCTS WHERE Type_num = 4";

        createListOfProduct(ButtonSQL);

        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Description"));
        CostColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Cost"));
        tableView.setItems(data);

    }

    @FXML
    void MyCartButtonPressed(ActionEvent event) throws IOException{

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==MyCartButton){
            stage = (Stage) MyCartButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("CartScreen.fxml"));
        }

        if(CartScreenController.CartScreenList.size() > 1) {
            CartScreenController.CartScreenList.remove(CartScreenController.CartScreenList.size() - 1);
        }
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
    void SearchButtonPressed(ActionEvent event) throws SQLException, IOException {

        searchText = SearchProductBar.getText();
        System.out.println(searchText);

        tableView.getItems().clear();

        String SearchSQL = "SELECT Product_ID, Product_name, Product_Description, Product_Price, Product_Quantity FROM PRODUCTS WHERE Product_name = ?";

        System.out.println(SearchSQL);

        try {
            prepState = conn.prepareStatement(SearchSQL);
            ResultSet rs;
            prepState.setString(1, searchText);
            rs = prepState.executeQuery();

            while (rs.next()) {

                Integer id = rs.getInt("Product_ID");
                String name = rs.getString("Product_name");
                String description = rs.getString("Product_Description");
                Integer cost = rs.getInt("Product_Price");
                String quantity = String.valueOf(rs.getInt("Product_Quantity"));
                data.add(new Product(id, name, description, cost, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Table");
        }


    }

    @FXML
    void SearchProductBarInput(InputMethodEvent event) throws IOException {
        searchText = SearchProductBar.getText();
        System.out.println(searchText);
    }


    public static Product selectedProduct;
    public static Integer selectedQuantity;

    @FXML
    void ViewDetailButtonPressed(ActionEvent event) throws IOException {

        selectedProduct = tableView.getSelectionModel().getSelectedItem();


        selectedProductID = tableView.getSelectionModel().getSelectedItem().getProduct_ID();
        System.out.println("Selected ID is: " + selectedProductID);
        selectedQuantity = Integer.valueOf(tableView.getSelectionModel().getSelectedItem().getQuantity());
        System.out.println("Selected ID quantity: " + selectedQuantity);

        Stage stage = null;
        Parent root = null;

        if(event.getSource()==ViewDetailButton){
            stage = (Stage) ViewDetailButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ProductInfoScreen.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void WelcomeLabelClicked(MouseEvent event) throws SQLException {

        tableView.getItems().clear();

        createListOfProduct(MainSQLStatement);

        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Description"));
        CostColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Cost"));
        tableView.setItems(data);

    }

    public static ObservableList<Product> data = FXCollections.observableArrayList();
    public static int selectedProductID;
    public static PreparedStatement prepState = null;
    public static Connection conn = null;
    public static String MainSQLStatement = "SELECT Product_ID, Product_name, Product_Description, Product_Price, Product_Quantity FROM PRODUCTS WHERE Product_Quantity <> 0 AND AVAILABILITY_STATUS <> 0";
    public static String searchText = null;


    @FXML
    void initialize() throws SQLException {

        System.out.println(LoginInScreenController.MemberID);

        assert WelcomeLabel != null : "fx:id=\"WelcomeLabel\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert SearchProductBar != null : "fx:id=\"SearchProductBar\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert MyProfileButton != null : "fx:id=\"MyProfileButton\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert MyCartButton != null : "fx:id=\"MyCartButton\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert ElectronicButton != null : "fx:id=\"ElectronicButton\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert DormDecor != null : "fx:id=\"DormDecor\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert BookButton != null : "fx:id=\"BookButton\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert ClothesButton != null : "fx:id=\"ClothesButton\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert ProductIDColumn != null : "fx:id=\"ProductIDColumn\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert ProductNameColumn != null : "fx:id=\"ProductNameColumn\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert DescriptionColumn != null : "fx:id=\"DescriptionColumn\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert CostColumn != null : "fx:id=\"CostColumn\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert SearchButton != null : "fx:id=\"SearchButton\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";
        assert ViewDetailButton != null : "fx:id=\"ViewDetailButton\" was not injected: check your FXML file 'MainMenuScreen.fxml'.";

        //clearing for new screen
        data.clear();

        //getting the connection
        conn = ConnectionDB.dbConnection();

        //calling method to execute a sql statement and get a observable list of product objects.
        createListOfProduct(MainSQLStatement);

        //Setting the columns of the GUI with the Object and the type it takes for the column
        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Product_ID"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Product_Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Description"));
        CostColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Cost"));

        //put the observable list in the table view
        tableView.setItems(data);


    }

    //method to get the list of objects
    public void createListOfProduct(String sqlStatement) throws SQLException {

        try {

            prepState = conn.prepareStatement(sqlStatement);
            ResultSet rs;
            rs = prepState.executeQuery();

            while (rs.next()) {

                Integer id = rs.getInt("Product_ID");
                String name = rs.getString("Product_name");
                String description = rs.getString("Product_Description");
                Integer cost = rs.getInt("Product_Price");
                String quantity = String.valueOf(rs.getInt("Product_Quantity"));
                data.add(new Product(id, name, description, cost, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Table");
        }

    }
}

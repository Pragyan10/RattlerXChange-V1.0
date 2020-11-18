import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.javafx.image.IntPixelGetter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InvoicesScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private ListView<Integer> invoiceListView;

    @FXML
    private TextField orderIDTextField;

    @FXML
    private TextField DateTextField;

    @FXML
    private ChoiceBox<String> productNameChoiceBox;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private TextField productDescriptionTextField;

    @FXML
    private TextField productConditionTextField;

    @FXML
    private TextField productQuantityTextField;

    @FXML
    private TextField productTotalTextField;

    public PreparedStatement prepState = null;
    public Connection conn =null;
    private final ObservableList<Integer> invoiceNumbers = FXCollections.observableArrayList();

    @FXML
    void ReturnToProfile(ActionEvent event) throws IOException {
        Stage stage = null;
        Parent root = null;

        if(event.getSource()==backButton){
            stage = (Stage) backButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ProfileScene.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert invoiceListView != null : "fx:id=\"invoiceListView\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert orderIDTextField != null : "fx:id=\"orderIDTextField\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert DateTextField != null : "fx:id=\"DateTextField\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert productNameChoiceBox != null : "fx:id=\"productNameChoiceBox\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert productPriceTextField != null : "fx:id=\"productPriceTextField\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert productDescriptionTextField != null : "fx:id=\"productDescriptionTextField\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert productConditionTextField != null : "fx:id=\"productConditionTextField\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert productQuantityTextField != null : "fx:id=\"productQuantityTextField\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";
        assert productTotalTextField != null : "fx:id=\"productTotalTextField\" was not injected: check your FXML file 'InvoicesScreen.fxml'.";

        conn = ConnectionDB.dbConnection();

        loadListView();

        invoiceListView.setItems(invoiceNumbers);

        String sql1 = "SELECT Order_id, Date from INVOICES where Invoices_num = ?";
        String sql2 = "SELECT Prod_ID from ORDER_ITEMS where Order_id = ?";
        String sql3 = "Select Product_name, Product_price, Product_description, product_condition FROM PRODUCTS " +
                "WHERE product_id = ?";
        String sql4 = "SELECT Quantity FROM ORDER_ITEMS where Order_id = ? AND prod_id = ?";
        String sql5 = "Select Amount from PAYMENTS WHERE invoice = ?";

        invoiceListView.getSelectionModel().selectedItemProperty().addListener(
                ((observableValue, integer, t1) -> {
                    try{
                        productPriceTextField.clear();
                        productDescriptionTextField.clear();
                        productDescriptionTextField.clear();
                        productConditionTextField.clear();
                        productQuantityTextField.clear();
                        productNameChoiceBox.getItems().clear();

                        ArrayList<Integer> productIds = new ArrayList<>();
                        prepState = conn.prepareStatement(sql1);
                        prepState.setInt(1, t1);
                        ResultSet set = prepState.executeQuery();

                        while(set.next()) {
                            orderIDTextField.setText(String.valueOf(set.getInt("Order_id")));
                            DateTextField.setText(String.valueOf(set.getDate("Date")));
                        }

                        prepState = conn.prepareStatement(sql2);
                        prepState.setInt(1, Integer.valueOf(orderIDTextField.getText()));
                        set = prepState.executeQuery();

                        while(set.next()){
                            productIds.add(set.getInt("Prod_id"));
                        }

                        //sql statement to get total of product bought
                        prepState = conn.prepareStatement(sql5);
                        prepState.setInt(1, t1);
                        set = prepState.executeQuery();

                        while(set.next()){
                            productTotalTextField.setText(String.valueOf(set.getInt("Amount")));
                        }

                        for(int i=0; i < productIds.size(); i++){
                            prepState = conn.prepareStatement(sql3);
                            prepState.setInt(1, productIds.get(i));
                            set = prepState.executeQuery();
                            while(set.next()){
                                productNameChoiceBox.getItems().add(set.getString("Product_name"));
                            }
                        }

                        //listener to change the description of multiple products
                        productNameChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue1, s, t11) -> {
                            int id = productNameChoiceBox.getSelectionModel().getSelectedIndex();
                            int selectedID = productIds.get(id);
                            System.out.println("Selected id: " + selectedID);

                            try{
                                prepState = conn.prepareStatement(sql3);
                                prepState.setInt(1, selectedID);
                                ResultSet rs = prepState.executeQuery();
                                while(rs.next()) {
                                    productDescriptionTextField.setText(rs.getString("Product_Description"));
                                    productConditionTextField.setText(rs.getString("Product_Condition"));
                                    productPriceTextField.setText(String.valueOf(rs.getInt("Product_price")));
                                }

                                //sql statement to get the product quantity of the item bought
                                prepState = conn.prepareStatement(sql4);
                                prepState.setInt(1, Integer.valueOf(orderIDTextField.getText()));
                                prepState.setInt(2, selectedID);
                                rs = prepState.executeQuery();

                                while(rs.next()){
                                    productQuantityTextField.setText(String.valueOf(rs.getInt("Quantity")));
                                }

                            }
                            catch(SQLException e){
                                e.printStackTrace();
                            }
                       });

                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                }
                ));
    }

    private void loadListView(){
        ArrayList<Integer> orderIds = new ArrayList<>();
        String sql1 = "SELECT Order_Id from ORDERS WHERE Customer = ?";
        String sql2 = "SELECT Invoices_num from INVOICES WHERE Order_id = ?";

        invoiceNumbers.clear();

        try {
            prepState = conn.prepareStatement(sql1);
            prepState.setInt(1, LoginInScreenController.MemberID);
            ResultSet rs = prepState.executeQuery();

            while(rs.next()){
                orderIds.add(rs.getInt("Order_id"));
            }

            //System.out.println(orderIds);


            for(int i=0; i < orderIds.size(); i++){
                prepState = conn.prepareStatement(sql2);
                prepState.setInt(1, orderIds.get(i));
                rs = prepState.executeQuery();
                while(rs.next()){
                    invoiceNumbers.add(rs.getInt("Invoices_num"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
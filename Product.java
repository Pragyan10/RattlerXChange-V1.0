public class Product {
    private int Product_ID;
    private String Product_Name;
    private String Description;
    private int Cost;
    private String Quantity;

    public Product(int Product_ID, String Product_Name, String Description, int Cost, String quantity){
        this.Product_ID = Product_ID;
        this.Product_Name = Product_Name;
        this.Description = Description;
        this.Cost = Cost;
        this.Quantity = quantity;
    }

    public Product(int Product_ID, String Product_Name,int Cost, String Quantity){
        this.Product_ID = Product_ID;
        this.Product_Name = Product_Name;
        this.Quantity = Quantity;
        this.Cost = Cost;
    }

    public void setProduct_ID(int product_ID) {
        Product_ID = product_ID;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public int getProduct_ID() {
        return Product_ID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public String getDescription() {
        return Description;
    }

    public int getCost() {
        return Cost;
    }

    public String getQuantity() {
        return Quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Product_ID=" + Product_ID +
                ", Product_Name='" + Product_Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Cost=" + Cost +
                '}';
    }



    /*
    public  SimpleIntegerProperty Product_ID;
    public  SimpleStringProperty Product_Name;
    public  SimpleStringProperty Product_Description;
    public  SimpleIntegerProperty Product_Cost;




    public Product(Integer Product_ID, String Product_Name, String Product_Description, Integer Product_Cost){
        this.Product_ID = new SimpleIntegerProperty(Product_ID);
        this.Product_Name = new SimpleStringProperty(Product_Name);
        this.Product_Description = new SimpleStringProperty(Product_Description);
        this.Product_Cost = new SimpleIntegerProperty(Product_Cost);
    }

    public int getProductID(){

        return Product_ID.get();
    }

    public void setProduct_ID(int Product_ID){
        this.Product_ID = new SimpleIntegerProperty(Product_ID);
    }


    public String getProductName(){

        return Product_Name.get();
    }

    public void setProduct_Name(String Product_Name){
        this.Product_Name = new SimpleStringProperty(Product_Name);
    }

    public String getProductDescription(){

        return Product_Description.getName();
    }

    public void setProduct_Description(String Product_Description){
        this.Product_Description = new SimpleStringProperty(Product_Description);
    }

    public Integer getProductCost(){
        return Product_Cost.get();
    }

    public void setProduct_Cost(int Product_Cost){
        this.Product_Cost = new SimpleIntegerProperty(Product_Cost);
    }
*/
}

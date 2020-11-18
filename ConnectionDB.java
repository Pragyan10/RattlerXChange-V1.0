import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDB {

    public static Connection dbConnection(){
        Connection conn = null;

        try{
            String url = "jdbc:sqlserver://localhost;database=RattlerXChangeV1;user=sa;password=MyPassword123";
            conn = DriverManager.getConnection(url);
            if(conn != null){
                System.out.println("connection successful");
//               int test = MyProfileScreenController.getId();
//               System.out.println("User id test: " + test);
            }
        }
        catch(SQLException ex){
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
        
    }

}

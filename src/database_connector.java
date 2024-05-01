import java.sql.*;

public class database_connector {
    Statement s;
    PreparedStatement stmt;
    Connection con;
    ResultSet rs;
    database_connector()
    {
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_system", "root", "MySql@python123");
            s= con.createStatement();
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Looks like server is busy..Please try later");
            System.out.println("-----------------------------------------------------");
        }
    }
}
import java.sql.*;
import java.util.Scanner;

public class entry_point_checker extends database_connector {
    Scanner sc=new Scanner(System.in);
    entry_point_checker()
    {
        super();
    }
    String entry()
    {
        String match="NULL";
        try {
            System.out.print("Enter your ID to proceed:");
            String ID=sc.next();
            stmt=con.prepareStatement("select ID from user_base;");
            rs=stmt.executeQuery();
            while(rs.next())
            {
                match=rs.getString(1);
                if(match.equals(ID))
                {
                    break;
                }
                else
                {
                    match="NULL";
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return match;
    }
}

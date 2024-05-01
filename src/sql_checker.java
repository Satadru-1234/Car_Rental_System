import java.sql.*;
import java.util.Scanner;

public class sql_checker extends database_connector{
    int count;
    Scanner sc=new Scanner(System.in);
    String email,fi_email;

    sql_checker()
    {
        super();
    }

    int check()
    {
        try
        {
            count=0;
            stmt=con.prepareStatement("select count(ID) from all_id;");
            rs= stmt.executeQuery();
            if(rs.next())
            {
                count=rs.getInt(1);
            }
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Maintenance required...Please Try later");
            System.out.println("-----------------------------------------------------");
        }
        return count;
    }

    String checker()
    {
        System.out.print("Please enter your email-id:");
        email=sc.next();
        fi_email=email.toLowerCase();
        String match="";
        try
        {
            stmt = con.prepareStatement("select EMAIL from user_pwd;");
            rs = stmt.executeQuery();
            while(rs.next())
            {
                match = rs.getString(1);
                if (match.equals(fi_email))
                {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Already exist this email..please try another one");
                    System.out.println("-----------------------------------------------------");
                    checker();
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return fi_email;
    }
}

import java.sql.*;
import java.util.Scanner;

public class user_operation extends database_connector {
    Scanner sc=new Scanner(System.in);
    String email;
    user_operation(String ID)
    {
        super();
        try
        {
            stmt = con.prepareStatement("select email from user_base where ID=(?);");
            stmt.setString(1, ID);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()) {
                email = rs.getString(1);
            }
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Error occurred");
            System.out.println("-----------------------------------------------------");
        }
    }

    int password_checker()
    {
        String pwd;
        int pwd_flag=0;
        String match_pwd;
        System.out.print("Confirm your password once again:");
        pwd=sc.next();
        try {
            stmt = con.prepareStatement("select pwd from user_pwd where EMAIL = (?);");
            stmt.setString(1, email);
            rs=stmt.executeQuery();
            while(rs.next()) {
                match_pwd = rs.getString(1);
                if (match_pwd.equals(pwd)) {
                    pwd_flag = 1;
                    break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return pwd_flag;
    }

    int  delete_account(String ID)
    {
        String match;
        int c=0;
        int flag = 0;
        try {
            stmt=con.prepareStatement("select STATUS from booking_details where UID=(?);");
            stmt.setString(1,ID);
            rs=stmt.executeQuery();
            while(rs.next())
            {
                match=rs.getString(1);
                if(match.equals("RESERVED")||match.equals("ONGOING")||match.equals("OVERDUE"))
                {
                    c=1;
                    break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        if(c==1)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Seems like your bookings and payments are not cleared\nPlease clear that before delete your account");
            System.out.println("-----------------------------------------------------");
        }
        else {
            int pf = password_checker();
            if (pf == 1) {
                try {
                    stmt = con.prepareStatement("delete from user_pwd where EMAIL = (?);");
                    stmt.setString(1, email);
                    stmt.executeUpdate();
                    stmt = con.prepareStatement("delete from user_base where EMAIL = (?);");
                    stmt.setString(1, email);
                    stmt.executeUpdate();
                    System.out.println("Account successfully deleted");
                    System.out.println("-------------------------------------------");
                    flag = 1;
                } catch (Exception e) {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Error occurred...Account not deleted");
                    System.out.println("-----------------------------------------------------");
                }
            } else {
                System.out.println("-----------------------------------------------------");
                System.out.println("email or password not matched...try again later.");
                System.out.println("-----------------------------------------------------");
            }
        }
        return flag;
    }

    void display_account()
    {
        int pc=password_checker();
        if(pc==1) {
            try {
                stmt = con.prepareStatement("select * from user_base where EMAIL=(?);");
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                System.out.println("-----------------------------------------------------");
                System.out.println("Here is your account details");
                System.out.println("-----------------------------------------------------");
                if (rs.next()) {
                    System.out.println("ID = " + rs.getString(1));
                    System.out.println("NAME = " + rs.getString(2));
                    System.out.println("CONTACT = " + rs.getString(3));
                    System.out.println("ADDRESS = " + rs.getString(4));
                    System.out.println("PIN = " + rs.getString(5));
                    System.out.println("EMAIL = " + rs.getString(6));
                }
                System.out.println("-----------------------------------------------------");
            }
            catch (Exception e) {
                System.out.println("-----------------------------------------------------");
                System.out.println("Cannot display records...Error occurred");
                System.out.println("-----------------------------------------------------");
            }
        }
        else {
            System.out.println("-----------------------------------------------------");
            System.out.println("Password not matched...try later");
            System.out.println("-----------------------------------------------------");
        }
    }
    void change_con()
    {
        int pc=password_checker();
        if(pc==1) {
            try {

                System.out.print("Enter your new contact number:");
                long new_con = sc.nextLong();
                stmt = con.prepareStatement("UPDATE user_base SET Contact=(?) where EMAIL=(?);");
                stmt.setLong(1, new_con);
                stmt.setString(2, email);
                stmt.executeUpdate();
                System.out.println("-----------------------------------------------------");
                System.out.println("Contact changes successful");
                System.out.println("-------------------------------------------");
            } catch (Exception e) {
                System.out.println("-----------------------------------------------------");
                System.out.println("Contact NOT changed....error occurred");
                System.out.println("-----------------------------------------------------");
            }
        }
        else {
            System.out.println("-----------------------------------------------------");
            System.out.println("Password not matched...try later");
            System.out.println("-----------------------------------------------------");
        }
    }
    void change_pwd()
    {
        int pc=password_checker();
        if(pc==1) {
            try {
                System.out.print("Enter your new password:");
                String new_pwd = sc.next();
                stmt = con.prepareStatement("UPDATE user_pwd SET pwd=(?) where EMAIL=(?);");
                stmt.setString(1, new_pwd);
                stmt.setString(2, email);
                System.out.println("-----------------------------------------------------");
                System.out.println("Password changes Successful");
                stmt.executeUpdate();
                System.out.println("-----------------------------------------------------");
            }
            catch (Exception e) {
                System.out.println("-----------------------------------------------------");
                System.out.println("Password NOT changed....error occurred");
                System.out.println("-----------------------------------------------------");
            }
        }
        else
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Password not matched");
            System.out.println("-----------------------------------------------------");
        }
    }
    void booking_history(String ID)
    {
        int pc=password_checker();
        int count=0;
        int checker_count=0;
        if(pc==1)
        {
            try {
                stmt=con.prepareStatement("select STATUS, B_ID, UID, BOOKING_DATE, START_DATE, RETURN_DATE, REG_NO " +
                        "from booking_details where UID=(?);");
                stmt.setString(1,ID);
                rs=stmt.executeQuery();
                while(rs.next())
                {
                    System.out.println("------------------------------------------------------");
                    System.out.println("Booking no. : "+(count+1));
                    System.out.println("------------------------------------------------------");
                    System.out.println("Current Status : "+rs.getString(1));
                    System.out.println("Booking ID : "+rs.getString(2));
                    System.out.println("User ID : "+rs.getString(3));
                    System.out.println("Booking Date : "+rs.getDate(4));
                    System.out.println("Start Date : "+rs.getDate(5));
                    System.out.println("Return Date : "+rs.getDate(6));
                    System.out.println("Car Registration No : "+rs.getString(7));
                    count=count+1;
                    checker_count++;
                }
                if(checker_count==0)
                {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Seems like no booking is done yet by this User ID");
                    System.out.println("-----------------------------------------------------");
                }
            }
            catch(Exception e)
            {
                System.out.println("-----------------------------------------------------");
                System.out.println("An error occurred");
                System.out.println("-----------------------------------------------------");
            }
        }
        else
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Password not match");
            System.out.println("-----------------------------------------------------");
        }
    }
    void payment_history(String ID)
    {
        int pc=password_checker();
        int count=0;
        int checker_count=0;
        if(pc==1)
        {
            try {
                stmt=con.prepareStatement("select * from payment_details where UID=(?);");
                stmt.setString(1,ID);
                rs=stmt.executeQuery();
                while(rs.next())
                {
                    System.out.println("------------------------------------------------------");
                    System.out.println("Payment no. : "+(count+1));
                    System.out.println("------------------------------------------------------");
                    System.out.println("Payment ID : "+rs.getString(1));
                    System.out.println("User ID : "+rs.getString(2));
                    System.out.println("Booking ID : "+rs.getString(3));
                    System.out.println("Payment type : "+rs.getString(4));
                    System.out.println("Amount of payment : "+rs.getInt(5));
                    System.out.println("Date of payment : "+rs.getDate(6));
                    count++;
                    checker_count++;
                }
                if(checker_count==0)
                {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Seems like no payment is done yet by this User ID");
                    System.out.println("-----------------------------------------------------");
                }
            }
            catch(Exception e)
            {
                System.out.println("-----------------------------------------------------");
                System.out.println("Cannot fetch payment history....Some error occurred");
                System.out.println("-----------------------------------------------------");
            }
        }
        else
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Password not match");
            System.out.println("-----------------------------------------------------");
        }
    }
}

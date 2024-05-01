import java.sql.*;
import java.util.Scanner;

public class user_base extends database_connector{
    Scanner sc=new Scanner(System.in);
    String email;
    String pwd;
    int count;

    user_base()
    {
        super();
    }

    void initial_id_display()
    {
        try {
            String ID="";
            stmt = con.prepareStatement("Select ID from user_base where EMAIL = (?)");
            stmt.setString(1,email);
            rs=stmt.executeQuery();
            if(rs.next())
            {
                ID=rs.getString(1);
            }
            System.out.println("-----------------------------------------------------");
            System.out.println("Your permanent ID = "+ID);
            System.out.println("-----------------------------------------------------");
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Cannon generate ID....Try later");
            System.out.println("-----------------------------------------------------");
        }
    }
    void login(long cont,int pin,String email)
    {
        try {
            int ID=count+1;
            String uid="CM1897"+ID;
            System.out.print("Enter first name:");
            String name=sc.next();
            System.out.print("Enter last surname:");
            String surname=sc.next();
            String full_name=name+" "+surname;
            sc.nextLine();
            System.out.print("Enter address:");
            String address=sc.nextLine();
            stmt = con.prepareStatement("Insert into user_base(ID,Name,Contact,Address,PIN,EMAIL) values(?,?,?,?,?,?)");
            stmt.setString(1,uid);
            stmt.setString(2,full_name);
            stmt.setLong(3,cont);
            stmt.setString(4,address);
            stmt.setInt(5,pin);
            stmt.setString(6,email);
            stmt.executeUpdate();
            System.out.println("-----------------------------------------------------");
            System.out.println("Processing.....please wait");
            System.out.println("-----------------------------------------------------");
            stmt=con.prepareStatement("insert into all_id (ID) values(?);");
            stmt.setString(1,uid);
            stmt.executeUpdate();
            System.out.println("-----------------------------------------------------");
            System.out.println("Successfully signed up");
            System.out.println("-----------------------------------------------------");
            initial_id_display();
            System.out.println("Please login again to continue");
            System.out.println("-----------------------------------------------------");
        }

        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Error occurred..Can't login....Try later");
            System.out.println("-----------------------------------------------------");
        }
    }

    void additional_details()
    {
        sql_checker obj1 = new sql_checker();
        Scanner sc=new Scanner(System.in);
        int ch=0;
        try
        {
            System.out.print("Enter contact number:");
            long cont = sc.nextLong();
            ch++;
            System.out.print("Enter PIN:");
            int pin = sc.nextInt();
            count=obj1.check();
            login(cont, pin, email);
        }
        catch(Exception e)
        {
            if(ch==0)
            {
                System.out.print("Enter valid contact no.");
            }
            additional_details();
        }
    }

    int new_user(String pwd)
    {
        int flag=0;
        try
        {
            stmt=con.prepareStatement("insert into user_pwd(Email,pwd) values(?,?)");
            stmt.setString(1,email);
            stmt.setString(2,pwd);
            stmt.executeUpdate();
            System.out.println("-----------------------------------------------------");
            System.out.println("You are one step behind to create an account");
            System.out.println("-----------------------------------------------------");
            flag=1;
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("We are facing a issue..try later");
            System.out.println("-----------------------------------------------------");
        }
        return flag;
    }

    void initial_details()
    {
        sql_checker obj = new sql_checker();
        email=obj.checker();
        System.out.print("Enter Password:");
        pwd = sc.next();
        int checkerbit = new_user(pwd);
        if(checkerbit==1)
        {
            System.out.println("Time for additional details:");
            additional_details();
        }
    }
}

import java.time.LocalDate;
import java.util.*;
import java.sql.*;

public class payment extends database_connector{
    Scanner sc=new Scanner(System.in);
    java.sql.Date date;
    java.sql.Date sdate,rdate;
    long millis;
    int rnf;
    String count;
    Random rand=new Random();
    int amount;
    payment(String reg)
    {
        super();
        millis = System.currentTimeMillis();
        date = new java.sql.Date(millis);
        try {
            stmt = con.prepareStatement("select FAIR from car_details where REG_NO=(?);");
            stmt.setString(1,reg);
            rs= stmt.executeQuery();
            if(rs.next())
            {
                rnf=rs.getInt(1);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }
    void pid_fetch()
    {
        try
        {
            stmt=con.prepareStatement("select count(PID) from payment_details;");
            rs= stmt.executeQuery();
            if(rs.next())
            {
                count=rs.getString(1);
            }
        }
        catch(Exception e)
        {
            System.out.println("Maintenance required...Please Try later");
        }
    }

    void payment_insert(String ID,String bid,String ftype,int amount)
    {
        pid_fetch();
        String pid="3CW"+ID+"C715RT"+count;
        try {
            stmt=con.prepareStatement("insert into payment_details (PID,UID,BID,FAIR_TYPE,PAYMENT_AMOUNT,PAYMENT_DATE) " +
                    "values(?,?,?,?,?,?);");
            stmt.setString(1,pid);
            stmt.setString(2,ID);
            stmt.setString(3,bid);
            stmt.setString(4,ftype);
            stmt.setInt(5,amount);
            stmt.setDate(6,date);
            stmt.executeUpdate();
            System.out.println("Payment processing....");
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Look like server is busy...Payment not successful\nPlease try later");
            System.out.println("-----------------------------------------------------");
        }
    }
    void payment_area(int value)
    {
        int amount;
        System.out.println("Enter payment amount:");
        System.out.println("-----------------------");
        System.out.print("Amount : ");
        amount=sc.nextInt();
        System.out.println("-----------------------");
        if(amount!=value)
        {
            payment_area(value);
        }
    }
    int payment_of_cancellation(String bid,String ID)
    {
        LocalDate date1=LocalDate.now();
        LocalDate date2=LocalDate.now();
        LocalDate cdate;
        cdate=date.toLocalDate();
        int flag=0;
        try {
            stmt=con.prepareStatement("select START_DATE, RETURN_DATE from booking_details where B_ID=(?);");
            stmt.setString(1,bid);
            rs=stmt.executeQuery();
            if(rs.next())
            {
                sdate=rs.getDate(1);
                date1=sdate.toLocalDate();
                rdate=rs.getDate(2);
                date2=rdate.toLocalDate();
            }
            if(cdate.isBefore(date1))
            {
                System.out.println("Seems like you want to cancel your booking before the start of trip");
                System.out.println("500 Rs will be taken as cancellation charge");
                payment_area(500);
                payment_insert(ID,bid,"CANCELLATION",500);
                flag=1;
            }
            if((cdate.isAfter(date1) && cdate.isBefore(date2)) || cdate.isEqual(date1)||cdate.isEqual(date2))
            {
                System.out.println("Seems like you want to cancel your booking before your return date");
                System.out.println("700 Rs will be taken extra as cancellation charge");
                int random=rand.nextInt(10,80);
                System.out.println("Your reserved car has run "+random+" km since your trip start date");
                amount=((rnf*random)+700);
                System.out.println("Payment amount "+amount);
                payment_area(amount);
                payment_insert(ID,bid,"MID JOURNEY CANCELLATION",amount);
                flag=1;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return flag;
    }

    void payment_overdue(String bid,String ID)
    {
        System.out.println("Booking Status : OVERDUE, You have to pay additional 1500 Rs with usual rent fair");
        int random= rand.nextInt(10,500);
        System.out.println("Your reserved car has run "+random+" km since your trip start date");
        amount=((rnf*random)+1500);
        System.out.println("Payment amount "+amount);
        payment_area(amount);
        payment_insert(ID,bid,"OVERDUE CLEARANCE",amount);
    }
    void payment_normal(String bid,String ID)
    {
        int random= rand.nextInt(10,500);
        System.out.println("Your reserved car has run "+random+" km since your trip start date");
        amount=((rnf*random)+1500);
        System.out.println("Payment amount "+amount);
        payment_area(amount);
        payment_insert(ID,bid,"NORMAL CLEARANCE",amount);
    }
}

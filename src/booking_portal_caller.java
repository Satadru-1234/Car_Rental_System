import java.util.*;
import java.sql.*;
import java.time.LocalDate;
public class booking_portal_caller extends database_connector {
    Scanner sc=new Scanner(System.in);
    java.sql.Date sdate,rdate;
    booking_portal_caller()
    {
        super();
        try {
            long  millis= System.currentTimeMillis();
            sdate=new java.sql.Date(millis);
            rdate=new java.sql.Date(millis);
            LocalDate date1=LocalDate.now();
            LocalDate date2;
            LocalDate date3;

            stmt = con.prepareStatement("select START_DATE, RETURN_DATE from booking_details;");
            rs=stmt.executeQuery();
            while(rs.next())
            {
                sdate=rs.getDate(1);
                rdate=rs.getDate(2);
                date2=rdate.toLocalDate();
                date3=sdate.toLocalDate();
                if(date1.isAfter(date3) || date1.isEqual(date3) && date1.isBefore(date2) || date1.isEqual(date2))
                {
                    stmt=con.prepareStatement("update booking_details set STATUS='ONGOING' where RETURN_DATE=(?) and START_DATE=(?) and STATUS='RESERVED' ;");
                    stmt.setDate(1,rdate);
                    stmt.setDate(2,sdate);
                    stmt.executeUpdate();
                }
                if(date1.isAfter(date2))
                {
                    stmt=con.prepareStatement("update booking_details set STATUS='OVERDUE' where RETURN_DATE=(?) and STATUS='ONGOING';");
                    stmt.setDate(1,rdate);
                    stmt.executeUpdate();
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    void caller(String ID)
    {
        booking_portal bp=new booking_portal();
        user_operation up=new user_operation(ID);
            int opt = 1;
            int checker;
            while (opt != 5) {
                System.out.println("-----------------------------------------------------");
                System.out.println("Welcome to our Booking Portal");
                System.out.println("-----------------------------------------------------");
                System.out.println("* Press 1 for car details:\n* Press 2 for car book:\n* Press 3 for car return:");
                System.out.println("* Enter 4 for Booking Cancellation:\n* Enter 5 for main menu:");
                System.out.print("Enter your choice:");
                opt = sc.nextInt();
                if (opt == 1) {
                    bp.car_details();
                }
                if (opt == 2) {
                    checker = up.password_checker();
                    if (checker == 1)
                    {
                        int flag1=bp.availability();
                        if(flag1==0)
                        {
                            System.out.println("-----------------------------------------------------");
                            System.out.println("Please Contact later...");
                            System.out.println("-----------------------------------------------------");
                        }
                        else {
                            bp.car_book(ID);
                        }
                    }
                    else {
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Password not match...Sorry...Cannot proceed");
                        System.out.println("-----------------------------------------------------");
                    }
                }
                if(opt==3)
                {
                    checker = up.password_checker();
                    if(checker==1)
                    {
                        bp.car_return(ID);
                    }
                    else {
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Password not match...Sorry...Cannot proceed");
                        System.out.println("-----------------------------------------------------");
                    }
                }
                if(opt==4) {
                checker = up.password_checker();
                if (checker == 1) {
                    bp.booking_cancel(ID);
                } else {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Password not match...Sorry...Cannot proceed");
                    System.out.println("-----------------------------------------------------");
                }
            }
            }

    }
}

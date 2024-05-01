
import java.time.LocalDate;
import java.util.*;
import java.sql.*;
import java.sql.Date;

public class booking_portal extends database_connector {
    Scanner sc=new Scanner(System.in);
    String strd, rtnd;
    String count;
    String reg;
    long millis;
    java.sql.Date date;
    LocalDate today;
    booking_portal()
    {
        super();
        millis = System.currentTimeMillis();
        date = new java.sql.Date(millis);
        today=LocalDate.now();
    }

    int registration_checker(String reg)
    {
        int flag=0;
        try {
            String match;
            stmt=con.prepareStatement("select REG_NO from car_details where STATUS='UNRESERVED';");
            rs=stmt.executeQuery();
            while(rs.next())
            {
                match=rs.getString(1);
                if(match.equals(reg))
                {
                    flag=1;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Error occurred");
            System.out.println("-----------------------------------------------------");
        }
        return flag;
    }
    int availability()
    {
        String avail;
        String ID;
        int flag=0;
        try {
            stmt=con.prepareStatement("select MODEL, REG_NO from car_details where STATUS = 'UNRESERVED';");
            rs =stmt.executeQuery();
            System.out.println("-----------------------------------------------------");
            System.out.println("Currently available vehicles.");
            System.out.println("-----------------------------------------------------");
            System.out.println("MODEL | REGISTRATION NO");
            System.out.println("-----------------------------------------------------");
            while(rs.next())
            {
                avail=rs.getString(1);
                ID=rs.getString(2);
                System.out.print((flag+1)+"."+avail+" | ");
                System.out.print(ID);
                System.out.println();
                flag=flag+1;
            }
            if(flag==0)
            {
                System.out.println("-----------------------------------------------------");
                System.out.println("Sorry...Looks like all cars are reserved");
                System.out.println("-----------------------------------------------------");
            }
            System.out.println("-----------------------------------------------------");
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Cannot fetch car availability....Try later");
            System.out.println("-----------------------------------------------------");
        }
        return flag;
    }
    void car_details()
    {
        String avail;
        String ID;
        String sts;
        int count=0;
        try {
            stmt=con.prepareStatement("select MODEL, REG_NO, STATUS from car_details;");
            rs =stmt.executeQuery();
            System.out.println("-----------------------------------------------------");
            System.out.println("Our Vehicles List............");
            System.out.println("-----------------------------------------------------");
            System.out.println("MODEL | REGISTRATION NO | STATUS");
            System.out.println("-----------------------------------------------------");
            while(rs.next())
            {
                avail=rs.getString(1);
                ID=rs.getString(2);
                sts=rs.getString(3);
                System.out.print((count+1)+"."+avail+" | ");
                System.out.print(ID+" | ");
                System.out.print(sts);
                System.out.println();
                count=count+1;
            }
        }
        catch(Exception e)
        {
            System.out.println("Cannot fetch car availability....Try later");
        }
        String in;
        do {
            System.out.println("--------------------------------------------------------------");
            System.out.print("Want Details of our vehicles ? \nIf YES, please Press 'Y'.Otherwise enter 'N' to continue:");
            in= sc.next();
            sc.nextLine();
            if (in.equals("Y") || in.equals("y")) {

                System.out.print("Enter registration no. of your preferred vehicle:");
                reg = sc.nextLine();
                int flag = registration_checker(reg);
                if (flag == 1) {
                    try {
                        stmt = con.prepareStatement("Select REG_NO, MODEL, COMPANY, SEATS, FAIR from car_details where REG_NO = (?);");
                        stmt.setString(1, reg);
                        rs = stmt.executeQuery();
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Here is the details..");
                        System.out.println("-----------------------------------------------------");
                        if (rs.next()) {
                            System.out.println("Registration no : " + rs.getString(1));
                            System.out.println("Model Name : " + rs.getString(2));
                            System.out.println("Company Name : " + rs.getString(3));
                            System.out.println("NO. of Seats : " + rs.getInt(4));
                            System.out.println("Rent Fair (in KM) : " + rs.getInt(5));
                        }
                    } catch (Exception e) {
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Cannot fetch car details...Try later");
                        System.out.println("-----------------------------------------------------");
                    }
                }
                else {
                    System.out.println("No car with this registration no is found");
                }
            }
        }while(!in.equals("N"));
        System.out.println("--------------------------------------------------------------");
    }
    void car_book(String ID)
    {
        try {
            int psn;
            id_fetch();
            String bid="US-"+ID+"-RES007"+count;

            String sts="RESERVED";
            System.out.print("Enter car Registration No.");
            reg = sc.nextLine();
            int flag=registration_checker(reg);
            if(flag==1) {
                date_input();
                Date date1 = Date.valueOf(strd);
                Date date2 = Date.valueOf(rtnd);
                System.out.print("Enter passenger_no. : ");
                psn = sc.nextInt();

                stmt = con.prepareStatement("insert into booking_details (REG_NO,UID,BOOKING_DATE,START_DATE,RETURN_DATE,PASSENGERS_NO,STATUS,B_ID)" +
                        "values(?,?,?,?,?,?,?,?);");
                stmt.setString(1, reg);
                stmt.setString(2, ID);
                stmt.setDate(3, date);
                stmt.setDate(4, date1);
                stmt.setDate(5, date2);
                stmt.setInt(6, psn);
                stmt.setString(7, sts);
                stmt.setString(8,bid);
                stmt.executeUpdate();
                System.out.println("Processing.....wait for few seconds");

                stmt = con.prepareStatement("update car_details set STATUS = 'RESERVED' where REG_NO=(?);");
                stmt.setString(1, reg);
                stmt.executeUpdate();
                System.out.println("-----------------------------------------------------");
                System.out.println("Booking Successful");
                System.out.println("-----------------------------------------------------");
                System.out.println("Your current Booking ID = "+bid);
                System.out.println("-----------------------------------------------------");
            }
            else
            {
                System.out.println("-----------------------------------------------------");
                System.out.println("No car found on given registration no.");
                System.out.println("-----------------------------------------------------");
            }
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("OOPS......an error occurred");
            System.out.println("-----------------------------------------------------");
        }
    }

    void date_input()
    {
        LocalDate sdate=LocalDate.now();
        LocalDate rdate=LocalDate.now();
        try {
            System.out.print("Enter Trip start date...'YYYY-MM-DD' format:");
            strd = sc.next();
            sdate = LocalDate.parse(strd);
            System.out.print("Enter Car Return date...'YYYY-MM-DD' format:");
            rtnd = sc.next();
            rdate = LocalDate.parse(rtnd);
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Date format not matched..Enter again");
            System.out.println("-----------------------------------------------------");
            date_input();
        }
        if(sdate.isBefore(today)||rdate.isBefore(today)||rdate.isBefore(sdate))
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Please enter valid date");
            System.out.println("-----------------------------------------------------");
            date_input();
        }
    }

    void id_fetch()
    {
        try
        {
            stmt=con.prepareStatement("select count(B_ID) from booking_details;");
            rs= stmt.executeQuery();
            if(rs.next())
            {
                count=rs.getString(1);
            }
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("Maintenance required...Please Try later");
            System.out.println("-----------------------------------------------------");
        }
    }


    void booking_cancel(String ID)
    {
        String match_sts="NULL";
        int flag=0;
        System.out.print("Enter your Booking ID:");
        String bid=sc.next();
        try {
            stmt = con.prepareStatement("Select STATUS from booking_details where UID=(?) and B_ID=(?);");
            stmt.setString(1, ID);
            stmt.setString(2, bid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                match_sts = rs.getString(1);
                if (match_sts.equals("OVERDUE"))
                {
                    flag = 2;
                }
                else if(match_sts.equals("CLEARED")||match_sts.equals("CLEARED(OVERDUE)"))
                {
                    flag=3;
                }
                else {
                    flag = 1;
                }
            }

            if (!match_sts.equals("NULL")) {
                if (flag == 1) {
                    String frn = "NULL";
                    String sts = "CANCELLED";
                    java.sql.Date date1 = new java.sql.Date(millis);

                    stmt = con.prepareStatement("select REG_NO, BOOKING_DATE from booking_details where B_ID=(?);");
                    stmt.setString(1, bid);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        frn = rs.getString(1);
                        date1 = rs.getDate(2);
                    }
                    payment pmt=new payment(frn);
                    int opt=pmt.payment_of_cancellation(bid,ID);
                    if(opt==1) {
                        stmt = con.prepareStatement("insert into cancel_details (CANCEL_B_ID, UID, BOOKING_DATE, CANCEL_DATE," +
                                " REG_NO, " + "STATUS)" + " values(?,?,?,?,?,?);");
                        stmt.setString(1, bid);
                        stmt.setString(2, ID);
                        stmt.setDate(3, date1);
                        stmt.setDate(4, date);
                        stmt.setString(5, frn);
                        stmt.setString(6, sts);
                        stmt.executeUpdate();
                        System.out.println("please wait for few seconds......");

                        update_car(frn);
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Processing....this may take a few seconds");
                        System.out.println("-----------------------------------------------------");
                        stmt = con.prepareStatement("update booking_details set STATUS='CANCELED' where B_ID=(?);");
                        stmt.setString(1, bid);
                        stmt.executeUpdate();
                        System.out.println("-----------------------------------------------------");
                        System.out.println("cancellation successful");
                        System.out.println("-----------------------------------------------------");
                    }
                    if(opt==0)
                    {
                        System.out.println("-----------------------------------------------------");
                        System.out.println("OOPS...An error occurred. Please try later");
                        System.out.println("-----------------------------------------------------");
                    }
                }
                if (flag == 2) {
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Reservation status : OVERDUE. You cannot cancel your booking");
                    System.out.println("----------------------------------------------------------------");
                }
                if(flag==3)
                {
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("This booking is already cleared");
                    System.out.println("----------------------------------------------------------------");
                }
            }
            else
            {
                System.out.println("-----------------------------------------------------");
                System.out.println("No Booking ID found with this User ID");
                System.out.println("-----------------------------------------------------");
            }
        }
        catch(Exception e)
        {
            System.out.println("An error occurred");
        }
        System.out.println("-------------------------------------------------------");
    }
    void update_car(String frn)
    {
        try {
            stmt = con.prepareStatement("update car_details set STATUS='UNRESERVED' where REG_NO=(?);");
            stmt.setString(1, frn);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println("An error occurred");
        }
    }
    void update_bookTable(String bid)
    {
        try {
            stmt = con.prepareStatement("update booking_details set STATUS='CLEARED' where B_ID=(?);");
            stmt.setString(1, bid);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println("An error occurred");
        }
    }
    void car_return(String ID)
    {
        String match="NULL";
        String frn="NULL";
        int flag=0;
        System.out.print("Enter you Booking ID:");
        String bid=sc.next();
        try {
            stmt = con.prepareStatement("select REG_NO, STATUS from booking_details where B_ID=(?) and UID=(?);");
            stmt.setString(1,bid);
            stmt.setString(2,ID);
            rs= stmt.executeQuery();
            if(rs.next())
            {
                frn=rs.getString(1);
                match = rs.getString(2);
                if(match.equals("OVERDUE"))
                {
                    flag=1;
                }
                if(match.equals("RESERVED"))
                {
                    flag=2;
                }
                if(match.equals("ONGOING"))
                {
                    flag=3;
                }
                if(match.equals("CLEARED")||match.equals("CLEARED(OVERDUE)"))
                {
                    flag=4;
                }
            }
            if(!match.equals("NULL"))
            {
                if(flag==2)
                {
                    System.out.println("RESERVATION STATUS : RESERVED...You cannot return your car");
                    System.out.println("If you want to cancel it...please select 'Booking Cancellation' option");
                }
                else if(flag==4)
                {
                    System.out.println("This booking is already cleared");
                }
                else {
                    payment pmt = new payment(frn);
                    if (flag == 1) {
                        pmt.payment_overdue(bid, ID);
                        update_bookTable(bid);
                        stmt = con.prepareStatement("update booking_details set STATUS='CLEARED(OVERDUE)' where B_ID=(?);");
                        stmt.setString(1, bid);
                        stmt.executeUpdate();
                    }
                    if (flag == 3) {
                        pmt.payment_normal(bid, ID);
                        update_bookTable(bid);
                    }
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Payment successful");
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Please wait for few seconds");
                    update_car(frn);
                    System.out.println("Return status : Successful");
                    System.out.println("-----------------------------------------------------");
                }
            }
            else {
                System.out.println("-----------------------------------------------------");
                System.out.println("No Booking ID found on this User ID ");
                System.out.println("-----------------------------------------------------");
            }
        }
        catch(Exception e)
        {
            System.out.println("-----------------------------------------------------");
            System.out.println("An error occurred..Contact later");
            System.out.println("-----------------------------------------------------");
        }
    }
}

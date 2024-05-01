import java.util.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("-----------------------------------------------------");
        System.out.println("WELCOME TO CAR RENTAL SERVICE:");
        System.out.println("-----------------------------------------------------");
        System.out.println("BEFORE PROCEED FURTHER ......\nEnter 1 if new user...and enter 2 if existing user:");
        System.out.println("-----------------------------------------------------");
        System.out.print("Enter your choice:");
        Scanner sc = new Scanner(System.in);
        int flag = 0;
        int op_flag;
        String ID="";
        user_base ub = new user_base();
        booking_portal_caller bpc=new booking_portal_caller();
        int ch = sc.nextInt();
        if (ch == 1) {
            ub.initial_details();
            flag=1;
        }
        if (ch == 2) {
            entry_point_checker epc = new entry_point_checker();
            ID = epc.entry();
            if(!ID.equals("NULL"))
            {
                flag=2;
            }
        }
        if (flag == 0) {
            System.out.println("-----------------------------------------------------");
            System.out.println("ID Not found..Try again or create an account");
            System.out.println("-----------------------------------------------------");
        }
        if(flag==2) {
            user_operation uop = new user_operation(ID);
            int opt;
            do {
                System.out.println("-----------------------------------------------------");
                System.out.println("Welcome to Main Menu to the car rental service.");
                System.out.println("-----------------------------------------------------");
                System.out.println("Your option:");
                System.out.println("* Enter 1 for change your password:\n* Enter 2 for show your current account info:");
                System.out.println("* Enter 3 to REMOVE your account:");
                System.out.println("* Enter 4 to change contact number:\n* Enter 5 to check booking history:");
                System.out.println("* Enter 6 to check payment history :\n* Enter 7 to redirect to booking portal:");
                System.out.println("* Enter 8 or more to exit:");
                System.out.print("Choose your operation : ");
                opt = sc.nextInt();
                if(opt==1) {
                    uop.change_pwd();
                }
                if (opt == 2) {
                    uop.display_account();
                }
                if (opt == 3) {
                    op_flag=uop.delete_account(ID);
                    if(op_flag==1) {
                        break;
                    }
                }
                if(opt==4) {
                    uop.change_con();
                }
                if(opt==5) {
                    uop.booking_history(ID);
                }
                if(opt==6) {
                    uop.payment_history(ID);
                }
                if(opt==7) {
                    bpc.caller(ID);
                }
            } while (opt !=8);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Thank you for choosing us..we hope to see you again later");
    }
}

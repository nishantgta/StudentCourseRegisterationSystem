package org.iitrpr.Pages;
import java.io.IOException;
import java.util.Scanner;

public class Login_page {
    public static String emailid,password;
    public static int login_page_front_end(Scanner in) throws IOException{
        System.out.println("LOGIN PAGE");
        System.out.println("Press 1 for student\nPress 2 for Faculty\nPress 3 for Academic Officials");
        System.out.print("Enter your choice:");
        int choice;
        do{
            choice = in.nextInt();
            if(choice == 1){
                System.out.println("Welcome back Student");
            }
            else if(choice == 2){
                System.out.println("Welcome back Faculty");
            }
            else if(choice == 3){
                System.out.println("Welcome back Academic Officials");
            }
            else{
                System.out.println("Please enter correct choice");
            }
        }while(choice>3 || choice < 0);
        System.out.print("Enter your EmailID:");
        emailid=in.next();
        System.out.print("Enter your password:");
        password=in.next();
        return choice;
    }
}

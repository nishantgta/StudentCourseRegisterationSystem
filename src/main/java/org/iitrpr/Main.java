package org.iitrpr;
import com.opencsv.exceptions.CsvValidationException;
import org.iitrpr.Pages.Academic_Office.Academic_Office_home_page;
import org.iitrpr.Pages.Faculty.Faculty_home_page;
import org.iitrpr.Pages.Login_page;
import org.iitrpr.Pages.Students.Student_home_page;
import org.iitrpr.Utilities.Check_password;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {

    public static Connection connect(){
        Connection conn=null;
        try{
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/academic_portal","postgres","123456");
        }catch(SQLException e){}
        return conn;
    }
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Login_page login = new Login_page();
        Check_password pass=new Check_password();
        Student_home_page student = new Student_home_page();
        boolean valid_user=false;
        int login_choice=0;
        try{
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/academic_portal","postgres","123456");
            Statement stmt = conn.createStatement();
            login_choice = login.login_page_front_end(in);
            valid_user = pass.check_password(login_choice,Login_page.emailid,Login_page.password,stmt);
            if(valid_user){
                System.out.println("Authenticated\n\n\n\n");
                if(login_choice==1){
                    int t1=0,t2=0;
                    while(t1!=1){
                        while(t2!=1) {
                            t2 = student.student_login_front_end(in,stmt,Login_page.emailid,Login_page.password,conn);
                        }
                        t2=0;
                        System.out.print("Press 1 to exit app, 0 to continue:");
                        t1=in.nextInt();
                    }
                }
                else if(login_choice==2){
                    int t1=0,t2=0;
                    while(t1!=1){
                        while(t2!=1){
                            t2= Faculty_home_page.faculty_login_front_end(in,stmt,Login_page.emailid,Login_page.password,conn);
                        }
                        t2=0;
                        System.out.print("Press 1 to exit app, 0 to continue:");
                        t1=in.nextInt();
                    }
                }
                else if(login_choice==3){
                    int t1=0,t2=0;
                    while(t1!=1){
                        while(t2!=1){
                            t2= Academic_Office_home_page.academic_login_front_end(in,stmt,Login_page.emailid,Login_page.password,conn);
                        }
                        t2=0;
                        System.out.print("Press 1 to exit app, 0 to continue:");
                        t1=in.nextInt();
                    }
                }
            }
            else{
                System.out.println("Not Authenticated");
            }
        }catch(SQLException | CsvValidationException e){
            e.printStackTrace();
        }
    }
}
package org.iitrpr.Utilities;

import java.sql.*;

public class Check_password {
    public static boolean check_password(int choice,String email,String password,Statement stmt){
        System.out.println(email+" "+password);
        String query="select * from ",table_name="";
        ResultSet rs; boolean ans=false;
        if(choice == 1){
            /*check for student*/
            table_name="students ";
        }
        else if(choice == 2){
            /*check for faculty*/
            table_name="faculty ";
        }
        else if(choice == 3){
            /*check for academic official*/
            table_name="academic_office ";
        }
        query+=table_name+" where emailid='"+email+"' and password="+"'"+password+"';";
        System.out.println(query);
        try{
            rs = stmt.executeQuery(query);
            while(rs.next()){
                ans=true;
                /*System.out.println(rs.getString("emailid")+","+rs.getString("password")+","+rs.getString("name"));*/
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return ans;
    }
}

package org.iitrpr.Pages.Faculty;
import org.iitrpr.Utilities.Generate_Transcript;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;

public class Faculty_home_page {

    public static String dir_path="/home/nishant/Documents/Academics/Software_Engineering/mini_project_2/src/main/java/org/iitrpr/grade_submission";
    public static void view_grade(Scanner in,Statement stmt){
        String student_id;
        System.out.print("Enter student Entry Number:");
        student_id=in.next();
        try{
            String query_student="select * from all_student_data where id='"+student_id+"' and grade!='NIL';";
            String check_student="select * from students where id='"+student_id+"';";
            ResultSet rs_check_student=stmt.executeQuery(check_student);
            boolean student_present=false,data_present=false;
            while(rs_check_student.next()){
                student_present=true;
                /*System.out.println(rs.getString(""));*/
            }

            if(!student_present){
                System.out.println("Invalid student ID");
                return;
            }
            ResultSet rs=stmt.executeQuery(query_student);
            while(rs.next()){
                data_present=true;
                System.out.println(rs.getString("courseid")+" "+rs.getString("session")+" "+rs.getString("grade"));
            }
            System.out.println(student_present+" "+data_present);
            if(!data_present){
                System.out.println("No course has been completed by the given student");
                return;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void register_course(Statement stmt,Scanner in,String emailid,String session,Connection conn){
        /*print all available courses for his/her department in current semester*/
        try{
            String query2="select * from faculty where emailid='"+emailid+"';";
            ResultSet rs_get_dept_faculty=stmt.executeQuery(query2); rs_get_dept_faculty.next();
            String dept=rs_get_dept_faculty.getString("department");
            String query1="select * from program_core p,course_catalog c where p.course_id=c.courseid and c.department='"+dept+"';";
            String query_engineer="select * from engineering_core e,course_catalog c where e.course_id=c.courseid and c.department='"+dept+"';";
            String elective_query="select * from elective elec,course_catalog c where elec.course_id=c.courseid and c.department='"+dept+"';";
            ResultSet rs_get_department_courses=stmt.executeQuery(query1);
            stmt=conn.createStatement();
            /*get all courses of the particular department of the faculty*/
            ArrayList <String> faculty_dept_courses=new ArrayList<>();
            ArrayList <String> courses_available_for_offering=new ArrayList<>();

            ArrayList <String> engineering_core=new ArrayList<>();
            ArrayList <String> ec_available_for_offering=new ArrayList<>();

            ArrayList <String> electives=new ArrayList<>();
            ArrayList <String> elective_available_for_offering=new ArrayList<>();

            /*------------------------------------------------------------------------------*/
            while(rs_get_department_courses.next()){
                faculty_dept_courses.add(rs_get_department_courses.getString("course_id"));
            }
            for(String s:faculty_dept_courses){
                stmt=conn.createStatement();
                /*if s course is not present in courses offered, then it is available for offering*/
                ResultSet rs=stmt.executeQuery("select * from courses_offered where courseid='"+s+"' and session='"+session+"';");
                if(!rs.isBeforeFirst()){
                    courses_available_for_offering.add(s);
                }
            }
            if(courses_available_for_offering.size()==0){
                System.out.println("NO PROGRAM COURSES ARE THERE TO BE OFFERED BY YOU");
            }
            else{
                System.out.println("PROGRAM CORE COURSES AVAILABLE FOR OFFERING");
                for(String s:courses_available_for_offering) System.out.println(s);
            }
            /*------------------------------------------------------------------------------*/



            /*------------------------------------------------------------------------------*/
            stmt=conn.createStatement();
            ResultSet eng_core=stmt.executeQuery(query_engineer);
            while(eng_core.next()){
                engineering_core.add(eng_core.getString("course_id"));
            }
            for(String s:engineering_core){
                stmt=conn.createStatement();
                ResultSet ec_bhai=stmt.executeQuery("select * from courses_offered where courseid='"+s+"' and session='"+session+"';");
                if(!ec_bhai.isBeforeFirst()){
                    ec_available_for_offering.add(s);
                }
            }
            if(ec_available_for_offering.size()==0){
                System.out.println("NO ENGINEERING CORE COURSES ARE THERE TO BE OFFERED BY YOU");
            }else{
                System.out.println("ENGINEERING CORE COURSES AVAILABLE FOR OFFERING");
                for(String s:ec_available_for_offering) System.out.println(s);
            }
            /*------------------------------------------------------------------------------*/

            /*------------------------------------------------------------------------------*/
            stmt=conn.createStatement();
            ResultSet elective_rs=stmt.executeQuery(elective_query);
            while(elective_rs.next()){
                electives.add(elective_rs.getString("course_id"));
            }
            for(String s:electives){
                stmt=conn.createStatement();
                ResultSet elec_bhai=stmt.executeQuery("select * from courses_offered where courseid='"+s+"' and session='"+session+"';");
                if(!elec_bhai.isBeforeFirst()){
                    elective_available_for_offering.add(s);
                }
            }
            if(elective_available_for_offering.size()==0){
                System.out.println("NO ELECTIVE COURSES ARE THERE TO BE OFFERED BY YOU");
            }else{
                System.out.println("ELECTIVE COURSES AVAILABLE FOR OFFERING");
                for(String s:elective_available_for_offering) System.out.println(s);
            }
            /*------------------------------------------------------------------------------*/

            if(courses_available_for_offering.size()==0 && ec_available_for_offering.size()==0 && elective_available_for_offering.size()==0){
                System.out.println("NO COURSES ARE AVAILABLE FOR YOUR OFFERING, PLEASE EXIT!");
                return;
            }

            /*------------------------------------------------------------------------------*/
            String courseid_x; double cg_constraint; int offer=0;
            while(offer==0){
                System.out.print("Enter ID of the course you want to offer:");
                courseid_x=in.next();
                System.out.print("Enter CG constraint if any, else press 0:");
                cg_constraint=in.nextDouble();
                /*in.nextLine();*/
                stmt=conn.createStatement();
                ResultSet check=stmt.executeQuery("select * from courses_offered where courseid='"+courseid_x+"' and session='"+session+"';");
                if(!check.isBeforeFirst()){
                    String insert_query="insert into courses_offered values('"+courseid_x+"','"+emailid+"','"+session+"',"+cg_constraint+");";
                    stmt= conn.createStatement(); stmt.executeUpdate(insert_query);
                    System.out.println("Courses has been registered successfully");
                }
                System.out.print("Press 0 to continue, else Press enter 1 to exit:");
                offer=in.nextInt();
                in.nextLine();
            }
            /*------------------------------------------------------------------------------*/
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void deregister_course(Statement stmt,Scanner in,String emailid,String session,Connection conn){
        /*------------------------------------------------------------------------------*/
        try{
            ResultSet rs=stmt.executeQuery("select * from event_table_acads where session='"+session+"' and lower(current_sem)='y' and lower(courses_float)='y' and lower(courses_add_drop)='0';");
            String dereg_course;
            int choice=0;
            if(rs.isBeforeFirst()){
                do{
                    stmt=conn.createStatement();
                    System.out.print("Enter the course to deregister:");
                    dereg_course=in.next();
                    in.nextLine();
                    ResultSet check_in_course_offered=stmt.executeQuery("select * from courses_offered where courseid='"+dereg_course+"' and instructorid='"+emailid+"' and session='"+session+"';");
                    if(check_in_course_offered.isBeforeFirst()){
                        stmt=conn.createStatement();
                        stmt.executeUpdate("delete from courses_offered where courseid='"+dereg_course+"' and instructorid='"+emailid+"' and session='"+session+"';");
                        break;
                    }
                    else{
                        System.out.println("The course you entered is not floated,can't deregister it");
                        System.out.print("Press 0 to exit, 1 to continue");
                        choice=in.nextInt();
                        in.nextLine();
                        if(choice==0) break;
                    }
                }while(true);
            }
            else{System.out.println("You can't deregister courses now.");}
        }catch(SQLException e){}
        /*------------------------------------------------------------------------------*/
    }

    public static void update_course_grade(String session,Statement stmt,Connection conn,String email) {
        try{
            ResultSet rs=stmt.executeQuery("select * from event_table_acads where lower(current_sem)='y' and lower(courses_float)='n' and lower(courses_add_drop)='n' and lower(grade_submission)='y';");
            if(!rs.isBeforeFirst()) {
                System.out.println("Grade submission not allowed right now.Please contact ACADEMIC OFFICE");
                return;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        /*open the corresponding file from which instructor will upload the grades for this session*/
        String instructor_name=email.split("@")[0];
        String csvFilePath = dir_path+"/"+instructor_name+".csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] header = reader.readNext();
            if (header == null) {
                System.out.println("CSV file is empty, try again after entering the grades");
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                /*System.out.println(String.join(", ", row));*/
                String cur_session=row[0],studentid=row[1],courseid=row[2],grade=row[3];
                String update_query="update all_student_data set grade='"+grade.toUpperCase()+"' where id='"+studentid+"' and courseid='"+courseid+"' and session='"+cur_session+"';";
                try{
                    stmt=conn.createStatement();
                    stmt.executeUpdate(update_query);
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            /*id,session,courseid is primary in all_student_data*/
            System.out.println("Grades updated successfully from CSV file!");
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void transcript(Statement stmt,Connection conn,Scanner in,String session)throws IOException{
        String id;
        System.out.print("Please enter student ID:");
        id=in.next();
        Generate_Transcript.transcript(id,stmt,conn,session,1);
    }
    public static int faculty_login_front_end(Scanner in, Statement stmt, String email, String password,Connection conn)throws IOException{
        int t=0,ch=0;
        System.out.println("----FACULTY LOGIN----");
        System.out.println("Press 1=View grade of all students");
        System.out.println("Press 2=Register the courses");
        System.out.println("Press 3=Deregister the courses");
        System.out.println("Press 4=Update course grade for students");
        System.out.println("Press 5=To generate transcript");
        System.out.print("Enter your choice:");
        ch=in.nextInt();
        ResultSet rs_current_sem;
        String curr_session="";
        try{
            rs_current_sem=stmt.executeQuery("select * from event_table_acads where lower(current_sem)='y';");
            while(rs_current_sem.next()){
                curr_session=rs_current_sem.getString("session");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(ch==1){
            view_grade(in,stmt);
        }
        else if(ch==2){
            register_course(stmt,in,email,curr_session,conn);
            System.out.println("option 2");
        }
        else if(ch==3){
            deregister_course(stmt,in,email,curr_session,conn);
        }
        else if(ch==4){
            update_course_grade(curr_session,stmt,conn,email);
        }
        else if(ch==5){
            transcript(stmt,conn,in,curr_session);
        }
        else{
            System.out.println("Please enter correct option");
        }
        System.out.print("Press 1 to go back to login page else 0 to continue:");
        t=in.nextInt();
        System.out.println("Value of t="+t);
        return t;
    }
}

package org.iitrpr.Pages.Academic_Office;

import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.iitrpr.Utilities.Generate_Transcript;

import java.util.Set;
import java.util.HashSet;

import java.util.HashMap;
public class Academic_Office_home_page {

    public static String dir_path="/home/nishant/Documents/Academics/Software_Engineering/Project/mini_project_2/src/main/java/org/iitrpr/grade_submission";
    public static String dir_path_2="/home/nishant/Documents/Academics/Software_Engineering/Project/mini_project_2/src/main/java/org/iitrpr/courses";
    public static void view_grade(Scanner in,Statement stmt){
        String student_id;
        System.out.println("Enter student Entry Number:");
        student_id=in.next();
        try{
            String query_student="select * from all_student_data where id='"+student_id+"' and grade!='NA';";
            String check_student="select * from students where id='"+student_id+"';";
            ResultSet rs_check_student=stmt.executeQuery(check_student);
            boolean student_present=false,data_present=false;
            while(rs_check_student.next()){
                student_present=true;
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

    public static void view_course_catalog(Statement stmt){
        String query="select * from course_catalog";
        try{
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()){
                System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void edit_course_catalog(Scanner in,Statement stmt){
        int ch=0,view=0;
        do{
            int n;
            String course_id="",course_name="",ltpsc="",department="",query="";
            System.out.println("EDIT COURSE CATALOG MENU");
            System.out.println("Press 1: To add a course");
            System.out.println("Press 2: To view course catalog");
            System.out.println("Press 3: To exit course catalog");
            System.out.print("Enter your choice:");
            ch=in.nextInt();
            in.nextLine();
            if(ch == 2){
                view_course_catalog(stmt);
            }
            else if(ch == 3){
                return;
            }
            else if(ch == 1){
                System.out.print("Enter course id:");
                course_id=in.next();
                in.nextLine();
                System.out.print("Enter course name:");
                course_name=in.nextLine();
                System.out.print("Enter department:");
                department=in.nextLine();
                System.out.print("Enter L-T-P-S-C:");
                ltpsc=in.next();
                in.nextLine();
                System.out.print("Enter number of prerequisites:");
                n=in.nextInt();
                in.nextLine();
                if(n == 0) n=1;
                String a[]=new String[n];
                System.out.print("Enter all n prerequisites space separated:");
                for(int i=0;i<n;i++){
                    a[i]=in.next();
                }
                String all_subjects="";
                query="insert into course_catalog values ('"+course_id+"','"+course_name+"','"+department+"','"+ltpsc+"','{";
                for(int i=0;i<n;i++){
                    all_subjects=all_subjects+"\""+a[i]+"\",";
                }
                all_subjects=all_subjects.substring(0,all_subjects.length()-1);
                query=query+all_subjects+"}');";
            }
            /*System.out.println(query);*/
            try{
                stmt.executeUpdate(query);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }while(true);
    }

    public static void remove_previous_grade_submissions(){
        /**/
        File directory=new File(dir_path);
        String file_names[]=directory.list();
        for(String x:file_names){
            String file_name_path=dir_path+"/"+x;
            try (CSVWriter writer = new CSVWriter(new FileWriter(file_name_path))) {
                String [] header={"Session","Studentid","Courseid","Grade"};
                writer.writeNext(header);
            } catch (IOException e) {
                System.out.println("Error writing to CSV file: " + e.getMessage());
            }
        }
    }

    public static void add_courses_in_the_beginning_of_semester(Statement stmt,Connection conn) throws CsvValidationException, IOException {
        String pc=dir_path_2+"/"+"program_core.csv";
        String ec=dir_path_2+"/"+"engineering_core.csv";
        String elec=dir_path_2+"/"+"elective.csv";
        /*------------------------------------------------------------------------------*/
        try (CSVReader reader = new CSVReader(new FileReader(pc))) {
            String[] header = reader.readNext();
            if (header == null) {
                System.out.println("Program core CSV file is empty");
            }
            stmt.executeUpdate("delete from program_core");
            String[] row;
            while ((row = reader.readNext()) != null) {
                stmt=conn.createStatement();
                String query="insert into program_core values('"+row[0]+"','"+row[1]+"',"+row[2]+","+row[3]+");";
                stmt.executeUpdate(query);
            }
            System.out.println("Program cores added for this semester successfully!");
            stmt=conn.createStatement();
        } catch (SQLException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        /*------------------------------------------------------------------------------*/

        /*------------------------------------------------------------------------------*/
        try (CSVReader reader = new CSVReader(new FileReader(ec))) {
            String[] header = reader.readNext();
            if (header == null) {
                System.out.println("Engineering core CSV file is empty");
            }
            /*delete previous semester data*/
            stmt.executeUpdate("delete from engineering_core");
            String[] row1;
            while ((row1 = reader.readNext()) != null) {
                /*System.out.println(String.join(", ", row));*/
                stmt=conn.createStatement();
                String query="insert into engineering_core values('"+row1[0]+"',"+row1[1]+","+row1[2]+");";
                stmt.executeUpdate(query);
            }
            System.out.println("Engineering cores added for this semester successfully!");
            stmt=conn.createStatement();
        } catch (SQLException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        /*------------------------------------------------------------------------------*/

        /*------------------------------------------------------------------------------*/
        try (CSVReader reader = new CSVReader(new FileReader(elec))) {
            String[] header = reader.readNext();
            if (header == null) {
                System.out.println("Electives CSV file is empty");
            }
            stmt.executeUpdate("delete from elective");
            String[] row;
            while ((row = reader.readNext()) != null) {
                stmt=conn.createStatement();
                String query="insert into electives values('"+row[0]+"',"+row[1]+","+row[2]+",'"+row[3]+"');";
                stmt.executeUpdate(query);
            }
            System.out.println("Electives added for this semester successfully!");
        } catch (SQLException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        /*------------------------------------------------------------------------------*/
    }

    /*
    * SEM3=19
    * SEM4=23.5
    * SEM5=30
    * SEM6
    * SEM7
    * SEM8
    * */

    public static void edit_sem_info(Scanner in, Statement stmt, Connection conn){
        int choice,t;
        String session,insert_query="",check_query="";
        ResultSet rs=null,rs1=null;
        do{
            System.out.println("----AVAILABLE OPTIONS----");
            System.out.println("Press 1=To start a new session");
            System.out.println("Press 2=To start course floating");
            System.out.println("Press 3=To start course registration");
            System.out.println("Press 4=To start grade submission");
            System.out.println("Press 5=To stop grade submission");
            System.out.println("Press 6=To stop course registration");
            System.out.println("Press 7=To stop course floating");
            System.out.println("Press 8=To end the session");
            System.out.println("Press 9=To go back to academic office home page");
            System.out.print("Enter your choice:");
            choice=in.nextInt();
            System.out.println("Choices in the funct="+choice);
            try{
                stmt=conn.createStatement();
                String curr_sem="";
                boolean is_sem_present=false;
                check_query="select * from event_table_acads where lower(current_sem)='y';";
                rs=stmt.executeQuery(check_query);
                while(rs.next()){
                    is_sem_present=true;
                    curr_sem=rs.getString("session");
                }
                /*System.out.println(is_sem_present+" "+curr_sem);*/
                if(choice == 1){
                    if(is_sem_present){
                        System.out.println("Already a session running. You can't enter a new session");
                    }
                    else{
                        System.out.print("Enter new session:");
                        session=in.next();
                        in.nextLine();
                        insert_query="insert into event_table_acads values('"+session+"','Y','0','0','0');";
                        System.out.println(insert_query);
                        stmt.executeUpdate(insert_query);
                    }
                }
                else if(choice == 2){
                    System.out.println("choice 2");
                    /*System.out.println("Is sem present:="+is_sem_present);*/
                    if(is_sem_present){
                        rs1=stmt.executeQuery("select * from event_table_acads where session='"+curr_sem+"' and courses_float='0';");
                        if(!rs1.isBeforeFirst()){
                            System.out.println("You can start course floating only once in a session");
                        }
                        else{
                            stmt.executeUpdate("update event_table_acads set courses_float='Y' where session='"+curr_sem+"';");
                            System.out.println("Course floating started for session="+curr_sem);
                        }
                    }
                    else{
                        System.out.println("There is no running session");
                    }
                }
                else if(choice == 3){
                    System.out.println("yo bhai");
                    rs=stmt.executeQuery("select * from event_table_acads where lower(current_sem)='y';");
                    /*current session and course floating*/
                    /*course have been floated but not even once course registeration started*/
                    String are_courses_floated="",course_reg;

                    if(!rs.isBeforeFirst()){
                        System.out.println("No session is currently running");
                    }
                    else{
                        rs.next();
                        are_courses_floated=rs.getString("courses_float");
                        course_reg=rs.getString("courses_add_drop");
                        System.out.println("courses floated="+are_courses_floated);
                        if(are_courses_floated.equalsIgnoreCase("Y")){
                            if( course_reg.equalsIgnoreCase("0")){
                                stmt.executeUpdate("update event_table_acads set courses_add_drop='Y' where session='"+curr_sem+"';");
                                System.out.println("Course registration opened");
                            }
                            else{
                                System.out.println("You can start course add/drop only once in a session");
                            }
                        }
                        else{
                            System.out.println("Can't open course registration before allowing course floating");
                        }
                    }
                }
                else if(choice == 4){
                    String courses_floated="",course_reg="",grade="";
                    rs=stmt.executeQuery("select * from event_table_acads where lower(current_sem)='y';");
                    if(!rs.isBeforeFirst()){
                        System.out.println("No session is currently running");
                    }
                    else{
                        rs.next();
                        courses_floated=rs.getString("courses_float");
                        course_reg=rs.getString("courses_add_drop");
                        grade=rs.getString("grade_submission");
                        if(courses_floated.equalsIgnoreCase("N")){
                            if(course_reg.equalsIgnoreCase("N")){
                                if(grade.equalsIgnoreCase("0")){
                                    stmt.executeUpdate("update event_table_acads set grade_submission='Y' where session='"+curr_sem+"';");
                                    System.out.println("Grade submission by faculties allowed");
                                }
                                else{
                                    System.out.println("Grade submission allowed only once.");
                                }
                            }
                            else{
                                if(course_reg.equalsIgnoreCase("Y")) System.out.println("First end course registration");
                                else System.out.println("First start course registration");
                            }
                        }
                        else{
                            System.out.println(courses_floated+" "+course_reg+" "+grade);
                            if(courses_floated.equalsIgnoreCase("Y")) System.out.println("First end course floating");
                            else System.out.println("First start course floating");
                        }
                    }
                }
                else if(choice == 5){
                    rs=stmt.executeQuery("select * from event_table_acads where session='"+curr_sem+"';");
                    if(!rs.isBeforeFirst()){
                        System.out.println("No session is currently running");
                    }
                    else{
                        rs.next();
                        String grade_submit=rs.getString("grade_submission");
                        if(grade_submit.equalsIgnoreCase("Y")){
                            stmt.executeUpdate("update event_table_acads set grade_submission='N' where session='"+curr_sem+"';");
                            System.out.println("Grade Submission stopped");
                        }
                        else{
                            System.out.println("Grade submission not yet started");
                        }
                    }
                }
                else if(choice == 6){
                    rs=stmt.executeQuery("select * from event_table_acads where session='"+curr_sem+"';");
                    if(!rs.isBeforeFirst()){
                        System.out.println("No session is currently running");
                    }
                    else{
                        rs.next();
                        String course_reg=rs.getString("courses_add_drop");
                        if(course_reg.equalsIgnoreCase("Y")){
                            stmt.executeUpdate("update event_table_acads set courses_add_drop='N' where session='"+curr_sem+"';");
                            System.out.println("COURSE REGISTRATION STOPPED");
                        }
                        else{
                            System.out.println("Course registration not yet started");
                        }
                    }
                }
                else if(choice == 7){
                    rs=stmt.executeQuery("select * from event_table_acads where session='"+curr_sem+"';");
                    if(!rs.isBeforeFirst()){
                        System.out.println("No session is currently running");
                    }
                    else{
                        rs.next();
                        String course_float=rs.getString("courses_float");
                        if(course_float.equalsIgnoreCase("Y")){
                            stmt.executeUpdate("update event_table_acads set courses_float='N' where session='"+curr_sem+"';");
                            System.out.println("COURSE FLOATING STOPPED");
                        }
                    }
                }
                else if(choice == 8){
                    rs=stmt.executeQuery("select * from event_table_acads where session='"+curr_sem+"';");
                    if(!rs.isBeforeFirst()){
                        System.out.println("No session is currently running");
                    }
                    else{
                        rs.next();
                        String course_float=rs.getString("courses_float"),course_rg=rs.getString("courses_add_drop"),grade=rs.getString("grade_submission");
                        System.out.println("yaar"+course_float+" "+course_rg+" "+grade);
                        if(course_float.equalsIgnoreCase("N")){
                            if(course_rg.equalsIgnoreCase("N")){
                                if(grade.equalsIgnoreCase("N")){
                                    stmt.executeUpdate("delete from event_table_acads where session='"+curr_sem+"';");
                                    System.out.println("session="+curr_sem+" finished. Thank you!!!!");
                                    remove_previous_grade_submissions();
                                }
                                else{
                                    System.out.println("End grade submission first");
                                }
                            }
                            else{
                                System.out.println("End course registration");
                            }
                        }
                        else{
                            System.out.println("End course floating");
                        }
                    }
                    /*check for students who have reached 8 semester and are eligible for graduation*/
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }while(choice!=9);
    }


    public static void transcript(Statement stmt,Connection conn,Scanner in,String session)throws IOException{
        String id;
        System.out.print("Please enter student ID:");
        id=in.next();
        in.nextLine();
        Generate_Transcript.transcript(id,stmt,conn,session,1);
    }
    public static int academic_login_front_end(Scanner in, Statement stmt, String email, String password,Connection conn) throws IOException, CsvValidationException {
        int t=0,ch=0;
        System.out.println("----ACADEMIC OFFICE LOGIN----");
        System.out.println("Press 1=View grade of all students");
        System.out.println("Press 2=To edit the course catalog(add or remove new courses)");
        System.out.println("Press 3=To generate transcripts of students");
        System.out.println("Press 4=To add a session information, whether to allow course registration,grade submission");
        System.out.println("Press 5=To specify a course for current session and its type");
        System.out.print("Enter your choice:");
        ch=in.nextInt();
        System.out.println("mera choice="+ch);
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
        if(ch == 1){
            System.out.println("yo");
            view_grade(in,stmt);
        }
        else if(ch == 2){
            if(email.equals("deanstaff@iitrpr.ac.in")){
                edit_course_catalog(in,stmt);
            }
            else{
                System.out.println("You are not allowed to edit course catalog");
            }
        }
        else if(ch == 3){
            transcript(stmt,conn,in,curr_session);
        }
        else if(ch == 4){
            edit_sem_info(in,stmt,conn);
        }
        else if(ch == 5){
            add_courses_in_the_beginning_of_semester(stmt,conn);
        }
        System.out.print("Press 1 to go back to login page else 0 to continue:");
        t=in.nextInt();
        return t;
    }
}

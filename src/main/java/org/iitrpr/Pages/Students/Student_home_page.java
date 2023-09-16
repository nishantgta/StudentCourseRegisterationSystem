package org.iitrpr.Pages.Students;
import java.sql.*;
import java.util.*;

import com.opencsv.CSVWriter;
import org.iitrpr.Utilities.Generate_Transcript;

import java.io.FileWriter;
import java.io.IOException;

public class Student_home_page {

    private static final Map<String,Double> grades_score=new HashMap<>();

    public static String dir_path="/home/nishant/Documents/Academics/Software_Engineering/Project/mini_project_2/src/main/java/org/iitrpr/grade_submission";
    public static void grades_init(){
        grades_score.put("A",10.0);
        grades_score.put("A-",9.0);
        grades_score.put("B",8.0);
        grades_score.put("B-",7.0);
        grades_score.put("C",6.0);
        grades_score.put("C-",5.0);
        grades_score.put("D",4.0);
        grades_score.put("D-",3.0);
        grades_score.put("E",2.0);
        grades_score.put("E-",1.0);
        grades_score.put("F",0.0);
    }
    public static void view_personal_info(Statement stmt,String email,String password,ResultSet rs){
        System.out.println("\n\n\n");
        try{
            while(rs.next()){
                System.out.println("ENTRY NUMBER:="+rs.getString("id"));
                System.out.println("NAME:="+rs.getString("name"));
                System.out.println("EMAIL ID:="+rs.getString("emailid"));
                System.out.println("CONTACT NUMBER:="+rs.getString("contact_no"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void edit_personal_info(String id,Scanner in,Statement stmt){
        String name,contact_no;
        System.out.print("Enter new name:");
        name=in.nextLine();
        System.out.print("Enter new contact number:");
        contact_no=in.nextLine();
        String updatequery="update students set name='"+name+"' ,contact_no='"+contact_no+"' where emailid='"+id+"';";
        try{
            stmt.executeUpdate(updatequery);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void check_graduation(Statement stmt,Connection conn){
        String extract_students_query="select * from all_student_data where semester=8;";
        Set<String> students_to_be_checked_for_grad=new HashSet<>();
        try{
            ResultSet rs=stmt.executeQuery(extract_students_query);
            if(!rs.isBeforeFirst()){
                System.out.println("Not completed graduation till now");
            }
            while(rs.next()){
                students_to_be_checked_for_grad.add(rs.getString("id"));
            }
            /*check for each student*/
            boolean has_btp=false;
            for(String s:students_to_be_checked_for_grad){
                double pc=0.0,ec=0.0,elec=0.0;
                String query1="select * from all_student_data where id='"+s+"';";
                stmt=conn.createStatement();
                ResultSet student_rs=stmt.executeQuery(query1);
                while(student_rs.next()){
                    String type=student_rs.getString("course_type");
                    /*get credits of that course*/
                    Statement stmt2= conn.createStatement();
                    String query="select * from course_catalog c where c.courseid='"+student_rs.getString("courseid")+"';";
                    ResultSet get_credit=stmt2.executeQuery(query);
                    get_credit.next();
                    double credits=Double.parseDouble(get_credit.getString("ltpsc").split("-")[4]);
                    String[] arr=get_credit.getString("ltpsc").split("-");
                    System.out.println(Arrays.toString(arr));
                    if(type.equalsIgnoreCase("PC")){
                        pc+=credits;
                    }
                    else if(type.equalsIgnoreCase("EC")){
                        ec+=credits;
                    }
                    else if(type.equalsIgnoreCase("EL")){
                        elec+=credits;
                    }
                    System.out.println(student_rs.getString("courseid")+" "+student_rs.getString("course_type")+" "+credits);
                    /*System.out.println(student_rs.getString("courseid"));*/
                    if(student_rs.getString("courseid").equalsIgnoreCase("CP302")){
                        has_btp=true;
                    }
                    /*--------------------------*/
                }
                System.out.println(pc+" "+ec+" "+elec);
                if(pc<20.0 || ec<20.0 || elec<20.0){
                    System.out.println("Student "+s+" is not eligible for graduation");
                }
                else{
                    if(has_btp) {
                        System.out.println("Student " + s + " is eligible for graduation");
                    }
                    else{
                        System.out.println("Student has not completed the btp!");
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int get_current_semester(String curr_session,String email){
        /*first look for current program cores and engineering cores for this semester for a given student*/
        /*find current semester for the student*/
        String student_start_string=email.substring(0,4),curr_year_string=curr_session.substring(0,4);
        int student_start=Integer.parseInt(student_start_string),curr_year=Integer.parseInt(curr_year_string),curr_semester;
        curr_semester=(curr_year-student_start+1)*2;
        System.out.println("current session bhai="+curr_session);
        if(curr_session.charAt(5)=='1'){
            curr_semester--;
        }
        return curr_semester;
    }

    public static void display_all_pc(String dept,int sem,Statement stmt,int batch,String session,Connection conn){
        System.out.println("\n\n\nPR0GRAM CORE AVAILABLE FOR THIS SEMESTER");
        System.out.println("SUBJECT  INSTRUCTOR-ID  SESSION   CGPA_CONSTRAINT");
        String query="select * from program_core where req_sem="+sem+" and department='"+dept+"' and batch="+batch+";";
        /*System.out.println(query);*/
        ResultSet rs;
        ArrayList <String> program_cores=new ArrayList <String>();
        try{
            rs=stmt.executeQuery(query);
            while(rs.next()){
                /*System.out.println(rs.getString("course_id")+" "+rs.getString("department"));*/
                program_cores.add(rs.getString("course_id"));
            }
            for(String s:program_cores){
                stmt=conn.createStatement();
                /*System.out.print(s+" ");*/
                /*this course should be present in course offering*/
                ResultSet rs_if_offered=stmt.executeQuery("select * from courses_offered where courseid='"+s+"' and session='"+session+"';");
                rs_if_offered.next();
                System.out.println(rs_if_offered.getString("courseid")+" "+rs_if_offered.getString("instructorid")+" "+rs_if_offered.getString("session")+" "+rs_if_offered.getString("cg_constraint"));
            }
            System.out.println();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void display_all_ec(int sem,Statement stmt,int batch,Connection conn,String session){
        System.out.println("\n\n\nENGINEERING CORE AVAILABLE FOR THIS SEMESTER:");
        System.out.println("SUBJECT  INSTRUCTOR-ID  SESSION   CGPA_CONSTRAINT");
        String query="select * from engineering_core where req_sem="+sem+" and batch="+batch+";";
        /*System.out.println(query);*/
        ResultSet rs;
        ArrayList <String> engineering_cores=new ArrayList <String>();
        try{
            rs=stmt.executeQuery(query);
            while(rs.next()){
                /*System.out.println(rs.getString("course_id"));*/
                engineering_cores.add(rs.getString("course_id"));
            }
            for(String s:engineering_cores){
                /*System.out.print(s+" ");*/
                stmt=conn.createStatement();
                ResultSet rs_if_offered=stmt.executeQuery("select * from courses_offered where courseid='"+s+"' and session='"+session+"';");
                rs_if_offered.next();
                System.out.println(rs_if_offered.getString("courseid")+" "+rs_if_offered.getString("instructorid")+" "+rs_if_offered.getString("session")+" "+rs_if_offered.getString("cg_constraint"));
            }
            System.out.println();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static double calculate_sgpa(Statement stmt,String id,String session,Connection conn){
        /**/
        grades_init();
        ResultSet rs;
        double tot_reg_credits=0.0,score=0.0;
        try{
            stmt=conn.createStatement();
            rs=stmt.executeQuery("select * from all_student_data d,course_catalog c where d.id='"+id+"' and d.session='"+session+"' and c.courseid=d.courseid and grade<>'NA';");
            if(!rs.isBeforeFirst()){
                System.out.println("Student didn't did any course in the session "+session);
            }
            else{
                while(rs.next()){
                    String[] ltpsc_array=rs.getString("ltpsc").split("-");
                    double curr_credit=Double.parseDouble(ltpsc_array[4]);
                    tot_reg_credits+=curr_credit;
                    score+=(curr_credit*grades_score.get(rs.getString("grade")));
                }
                return  (score/tot_reg_credits);
            }
        }catch(SQLException e){e.printStackTrace();}
        return 0.0;
    }

    public static double calculate_cgpa(Statement stmt,String id,String session,Connection conn){
        id=id.substring(0,11).toUpperCase();
        grades_init();
        ResultSet rs;
        double tot_earned_credits=0.0,score=0.0;
        try{
            stmt=conn.createStatement();
            rs=stmt.executeQuery("select * from all_student_data d,course_catalog c where d.id='"+id+"' and c.courseid=d.courseid and grade<>'NA';");
            if(!rs.isBeforeFirst()){
                System.out.println("Student didn't did any course in the session "+session);
            }
            else{
                while(rs.next()){
                    String[] ltpsc_array=rs.getString("ltpsc").split("-");
                    double curr_credit=Double.parseDouble(ltpsc_array[4]);
                    if(!rs.getString("ltpsc").equalsIgnoreCase("F"))  tot_earned_credits+=curr_credit;
                    String gr=rs.getString("grade");
                    score+=(curr_credit*grades_score.get(rs.getString("grade")));
                }
                return  (score/tot_earned_credits);
            }
        }catch(SQLException e){e.printStackTrace();}
        return 0.0;
    }

    public static void enter_in_instructor_csv(String id,String courseid,String session,String instructor_id){
        String[] ins_name=instructor_id.split("@");
        String file_path=dir_path+"/"+ins_name[0]+".csv";
        try (CSVWriter writer = new CSVWriter(new FileWriter(file_path,true))) {
            String[] data = {session,id,courseid,"NA"};
            writer.writeNext(data);
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public static void register_core_courses(String email,Statement stmt,int sem,String dept,int batch,Connection conn,String session,int mode){
        String id=email.substring(0,11).toUpperCase(),query;
        /*System.out.println("CURRENT CGPA="+calculate_cgpa(stmt,id,session,conn));*/
        String type="";
        if(mode == 1) {
            query = "select * from program_core where req_sem=" + sem + " and department='" + dept + "' and batch=" + batch + ";";
            type="PC";
        }
        else {
            query = "select * from engineering_core where req_sem=" + sem + " and batch=" + batch + ";";
            type="EC";
        }
        /*System.out.println(id+" "+query);*/
        try{
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next())/*for all cores, I will check whether he satisfies all the requirements*/{
                stmt=conn.createStatement();
                String core_course=rs.getString("course_id");
                /*search for core_courese in course catalog*/
                ResultSet course_in_catalog=stmt.executeQuery("select * from course_catalog where courseid='"+core_course+"';");
                course_in_catalog.next();
                Array pre_req= course_in_catalog.getArray("prerequisites");
                String []pre_req_array=(String[]) pre_req.getArray();
                int count=0,f=0;
                if(!pre_req_array[0].equalsIgnoreCase("NIL")){
                    f=1;
                    for (String s : pre_req_array) {
                        stmt = conn.createStatement();
                        /*find the pre_req in all_student_data*/
                        ResultSet check_pre_req = stmt.executeQuery("select * from all_student_data where id='" + id + "' and courseid='" + s + "' and grade<>'F';");
                        if (check_pre_req.isBeforeFirst()) {
                            count++;
                        } else {
                            System.out.println("Prerequisite " + s + " not satisfied.");
                            continue;
                        }
                    }
                }
                if(f == 0 || (f == 1 && count==pre_req_array.length)){
                    /*register the core courses*/
                    stmt=conn.createStatement();
                    String check_query="select * from all_student_data where id='"+id+"' and courseid='"+core_course+"' and grade<>'F';";
                    ResultSet check_if_course_already_completed=stmt.executeQuery(check_query);
                    if(check_if_course_already_completed.isBeforeFirst()){
                        System.out.println("Course "+core_course+" already completed or you are registered in it.");
                    }
                    else{
                        /*check whether the course is offered or not*/
                        stmt=conn.createStatement();
                        ResultSet check_if_course_offered=stmt.executeQuery("select * from courses_offered where courseid='"+core_course+"' and session='"+session+"';");
                        if(check_if_course_offered.isBeforeFirst()){
                            stmt=conn.createStatement();
                            String register_query,record_query;
                            check_if_course_offered.next();
                            String instructor_id=check_if_course_offered.getString("instructorid");
                            /*check CG constraint*/
                            double cg_constraint=check_if_course_offered.getDouble("cg_constraint");
                            double current_cgpa=calculate_cgpa(stmt,id,session,conn);
                            if(current_cgpa<cg_constraint) {
                                System.out.println("You are failing CGPA requirements,hence you can't register in the course "+core_course);
                                continue;
                            }
                            register_query="insert into all_student_data values('"+id+"','"+session+"',"+batch+",'"+core_course+"','NA',"+sem+",'"+type+"');";
                            /*System.out.println(register_query);*/
                            enter_in_instructor_csv(id,core_course,session,instructor_id);
                            stmt.executeUpdate(register_query);
                        }
                        else{
                            System.out.println("The course "+ core_course+" is not yet offered.");
                        }
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static double return_earned_credits_in_sem(String id,String session,int batch,Statement stmt,Connection conn){
        /*System.out.println("yo bhai");*/
        ResultSet rs,rs_ltpsc;
        String course,ltpsc;
        double tot_credits=0.0;
        try{
            rs=stmt.executeQuery("select * from all_student_data where id='"+id+"' and session='"+session+"' and batch="+batch+";");
            while(rs.next()){
                stmt=conn.createStatement();
                //System.out.println(rs.getString("id")+" "+rs.getString("session")+" "+rs.getString("batch")+" "+rs.getString("courseid")+" "+rs.getString("grade")+" "+rs.getString("semester"));
                /*bring LTPSC from course catalog*/
                course=rs.getString("courseid");
                rs_ltpsc=stmt.executeQuery("select * from course_catalog where courseid='"+course+"';");
                rs_ltpsc.next();
                ltpsc=rs_ltpsc.getString("ltpsc");
                String[] ltpsc_array=ltpsc.split("-");
                double credit=Double.parseDouble(ltpsc_array[4]);
                tot_credits+=credit;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return tot_credits;
    }

    public static void register_electives(String email,Statement stmt,int sem,String dept,int batch,Connection conn,String session,Scanner in){
        /*calculate the remaining credits for electives, first calculate credits for previous two sessions*/
        /**/
        String id=email.substring(0,11).toUpperCase(),query;
        int year=Integer.parseInt(session.substring(0,4)),number=Integer.parseInt(session.substring(5));
        String prev_session="",prev_prev_session="";
        if(number == 1){
            prev_session=Integer.toString(year-1)+"-2";
            prev_prev_session=Integer.toString(year-1)+"-1";
        }
        else if(number == 2){
            prev_session=Integer.toString(year)+"-1";
            prev_prev_session=Integer.toString(year-1)+"-2";
        }
        /*credits in previous semester */

        double prev_credits=return_earned_credits_in_sem(id,prev_session,batch,stmt,conn),prev_prev_credit=return_earned_credits_in_sem(id,prev_prev_session,batch,stmt,conn),total_credits=0.0;
        total_credits=Math.ceil(((prev_credits+prev_prev_credit)/2.0)*1.25);
        double reg_cred=return_earned_credits_in_sem(id,session,batch,stmt,conn);
        System.out.println("Total Available credits in this sem:"+total_credits);
        System.out.println("Credits registered till now:"+reg_cred);
        System.out.println("Available credits for electives"+(total_credits-reg_cred));
        /*check all the floated electives*/
        try{
            ResultSet electives_stated_by_acads=stmt.executeQuery("select * from elective;");
            ArrayList<String> elec_acad=new ArrayList<>();
            ArrayList<String> elec_available=new ArrayList<>();
            while(electives_stated_by_acads.next()){
                elec_acad.add(electives_stated_by_acads.getString("course_id"));
            }
            for(String s:elec_acad){
                stmt=conn.createStatement();
                ResultSet check_if_offered=stmt.executeQuery("select * from courses_offered where courseid='"+s+"';");
                if(check_if_offered.isBeforeFirst()){
                    elec_available.add(s);
                }
            }
            for(String s:elec_available){
                System.out.println(s);
            }
            if(elec_available.size()==0){
                System.out.println("No elective courses are offered by the faculty");
                return;
            }
            String elective_course;
            do{
                System.out.println("Press -1 to exit or 0 to continue");
                /*System.out.println("ashish"+inp);*/
                int ash=in.nextInt();
                if(ash==-1){return;}
                else if (ash!=0){continue;}
                System.out.print("Enter Elective course:");
                elective_course=in.next();
                if(elec_available.contains(elective_course)){
                    stmt=conn.createStatement();
                    stmt.executeUpdate("insert into all_student_data values('"+id+"','"+session+"',"+batch+",'"+elective_course+"','NA',"+sem+",'EL');");
                }
                else{
                    System.out.println("The entered elective course is not present in offered electives");
                }
            }while(true);

        }catch(SQLException e){}
    }

    /*
    * sem 1-> 6
    * sem 2-> 6
    * sem 3-> 7.5
    * sem 4-> 8
    * sem 5->
    * */

    /*
    * sem1=6
    * sem2=6
    * sem3=*/

    public static void add_drop(Statement stmt,String email,String password,Connection conn,Scanner in){
        /*check whether add/drop allowed for a semester*/
        ResultSet rs_if_current_sem,rs_student;
        String id=email.substring(0,11).toUpperCase();
        /*current semester,courses_add_drop*/
        try{
            rs_if_current_sem=stmt.executeQuery("select * from event_table_acads where (current_sem='Y' or current_sem='y') and (courses_add_drop='Y' or courses_add_drop='y');");
            if(rs_if_current_sem.isBeforeFirst()){
                System.out.println("sem present");
                /*there is a semester currently running*/
                String session="";
                while(rs_if_current_sem.next()){
                    session=rs_if_current_sem.getString("session");
                }
                System.out.println("current session bhai="+session);
                int current_semester=get_current_semester(session,email);
                /*pc and ec in this semester and this branch*/

                String department=""; /*student branch->department*/
                stmt = conn.createStatement();
                rs_student=stmt.executeQuery("select * from students where emailid='"+email+"' and password='"+password+"';");

                while(rs_student.next()){
                    department=rs_student.getString("department");
                }

                int batch=Integer.parseInt(email.substring(0,4));
                display_all_pc(department,current_semester,stmt,batch,session,conn);
                stmt=conn.createStatement();
                display_all_ec(current_semester,stmt,batch,conn,session);
                /*now register all program core and engineering core*/
                register_core_courses(email,stmt,current_semester,department,batch,conn,session,1);
                register_core_courses(email,stmt,current_semester,department,batch,conn,session,2);
                System.out.println("Total registered credits for this session="+return_earned_credits_in_sem(id,session,batch,stmt,conn));
                if(current_semester == 1 || current_semester == 2) return ;
                /*register for elective courses*/
                //register_electives(email,stmt,current_semester,department,batch,conn,session,in);
            }
            else{
                System.out.println("No semester is currently running or the course add/drop is not yet allowed");
            }
        }
        catch(SQLException e){e.printStackTrace();}
    }



    public static int student_login_front_end(Scanner in,Statement stmt,String email,String password,Connection conn){
        Statement stmt1=stmt;
        int t=0,ch=0;
        ResultSet rs=null;
        System.out.println("----STUDENT LOGIN----");
        System.out.println("Press 1=To view records up to current semester");
        System.out.println("Press 2=To view current CGPA");
        System.out.println("Press 3=To register for current semester");
        System.out.println("Press 4=To view personal information");
        System.out.println("Press 5=Edit personal information");
        System.out.println("Press 6=To check the list of eligible students for graduation");
        System.out.print("Enter your choice:");
        ch=in.nextInt();
        in.nextLine();
        System.out.println("bhai choice="+ch);
        try{
            String query="select * from students where emailid='"+email+"' and password='"+password+"';";
            rs=stmt.executeQuery(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(ch==4){
            /*view all records*/
            view_personal_info(stmt,email,password,rs);
        }
        else if(ch==2){
            try{
                ResultSet rs_bhai=stmt.executeQuery("select * from event_table_acads;");
                rs_bhai.next();
                System.out.println("YOur current CGPA is:"+calculate_cgpa(stmt,email,rs_bhai.getString("session"),conn));
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        else if(ch==3){
            add_drop(stmt,email,password,conn,in);
        }
        else if(ch==1){
            System.out.println("Record generated as a transcript in Transcript folder");
            try{
                ResultSet rs_session=stmt.executeQuery("select * from event_table_acads;");
                rs_session.next();
                Generate_Transcript.transcript(email.substring(0,11).toUpperCase(),stmt,conn,rs_session.getString("session"),3);
            }catch(SQLException e){} catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else if(ch==5){
            edit_personal_info(email,in,stmt);
        }
        else if(ch==6){
            try{
                stmt=conn.createStatement();
            }catch(SQLException e){
                e.printStackTrace();
            }
            check_graduation(stmt,conn);
        }
        System.out.print("Press 1 to go back to login page else 0 to continue:");
        t=in.nextInt();
        System.out.println("Exit choice="+t);
        return t;
    }
}

package org.iitrpr.Utilities;
import java.sql.*;
import org.iitrpr.Pages.Students.Student_home_page;

import java.io.FileWriter;
import java.io.IOException;
public class Generate_Transcript {
    public static void transcript(String id,Statement stmt,Connection conn,String session,int role)throws IOException{
        id=id.toUpperCase();
        /*all info up to current semester with SGPA, CGPA calculated and grades printed along with the courses*/
        /*find number of semesters up to current semester*/
        String filepath="";
        if(role == 1){
            filepath="/home/nishant/Documents/Academics/Software_Engineering/mini_project_2/src/main/java/org/iitrpr/Transcript/acad_transcript.txt";
        }
        else if(role == 2){
            filepath="/home/nishant/Documents/Academics/Software_Engineering/mini_project_2/src/main/java/org/iitrpr/Transcript/faculty_transcript.txt";
        }
        else{
            filepath="/home/nishant/Documents/Academics/Software_Engineering/mini_project_2/src/main/java/org/iitrpr/Transcript/student_transcript.txt";
        }

        String query="select * from all_student_data where id='"+id+"';";
        int current_semester=Student_home_page.get_current_semester(session,id);
        System.out.println("Current Semester="+current_semester);
        String og_session=id.substring(0,4)+"-1";
        FileWriter writer=new FileWriter(filepath);
        try{
            ResultSet rs=stmt.executeQuery(query);
            writer.write("STUDENT ID="+id);
            for(int i=1;i<=current_semester;i++){
                stmt=conn.createStatement();
                writer.write("\nSEMESTER="+i+" SESSION="+og_session+"\n");
                String query_get_data="select * from all_student_data where id='"+id+"' and semester="+i+" and session='"+og_session+"' and grade!='NA';";
                ResultSet get_data=stmt.executeQuery(query_get_data);
                while(get_data.next()){
                    String s1,s2,s3;
                    s1=get_data.getString("courseid");
                    s2=get_data.getString("grade");
                    s3=get_data.getString("course_type");
                    writer.write(s1+" "+s2+" "+s3+"\n");
                }
                double sgpa=Student_home_page.calculate_sgpa(stmt,id,og_session,conn);
                writer.write("SGPA for session "+og_session+" is="+sgpa+"\n");
                /*move og_session to next session*/
                if(og_session.charAt(5)=='1'){
                    og_session=og_session.substring(0,4)+"-2";
                }
                else{
                    int year=Integer.parseInt(og_session.substring(0,4))+1;
                    og_session=Integer.toString(year)+"-1";
                }
                writer.write("\n\n");
            }
            double cgpa=Student_home_page.calculate_cgpa(stmt,id,session,conn);
            writer.write("CGPA="+cgpa+"\n");
            writer.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}

package org.iitrpr.Pages.Students;

import com.opencsv.exceptions.CsvValidationException;
import org.iitrpr.Main;
import org.iitrpr.Pages.Academic_Office.Academic_Office_home_page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class Student_home_pageTest8 {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Student_home_pageTest8() throws SQLException {
    }
    @BeforeEach
    void setUp() {
        try{
            stmt.executeUpdate("insert into elective values('GE105','2021-1',2020,'Mechanical Engineering');");
            stmt.executeUpdate("insert into courses_offered values('GE105','harpreetsingh@iitrpr.ac.in','2021-1',0.0);");
            stmt.executeUpdate("delete from all_student_data where session='2021-1';");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void register_electives() {
        String input="0\nGE109\n0\nGE105\n-1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Student_home_page.register_electives("2020csb1103@iitrpr.ac.in",stmt,3,"Computer Science and Engineering",2020,conn,"2021-1",sc);
    }

    @AfterEach
    void tearDown() {
        try{
            stmt.executeUpdate("delete from elective where course_id='GE105';");
            stmt.executeUpdate("delete from program_core where course_id='CS203';");
            stmt.executeUpdate("delete from courses_offered where courseid='GE105';");
            stmt.executeUpdate("delete from all_student_data where courseid='CS203' and id='2020CSB1103';");
            stmt.executeUpdate("delete from all_student_data where courseid='GE105' and id='2020CSB1103';");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1',2020,'HS201','NA',3,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1',2020,'CS201','NA',3,'PC');");
        }catch(SQLException e){
        }
    }

}


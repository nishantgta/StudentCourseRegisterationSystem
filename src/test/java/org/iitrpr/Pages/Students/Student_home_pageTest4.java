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

class Student_home_pageTest4 {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Student_home_pageTest4() throws SQLException {
    }
    @BeforeEach
    void setUp() {
        try{
            /*stmt.executeUpdate("insert into program_core values('CS203','Computer Science and Engineering',3,2020);");*/
            stmt.executeUpdate("update courses_offered set cg_constraint=10.0 where courseid='CS201' and instructorid='anilshukla@iitrpr.ac.in';");
            stmt.executeUpdate("delete from all_student_data where session='2021-1';");
        }catch(SQLException e){}
    }

    @Test
    void student_login_front_end() {
        String input="3\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Student_home_page.student_login_front_end(sc,stmt,"2020csb1103@iitrpr.ac.in","verma12",conn);
    }

    @AfterEach
    void tearDown() {
        try{
            stmt.executeUpdate("update courses_offered set cg_constraint=0.0 where courseid='CS201' and instructorid='anilshukla@iitrpr.ac.in';");
            stmt.executeUpdate("delete from program_core where course_id='CS203';");
            stmt.executeUpdate("delete from all_student_data where courseid='CS203' and id='2020CSB1103';");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1',2020,'HS201','NA',3,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1',2020,'CS201','NA',3,'PC');");
        }catch(SQLException e){
        }
    }

}


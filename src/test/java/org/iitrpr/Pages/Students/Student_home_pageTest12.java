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

class Student_home_pageTest12 {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Student_home_pageTest12() throws SQLException {
    }
    @BeforeEach
    void setUp() {
        try{
            stmt.executeUpdate("insert into course_catalog values('CS500','COMPUTER','Computer Science and Engineering','3-1-2-6-20','{\"NIL\"}') on conflict(courseid) do nothing;");
            stmt.executeUpdate("insert into course_catalog values('HS500','HUMAN','Humanities and Social Sciences','3-1-2-6-20','{\"NIL\"}')on conflict(courseid) do nothing;");
            stmt.executeUpdate("insert into course_catalog values('MA500','math','Mathematics','3-1-2-6-20','{\"NIL\"}')on conflict(courseid) do nothing;");

            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2023-2',2020,'CS500','A',8,'PC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2023-2',2020,'HS500','A',8,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2023-2',2020,'MA500','A',8,'EL');");

            stmt.executeUpdate("insert into elective values('GE105','2021-1',2020,'Mechanical Engineering');");
            stmt.executeUpdate("insert into courses_offered values('GE105','harpreetsingh@iitrpr.ac.in','2021-1',0.0);");
            stmt.executeUpdate("delete from all_student_data where session='2021-1';");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void register_electives() {
        String input="6\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Student_home_page.student_login_front_end(sc,stmt,"2020csb1103@iitrpr.ac.in","verma12",conn);
    }

    @AfterEach
    void tearDown() {
        try{
            stmt.executeUpdate("delete from elective where course_id='GE105';");
            stmt.executeUpdate("delete from program_core where course_id='CS203';");
            stmt.executeUpdate("delete from courses_offered where courseid='GE105';");
            stmt.executeUpdate("delete from all_student_data where session='2023-2'");
            stmt.executeUpdate("delete from all_student_data where courseid='CS203' and id='2020CSB1103';");
            stmt.executeUpdate("delete from all_student_data where courseid='GE105' and id='2020CSB1103';");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1',2020,'HS201','NA',3,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1',2020,'CS201','NA',3,'PC');");
        }catch(SQLException e){
        }
    }

}



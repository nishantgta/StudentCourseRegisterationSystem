package org.iitrpr.Pages.Faculty;

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

class Faculty_home_pageTest7 {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Faculty_home_pageTest7() throws SQLException {
    }

    @BeforeEach
    void setUp(){
        try{
            stmt.executeUpdate("delete from courses_offered where session='2021-1'");
            stmt.executeUpdate("insert into elective values('CS302',3,2020);");
            /*stmt.executeUpdate("delete from program_core;");
            stmt.executeUpdate("delete from engineering_core;");
            stmt.executeUpdate("delete from elective;");*/
        }catch(SQLException e){}
    }

    @Test
    void faculty_login_front_end() throws IOException {
        String input="2\nCS201\n0.0\n1\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Faculty_home_page.faculty_login_front_end(sc,stmt,"anilshukla@iitrpr.ac.in","anil12",conn);
    }

    @AfterEach
    void tearDown() {
        try{
            /*stmt.executeUpdate("insert into program_core values('CS201','Computer Science and Engineering',3,2020);");
            stmt.executeUpdate("insert into program_core values('HS201','Humanities and Social Sciences',3,2020);");*/
            //stmt.executeUpdate("insert into courses_offered values('CS201','anilshukla@iitrpr.ac.in','2021-1',0.0);");
            stmt.executeUpdate("delete from elective where course_id='CS302';");
            stmt.executeUpdate("insert into courses_offered values('HS201','amritesh@iitrpr.ac.in','2021-1',0.0);");
        }catch(SQLException e){}
    }
}
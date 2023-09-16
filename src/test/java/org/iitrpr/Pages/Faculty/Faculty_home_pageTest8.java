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

class Faculty_home_pageTest8 {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Faculty_home_pageTest8() throws SQLException {
    }

    @BeforeEach
    void setUp(){
        try{
            stmt.executeUpdate("delete from event_table_acads;");
            stmt.executeUpdate("insert into event_table_acads values('2020-1','Y','N','N','Y');");
        }catch(SQLException e){}
    }

    @Test
    void faculty_login_front_end() throws IOException {
        String input="4\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Faculty_home_page.faculty_login_front_end(sc,stmt,"anilshukla@iitrpr.ac.in","anil12",conn);
    }

    @AfterEach
    void tearDown() {
        try{
            stmt.executeUpdate("insert into event_table_acads values('2021-1','Y','Y','Y','0');");
            stmt.executeUpdate("insert into engineering_core values('HS201',3,2020);");
        }catch(SQLException e){}
    }
}

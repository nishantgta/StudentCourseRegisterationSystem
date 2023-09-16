package org.iitrpr.Pages.Academic_Office;

import com.opencsv.exceptions.CsvValidationException;
import org.iitrpr.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

class Academic_Office_home_pageTest2 {
    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    /*void Academic_Office_home_pageTest() throws SQLException {
    }*/

    Academic_Office_home_pageTest2() throws SQLException {
    }

    @BeforeEach
    void setUp() {
        String query="delete from all_student_data;";
        try{stmt.executeUpdate(query);}catch(SQLException e){}
    }

    @Test
    void academic_login_front_end() throws CsvValidationException, IOException {
        String input="1\n2020CSB1103\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Academic_Office_home_page.academic_login_front_end(sc,stmt,"deanstaff@iitrpr.ac.in","acad1",conn);
    }

    @AfterEach
    void tearDown() {
        try{
            stmt=conn.createStatement();
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2020-1','2020','CS101','B',1,'PC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2020-2','2020','MA102','A-',2,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2020-2','2020','GE104','B',2,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1','2020','HS201','NA',3,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2020-1','2020','GE103','A',1,'EC');");
            stmt.executeUpdate("insert into all_student_data values('2020CSB1103','2021-1','2020','CS201','NA',3,'PC');");
        }catch(SQLException e){}
    }


    /*@Test
    void academic_login_front_end() throws CsvValidationException, IOException {
        String input="1\n1\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Academic_Office_home_page.academic_login_front_end(sc,stmt,"deanstaff@iitrpr.ac.in","acad1",conn);
    }

    @Test
    void academic_login_front_end_1() throws CsvValidationException, IOException {
        String input="2\n1\nHS302\nINDUSTRIAL MANAGEMENT\nHumanities and Social Sciences\n3-1-0-5-3\n0\nNIL\n3\n1\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Academic_Office_home_page.academic_login_front_end(sc,stmt,"deanstaff@iitrpr.ac.in","acad1",conn);
    }

    @Test
    void academic_login_front_end_2() throws CsvValidationException, IOException {
        String input="3\n2020CSB1103\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Academic_Office_home_page.academic_login_front_end(sc,stmt,"deanstaff@iitrpr.ac.in","acad1",conn);
    }*/




}

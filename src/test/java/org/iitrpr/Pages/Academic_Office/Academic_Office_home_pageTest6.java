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

import static org.junit.jupiter.api.Assertions.*;

class Academic_Office_home_pageTest6 {
    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Academic_Office_home_pageTest6() throws SQLException {
    }

    @BeforeEach
    void setUp()  {
        String query1="delete from event_table_acads;";
        String query2="insert into event_table_acads values('2020-1','Y','N','N','0');";
        try{
            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void academic_login_front_end_6() throws CsvValidationException, IOException {
        String input="4\n5\n4\n5\n9\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Academic_Office_home_page.academic_login_front_end(sc,stmt,"office-academics-1@iitrpr.ac.in","acad2",conn);
    }

    @AfterEach
    void tearDown() {
        String query1="insert into event_table_acads values('2021-1','Y','Y','Y','0');";
        try{
            stmt.executeUpdate(query1);
        }catch(SQLException e){
            e.printStackTrace();
        }
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
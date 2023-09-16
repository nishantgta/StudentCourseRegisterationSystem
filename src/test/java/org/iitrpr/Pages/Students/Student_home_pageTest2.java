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

class Student_home_pageTest2 {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Student_home_pageTest2() throws SQLException {
    }
    @BeforeEach
    void setUp() {
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
    }

}

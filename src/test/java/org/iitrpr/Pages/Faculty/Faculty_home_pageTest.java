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

class Faculty_home_pageTest {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Faculty_home_pageTest() throws SQLException {
    }

    @BeforeEach
    void setUp(){}

    @Test
    void faculty_login_front_end() throws IOException {
        String input="5\n2020CSB1103\n1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Faculty_home_page.faculty_login_front_end(sc,stmt,"neeraj@iitrpr.ac.in","neeraj12",conn);
    }

    @AfterEach
    void tearDown() {

    }
}
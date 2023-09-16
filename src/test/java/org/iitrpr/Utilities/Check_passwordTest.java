package org.iitrpr.Utilities;

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

class Check_passwordTest {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    Check_passwordTest() throws SQLException {
    }

    @Test
    void check_password() {
        Check_password.check_password(1,"2020csb1103@iitrpr.ac.in","verma12",stmt);
    }

    @Test
    void check_password_1() {
        Check_password.check_password(2,"neeraj@iitrpr.ac.in","neeraj12",stmt);
    }

    @Test
    void check_password_2() {
        Check_password.check_password(3,"deanstaff@iitrpr.ac.in","acad1",stmt);
    }
}
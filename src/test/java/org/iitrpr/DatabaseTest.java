package org.iitrpr;

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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Academic_Office_home_page acad=new Academic_Office_home_page();
    Scanner in =new Scanner(System.in);

    DatabaseTest() throws SQLException {
    }

    @Test
    void create_tables() throws IOException {
        Database.create_tables(stmt);
    }

    @Test
    void insert_data_in_a_table() {

    }

    @Test
    void insert_data() throws IOException {
        Database.insert_data(stmt);
    }

    @Test
    void main() {
    }
}
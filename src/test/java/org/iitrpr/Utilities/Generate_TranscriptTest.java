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

class Generate_TranscriptTest {

    Connection conn=Main.connect();
    Statement stmt=conn.createStatement();
    Scanner in =new Scanner(System.in);

    Generate_TranscriptTest() throws SQLException {
    }

    @Test
    void transcript() throws IOException {
        Generate_Transcript.transcript("2020CSB1103",stmt,conn,"2021-1",1);
    }
}
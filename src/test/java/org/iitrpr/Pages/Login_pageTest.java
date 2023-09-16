package org.iitrpr.Pages;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class Login_pageTest {

    @Test
    void login_page_front_end() throws IOException {
        String input="1\n2020csb1103@iitrpr.ac.in\nverma12\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Login_page.login_page_front_end(sc);
    }

    @Test
    void login_page_front_end_1() throws IOException {
        String input="2\nneeraj@iitrpr.ac.in\nneeraj12\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Login_page.login_page_front_end(sc);
    }

    @Test
    void login_page_front_end_2() throws IOException {
        String input="3\nndeanstaff@iitrpr.ac.in\nacad1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Login_page.login_page_front_end(sc);
    }

    @Test
    void login_page_front_end_3() throws IOException {
        String input="9\n3\nndeanstaff@iitrpr.ac.in\nacad1\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        Scanner sc=new Scanner(in);
        Login_page.login_page_front_end(sc);
    }
}
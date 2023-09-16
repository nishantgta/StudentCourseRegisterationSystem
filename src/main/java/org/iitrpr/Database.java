package org.iitrpr;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class Database {

    static String tables_path="/home/nishant/Documents/Academics/Software_Engineering/Project/mini_project_2/src/main/java/org/tables/tables.txt";
    static String data_path="/home/nishant/Documents/Academics/Software_Engineering/Project/mini_project_2/src/main/java/org/tables/";
    public static void create_tables(Statement stmt) throws IOException{
        File file;
        file = new File(tables_path);
        Scanner sc = new Scanner(file);
        String create_tables_query = "";
        while(sc.hasNextLine()){
            create_tables_query+=sc.nextLine()+" ";
        }
        /*try{
            stmt.executeUpdate(create_tables_query);
        }
        catch(SQLException e){
            e.printStackTrace();
        }*/
    }

    public static void insert_data_in_a_table(Scanner sc,String table_name,ResultSet rs,Statement stmt){
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int num_columns=rsmd.getColumnCount();

            String[] columms_types=new String[num_columns];
            for(int i=1;i<=num_columns;i++){
                columms_types[i-1]=rsmd.getColumnTypeName(i);
                /*System.out.print(columms_types[i-1]+" ");*/
            }
            System.out.println();
            sc.nextLine();
            while(sc.hasNextLine()){
//                System.out.println(table_name);
                String row_in_csv = sc.nextLine();
                if(row_in_csv.length()==0) continue;
                /*System.out.println(row_in_csv);*/
                String[] row_data=row_in_csv.split(",");
                if(row_data.length==0) continue;
                /*System.out.println("table_name="+table_name+",row_data="+row_data.length+",num_columns="+num_columns);*/
                /*System.out.println(row_in_csv);*/
                for(int i=0;i< row_data.length;i++){
                    row_data[i]=row_data[i].replace(':',',');
                    row_data[i]=row_data[i].replace("\"\"","\"");
                    if(row_data[i].charAt(0)=='"'){
                        row_data[i]=row_data[i].substring(1);
                    }
                    if(row_data[i].charAt(row_data[i].length()-1)=='"'){
                        row_data[i]=row_data[i].substring(0,row_data[i].length()-1);
                    }
                    System.out.print(row_data[i]+" ");
                }
                System.out.println();
                String query = "INSERT INTO "+table_name+" VALUES(";
                for(int i=0;i<num_columns;i++){
                    if(i!=num_columns-1){
                        if(columms_types[i].equals("int4") ||columms_types[i].equals("numeric"))    query+=row_data[i]+",";
                        else query+="'"+row_data[i]+"',";
                    }
                    else{
                        if(columms_types[i].equals("int4") ||columms_types[i].equals("numeric"))    query+=row_data[i]+");";
                        else query+="'"+row_data[i]+"');";
                    }
                }
                System.out.println(query);
                /*stmt.executeUpdate(query);*/
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void insert_data(Statement stmt) throws IOException{
        File file;
        boolean first=true;
        String[] tables_array={"departments","students","course_catalog","courses_offered","faculty","academic_office","program_core","engineering_core"};
        try{
            for(int i=0;i<tables_array.length;i++){
                ResultSet rs=stmt.executeQuery("select * from "+tables_array[i]+";");
                String new_path=data_path+tables_array[i]+".csv";
                file=new File(new_path);
                Scanner sc = new Scanner(file);
                System.out.println(new_path);
                insert_data_in_a_table(sc,tables_array[i],rs,stmt);
            }
//            for_testing(stmt);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {
        String db_url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "123456";
        String db_name="academic_portal";
        String all_databases_query = "SELECT datname from pg_database;";

        ResultSet rs,rs2 ;

        try(Connection conn = DriverManager.getConnection(db_url, username, password); Statement stmt = conn.createStatement()) {
              rs = conn.getMetaData().getCatalogs();
              rs2 = stmt.executeQuery(all_databases_query);
              boolean flag=false;
              while(rs2.next()){
                  String output = rs2.getString("datname");
                  if(output.equals(db_name)){
                      System.out.println("Database already exists");
                      flag=true;
                  }
              }
              String delete_database = "DROP DATABASE academic_portal";
              if(flag){
                 /* stmt.executeUpdate(delete_database);*/
              }
              String sql = "CREATE DATABASE academic_portal";
              /*stmt.executeUpdate(sql);*/
              System.out.println("Database created successfully...");
              db_url = "jdbc:postgresql://localhost:5432";
              Connection conn2 = DriverManager.getConnection(db_url+"/"+db_name, username, password);
              Statement stmt2 = conn2.createStatement();
              create_tables(stmt2);
              insert_data(stmt2);

        } catch (SQLException e) {
            System.out.println("error="+e.getErrorCode());
            if(e.getErrorCode() == 1007){
                //Database already exists
                System.out.println("This database already exists");
                System.out.println(e.getMessage());
            }
            else{
                e.printStackTrace();
            }
        }
    }
}

/*insert into course_catalog values('CS301','YO','1-2-3-4-5','{"CS305","CS302"}');*/

/*
create table t (
    tId char(6) primary key,
    constraint chk_t_tId check (tId ~ '^[0-9a-zA-Z]{6}$'));*/
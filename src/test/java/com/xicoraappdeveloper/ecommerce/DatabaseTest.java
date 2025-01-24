package com.xicoraappdeveloper.ecommerce;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/appecommerce";
        String user = "ecommerce";
        String password = "passwordecommerceapp";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


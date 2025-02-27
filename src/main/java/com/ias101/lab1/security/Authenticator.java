package com.ias101.lab1.security;

import com.ias101.lab1.database.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Authentication class for user validation
 */
public class Authenticator {
    /**
     * Authenticates a user by checking username and password against database
     *
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return boolean Returns true if authentication successful, false otherwise
     * @throws RuntimeException if there is a SQL error during authentication
     */
    public static boolean authenticateUser(String username, String password) {
        username = sanitize(username);
        password = sanitize(password);

        try(var conn = DBUtil.connect("jdbc:sqlite:src/main/resources/database/sample.db",
                "root","root")) {
            try(var statement = conn.createStatement()) {
                var query = """
                        SELECT * FROM user_data
                        WHERE username =\s""" + "'" + username + "'"
                        + "AND password = " + "'" + password + "'";
                System.out.println(query);
                ResultSet rs = statement.executeQuery(query);

                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String sanitize(String input) {
        if (input == null) {
            return "";
        }

        // Allow only alphanumeric characters and underscores, this function uses pattern matching
        if (!input.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Invalid input: Only alphanumeric characters and underscores are allowed.");
        }

        // Replaces old character to new character, this function uses regular expression
        return input.replace("'", "''").replaceAll("[^a-zA-Z0-9_]", "");
    }
}
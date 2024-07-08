package org.example.Controller;

import org.example.Model.Directory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.example.DB.DatabaseConnector.connectToDatabase;

public class DirectoryController
{
    static Connection connection;

    static
    {
        try
        {
            connection = connectToDatabase();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void createDirectory(Directory upload_dir) throws SQLException
    {

        String query = "INSERT INTO directories (id_directory, id_user, path_directory, name_directory, created_date) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps;
        try
        {
            ps = connection.prepareStatement(query);
            ps.setString(1, upload_dir.getId_directory());
            ps.setString(2, upload_dir.getId_user());
            ps.setString(3, upload_dir.getPath_directory());
            ps.setString(4, upload_dir.getName_directory());
            ps.setString(5, upload_dir.getCreated_date());
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        int new_row_file = ps.executeUpdate();
        if (new_row_file > 0)
        {
            System.out.println("'" + upload_dir.getName_directory() + "' created!");
        } else
        {
            System.out.println("failed create directory!");
        }
    }
}

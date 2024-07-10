package org.example.Controller;

import org.example.Model.Directory;
import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static Directory findDirectoryByPath(String path_directory) throws SQLException
    {
        Directory file;
        String login_query = "SELECT * FROM directories WHERE path_directory = ?";
        PreparedStatement ps;
        try
        {
            ps = connection.prepareStatement(login_query);
            ps.setString(1, path_directory);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            file = new Directory(
                    rs.getString("id_directory"),
                    rs.getString("id_user"),
                    rs.getString("path_directory"),
                    rs.getString("name_directory"),
                    rs.getString("created_date")
            );
            return file;
        }
        System.out.println("Can not find " + path_directory);
        return null;
    }

    public static User getUserUploadByDirectoryId(String id_dir)
    {
        User user_upload = null;
        String query = "SELECT u.* FROM users u, directories d WHERE u.id = d.id_user AND d.id_directory = ?";
        PreparedStatement ps;
        try
        {
            ps = connection.prepareStatement(query);
            ps.setString(1, id_dir);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                user_upload = new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("fullname"),
                        rs.getString("date_created"),
                        rs.getBoolean("anonymous"),
                        rs.getBoolean("activated"),
                        rs.getInt("id_role")
                );
            }
            return user_upload;
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}

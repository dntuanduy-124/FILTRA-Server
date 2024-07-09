package org.example.Controller;

import org.example.Model.File;
import org.example.Model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.DB.DatabaseConnector.connectToDatabase;

public class FileController
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
    public static void uploadFile(User user_upload, org.example.Model.File upload_file) throws IOException, SQLException
    {
        String query = "INSERT INTO files (id_file, id_user_upload, filename, filepath, filetype, upload_date, filesize) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps;
        try
        {
            ps = connection.prepareStatement(query);
            ps.setString(1, upload_file.getId_file());
            ps.setString(2, user_upload.getId());
            ps.setString(3, upload_file.getFilename());
            ps.setString(4, upload_file.getFilepath());
            ps.setString(5, upload_file.getFiletype());
            ps.setString(6, upload_file.getUpload_date());
            ps.setString(7, upload_file.getFilesize());
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        int new_row_file = ps.executeUpdate();
        if (new_row_file > 0)
        {
            System.out.println(user_upload.getUsername() + " UPLOAD '" + upload_file.getFilename() + "' SUCCESS!");
        } else
        {
            System.out.println("UPLOAD FAILED!");
        }
    }

    public static File findFileByPath(String path_file) throws SQLException
    {
        File file = null;
        String login_query = "SELECT * FROM files WHERE filepath = ?";
        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement(login_query);
            ps.setString(1, path_file);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            file = new File(
                    rs.getString("id_file"),
                    rs.getString("id_user_upload"),
                    rs.getString("filename"),
                    rs.getString("filepath"),
                    rs.getString("filetype"),
                    rs.getString("upload_date"),
                    rs.getString("filesize")
            );
            return file;
        }
        System.out.println("Can not find " + path_file);
        return null;
    }

}

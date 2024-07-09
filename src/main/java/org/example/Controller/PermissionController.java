package org.example.Controller;

import org.example.Model.File;
import org.example.Model.Permission;
import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static org.example.DB.DatabaseConnector.connectToDatabase;

public class PermissionController
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

    public static ArrayList<File> getAllFileReceived(String id_user) throws SQLException
    {
        ArrayList<File> file_receives = new ArrayList<>();
        String query = "SELECT f.* FROM files f, permissions p WHERE f.id_file = p.id_file AND p.id_user = ?";
        PreparedStatement ps;
        try
        {
            ps = connection.prepareStatement(query);
            ps.setString(1, id_user);
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            File permit_file = new File(
                    rs.getString("id_file"),
                    rs.getString("id_user_upload"),
                    rs.getString("filename"),
                    rs.getString("filepath"),
                    rs.getString("filetype"),
                    rs.getString("upload_date"),
                    rs.getString("filesize")
            );
            file_receives.add(permit_file);
        }
        return file_receives;
    }

    public static boolean sharingWithPermission(String email, String permission, String file_path) throws SQLException
    {
        boolean write_permit = false;
        boolean read_permit = false;
        if (permission.equalsIgnoreCase("READ"))
        {
            read_permit = true;
        } else if (permission.equalsIgnoreCase("WRITE"))
        {
            write_permit = true;
        } else
        {
            read_permit = write_permit = true;
        }
        User user_receive = AccountController.findUserByEmail(email);
        org.example.Model.File file_share = FileController.findFileByPath(
                file_path);
        if (user_receive != null && file_share != null)
        {
            Permission user_permission = new Permission(
                    UUID.randomUUID().toString(),
                    file_share.getId_file(),
                    null,
                    user_receive.getId(),
                    read_permit,
                    write_permit);

            String query = "INSERT INTO permissions (id_permission, id_file, id_directory, id_user, isRead, isWrite) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement ps;
            try
            {
                ps = connection.prepareStatement(query);
                ps.setString(1, user_permission.getId_permission());
                ps.setString(2, user_permission.getId_file());
                ps.setString(3, user_permission.getId_directory());
                ps.setString(4, user_permission.getId_user());
                ps.setBoolean(5, user_permission.isRead());
                ps.setBoolean(6, user_permission.isWrite());
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
            int new_permission = ps.executeUpdate();
            if (new_permission > 0)
            {
                System.out.println("Set permission success!");
                return true;
            }
            System.out.println("Failed to set permission!");
            return false;
        }
        return false;
    }
}
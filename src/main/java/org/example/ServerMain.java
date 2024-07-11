package org.example;

import org.example.Controller.AccountController;
import org.example.Controller.DirectoryController;
import org.example.Model.User;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class ServerMain
{
    static final int CONTROL_PORT = 2100;
    static ServerSocket ss;

    public static void main(String[] args)
    {
        Thread server_management = new Thread(() ->
        {
            try
            {
                serverManagementProgram();
            } catch (IOException | SQLException e)
            {
                throw new RuntimeException(e);
            }
        });
        server_management.start();

        try
        {
            ss = new ServerSocket(CONTROL_PORT);
            System.out.println("Server running now ...");
            while (true)
            {
                Socket clientSocket = ss.accept();
                Thread client = new ControlThread(clientSocket);
                client.start();
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void serverManagementProgram() throws IOException, SQLException
    {

        Scanner sc = new Scanner(System.in);
        while (true)
        {
            System.out.print("Filtra-Server> ");
            String cmd = sc.nextLine();
            if (cmd.equalsIgnoreCase("SERVEROFF"))
            {
                System.out.print("Are you sure? Y/N: ");
                cmd = sc.nextLine();
                if (cmd.equalsIgnoreCase("Y"))
                {
                    System.out.println("Server is turning off");
                    ss.close();
                }
                continue;
            }
            executeProgram(cmd);
        }
    }

    public static void executeProgram(String cmd) throws SQLException
    {
        String commander = cmd.substring(0, (cmd + " ").indexOf(" ")).trim().toUpperCase();
        switch (commander)
        {
            case "USERS":
                AccountController.showAllUser();
                break;
            case "BLOCK":
                blockUser(cmd);
                break;
            case "UNBLOCK":
                unBlockUser(cmd);
                break;
            case "LSBLK":
                listBlockedUsers();
                break;
            case "SETDATA":
                setUserDataSize(cmd);
                break;
            case "HELP":
                showHelp();
                break;
            default:
                System.out.println("Wrong command!");
                break;
        }
    }

    private static void listBlockedUsers()
    {
        for (String id : Objects.requireNonNull(AccountController.getBlockedUsers()))
        {
            System.out.println(id);
        }
    }

    private static void blockUser(String cmd) throws SQLException
    {
        if (cmd.length() == 5)
        {
            System.out.println("Usage: block <id-user>");
            return;
        }
        String id_user = cmd.substring(cmd.indexOf(" ") + 1).trim();
        if (id_user.isEmpty())
        {
            System.out.println("Usage: block <id-user>");
            return;
        }
        if (AccountController.isUserBlocked(id_user))
        {
            System.out.println(id_user + " had been blocked!");
        } else
        {
            AccountController.blockUserById(id_user);
        }
    }

    private static void unBlockUser(String cmd)
    {
        if (cmd.length() == 7)
        {
            System.out.println("Usage: unblock <id-user>");
            return;
        }
        String id_user = cmd.substring(cmd.indexOf(" ") + 1).trim();
        if (id_user.isEmpty())
        {
            System.out.println("Usage: unblock <id-user>");
            return;
        }
        if (AccountController.isUserBlocked(id_user))
        {
            AccountController.unblockUserById(id_user);
            System.out.println("Unblocked user " + id_user);
        } else
        {
            System.out.println("User was not blocked!");
        }
    }

    private static void showHelp()
    {
        System.out.println("users - show all user");
        System.out.println("block - block a user");
        System.out.println("setdata - set capacity size for a user");
        System.out.println("help - show this help");
    }

    private static void setUserDataSize(String cmd) throws SQLException
    {
        String str_parameters = cmd.substring((cmd + " ").indexOf(" ")).trim();
        String[] parameters = str_parameters.split(" ");
        if (parameters[0].isEmpty() || parameters[1].isEmpty())
        {
            System.out.println("Usage: SETDATA <id-user> <number-of-MegaByte>");
            System.out.println("EX: SETDATA a1234-x1y2z3-abcxyz789 500");
            return;
        }
        User user = AccountController.getUserById(parameters[0]);

        if (user == null)
        {
            System.out.println("Can not find " + parameters[0]);
            return;
        }
        File user_dir = new File(ControlThread.UPLOAD_DIRECTORY + File.separator + user.getUsername());
        long max_data_size = (long) (Long.parseLong(parameters[1]) * Math.pow(1024, 2));
        long user_current_data = DirectoryController.calculateDirectorySize(user_dir);
        if (max_data_size < user_current_data)
        {
            System.out.println("The current data size is " + user_current_data + "MB!");
            return;
        }
        if (DirectoryController.setUserDataById(parameters[0], max_data_size))
        {
            System.out.println("Set data for " + parameters[0] + " success!");
        } else
        {
            System.out.println("Failed to set data for " + parameters[0] + "!");
        }
    }

}

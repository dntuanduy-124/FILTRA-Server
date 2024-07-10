package org.example;

import org.example.Controller.AccountController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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
            default:
                System.out.println("Wrong command!");
                break;
        }
    }

    private static void blockUser(String cmd) throws SQLException
    {
        String id_user = cmd.substring((cmd + " ").indexOf(" ") + 1).trim();
        if (AccountController.isUserBlocked(id_user))
        {
            System.out.println(id_user + " had been blocked!");
        } else
        {
            AccountController.blockUserById(id_user);
        }
    }
}
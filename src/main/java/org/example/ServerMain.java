package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain
{
    public static void main(String[] args) throws IOException
    {
        int CONTROL_PORT = 2100;
        try (ServerSocket ss = new ServerSocket(CONTROL_PORT))
        {
            System.out.println("Server running now ...");
            while (true)
            {
                Socket clientSocket = ss.accept();
                Thread client = new ControlThread(clientSocket);
                System.out.println("Client thread " + client.getId() + " connected!");
                client.start();
            }
        }
    }
}
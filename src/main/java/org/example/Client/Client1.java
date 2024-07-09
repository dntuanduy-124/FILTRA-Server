package org.example.Client;

import com.google.gson.Gson;
import org.example.Model.User;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class Client1
{
    static BufferedReader in;
    static PrintWriter out;
    static Scanner sc;
    static Socket controlSocket;
    static User user_login;
    static String commander;
    static final int CONTROL_PORT = 2100;
    static final int DATA_PORT = 2000;
    static final String DOWNLOAD_DIRECTORY = "download";
    static final String SERVER_NAME = "localhost";
    static boolean isPaused = false;
    static final Object pauseLock = new Object();

    public static void main(String[] args)
    {
        try
        {
            controlSocket = new Socket("localhost", CONTROL_PORT);
            in = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
            out = new PrintWriter(controlSocket.getOutputStream(), true);
            sc = new Scanner(System.in);
            System.out.println("---- WELCOME TO FILTRA SERVER ----");
            startingProgram();
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void startingProgram() throws IOException
    {
        while (true)
        {
            System.out.print("> ");
            String raw_cmd = sc.nextLine().trim();
            sendCommandToServer(raw_cmd);
            System.out.println("-----------------------------------------");
            if (commander.equals("QUIT"))
            {
                return;
            }
        }

    }

    private static void sendCommandToServer(String raw_command) throws IOException
    {
        commander = raw_command.substring(0, (raw_command + " ").indexOf(" ")).toUpperCase();
        switch (commander)
        {
            case "PD":
                pauseDownload();
                break;
            case "RD":
                resumeDownload();
                break;
            case "PU":
                pauseUpload();
                break;
            case "RU":
                resumeUpload();
                break;
            case "LOG":
                login();
                break;
            case "REG":
                register();
                break;
            case "OUT":
                logout();
                break;
            case "OTP":
                activeEmail();
                break;
            case "INFO":
                showProfile();
                break;
            case "HELP":
                showHelp();
                break;
            case "QUIT":
                quitServer();
                break;
            case "CD":
                moveToDirectory(raw_command);
                break;
            case "LS":
                listFileAndDirectory(raw_command);
                break;
            case "MKDIR":
                makeDirectory(raw_command);
                break;
            case "UP":
                new Thread(() ->
                {
                    try
                    {
                        uploadFile(raw_command);
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }).start();
                break;
            case "GET":
                System.out.println("Type 'pd' to pause download");
                System.out.println("Type 'rd' to resume download");
                new Thread(() ->
                {
                    try
                    {
                        downloadFile(raw_command);
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }).start();
                break;
            case "RM":
                removeFileOrDirectory(raw_command);
                break;
            default:
                System.out.println("Type 'help'");
                break;
        }
    }

    private static void showHelp()
    {
        //viet trang help cho client
        System.out.println("reg - register new account");
        System.out.println("log - login to your account");
        System.out.println("otp - verify your email");
        System.out.println("ls - show file on server (or use 'ls <folder-name>')");
        System.out.println("get - download file from server (usage: get <file-name>)");
        System.out.println("up - upload file to server (usage: up <file-name>)");
        System.out.println("out - logout");
        System.out.println("quit - quit from the server");
        System.out.println("help - see this help");
    }

    private static void pauseDownload()
    {
        System.out.println("Paused download ... (Type 'rd' to resume download)");
        out.println(commander);
    }

    private static void resumeDownload()
    {
        System.out.println("Resume download");
        out.println(commander);
    }

    private static void pauseUpload()
    {
        System.out.println("Paused upload ... (type 'ru' to resume upload)");
        synchronized (pauseLock)
        {
            isPaused = true;
        }
    }

    private static void resumeUpload()
    {
        System.out.println("Resume upload!");
        synchronized (pauseLock)
        {
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    private static void moveToDirectory(String raw_cmd) throws IOException
    {
        out.println(raw_cmd);
        String cd_status = in.readLine();
        System.out.println(cd_status);
    }

    private static void showProfile()
    {
        System.out.println(user_login.toString());
    }

    private static void activeEmail() throws IOException
    {
        out.println(commander);
        String activate_status = in.readLine();
        if (activate_status.contains("nope"))
        {
            System.out.println("Please check your Email to get the OTP");
            System.out.print("Type your OTP: ");
            String otp = sc.nextLine();
            out.println(otp);
            activate_status = in.readLine();
            System.out.println(activate_status);
            return;
        }
        System.out.println(activate_status);
    }

    private static void removeFileOrDirectory(String raw_cmd) throws IOException
    {
        if (raw_cmd.length() == 2)
        {
            System.out.println("Usage: rm <file-or-directory-name>");
            return;
        }
        out.println(raw_cmd);
        String remove_status = in.readLine();
        System.out.println(remove_status);
    }

    private static void listFileAndDirectory(String raw_cmd) throws IOException
    {
        out.println(raw_cmd);
        String response_LS;
        while ((response_LS = in.readLine()) != null)
        {
            if (response_LS.contains("Login"))
            {
                System.out.println(response_LS);
                break;
            }
            if (response_LS.equals("END"))
            {
                break;
            }
            System.out.println(response_LS);

        }
        out.flush();
    }

    private static void quitServer() throws IOException
    {
        System.out.println("BYE");
        controlSocket.close();
    }

    private static void downloadFile(String raw_cmd) throws IOException
    {
        if (raw_cmd.length() == 3)
        {
            System.out.println("Usage: get <file-name>");
            return;
        }
        File download_directory = new File(DOWNLOAD_DIRECTORY);
        if (!download_directory.exists())
        {
            download_directory.mkdirs();
        }
        out.println(raw_cmd);
        String filename_download = raw_cmd.substring(raw_cmd.indexOf(" ") + 1);
        String new_file_name = getUniqueFileName(filename_download);
        String download_status = in.readLine();
        System.out.println(download_status);
        if (download_status.contains("Login") || download_status.contains("not found"))
        {
            return;
        }
        try (Socket dataSocket = new Socket(SERVER_NAME, DATA_PORT); BufferedInputStream in = new BufferedInputStream(dataSocket.getInputStream()); BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new_file_name)))
        {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            dataSocket.close();
            System.out.println("Downloaded successful!\nLocation: '" + new_file_name + "'");
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static String getUniqueFileName(String filename)
    {
        File file = new File(DOWNLOAD_DIRECTORY + File.separator + filename);
        if (!file.exists())
        {
            return file.getAbsolutePath();
        }

        String name = filename;
        String extension = "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1)
        {
            name = filename.substring(0, dotIndex);
            extension = filename.substring(dotIndex);
        }

        int count = 1;
        while (file.exists())
        {
            String new_file_name = name + "(" + count + ")" + extension;
            file = new File(DOWNLOAD_DIRECTORY + File.separator + new_file_name);
            count++;
        }
        return file.getAbsolutePath();
    }

    private static void uploadFile(String raw_cmd) throws IOException
    {

        if (raw_cmd.length() == 2)
        {
            System.out.println("Usage: up <file-name>");
            return;
        }
        String filename = raw_cmd.substring(raw_cmd.indexOf(" ") + 1);
        File uploadFile = new File(DOWNLOAD_DIRECTORY + File.separator + filename);
        if (!uploadFile.exists())
        {
            System.out.println(uploadFile.getName() + " not found!");
            return;
        }
        System.out.println("Type 'pu' to pause upload");
        System.out.println("Type 'ru' to resume upload");
        out.println(raw_cmd);
        String upload_status = in.readLine();

        System.out.print("\r" + upload_status);
        if (upload_status.contains("Login"))
        {
            return;
        }

        File download_dir = new File(DOWNLOAD_DIRECTORY);
        if (!download_dir.exists())
        {
            download_dir.mkdirs();
        }

        try (Socket dataSocket = new Socket(SERVER_NAME, DATA_PORT); BufferedInputStream in = new BufferedInputStream(new FileInputStream(uploadFile)); BufferedOutputStream out = new BufferedOutputStream(dataSocket.getOutputStream()))
        {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1)
            {
                synchronized (pauseLock)
                {
                    while (isPaused)
                    {
                        pauseLock.wait();
                    }
                    out.write(buffer, 0, bytesRead);
                }
            }
            out.flush();
            dataSocket.close();
            System.out.print("\rUploaded successful!\n> ");
        } catch (IOException | InterruptedException e)
        {
            System.out.println(e.getMessage());
        }

    }

    private static void makeDirectory(String raw_cmd) throws IOException
    {
        out.println(raw_cmd);
        String mkdir_status = in.readLine();
        System.out.println(mkdir_status);
    }

    private static void logout() throws IOException
    {
        out.println(commander);
        String logout_status = in.readLine();
        System.out.println(logout_status);
    }

    private static void login() throws IOException
    {
        out.println(commander);
        String login_status = in.readLine();
        if (login_status.contains("logout"))
        {
            System.out.println(login_status);
            return;
        }
        System.out.print("Username: ");
        String message = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        out.println(message);
        out.println(pass);
        login_status = in.readLine();
        if (login_status.contains("failed"))
        {
            System.out.println(login_status);
            return;
        }
        Gson gson = new Gson();
        user_login = gson.fromJson(login_status, User.class);
        System.out.println("Login success!");
    }

    private static void register() throws IOException
    {
        out.println(commander);
        String fullname, username, email, passwd, rePasswd;
        String register_status = in.readLine();
        if (register_status.contains("logout"))
        {
            System.out.println(register_status);
            return;
        }
        do
        {
            System.out.print("Fullname: ");
            fullname = sc.nextLine();
            System.out.print("Username: ");
            username = sc.nextLine();
            System.out.print("Email: ");
            email = sc.nextLine();
            System.out.print("Password: ");
            passwd = sc.nextLine();
            System.out.print("Retype password: ");
            rePasswd = sc.nextLine();
        } while (!rePasswd.equals(passwd));
        Gson gson = new Gson();
        User register_user = new User(UUID.randomUUID().toString(), fullname, username, email, passwd, LocalDateTime.now().toString(), true, false, 2);
        out.println(gson.toJson(register_user));
        register_status = in.readLine();
        System.out.println(register_status);
    }
}



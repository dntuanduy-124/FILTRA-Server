package org.example;

import com.google.gson.Gson;
import org.example.Controller.AccountController;
import org.example.Controller.DirectoryController;
import org.example.Controller.FileController;
import org.example.Controller.PermissionController;
import org.example.Model.Directory;
import org.example.Model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ControlThread extends Thread
{
    BufferedReader in;
    PrintWriter out;
    Socket clientSocket;
    String raw_cmd;
    int indexCommander;
    User user_login;
    static String UPLOAD_DIRECTORY = "upload";
    public String WORKING_DIRECTORY = UPLOAD_DIRECTORY;
    final int DATA_PORT = 2000;
    public static boolean isPaused = false;
    public static final Object pauseLock = new Object();

    public ControlThread(Socket clientSocket) throws IOException
    {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true); // Changed this line
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                raw_cmd = in.readLine();
                if (raw_cmd == null)
                {
                    clientSocket.close();
                    System.out.println("Client disconnected");
                    break;
                }
                indexCommander = (raw_cmd + " ").indexOf(" ");
                String commander = raw_cmd.substring(0, indexCommander).toUpperCase();
                startingProgramByCommander(commander);
            }
        } catch (IOException | SQLException e)
        {
            System.out.println(e.getMessage());
        } finally
        {
            try
            {
                clientSocket.close();
            } catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    private void startingProgramByCommander(String commander) throws SQLException, IOException
    {
        switch (commander)
        {
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
                activateEmail();
                break;
            case "LS":
                listFileAndDirectory();
                break;
            case "MKDIR":
                createDirectory();
                break;
            case "UP":
                uploadFile();
                break;
            case "GET":
                downloadFile();
                break;
            case "SHR":
                shareFile();
                break;
            case "LSHR":
                listFileAndDirectoryReceived();
                break;
            case "RM":
                removeFileOrDirectory();
                break;
            case "CD":
                moveToDirectory();
                break;
            case "PD":
                pauseDownload();
                break;
            case "RD":
                resumeDownload();
                break;
            default:
                System.out.println("Wrong command!");
                break;
        }
    }

    private void listFileAndDirectoryReceived() throws SQLException
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        out.println("-- Received --");
        Gson gson = new Gson();
        ArrayList<org.example.Model.File> files_received = PermissionController.getAllFileReceived(user_login.getId());
        ArrayList<Directory> directories_received = PermissionController.getAllDirectoryReceived(user_login.getId());
        String[] list_file_share = new String[files_received.size()];
        String[] list_dir_share = new String[directories_received.size()];
        for (int i = 0; i < list_file_share.length; i++)
        {
            User user_shared = FileController.getUserUploadByFileId(files_received.get(0).getId_file());
            list_file_share[i] = "F:\t" + files_received.get(i).getFilename() + "\tFROM: " + user_shared.getEmail();
        }
        for (int i = 0; i < list_dir_share.length; i++)
        {
            User user_shared = DirectoryController.getUserUploadByDirectoryId(directories_received.get(0).getId_directory());
            list_dir_share[i] = "D:\t" + directories_received.get(i).getName_directory() + "\tFROM: " + user_shared.getEmail();
        }
        out.println(gson.toJson(list_file_share));
        out.println(gson.toJson(list_dir_share));
    }

    private void shareFile() throws SQLException
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        if (!(raw_cmd.contains("-e") && raw_cmd.contains("-p") && (raw_cmd.contains("-f") || raw_cmd.contains("-d"))))
        {
            out.println("Usage: shr -e <email-receive> -p <WRITE|READ|FULL> -f|-d <file|directory-name>");
            return;
        }
        String raw_parameters = raw_cmd.substring(indexCommander + 1);
        String[] share_parts = raw_parameters.split("-");
        String email_user = share_parts[1].substring(share_parts[1].indexOf("e") + 2).trim();
        String permission = share_parts[2].substring(share_parts[2].indexOf("p") + 2).trim();
        String file_sharing = "";
        if (share_parts[3].charAt(0) == 'f')
        {
            file_sharing = share_parts[3].substring(share_parts[3].indexOf("f") + 2).trim();
        } else if (share_parts[3].charAt(0) == 'd')
        {
            file_sharing = share_parts[3].substring(share_parts[3].indexOf("d") + 2).trim();
        }
        File file_shr = new File(WORKING_DIRECTORY + File.separator + file_sharing);
        if (email_user.isEmpty() || permission.isEmpty() || file_sharing.isEmpty())
        {
            out.println("Usage: shr -e <email-receive> -p <WRITE|READ|FULL> -f|-d <file|directory-name>");
            return;
        }
        if (file_shr.isFile())
        {
            if (PermissionController.sharingWithPermission(email_user, permission, file_shr.getAbsolutePath(), "f"))
            {
                out.println("Sharing '" + file_sharing + "'" + " to " + email_user);
            } else
            {
                out.println("Sharing failed!");
            }
        } else if (file_shr.isDirectory())
        {
            if (PermissionController.sharingWithPermission(email_user, permission, file_shr.getAbsolutePath(), "d"))
            {
                out.println("Sharing '" + file_sharing + "'" + " to " + email_user);
            } else
            {
                out.println("Sharing failed!");
            }
        }

    }

    private void pauseDownload()
    {
        synchronized (pauseLock)
        {
            isPaused = true;
        }
    }

    private void resumeDownload()
    {
        synchronized (pauseLock)
        {
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    private void moveToDirectory() throws IOException
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        if (raw_cmd.length() == 2)
        {
            WORKING_DIRECTORY = UPLOAD_DIRECTORY + File.separator + user_login.getUsername();
            out.println("You're in home");
            return;
        }
        String destination = raw_cmd.substring(indexCommander + 1);
        File newDir = new File(WORKING_DIRECTORY, destination).getCanonicalFile();
        File userRootDir = new File(UPLOAD_DIRECTORY + File.separator + user_login.getUsername()).getCanonicalFile();
        if (!newDir.getPath().startsWith(userRootDir.getPath()))
        {
            WORKING_DIRECTORY = UPLOAD_DIRECTORY + File.separator + user_login.getUsername();
            out.println("You're in home");
            return;
        }
        if (newDir.exists() && newDir.isDirectory())
        {
            WORKING_DIRECTORY = newDir.getPath();
            out.println("You're in " + destination);
        } else
        {
            out.println("Not a valid directory!");
        }
    }

    private void activateEmail() throws SQLException, IOException
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        } else if (AccountController.isEmailActivated(user_login.getEmail()))
        {
            out.println("Email already activated!");
            return;
        }
        out.println("nope");
        Random numberOTP = new Random();
        int otp_random = numberOTP.nextInt(99999);
        String sendOTP = String.format("%05d", otp_random);
        AccountController.sentEmail(user_login.getEmail(), sendOTP);
        String otp_from_client = in.readLine();
        if (AccountController.isActivateAccount(otp_from_client, sendOTP, user_login.getEmail()))
        {
            out.println("Verified OTP successfully!");
            return;
        }
        out.println("Your OTP is not valid!");
    }

    private void removeFileOrDirectory()
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        String remove_path = raw_cmd.substring(indexCommander + 1);
        File remove_file = new File(WORKING_DIRECTORY + File.separator + remove_path);
        if (!remove_file.exists())
        {
            out.println("'" + remove_file.getName() + "'" + " not found!");
            return;
        }
        if (remove_file.delete())
        {
            out.println("Removed " + "'" + remove_file.getName() + "'");
        } else
        {
            out.println("Failed to remove " + "'" + remove_file.getName() + "'");
        }
    }

    private void listFileAndDirectory()
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        if (raw_cmd.length() == 2)
        {
            File currentDir = new File(WORKING_DIRECTORY);
            walk(currentDir, currentDir.getAbsolutePath().length());
        } else
        {
            String directory_path = raw_cmd.substring(indexCommander + 1);
            if (directory_path.isEmpty())
            {
                out.println("The specified path is not valid!");
                return;
            }

            directory_path = WORKING_DIRECTORY + File.separator + directory_path;
            File folder = new File(directory_path);
            if (folder.exists() && folder.isDirectory())
            {
                walk(folder, folder.getAbsolutePath().length());
            } else
            {
                out.println("The specified path is not valid!");
            }
        }
        out.println("END");
    }

    public void walk(File folder, int baseLength)
    {
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null)
        {
            for (File file : listOfFiles)
            {
                String relativePath = file.getAbsolutePath().substring(baseLength + 1);
                if (file.isDirectory())
                {
                    out.println("D:\t" + relativePath);
                } else
                {
                    out.println("F:\t" + relativePath);
                }
            }
        } else
        {
            out.println("The folder is empty or cannot be read.");
        }
    }

    private void downloadFile() throws IOException
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        String file_download_name = raw_cmd.substring(indexCommander + 1);
        File file_download = new File(WORKING_DIRECTORY + File.separator + file_download_name);
        if (!file_download.exists())
        {
            out.println("'" + file_download.getName() + "'" + " not found!");
            return;
        }
        ServerSocket serverDataSocket = new ServerSocket(DATA_PORT);
        out.println("Downloading ... > ");
        Socket clientDataSocket = serverDataSocket.accept();
        Thread dataThread = new DataThread(clientDataSocket, file_download, "GET", user_login);
        dataThread.start();
        serverDataSocket.close();
    }

    private void uploadFile() throws IOException
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        File upload_dir = new File(WORKING_DIRECTORY);
        if (!upload_dir.exists())
        {
            upload_dir.mkdirs();
        }
        String file_upload_name = raw_cmd.substring(indexCommander + 1);
        String new_file_name = getUniqueFileName(file_upload_name);
        File file_upload = new File(new_file_name);
        ServerSocket serverDataSocket = new ServerSocket(DATA_PORT);
        out.println("Uploading ...");
        Socket clientDataSocket = serverDataSocket.accept();
        Thread dataThread = new DataThread(clientDataSocket, file_upload, "UP", user_login);
        dataThread.start();
        serverDataSocket.close();
    }

    private void createDirectory() throws SQLException
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }

        int indexCommander = raw_cmd.indexOf(' ');
        if (indexCommander == -1)
        {
            out.println("usage: 'mkdir <directory-name>'");
            return;
        }

        String directoryName = raw_cmd.substring(indexCommander + 1).trim();
        if (directoryName.isEmpty())
        {
            out.println("usage: 'mkdir <directory-name>'");
            return;
        }
        String uniqueDirectoryName = getUniqueFileName(directoryName);
        File new_dir = new File(uniqueDirectoryName);
        if (!new_dir.exists())
        {
            if (new_dir.mkdirs())
            {
                Directory dir = new Directory(UUID.randomUUID().toString(), user_login.getId(), new_dir.getAbsolutePath(), new_dir.getName(), LocalDateTime.now().toString());
                DirectoryController.createDirectory(dir);
                out.println("Create directory success!");
            } else
            {
                out.println("Failed to create directory!");
            }
        }
    }

    private void logout()
    {
        if (user_login == null)
        {
            out.println("Login first!");
            return;
        }
        out.println("See you again " + user_login.getUsername() + "!");
        user_login = null;
        WORKING_DIRECTORY = UPLOAD_DIRECTORY;
    }

    private void register() throws IOException, SQLException
    {
        if (user_login != null)
        {
            out.println("You need to logout first!");
            return;
        }
        out.println("-- REGISTER --");
        Gson gson = new Gson();
        User registerUser = gson.fromJson(in.readLine(), User.class);
        if (AccountController.isUserExist(registerUser.getUsername(), registerUser.getEmail()))
        {
            out.println("Username or Email existed!");
        } else
        {
            if (AccountController.createUser(registerUser))
            {
                out.println("Register success!");
                createPersonalDirectory(registerUser);
            } else
            {
                out.println("Register failed!");
            }
        }
    }

    private void createPersonalDirectory(User new_user) throws SQLException
    {
        File personalDir = new File(UPLOAD_DIRECTORY + File.separator + new_user.getUsername());
        if (personalDir.mkdirs())
        {
            Directory userDir = new Directory(UUID.randomUUID().toString(), new_user.getId(), personalDir.getAbsolutePath(), personalDir.getName(), LocalDateTime.now().toString());
            DirectoryController.createDirectory(userDir);
        }
    }

    private String getUniqueFileName(String filename)
    {
        File file = new File(WORKING_DIRECTORY + File.separator + filename);

        if (!file.exists() && !file.isDirectory())
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
        while (file.exists() || file.isDirectory())
        {
            String newFileName = name + "(" + count + ")" + extension;
            file = new File(WORKING_DIRECTORY + File.separator + newFileName);
            count++;
        }
        return file.getAbsolutePath();
    }

    private void login() throws IOException, SQLException
    {
        if (user_login != null)
        {
            out.println("You need to logout first!");
            return;
        }
        out.println("-- LOGIN --");
        String username = in.readLine();
        String passwd = in.readLine();
        user_login = AccountController.loginUser(username, passwd);
        if (user_login != null)
        {
            Gson gson = new Gson();
            out.println(gson.toJson(user_login));
            WORKING_DIRECTORY = UPLOAD_DIRECTORY + File.separator + user_login.getUsername();
        } else
        {
            out.println("Login failed!");
        }
    }
}

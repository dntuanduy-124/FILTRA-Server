package org.example;

import com.google.gson.Gson;
import org.example.Controller.AccountController;
import org.example.Controller.DirectoryController;
import org.example.Model.Directory;
import org.example.Model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
    String UPLOAD_DIRECTORY = "upload";
    final int DATA_PORT = 2000;

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
            case "RM":
                removeFileOrDirectory();
                break;
            default:
                System.out.println("Wrong command!");
                break;
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
        File remove_file = new File(
                UPLOAD_DIRECTORY +
                        File.separator +
                        user_login.getUsername() +
                        File.separator +
                        remove_path
        );
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
            File currentDir = new File(UPLOAD_DIRECTORY +
                    File.separator +
                    user_login.getUsername());
            walk(currentDir, currentDir.getAbsolutePath().length());
        } else
        {
            String directory_path = raw_cmd.substring(indexCommander + 1);
            if (directory_path.isEmpty())
            {
                out.println("The specified path is not valid!");
                return;
            }

            directory_path = UPLOAD_DIRECTORY +
                    File.separator +
                    user_login.getUsername() +
                    File.separator + directory_path;
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
        File file_download = new File(
                UPLOAD_DIRECTORY +
                        File.separator +
                        user_login.getUsername() +
                        File.separator +
                        file_download_name
        );
        if (!file_download.exists())
        {
            out.println("'" + file_download.getName() + "'" + " not found!");
            return;
        }
        ServerSocket serverDataSocket = new ServerSocket(DATA_PORT);
        out.println("READY");
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
        File upload_dir = new File(UPLOAD_DIRECTORY +
                File.separator +
                user_login.getUsername());
        if (!upload_dir.exists())
        {
            upload_dir.mkdirs();
        }
        String file_upload_name = raw_cmd.substring(indexCommander + 1);
        String new_file_name = getUniqueFileName(file_upload_name);
        File file_upload = new File(new_file_name);
        ServerSocket serverDataSocket = new ServerSocket(DATA_PORT);
        out.println("READY");
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
        File file = new File(UPLOAD_DIRECTORY + File.separator + user_login.getUsername() + File.separator + filename);
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
            file = new File(UPLOAD_DIRECTORY + File.separator + user_login.getUsername() + File.separator + newFileName);
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
        } else
        {
            out.println("Login failed!");
        }
    }
}

import java.net.*;
import java.io.*;

public class Server {
    int cntTasks;
    int portNumber;
    ServerGUI out;
    InetAddress inetAddress;
    ServerSocket serverSocket;

    class serverTask implements Runnable {
        int idTask;
        Socket clientSocket;
        File Task;
        File Result;

        serverTask(int idTask, File fileIn, File fileOut) {
            this.Task = fileIn;
            this.Result = fileOut;
            this.idTask = idTask;
        }

        public void run() {
            synchronized (serverSocket) {
                try {
                    this.clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    out.log.append(e.toString() + "\n");
                }
            }

            try (
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                FileInputStream fileInputStream = new FileInputStream(Task);
                FileOutputStream fileOutputStream = new FileOutputStream(Result);
            ) {
                //Send Task to Client
                outputStream.writeInt(idTask);
                outputStream.writeInt((int) Task.length());
                byte[] bufTask = new byte[(int) Task.length()];
                fileInputStream.read(bufTask);
                outputStream.write(bufTask);
                out.log.append("Task with id " + idTask + " send to client\n");

                //Read Result from Client
                int sizeBufRes = inputStream.readInt();
                byte[] bufRes = new byte[sizeBufRes];
                inputStream.read(bufRes);
                fileOutputStream.write(bufRes);
                out.log.append("Solution the task with id " + idTask + " is received\n");

                if (Result.length() == 0) throw new IOException();
            } catch (IOException e) {
                out.log.append("Task with id " + idTask + " not done correctly. Re add it!\n");
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    out.log.append(e.toString() + "__\n");
                }
            }
        }
    }

    Server(int portNumber, ServerGUI out) {
        cntTasks = 0;
        this.portNumber = portNumber;
        this.out = out;
        try {
            serverSocket = new ServerSocket(portNumber);
            inetAddress = serverSocket.getInetAddress();
            out.log.append("Server is started\n");
        } catch (IOException e) {
            out.log.append(e.toString() + "\n");
        }
    }

    void addTask(File task, File result) {
        new Thread(new serverTask(++cntTasks, task, result)).start();
        out.log.append("Task with id " + cntTasks + " added \n");
    }

    void closeServerSocket() {
        try {
            serverSocket.close();
            out.log.append("Server is stopped\n");
        } catch (IOException e) {
            out.log.append(e.toString() + "\n");
        }
    }
}

import sun.print.resources.serviceui;

import java.net.*;
import java.io.*;

public class Client {
    ClientGUI out;
    String command;
    String host;
    int port;

    Client(String host, int port, String command, ClientGUI out) {
        this.host = host;
        this.port = port;
        this.command = command;
        this.out = out;
        solve();
    }

    private void solve() {
        try (
            Socket socket = new Socket(host, port);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {
            socket.setSoTimeout(1000);
            out.log.append("Connected to server\n");

            //Read taskID from Server and Create Temp Files
            int idTask = inputStream.readInt();
            File task = File.createTempFile("Task", ".lp");
            File result = File.createTempFile("Solution", ".out");

            //Read task from Server
            try (FileOutputStream fileOutputStream = new FileOutputStream(task)) {
                int sizeBufIn = inputStream.readInt();
                byte[] bufIn = new byte[sizeBufIn];
                inputStream.read(bufIn);
                fileOutputStream.write(bufIn);
                out.log.append("Task with id " + idTask + " received\n");
            }

            //Solve Task
            ProcessBuilder proc = new ProcessBuilder(command, task.toString());
            proc.redirectOutput(result);
            proc.start().waitFor();

            out.log.append("Task solved\n");

            //Send Result to Server
            try (FileInputStream fileInputStream = new FileInputStream(result)) {
                outputStream.writeInt((int) result.length());
                byte[] bufOut = new byte[(int) result.length()];
                fileInputStream.read(bufOut);
                outputStream.write(bufOut);
                out.log.append("Solution send to server\n");
            }

            //Delete Temp Files
            task.delete();
            result.delete();
        } catch (SocketTimeoutException e) {
            out.log.append("No Tasks...\n");
        } catch (IOException e) {
            out.log.append(e.toString() + "\n");
        } catch (InterruptedException e) {
            out.log.append(e.toString() + "\n");
        } finally {
            out.log.append("Disconnected.\n");
        }
    }
}

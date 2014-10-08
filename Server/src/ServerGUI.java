import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ServerGUI extends JPanel
                       implements ActionListener {
    Server server;
    //test git
    JButton startServer;
    JButton stopServer;
    JButton chooseTask;
    JButton chooseResult;
    JButton addTask;
    JTextArea log;
    JFileChooser fc;

    ServerGUI() {
        super(new BorderLayout());

        //Create the log.
        log = new JTextArea(10,30);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser.
        fc = new JFileChooser();

        //Create a buttons.
        startServer = new JButton("Start Server");
        startServer.addActionListener(this);
        stopServer = new JButton("Stop Server");
        stopServer.addActionListener(this);
        chooseTask = new JButton("Choose Task...");
        chooseTask.addActionListener(this);
        chooseResult = new JButton("Choose Result...");
        chooseResult.addActionListener(this);
        addTask = new JButton("Add Task");
        addTask.addActionListener(this);

        //Put the buttons in a separate panel.
        JPanel buttonPanelServer = new JPanel();
        buttonPanelServer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanelServer.add(startServer);
        buttonPanelServer.add(stopServer);
        JPanel buttonPanelTask = new JPanel();
        buttonPanelTask.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        buttonPanelTask.setLayout(new BoxLayout(buttonPanelTask, BoxLayout.Y_AXIS));
        buttonPanelTask.add(chooseTask);
        buttonPanelTask.add(chooseResult);
        buttonPanelTask.add(addTask);

        //Add the buttons and the log to this panel.
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buttonPanelServer, BorderLayout.PAGE_START);
        add(buttonPanelTask, BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.PAGE_END);
    }

    File task = null;
    File result = null;

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == startServer) {
            if (server == null)
                server = new Server(12313, this);
            else
                log.append("Server already started\n");
        } else if (e.getSource() == stopServer) {
            if (server == null)
                log.append("Server already stopped\n");
            else {
                server.closeServerSocket();
                server = null;
            }
        } else if (e.getSource() == chooseTask) {
            fc.showOpenDialog(this);
            task = fc.getSelectedFile();
            if (task != null) {
                log.append("Choosing task: " + task.getName() + ".\n");
            } else {
                log.append("Task not choose\n");
            }
        } else if (e.getSource() == chooseResult) {
            fc.showOpenDialog(this);
            result = fc.getSelectedFile();
            if (result != null) {
                log.append("Choosing file to results: " + result.getName() + ".\n");
            } else {
                log.append("File to results not choose\n");
            }
        } else if (e.getSource() == addTask) {
            if (task != null && result != null && server != null) {
                server.addTask(task, result);
                task = null;
                result = null;
            } else if (task == null) {
                log.append("Task not choose\n");
            } else if (result == null) {
                log.append("File to results not choose\n");
            } else if (server == null) {
                log.append("Server is not started\n");
            }
        }
    }

    private static void createAndShowGUI() {
        JFrame jFrame = new JFrame("Server");
        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(300, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.add(new ServerGUI());

        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
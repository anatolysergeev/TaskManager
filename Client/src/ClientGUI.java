import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Inet4Address;
import java.text.NumberFormat;

public class ClientGUI extends JPanel
        implements ActionListener {
    //Values for the fields
    private String host = "127.0.0.1";
    private int port = 12313;

    //Labels to identify the fields
    private JLabel hostLabel;
    private JLabel portLabel;

    //Strings for the labels
    private static String hostString = "Host address: ";
    private static String portString = "Port: ";

    //Fields for data entry
    private JFormattedTextField hostField;
    private JFormattedTextField portField;

    JButton chooseProgramme;
    JButton makeTask;
    JTextArea log;
    JFileChooser fc;

    ClientGUI() {
        super(new BorderLayout());

        //Create the log.
        log = new JTextArea(10,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser.
        fc = new JFileChooser();

        //Create the labels
        hostLabel = new JLabel(hostString);
        portLabel = new JLabel(portString);

        //Create the text fields
        hostField = new JFormattedTextField();
        hostField.setValue(new String(host));
        hostField.setColumns(10);

        portField = new JFormattedTextField();
        portField.setValue(new Integer(port));
        portField.setColumns(10);

        //Tell accessibility tools about label/textfield pairs.
        hostLabel.setLabelFor(hostField);
        portLabel.setLabelFor(portField);

        //Create a buttons.
        chooseProgramme = new JButton("Choose Programme");
        chooseProgramme.addActionListener(this);
        makeTask = new JButton("Make Task");
        makeTask.addActionListener(this);

        //Create Panels
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(hostLabel);

        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(hostField);

        JPanel lfPane = new JPanel();
        lfPane.add(labelPane);
        lfPane.add(fieldPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        buttonPanel.add(chooseProgramme);
        buttonPanel.add(makeTask);

        //Add the buttons and the log to this panel.
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lfPane, BorderLayout.PAGE_START);
        add(buttonPanel, BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.PAGE_END);
    }

    File programme;

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseProgramme) {
            fc.showOpenDialog(this);
            programme = fc.getSelectedFile();
            if (programme != null) {
                log.append("Choosing programme: " + programme.getName() + ".\n");
            } else {
                log.append("Programme not choose\n");
            }
        } else if (e.getSource() == makeTask) {
            if (programme != null)
                new Client(hostField.getText(), port, programme.toString(), this);
            else
                log.append("Programme not choose\n");
        }
    }

    private static void createAndShowGUI() {
        JFrame jFrame = new JFrame("Client");
        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(300, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.add(new ClientGUI());

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
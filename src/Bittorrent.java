
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Bittorrent extends JPanel implements ActionListener {

    private final JPanel _infoPanel;
    private static final int _WIDTH = 800;
    private static final int _HEIGHT = 600;
    private static final String[] buttons = {"Start", "Stop", "Pause", "Delete"};

    public Bittorrent() {
        super(new BorderLayout());

        JToolBar toolBar = new JToolBar("Toolbar", JToolBar.VERTICAL);
        _addButtons(toolBar, buttons);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Error loading the appearance of window");
        }

        JButton stopButton = new JButton("Cancel");
        stopButton.setActionCommand("CANCEL");
        stopButton.addActionListener(this);

        JButton startButton = new JButton("Start");
        startButton.setActionCommand("START");
        startButton.addActionListener(this);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setActionCommand("PAUSE");
        pauseButton.addActionListener(this);
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("DELETE");
        deleteButton.addActionListener(this);

        JMenuBar menubar = new JMenuBar();
        JMenu menuBittorrentOption = new JMenu("Help");
        JMenu menuFileOption = new JMenu("File");
        menubar.add(menuFileOption);
        menubar.add(menuBittorrentOption);

        JMenuItem about = new JMenuItem("About");
        about.setActionCommand("ABOUT");
        about.addActionListener(this);

        JMenuItem openFile = new JMenuItem("Open");
        openFile.setActionCommand("OPEN");
        openFile.addActionListener(this);

        JMenuItem exitApp = new JMenuItem("Exit");
        exitApp.setActionCommand("EXIT");
        exitApp.addActionListener(this);

        menuBittorrentOption.add(about);
        menuFileOption.add(openFile);
        menuFileOption.add(exitApp);

        _infoPanel = new JPanel();
        JPanel ip = new JPanel(new GridLayout(0, 3));
        ip.add(new JLabel("Name"));
        ip.add(new JLabel("Size"));
        ip.add(new JLabel("% downloaded"));
        _infoPanel.add(ip);
        _infoPanel.setPreferredSize(new Dimension(_WIDTH, 50));

        setPreferredSize(new Dimension(_WIDTH, _HEIGHT));
        add(menubar, BorderLayout.NORTH);
        add(_infoPanel, BorderLayout.SOUTH);
        add(toolBar, BorderLayout.EAST);
    }

    /**
     * Function adding all buttons from array
     * @param toolBar
     * @param buttons 
     */
    private void _addButtons(JToolBar toolBar, String[] buttons) {
        for (String element : buttons) {
            JButton button = _makeButton(element);
            toolBar.add(button);
        }
    }

    /**
     * Function to make buttons
     * @param name
     * @return 
     */
    private JButton _makeButton(String name) {
        String imgLocation = "images/icons/" + name + ".png";
        URL imageURL = Bittorrent.class.getResource(imgLocation);
        JButton button = new JButton();
        button.addActionListener(this);

        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL, name));
        } else {
            button.setText(name);
        }

        return button;
    }

    /**
     * Function to show application GUI
     */
    private static void _showGUI() {
        JFrame frame = new JFrame("BitTorrent");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Bittorrent());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Function to show dialog about authors
     */
    private static void _showAboutDialog() {
        String authors = "GitHub:"
                + "\nhttps://github.com/niesuch/bittorrentclient"
                + "\n\nAuthors:"
                + "\n- Banasiuk Paweł"
                + "\n- Grzebuła Łukasz"
                + "\n- Kolek Robert"
                + "\n- Niesłuchowski Kamil"
                + "\n- Puszczyński Paweł";

        final JOptionPane pane = new JOptionPane(authors);
        final JDialog dialog = pane.createDialog((JFrame) null, "About");
        dialog.setVisible(true);
    }

    /**
     * Function to open JFileChooser
     *
     * @return
     */
    private String _openFileChooser() {
        JFileChooser fc = new JFileChooser();
        int chooser = fc.showOpenDialog(this);

        if (chooser == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (null != cmd) {
            switch (cmd) {
                case "ABOUT":
                    _showAboutDialog();
                    break;
                case "OPEN":
                    _openFileChooser();
                    break;
                case "EXIT":
                    System.exit(0);
                case "START":
                    break;
                case "STOP":
                    break;
                case "PAUSE":
                    break;
                case "DELETE":
                    break;
            }
        }
    }

    public static void main(String[] args) {
        _showGUI();
    }

}

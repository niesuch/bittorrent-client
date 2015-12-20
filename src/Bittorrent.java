
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Bittorrent extends JPanel implements ActionListener {

    private JPanel _infoPanel;
    private static final int _WIDTH = 800;
    private static final int _HEIGHT = 600;

    public Bittorrent() {
        super(new BorderLayout());

        JMenuBar menubar = new JMenuBar();
        JMenu menuBittorrentOption = new JMenu("Help");
        JMenu menuFileOption = new JMenu("File");
        menubar.add(menuFileOption);
        menubar.add(menuBittorrentOption);

//      ###### Menu options ######
//      ** BitTorrent option
        JMenuItem about = new JMenuItem("About");
        about.setActionCommand("ABOUT");
        about.addActionListener(this);

//      ** File option
        JMenuItem openFile = new JMenuItem("Open");
        openFile.setActionCommand("OPEN");
        openFile.addActionListener(this);

        JMenuItem exitApp = new JMenuItem("Exit");
        exitApp.setActionCommand("EXIT");
        exitApp.addActionListener(this);
//      ###### End of menu options ######

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
    }

    private static void _showGUI() {
        JFrame frame = new JFrame("BitTorrent");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Bittorrent());
        frame.pack();
        frame.setVisible(true);
    }

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
//      dialog.setLocation(10, 10); // if we have set static location
        dialog.setVisible(true);
    }

    private String _openFileChooser() {
        JFileChooser fc = new JFileChooser();
        int chooser = fc.showOpenDialog(this);

        if (chooser == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath();
        }
        
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("ABOUT".equals(cmd)) {
            _showAboutDialog();
        } else if ("OPEN".equals(cmd)) {
            _openFileChooser();
        } else if ("EXIT".equals(cmd)) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        _showGUI();
    }

}

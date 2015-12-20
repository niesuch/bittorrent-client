import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Bittorrent extends JPanel {

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

        // ###### Menu options ######
        // ** BitTorrent option
        JMenuItem about = new JMenuItem("About");
        about.setActionCommand("ABOUT");

        // ** File option
        JMenuItem openFile = new JMenuItem("Open");
        openFile.setActionCommand("OPEN");

        JMenuItem exitApp = new JMenuItem("Exit");
        exitApp.setActionCommand("EXIT");
        // ###### End of menu options ######
        
        menuBittorrentOption.add(about);
        menuFileOption.add(openFile);
        menuFileOption.add(exitApp);

        _infoPanel = new JPanel();
        JPanel dp = new JPanel(new GridLayout(0, 3));
        dp.add(new JLabel("Name"));
        dp.add(new JLabel("Size"));
        dp.add(new JLabel("% downloaded"));
        _infoPanel.add(dp);
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

    public static void main(String[] args) {
        _showGUI();
    }

}

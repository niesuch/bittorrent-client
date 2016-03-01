
import Manager.DownloadManager;
import Utils.TableUtils.ProgressRenderer;
import Utils.TableUtils.DownloadsTableModel;
import Utils.TableUtils.ColumnKeeper;
import Utils.Utils;
import Config.Config;
import Sockets.TCPConnection;
import Sockets.TCPEchoSelectorProtocol;
import Sockets.TCPServerSelector;
import TorrentMetadata.TorrentFile;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Bittorrent class - main class
 *
 * @author Niesuch
 */
public class Bittorrent extends JFrame implements Observer
{

    private final DownloadsTableModel _tableModel = new DownloadsTableModel();
    private final JTable _table;
    private JButton _pauseButton, _resumeButton;
    private JButton _cancelButton, _deleteButton;
    private final JPanel _infoPanel, _downloadsPanel, _buttonsPanel;
    private DownloadManager _selectedDownload;
    private final JTextField[] _textFields;
    private final String[] _formLabels =
    {
        "Name: ", "Size: ", "% downloaded: ", "Status: ",
        "Download: ", "Upload: ", "Time remaining: ", "Pieces: ",
        "Peer data including IP addresses: ", "Speed download from them: ",
        "Speed upload to them: ", "Port using: ", "Port client: "
    };

    public Bittorrent()
    {
        setTitle("BitTorrent");
        setSize(Config.getWindowWidth(), Config.getWindowHeight());

        _table = new JTable(_tableModel);
        _table.setAutoCreateRowSorter(true);
        _downloadsPanel = new JPanel();
        _buttonsPanel = new JPanel();
        _infoPanel = new JPanel(new BorderLayout());
        _textFields = new JTextField[_formLabels.length];

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        _initNativeWindowView();
        _initMenuBar();
        _initTable();
        _initInfoPanel();
        _initButtonsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _downloadsPanel, _infoPanel);
        splitPane.setResizeWeight(0.7);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_buttonsPanel, BorderLayout.SOUTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Initialization buttons panel
     */
    private void _initButtonsPanel()
    {
        _pauseButton = new JButton("Pause");
        _pauseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _actionPause();
            }
        });
        _pauseButton.setEnabled(false);
        _buttonsPanel.add(_pauseButton);

        _resumeButton = new JButton("Resume");
        _resumeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _actionResume();
            }
        });
        _resumeButton.setEnabled(false);
        _buttonsPanel.add(_resumeButton);

        _cancelButton = new JButton("Cancel");
        _cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _actionCancel();
            }
        });
        _cancelButton.setEnabled(false);
        _buttonsPanel.add(_cancelButton);

        _deleteButton = new JButton("Delete");
        _deleteButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _actionDelete();
            }
        });
        _deleteButton.setEnabled(false);
        _buttonsPanel.add(_deleteButton);
    }

    /**
     * Initialization menu bar
     */
    private void _initMenuBar()
    {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileOpenMenuItem = new JMenuItem("Open", KeyEvent.VK_X);
       

        fileOpenMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TorrentFile openedTorrent = null;
                String torrent = Utils.openFileChooser();
                File torrentFile = new File(torrent);
                try
                {
                    openedTorrent = TorrentFile.load(torrentFile);
//                    String msg = "<html>File <span style='color:green'>" + torrentFile.getName() + "</span> opened succesfully</html>";
//                    JOptionPane.showMessageDialog(null, msg, "Torrent Load", JOptionPane.INFORMATION_MESSAGE);
                    _actionAdd(openedTorrent.fileName, openedTorrent.getTorrentSize());
                     TCPConnection oTCPConnection =new TCPConnection("127.0.0.1", 1337);
                }   
                catch (IOException | NoSuchAlgorithmException ex) 
                {
                    String s = "<html> Failed to open <span style='color:red'>" + torrentFile.getName() +"</span> </html>";
                    JOptionPane.showMessageDialog(null, s, "Torrent Load Failure", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(Bittorrent.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        });
        
        JMenuItem torrentCreateMenuItem = new JMenuItem("Create new torrent from file", KeyEvent.VK_X);

        torrentCreateMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URI testAnnounce = null;
                String loadedFile = Utils.openFileChooser();
                File createTorrentFile = new File(loadedFile);
                TorrentFile createdTorrent = null;
                File newFile = null;
                FileOutputStream fopNewFile = null;
                String savingName = loadedFile;
                
                int spaceIndex = savingName.indexOf(".");
                if (spaceIndex != -1)
                {
                    savingName = savingName.substring(0, spaceIndex);
                }
                
                savingName += "(createdTorrent).torrent";
                try
                {
                    testAnnounce = new URI("udp://tracker.openbittorrent.com");
                    createdTorrent = TorrentFile.create(createTorrentFile,testAnnounce , "robert");

                    newFile = new File(savingName);
                    fopNewFile = new FileOutputStream(newFile);
                    
                    // if file doesnt exists, then create it
                    if (!newFile.exists()) 
                    {
                        newFile.createNewFile();
                    }
                    
                    createdTorrent.save(fopNewFile);
                    String msg = "<html>Saving <span style='color:green'>" + createTorrentFile.getName() + "</span> to <span style='color:green'>"  + newFile.getPath() + "</span> succesfull</html>";
                    JOptionPane.showMessageDialog(null, msg, "Creating new .torrent", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(msg);
                    fopNewFile.flush();
                    fopNewFile.close();
                } 
                catch (URISyntaxException | NoSuchAlgorithmException | InterruptedException | IOException ex)
                {
                    String s = "<html> Creating torrent from <span style='color:red'>" + createTorrentFile.getName() +"</span> FAILED </html>";
                    JOptionPane.showMessageDialog(null, s, "Create Torrent Failed", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(Bittorrent.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        });

        JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);

        fileExitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem helpAboutMenuItem = new JMenuItem("About", KeyEvent.VK_X);

        helpAboutMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _showAboutDialog();
            }
        });

        JMenu mViewMenu = new JMenu("View");
        mViewMenu.setMnemonic(KeyEvent.VK_F);
        TableColumnModel model = _table.getColumnModel();

        for (int i = 0; i < _tableModel.getColumnCount(); i++)
        {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(
                    _tableModel.getColumnName(i));
            item.setSelected(true);
            TableColumn column = model.getColumn(i);
            item.addActionListener(new ColumnKeeper(column, _table, _tableModel));
            mViewMenu.add(item);
        }

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu.add(fileOpenMenuItem);
        fileMenu.add(torrentCreateMenuItem);
        fileMenu.add(fileExitMenuItem);
        helpMenu.add(helpAboutMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(mViewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Initialization download table
     */
    private void _initTable()
    {
        _table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                _tableSelectionChanged();
            }
        });
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true);
        _table.setDefaultRenderer(JProgressBar.class, renderer);
        _table.setRowHeight((int) renderer.getPreferredSize().getHeight());

        _downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
        _downloadsPanel.setLayout(new BorderLayout());
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _downloadsPanel.add(new JScrollPane(_table), BorderLayout.CENTER);

        int i = 0;
        for (int size : _tableModel.getColumnSizes())
        {
            if (size != 0)
            {
                _table.getColumnModel().getColumn(i).setPreferredWidth(size);
            }
            i++;
        }
    }

    /**
     * Initialization information panel
     */
    private void _initInfoPanel()
    {
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations"));

        JPanel form = new JPanel(new GridLayout(0, 2));

        int i = 0;
        for (String formLabel : _formLabels)
        {
            form.add(new JLabel(formLabel));
            _textFields[i] = new JTextField(10);
            form.add(_textFields[i++]);
        }

        formPanel.add(form);
        _infoPanel.add(formPanel);
        _infoPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);

    }

    /**
     * Initialization native window view
     */
    private void _initNativeWindowView()
    {
        Utils.nativeWindowView();
    }

    /**
     * Pause action for button
     */
    private void _actionPause()
    {
        _selectedDownload.pause();
        updateButtons();
    }

    /**
     * Resume action for button
     */
    private void _actionResume()
    {
        _selectedDownload.resume();
        updateButtons();
    }

    /**
     * Cancel action for button
     */
    private void _actionCancel()
    {
        _selectedDownload.cancel();
        updateButtons();
    }

    /**
     * Delete action for button
     */
    private void _actionDelete()
    {
        _tableModel.deleteDownload(_table.getSelectedRow());
        _selectedDownload = null;
        updateButtons();
    }

    private void _actionAdd(String torrentName, long size)
    {
        _tableModel.addDownload(new DownloadManager(torrentName, size));
    }

    /**
     * Function to update buttons views
     */
    private void updateButtons()
    {
        if (_selectedDownload != null)
        {
            int status = _selectedDownload.getStatus();
            switch (status)
            {
                case DownloadManager.DOWNLOADING:
                    _pauseButton.setEnabled(true);
                    _resumeButton.setEnabled(false);
                    _cancelButton.setEnabled(true);
                    _deleteButton.setEnabled(true);
                    break;
                case DownloadManager.PAUSED:
                    _pauseButton.setEnabled(false);
                    _resumeButton.setEnabled(true);
                    _cancelButton.setEnabled(true);
                    _deleteButton.setEnabled(false);
                    break;
                case DownloadManager.ERROR:
                    _pauseButton.setEnabled(false);
                    _resumeButton.setEnabled(true);
                    _cancelButton.setEnabled(false);
                    _deleteButton.setEnabled(true);
                    break;
                default:
                    _pauseButton.setEnabled(false);
                    _resumeButton.setEnabled(false);
                    _cancelButton.setEnabled(false);
                    _deleteButton.setEnabled(true);
            }
        } else
        {
            _pauseButton.setEnabled(false);
            _resumeButton.setEnabled(false);
            _cancelButton.setEnabled(false);
            _deleteButton.setEnabled(false);
        }
    }

    /**
     * Function to change table selection
     */
    private void _tableSelectionChanged()
    {
        if (_selectedDownload != null)
        {
            _selectedDownload.deleteObserver(Bittorrent.this);
            _setFields(_table.getSelectedRow());
        } else
        {
            _selectedDownload = _tableModel.getDownload(_table.getSelectedRow());
            _selectedDownload.addObserver(Bittorrent.this);
            updateButtons();
        }
    }

    /**
     * Function set text to text field in form
     *
     * @param index
     */
    private void _setFields(int index)
    {
        // Informations from all table columns
        if (index >= 0)
        {
            for (int i = 0; i < _tableModel.getColumnCount(); i++)
            {
                _textFields[i].setText(_tableModel.getValueAt(index, i).toString());
            }
        }
        // Other informations
        // _textFields[0].setText(_table.getValueAt(index, 0).toString());
        // ...
    }

    /**
     * Function to update buttons state
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (_selectedDownload != null && _selectedDownload.equals(o))
        {
            updateButtons();
        }
    }

    /**
     * Function to show dialog about authors
     */
    private static void _showAboutDialog()
    {
        String authors = "GitHub:"
                + "\nhttps://github.com/niesuch/bittorrentclient"
                + "\n\nAuthors:"
                + "\n- Banasiuk Paweł"
                + "\n- Grzebuła Łukasz"
                + "\n- Kolek Robert"
                + "\n- Niesłuchowski Kamil"
                + "\n- Puszczyński Paweł"
                + "\n\nApplication version: 1.0";

        Utils.generateDialogWithString(authors, "About");
    }

    public static void main(String[] args)
    {
        Bittorrent bittorrent = new Bittorrent();
        bittorrent.setVisible(true);
    }
}


import Utils.Utils;
import Config.Config;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Bittorrent extends JFrame implements Observer
{

    private final DownloadsTableModel _tableModel = new DownloadsTableModel();
    private final JTable _table;
    private JButton _pauseButton, _resumeButton;
    private JButton _cancelButton, _deleteButton;
    private final JPanel _infoPanel, _downloadsPanel, _buttonsPanel;
    private final JSplitPane _splitPane;
    private Download _selectedDownload;
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
        setSize(Config.WIDTH, Config.HEIGHT);

        _table = new JTable(_tableModel);
        _table.setAutoCreateRowSorter(true);
        _downloadsPanel = new JPanel();
        _buttonsPanel = new JPanel();
        _infoPanel = new JPanel();
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

        _splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _downloadsPanel, _infoPanel);
        _splitPane.setResizeWeight(0.7);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_buttonsPanel, BorderLayout.SOUTH);
        getContentPane().add(_splitPane, BorderLayout.CENTER);

        // Test adding row
        _actionAdd("Test 1");
        _actionAdd("Test 2");
        _actionAdd("Test 3");
        _actionAdd("Test 4");
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
                new Utils().openFileChooser();
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

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu.add(fileOpenMenuItem);
        fileMenu.add(fileExitMenuItem);
        helpMenu.add(helpAboutMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
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

    }

    /**
     * Initialization native window view
     */
    private void _initNativeWindowView()
    {
        new Utils().nativeWindowView();
    }

    /**
     * Pause action for button
     */
    private void _actionPause()
    {

    }

    /**
     * Resume action for button
     */
    private void _actionResume()
    {

    }

    /**
     * Cancel action for button
     */
    private void _actionCancel()
    {

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

    private void _actionAdd(String str)
    {
        _tableModel.addDownload(new Download(str));
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
                case Download.DOWNLOADING:
                    _pauseButton.setEnabled(true);
                    _resumeButton.setEnabled(false);
                    _cancelButton.setEnabled(true);
                    _deleteButton.setEnabled(true);
                    break;
                case Download.PAUSED:
                    _pauseButton.setEnabled(false);
                    _resumeButton.setEnabled(true);
                    _cancelButton.setEnabled(true);
                    _deleteButton.setEnabled(false);
                    break;
                case Download.ERROR:
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
        _textFields[0].setText(_table.getValueAt(index, 0).toString());
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

        new Utils().generateDialogWithString(authors, "About");
    }

    public static void main(String[] args)
    {
        Bittorrent bittorrent = new Bittorrent();
        bittorrent.setVisible(true);
    }
}

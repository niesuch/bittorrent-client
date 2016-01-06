
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Bittorrent extends JFrame implements Observer
{

    private final DownloadsTableModel _tableModel = new DownloadsTableModel();
    private final JTable _table;
    private final JButton _pauseButton, _resumeButton;
    private final JButton _cancelButton, _deleteButton;
    private Download _selectedDownload;
    private static final int _WIDTH = 800;
    private static final int _HEIGHT = 600;

    public Bittorrent()
    {
        setTitle("BitTorrent");
        setSize(_WIDTH, _HEIGHT);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileOpenMenuItem = new JMenuItem("Open", KeyEvent.VK_X);

        fileOpenMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _openFileChooser();
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

        fileMenu.add(fileOpenMenuItem);
        fileMenu.add(fileExitMenuItem);
        helpMenu.add(helpAboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        _table = new JTable(_tableModel);
        _table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                tableSelectionChanged();
            }
        });
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true);
        _table.setDefaultRenderer(JProgressBar.class, renderer);
        _table.setRowHeight((int) renderer.getPreferredSize().getHeight());

        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(_table), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
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
        buttonsPanel.add(_pauseButton);

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
        buttonsPanel.add(_resumeButton);

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
        buttonsPanel.add(_cancelButton);

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
        buttonsPanel.add(_deleteButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(downloadsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        // Test adding row
        _actionAdd();
        _actionAdd();
        _actionAdd();
        _actionAdd();
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

    private void _actionAdd()
    {
        _tableModel.addDownload(new Download("test"));
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
    private void tableSelectionChanged()
    {
        if (_selectedDownload != null)
        {
            _selectedDownload.deleteObserver(Bittorrent.this);
        } else
        {
            _selectedDownload = _tableModel.getDownload(_table.getSelectedRow());
            _selectedDownload.addObserver(Bittorrent.this);
            updateButtons();
        }
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

        final JOptionPane pane = new JOptionPane(authors);
        final JDialog dialog = pane.createDialog((JFrame) null, "About");
        dialog.setVisible(true);
    }

    /**
     * Function to open JFileChooser
     *
     * @return
     */
    private String _openFileChooser()
    {
        JFileChooser fc = new JFileChooser();
        int chooser = fc.showOpenDialog(this);

        if (chooser == JFileChooser.APPROVE_OPTION)
        {
            return fc.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    public static void main(String[] args)
    {
        Bittorrent bittorrent = new Bittorrent();
        bittorrent.setVisible(true);
    }
}

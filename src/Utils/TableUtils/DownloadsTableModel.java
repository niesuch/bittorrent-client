package Utils.TableUtils;

import Manager.DownloadManager;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

/**
 * DownloadTableModel class - class which support table with downloads
 *
 * @author Niesuch
 */
public class DownloadsTableModel extends AbstractTableModel implements Observer
{

    private static final String[] _columnNames =
    {
        "Name", "Size", "% downloaded", "Status", "Download", "Upload",
        "Time remaining", "Pieces"
    };

    private static final Class[] _columnClasses =
    {
        String.class, String.class, JProgressBar.class,
        String.class, String.class, String.class, String.class,
        String.class
    };

    private static final int[] _columnSizes =
    {
        120, 120, 240, 100, 0, 0, 100, 0
    };

    private final ArrayList _downloadList = new ArrayList();

    /**
     * Return column sizes
     *
     * @return
     */
    public int[] getColumnSizes()
    {
        return _columnSizes;
    }

    /**
     * Return array with all columns names
     *
     * @return
     */
    public String[] getAllCollumns()
    {
        return _columnNames;
    }

    @Override
    public int getColumnCount()
    {
        return _columnNames.length;
    }

    @Override
    public String getColumnName(int col)
    {
        return _columnNames[col];
    }

    @Override
    public Class getColumnClass(int col)
    {
        return _columnClasses[col];
    }

    @Override
    public int getRowCount()
    {
        return _downloadList.size();
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        DownloadManager download = (DownloadManager) _downloadList.get(row);
        switch (col)
        {
            case 0: // Name
                return download.getFileName();
            case 1: // Size
                long size = download.getSize();
                return (size == -1) ? "" : Long.toString(size);
            case 2: // Progress
                return download.getProgress();
            case 3: // Status
                return download.STATUSES[download.getStatus()];
            case 4: // Download
                return download.getDownloadSpeed() + " kb/s";
            case 5: // Upload
                return download.getUploadSpeed() + " kb/s";
            case 6: // Time remaining
                int time = download.getTimeRemaining();
                return (time == -1) ? "" : Integer.toString(time);

        }
        return "";
    }

    @Override
    public void update(Observable o, Object arg)
    {
        int index = _downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }

    /**
     * Adding new download row
     *
     * @param download
     */
    public void addDownload(DownloadManager download)
    {
        download.addObserver(this);
        _downloadList.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    /**
     * Return download row
     *
     * @param row
     * @return
     */
    public DownloadManager getDownload(int row)
    {
        return (DownloadManager) _downloadList.get(row);
    }

    /**
     * Deleting download row
     *
     * @param row
     */
    public void deleteDownload(int row)
    {
        _downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }
}

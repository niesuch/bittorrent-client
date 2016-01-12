
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

class DownloadsTableModel extends AbstractTableModel implements Observer
{
    // TODO: if I have time show/hide columns
    // TODO: more informations: information on the "pieces", Peer data including IP addresses,
    // the speeds at which you are downloading and uploading to/from
    // them, the port they are running BitTorrent on, and the BitTorrent client they are using.

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

    private final ArrayList _downloadList = new ArrayList();

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
        Download download = (Download) _downloadList.get(row);
        switch (col)
        {
            case 0: // Name
                return download.getFileName();
            case 1: // Size
                int size = download.getSize();
                return (size == -1) ? "" : Integer.toString(size);
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

    public void addDownload(Download download)
    {
        download.addObserver(this);
        _downloadList.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public Download getDownload(int row)
    {
        return (Download) _downloadList.get(row);
    }

    public void deleteDownload(int row)
    {
        _downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }
}

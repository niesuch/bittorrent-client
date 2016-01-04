
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

class DownloadsTableModel extends AbstractTableModel implements Observer
{

    private static final String[] _columnNames =
    {
        "Name", "Size", "% downloaded", "Status"
    };

    private static final Class[] _columnClasses =
    {
        String.class, String.class, JProgressBar.class,
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
                return Download.STATUSES[download.getStatus()];
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

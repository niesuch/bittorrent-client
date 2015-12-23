
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
        return 0;
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        return null;
    }

    @Override
    public void update(Observable o, Object arg)
    {

    }
}

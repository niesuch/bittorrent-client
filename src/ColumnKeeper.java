
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * ColumnKeeper class - class for columns action
 *
 * @author Niesuch
 */
class ColumnKeeper implements ActionListener
{

    private TableColumn _tColumn;
    private JTable _table;
    private DownloadsTableModel _tableModel;

    public ColumnKeeper(TableColumn column, JTable table, DownloadsTableModel tableModel)
    {
        _tColumn = column;
        _table = table;
        _tableModel = tableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
        TableColumnModel model = _table.getColumnModel();

        if (item.isSelected())
        {
            model.addColumn(_tColumn);
        } else
        {
            model.removeColumn(_tColumn);
        }
        _table.tableChanged(new TableModelEvent(_tableModel));
        _table.repaint();
    }
}

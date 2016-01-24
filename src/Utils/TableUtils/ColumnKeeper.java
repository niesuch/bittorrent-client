package Utils.TableUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
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
public class ColumnKeeper implements ActionListener
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
            String colName = _table.getModel().getColumnName(_tColumn.getModelIndex());
            int index = Arrays.asList(_tableModel.getAllCollumns()).indexOf(colName);

            model.addColumn(_tColumn);
            model.moveColumn(model.getColumnCount() - 1, verifyPositionToAdd(index));
        } else
        {
            model.removeColumn(_tColumn);
        }
        _table.tableChanged(new TableModelEvent(_tableModel));
        _table.repaint();
    }

    /**
     * Checkout if a column exists
     *
     * @param name
     * @return
     */
    public Boolean columnExists(String name)
    {
        for (int i = 0; i < _table.getColumnCount(); i++)
        {
            if (_table.getColumnName(i).equals(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify the position to add column
     *
     * @param index
     * @return
     */
    public int verifyPositionToAdd(int index)
    {
        int iter = index;
        for (int i = 0; i < iter; i++)
        {
            if (!columnExists(_tableModel.getColumnName(i)))
            {
                index--;
            }
        }

        return index;
    }
}

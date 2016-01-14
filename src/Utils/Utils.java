package Utils;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Utils class - Class with general methods which can be use everywhere
 *
 * @author Niesuch
 */
public class Utils
{

    public Utils()
    {

    }

    /**
     * Function to open JFileChooser
     *
     * @return
     */
    public String openFileChooser()
    {
        JFileChooser fc = new JFileChooser();
        int chooser = fc.showOpenDialog(null);

        if (chooser == JFileChooser.APPROVE_OPTION)
        {
            return fc.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    /**
     * Function to generate dialog with user string
     *
     * @param str
     * @param title
     */
    public void generateDialogWithString(String str, String title)
    {
        final JOptionPane pane = new JOptionPane(str);
        final JDialog dialog = pane.createDialog((JFrame) null, title);
        dialog.setVisible(true);
    }

    /**
     * Function to initialization native window view
     */
    public void nativeWindowView()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.err.println("Error loading the appearance of windows.");
        }
    }
}

import gui.CrudAppGUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CrudAppGUI().setVisible(true));
    }
}

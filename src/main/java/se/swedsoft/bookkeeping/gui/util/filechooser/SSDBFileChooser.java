package se.swedsoft.bookkeeping.gui.util.filechooser;


import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterDB;


/**
 * Date: 2006-feb-13
 * Time: 14:47:02
 */
public class SSDBFileChooser extends SSFileChooser {

    private static SSDBFileChooser cInstance;

    public static SSDBFileChooser getInstance() {
        if (cInstance == null) {
            cInstance = new SSDBFileChooser();
        }
        return cInstance;
    }

    /**
     *
     */
    private SSDBFileChooser() {
        addChoosableFileFilter(new SSFilterDB());
    }
}

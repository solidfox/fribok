package se.swedsoft.bookkeeping.gui.util;

/**
 * Date: 2006-jan-30
 * Time: 13:16:45
 */
public interface SSSelectionListener <T extends se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable>  {
    public void selected(T selected);

}

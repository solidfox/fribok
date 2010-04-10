package se.swedsoft.bookkeeping.gui.util;


/**
 * Date: 2006-jan-30
 * Time: 13:16:45
 */
public interface SSValueListener <T extends se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable> {
    void value(T selected, Object Value);

}

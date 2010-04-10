package se.swedsoft.bookkeeping.data.common;


import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;


/**
 * User: Andreas Lago
 * Date: 2006-mar-28
 * Time: 11:12:04
 */
public enum SSHeadingType implements SSTableSearchable {
    HEADING1("Rubrik 1"), HEADING2("Rubrik 2"), HEADING3("Resultat");

    private String iName;

    /**
     *
     * @param iName
     */
    SSHeadingType(String iName) {
        this.iName = iName;
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        if (this == HEADING1) {
            return "Rubrik 1";
        }
        if (this == HEADING2) {
            return "Rubrik 2";
        }
        if (this == HEADING3) {
            return "Resultat";
        }

        return iName;
    }

    public String toString() {
        return toRenderString();
    }
}


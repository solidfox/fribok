package se.swedsoft.bookkeeping.data.backup;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2006-mar-03
 * Time: 09:54:57
 */
public class SSBackupData implements Serializable  {

    static final long serialVersionUID = 1L;

    private List<SSBackup> iBackups;


    public SSBackupData(){
        iBackups = new LinkedList<SSBackup>();
    }


    /**
     *
     * @return the backups
     */
    public List<SSBackup> getBackups() {
        return iBackups;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.backup.SSBackupData");
        sb.append("{iBackups=").append(iBackups);
        sb.append('}');
        return sb.toString();
    }
}

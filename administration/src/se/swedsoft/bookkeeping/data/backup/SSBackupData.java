package se.swedsoft.bookkeeping.data.backup;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;

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

    

}

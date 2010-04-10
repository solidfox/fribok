package se.swedsoft.bookkeeping.resources;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * User: Andreas Lago
 * Date: 2006-maj-02
 * Time: 14:32:11
 */
public class SSResourceBundle extends ResourceBundle {

    private ResourceBundle iBundle;

    /**
     *
     * @param iBundle
     */
    public SSResourceBundle(ResourceBundle iBundle) {
        this.iBundle = iBundle;
    }

    @Override
    public void setParent(ResourceBundle parent) {
        super.setParent(parent);
    }

    @Override
    public Enumeration<String> getKeys() {
        return iBundle.getKeys();
    }

    /**
     *
     * @param key
     * @return If the key exists in the bundle
     */
    public boolean hasKey(String key){
        try {
            iBundle.getObject(key);
        } catch (MissingResourceException e) {
            return false;
        }
        return true;
    }

    @Override
    protected Object handleGetObject(String key) {

        Object iObject;
        try {
            iObject = iBundle.getObject(key);
        } catch (Exception e) {
            return null;
        }
       return iObject ;

    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.resources.SSResourceBundle");
        sb.append("{iBundle=").append(iBundle);
        sb.append('}');
        return sb.toString();
    }
}

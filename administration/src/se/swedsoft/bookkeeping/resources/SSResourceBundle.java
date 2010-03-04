package se.swedsoft.bookkeeping.resources;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.MissingResourceException;

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

    /**
     * Sets the parent bundle of this bundle.
     * The parent bundle is searched by {@link #getObject getObject}
     * when this bundle does not contain a particular resource.
     *
     * @param parent this bundle's parent bundle.
     */
    @Override
    public void setParent(ResourceBundle parent) {
        super.setParent(parent);
    }


    /**
     * Returns an enumeration of the keys.
     */
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




    /**
     * Gets an object for the given key from this resource bundle.
     * Returns null if this resource bundle does not contain an
     * object for the given key.
     *
     * @param key the key for the desired object
     *
     * @return the object for the given key, or null
     *
     * @throws NullPointerException if <code>key</code> is <code>null</code>
     */
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



}

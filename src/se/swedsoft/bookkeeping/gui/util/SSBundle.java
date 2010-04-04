/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The resourcebundle for this application
 */
public class SSBundle extends ResourceBundle {

    /** */
    private static SSBundle cBundle;

    static {
        cBundle = new SSBundle(ResourceBundle.getBundle("book"));
    }

    /** */
    private ResourceBundle iBundle;

    /**
     * Default constructor.
     * @param bundle the bundle to use and to protect us from
     */
    public SSBundle(ResourceBundle bundle) {
        iBundle = bundle;
    }

    /**
     * Returns the bundle to use.
     * @return a "Safe" resource bundle
     */
    public static SSBundle getBundle() {
        return cBundle;
    }

    /**
     * Returns an enumeration of the keys.
     * @return
     */
    @Override
    public Enumeration<String> getKeys() {
        return iBundle.getKeys();
    }

    /**
     *
     * @param key
     * @return true if the key exists in the bundle
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
     * @return the object for the given key, or null
     * @throws NullPointerException if <code>key</code> is <code>null</code>
     */
    @Override
    protected Object handleGetObject(String key) {

        Object o = null;

        try {
            o = iBundle.getObject(key);
        } catch (MissingResourceException e) {
            o = "???" + key + "???";
        }

        return o;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.SSBundle");
        sb.append("{iBundle=").append(iBundle);
        sb.append('}');
        return sb.toString();
    }
} // End of class SSBundle
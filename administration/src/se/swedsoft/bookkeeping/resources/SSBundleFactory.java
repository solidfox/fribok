package se.swedsoft.bookkeeping.resources;

import java.util.*;

/**
 * Date: 2006-mar-20
 * Time: 14:26:12
 */
public class SSBundleFactory {

    private static SSBundleFactory cInstance;

    static{
        cInstance = new SSBundleFactory();
    }




    private Map<String, ResourceBundle> iBundles;

    /**
     *
     */
    private SSBundleFactory(){
        iBundles = new HashMap<String, ResourceBundle>();
        
    }


    /**
     *
     * @param pClass
     * @return
     */
    public ResourceBundle getBundle(Class pClass){
        String iName = pClass.getName();

        return iBundles.get(iName);
    }

    /**
     *
     * @param pClass
     * @param pBundle
     */
    public void addBundle(Class pClass, String pBundle){
        String iName = pClass.getName();

        ResourceBundle iBundle = ResourceBundle.getBundle(pBundle);

        iBundles.put(iName, iBundle);
    }


    

    private class SSResourceBundle extends ResourceBundle{

        private ResourceBundle iBundle;


        /**
         * Default constructor.
         *
         * @param pBundle The bundle to use and to protect us from.
         */
        public SSResourceBundle(ResourceBundle pBundle) {
            iBundle = pBundle;
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
         * @return the object for the given key, or null
         * @throws NullPointerException if <code>key</code> is <code>null</code>
         */
        protected Object handleGetObject(String key) {
            try {
                return iBundle.getObject(key);
            } catch (MissingResourceException e) {
                return "???" + key + "???";
            }
        }

        /**
         * Returns an enumeration of the keys.
         */
        public Enumeration<String> getKeys() {
            return iBundle.getKeys();
        }

    }

}


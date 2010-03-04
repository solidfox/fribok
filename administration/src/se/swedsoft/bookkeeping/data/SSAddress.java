/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import java.io.Serializable;

/**
 * @author Roger Björnstedt
 */
public class SSAddress implements Serializable {

    //Constant for serialization versioning.
    static final long serialVersionUID = 1L;



    private String iName;

    private String iAddress;

    private String iStreet;

    private String iZipCode;

    private String iCity;

    private String iCountry;



    /**
     * Default constructor.
     */
    public SSAddress() {
        iName    = "";
        iAddress = "";
        iStreet  = "";
        iZipCode = "";
        iCity    = "";
        iCountry = "";
    }

    public void dispose()
    {
        iName=null;
        iAddress=null;
        iStreet=null;
        iZipCode=null;
        iCity=null;
        iCountry=null;
    }

    /**
     * Copy constructor
     * @param iAddress
     */
    public SSAddress(SSAddress iAddress) {
        this.iName    = iAddress.iName;
        this.iAddress = iAddress.iAddress;
        this.iStreet  = iAddress.iStreet;
        this.iZipCode = iAddress.iZipCode;
        this.iCity    = iAddress.iCity;
        this.iCountry = iAddress.iCountry;
    }


    /**
     * Constructor
     *
     * @param pName
     * @param pAddress
     * @param pStreet
     * @param pZipCode
     * @param pCity
     * @param pCountry
     */
    public SSAddress(String pName, String pAddress, String pStreet, String pZipCode, String pCity, String pCountry) {
        iName    = pName;
        iAddress = pAddress;
        iStreet  = pStreet;
        iZipCode = pZipCode;
        iCity    = pCity;
        iCountry = pCountry;
    }
    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param pName
     */
    public void setName(String pName) {
        iName = pName;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getAddress1() {
        return iAddress;
    }

    /**
     *
     * @param address
     */
    public void setAddress1(String address) {
        iAddress = address;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getAddress2() {
        return iStreet;
    }

    /**
     *
     * @param street
     */
    public void setAddress2(String street) {
        iStreet = street;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getZipCode() {
        return iZipCode;
    }

    /**
     *
     * @param zipCode
     */
    public void setZipCode(String zipCode) {
        iZipCode = zipCode;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getCity() {
        return iCity;
    }

    /**
     *
     * @param city
     */
    public void setCity(String city) {
        iCity = city;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getCountry() {
        return iCountry;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        iCountry = country;
    }


    //////////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public String getPostalAddress() {
        return iZipCode + " " + iCity;
    }

    //////////////////////////////////////////////////////

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object.
     *
     * @return a clone of this instance.
     */
    @Override
    protected SSAddress clone() {
        return new SSAddress(this);
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if(obj instanceof SSAddress){
            SSAddress iOther =  (SSAddress)obj;

            return iName   .equals( iOther.iName    ) &&
                    iAddress.equals( iOther.iAddress ) &&
                    iStreet .equals( iOther.iStreet  ) &&
                    iZipCode.equals( iOther.iZipCode ) &&
                    iCity   .equals( iOther.iCity    ) &&
                    iCountry.equals( iOther.iCountry ) ;

        }

        return super.equals(obj);
    }

    /**
     *
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Address: \n");
        sb.append("  ");
        sb.append(iName);
        sb.append("\n");

        sb.append("  ");
        sb.append(iAddress);
        sb.append(" ");
        sb.append(iStreet);
        sb.append("\n");

        sb.append("  ");
        sb.append(iZipCode);
        sb.append(" ");
        sb.append(iCity);
        sb.append("\n");

        sb.append(" ");
        sb.append(iCountry);
        sb.append("\n");

        return sb.toString();
    }

}

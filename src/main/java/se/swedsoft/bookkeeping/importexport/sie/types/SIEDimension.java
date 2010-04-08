package se.swedsoft.bookkeeping.importexport.sie.types;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2006-feb-23
 * Time: 11:24:31
 */
public class SIEDimension {


    private Integer iNumber;

    private String iName;

    // Means that an dimension is reserved and can't be changed
    private boolean iReserved;

    /**
     *
     */
    public SIEDimension(){
        iNumber   = null;
        iName     = null;
        iReserved = false;
    }

    /**
     *
     * @param pNumber
     * @param pName
     */
    public SIEDimension(Integer pNumber, String pName){
        iNumber   = pNumber;
        iName     = pName;
        iReserved = false;
    }

    /**
     *
     * @param pNumber
     * @param pReserved
     * @param pName
     */
    private SIEDimension(Integer pNumber, boolean pReserved, String pName){
        iNumber   = pNumber;
        iName     = pName;
        iReserved = pReserved;
    }

    /**
     *
     * @return
     */
    public Integer getNumber() {
        return iNumber;
    }

    /**
     *
     * @param iNumber
     */
    public void setNumber(Integer iNumber) {
        this.iNumber = iNumber;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param iName
     */
    public void setName(String iName) {
        this.iName = iName;
    }

    /**
     *
     * @param pDimension
     */
    public void setSuperDimension(Integer pDimension) {
        System.out.println("SIEDimension.setSuperDimension(" + pDimension + ')');
    }
    
    /**
     *
     * @return
     */
    public boolean isReserved() {
        return iReserved;
    }

    
    public boolean equals(Object obj) {
        if(obj instanceof SIEDimension){
            SIEDimension iObject = (SIEDimension)obj;

            return iObject.iNumber.equals(iNumber);
        }
        return super.equals(obj);
    }

    public static List<SIEDimension> getDefaultDimensions(){
        List<SIEDimension> iDimensions = new LinkedList<SIEDimension>();

        iDimensions.add(new SIEDimension( 1, true, "Kostnadsställe / Resultatenhet") );
        iDimensions.add(new SIEDimension( 2, true, "Kostnadsbärare") );
        iDimensions.add(new SIEDimension( 3, true, "Reseverad") );
        iDimensions.add(new SIEDimension( 4, true, "Reseverad") );
        iDimensions.add(new SIEDimension( 5, true, "Reseverad") );
        iDimensions.add(new SIEDimension( 6, true, "Projekt") );
        iDimensions.add(new SIEDimension( 7, true, "Anställd") );
        iDimensions.add(new SIEDimension( 8, true, "Kund") );
        iDimensions.add(new SIEDimension( 9, true, "Leverantör") );
        iDimensions.add(new SIEDimension(10, true, "Faktura") );
        iDimensions.add(new SIEDimension(11, true, "Reseverad") );
        iDimensions.add(new SIEDimension(12, true, "Reseverad") );
        iDimensions.add(new SIEDimension(13, true, "Reseverad") );
        iDimensions.add(new SIEDimension(14, true, "Reseverad") );
        iDimensions.add(new SIEDimension(15, true, "Reseverad") );
        iDimensions.add(new SIEDimension(16, true, "Reseverad") );
        iDimensions.add(new SIEDimension(17, true, "Reseverad") );
        iDimensions.add(new SIEDimension(18, true, "Reseverad") );
        iDimensions.add(new SIEDimension(19, true, "Reseverad") );

        return iDimensions;
    }

    /**
     *
     * @param iDimensions
     * @param pNumber
     * @return
     */
    public static SIEDimension getDimension(List<SIEDimension> iDimensions, int pNumber) {
        for(SIEDimension iDimension: iDimensions){
            if(iDimension.iNumber == pNumber){
                return iDimension;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.sie.types.SIEDimension");
        sb.append("{iName='").append(iName).append('\'');
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iReserved=").append(iReserved);
        sb.append('}');
        return sb.toString();
    }
}

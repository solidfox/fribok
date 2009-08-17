package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * User: Andreas Lago
 * Date: 2006-jul-24
 * Time: 11:40:06
 */
public class SSProductMath {


    /**
     * Gets the product with the specific nr from the list, if any
     *
     * @return
     */
    public static SSProduct getProduct(List<SSProduct> iProducts, String iProductNr) {
        for(SSProduct iCurrent: iProducts){
            String iNumber = iCurrent.getNumber();

            if(iNumber != null && iNumber.equals(iProductNr)){
                return iCurrent;
            }
        }
        return null;
    }

    /**
     * Returns the products that isnt a parcel
     *
     * @return
     */
    public static List<SSProduct> getNormalProducts() {
        List<SSProduct> iProducts = SSDB.getInstance().getProducts();
        List<SSProduct> iFiltered = new LinkedList<SSProduct>();

        for (SSProduct iProduct : iProducts) {
            if(! iProduct.isParcel() ) iFiltered.add(iProduct);
        }
        return iFiltered;
    }

    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getLastPurchasePrice(SSProduct iProduct, Date iDate) {
        List<SSSupplierInvoice> iSupplierInvoices =  new LinkedList<SSSupplierInvoice>( SSDB.getInstance().getSupplierInvoices() );

        Collections.sort(iSupplierInvoices, new Comparator<SSSupplierInvoice>() {
            public int compare(SSSupplierInvoice o1, SSSupplierInvoice o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {
            List<SSSupplierInvoiceRow> iRows = iSupplierInvoice.getRows();

            for (SSSupplierInvoiceRow iRow : iRows) {
                if( iRow.hasProduct(iProduct)){
                    return iRow.getUnitprice();
                }
            }
        }

        return iProduct.getPurchasePrice() ;
    }

    /**
     *
     * @param iProducts
     * @return
     */
    public static List<SSProduct> getStockProducts(List<SSProduct> iProducts) {
        List<SSProduct> iFiltered = new LinkedList<SSProduct>();

        for (SSProduct iProduct : iProducts) {

            if(iProduct.isParcel()) continue;

            if( iProduct.isStockProduct() ) {
                iFiltered.add(iProduct);
            }
        }
        return iFiltered;
    }

    /**
     *
     * @param iParcel
     * @param iProduct
     * @return
     */
    public static Integer getProductCount(SSProduct iParcel, SSProduct iProduct) {
        Integer iCount = 0;

        for (SSProductRow iRow : iParcel.getParcelRows()) {
            if( iRow.hasProduct(iProduct) ){

                Integer iQuantity = iRow.getQuantity();

                if(iQuantity != null) iCount = iCount + iQuantity;
            }

        }
        return iCount;
    }



    /**
     *
     * @param iProducts
     * @return
     */
    public static Map<SSProduct, BigDecimal> getInprices(List<SSProduct> iProducts) {
        Map<SSProduct, BigDecimal> IInprices = new HashMap<SSProduct, BigDecimal>();
        for (SSProduct iProduct : iProducts) {
            IInprices.put(iProduct, getInprice(iProduct) );
        }
        return IInprices;
    }

    /**
     *
     * @param iProducts
     * @return
     */
    public static Map<SSProduct, BigDecimal> getInprices(List<SSProduct> iProducts, Date iDate) {
        Map<SSProduct, BigDecimal> IInprices = new HashMap<SSProduct, BigDecimal>();
        for (SSProduct iProduct : iProducts) {
            IInprices.put(iProduct, getInprice(iProduct, iDate) );
        }
        return IInprices;
    }


    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getInprice(SSProduct iProduct) {
        return getInprice(iProduct, null);
    }

    /**
     *
     * @param iProduct
     * @param iDate
     * @return
     */
    public static BigDecimal getInprice(SSProduct iProduct, Date iDate) {
        // Paket produkt
        if(iProduct.isParcel()){

            BigDecimal iInpriceSum = new BigDecimal(0);

            for (SSProductRow iRow : iProduct.getParcelRows()) {
                SSProduct iRowProduct   = iRow.getProduct();
                // Undvik inf loop
                if( iProduct.equals( iRowProduct) ) continue;

                Integer iQuantity = iRow.getQuantity();

                // Lägg till inpriset för produkten multiplicerat med antalet
                if(iRowProduct == null || iQuantity == null) continue;

                BigDecimal iInprice = getInprice(iRowProduct, iDate);

                // Om endast en rad saknar inpris så kan vi inte räkna ut något inpris
                if(iInprice == null) return null;

                iInpriceSum = iInpriceSum.add( iInprice.multiply( new BigDecimal(iQuantity) ) );

            }
            return iInpriceSum;
        }
        List<SSSupplierInvoice> iSupplierInvoices = SSDB.getInstance().getSupplierInvoices();


        List<SSSupplierInvoice> iFiltered = new LinkedList<SSSupplierInvoice>( iSupplierInvoices );

        // Sortera efter datum i fallande ordning
        Collections.sort(iFiltered, new Comparator<SSSupplierInvoice>() {
            public int compare(SSSupplierInvoice o1, SSSupplierInvoice o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        for (SSSupplierInvoice iSupplierInvoice : iFiltered) {

            if(iDate == null || SSSupplierInvoiceMath.inPeriod(iSupplierInvoice, iDate)) {

                List<SSSupplierInvoiceRow> iRows = iSupplierInvoice.getRows();

                for (SSSupplierInvoiceRow iRow : iRows) {
                    if( iRow.hasProduct(iProduct)){

                        BigDecimal iUnitPrice   = iRow.getUnitprice();
                        BigDecimal iUnitFreight = iRow.getUnitFreight();

                        if( iUnitPrice == null) continue;

                        BigDecimal iValue;

                        if( iUnitFreight != null){
                            iValue = iUnitPrice.add(iUnitFreight);
                        } else {
                            iValue = iUnitPrice;
                        }
                        BigDecimal iLocalValue = SSSupplierInvoiceMath.convertToLocal(iSupplierInvoice, iValue);
                        return iLocalValue == null ? iProduct.getStockPrice() : iLocalValue;
                    }
                }
            }
        }
        return iProduct.getStockPrice();
    }



    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getContribution(SSProduct iProduct) {
        BigDecimal iInprice = getInprice(iProduct);

        if(iInprice == null || iProduct.getSellingPrice() == null) return null;

        // Enhetspros - Senaste inpris
        return iProduct.getSellingPrice().subtract(iInprice);
    }

    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getContribution(SSProduct iProduct, Date iDate) {
        BigDecimal iInprice = getInprice(iProduct, iDate);

        if(iInprice == null || iProduct.getSellingPrice() == null) return null;

        // Enhetspros - Senaste inpris
        return iProduct.getSellingPrice().subtract(iInprice);
    }



    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getContributionRate(SSProduct iProduct) {
        BigDecimal iContribution  = getContribution(iProduct);
        BigDecimal iSellingPrice  = iProduct.getSellingPrice();

        if(iContribution == null || iSellingPrice == null) return null;

        if(iSellingPrice.signum() == 0 ) return null;

        // TB / Enhetspris
        return iContribution.divide(iSellingPrice, 20, RoundingMode.HALF_UP ).scaleByPowerOfTen(2);
    }

    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getContributionRate(SSProduct iProduct, Date iDate) {
        BigDecimal iContribution = getContribution(iProduct, iDate);
        BigDecimal iSellingPrice = iProduct.getSellingPrice();

        if(iContribution == null || iSellingPrice == null) return null;

        if(iSellingPrice.signum() == 0 ) return null;

        // TB / Enhetspris
        return iContribution.divide(iSellingPrice, 20, RoundingMode.HALF_UP ).scaleByPowerOfTen(2);
    }

    public static BigDecimal getContributionRate(SSProduct iProduct, Date iDate, BigDecimal iContribution) {
        //BigDecimal iContribution = getContribution(iProduct, iDate);
        BigDecimal iSellingPrice = iProduct.getSellingPrice();

        if(iContribution == null || iSellingPrice == null) return null;

        if(iSellingPrice.signum() == 0 ) return null;

        // TB / Enhetspris
        return iContribution.divide(iSellingPrice, 20, RoundingMode.HALF_UP ).scaleByPowerOfTen(2);
    }





    /**
     *
     * @param iProduct
     * @return
     */
    public static Integer getSaleCount(SSProduct iProduct) {
        return getSaleCount(iProduct, null, null);
    }

    /**
     *
     * @param iProduct
     * @param iFrom
     * @param iTo
     * @return
     */
    public static Integer getSaleCount(SSProduct iProduct, Date iFrom, Date iTo) {
        List<SSInvoice>       iInvoices       = SSDB.getInstance().getInvoices();
        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();

        Integer iSaleCount = 0;
        for (SSInvoice iInvoice : iInvoices) {

            if(SSInvoiceMath.inPeriod(iInvoice,  iFrom, iTo)){
                Integer iCount = SSInvoiceMath.getProductCount(iInvoice, iProduct);

                if(iCount != null) iSaleCount += iCount;
            }
        }

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            if(SSCreditInvoiceMath.inPeriod(iCreditInvoice,  iFrom, iTo)){
                Integer iCount = SSCreditInvoiceMath.getProductCount(iCreditInvoice, iProduct);

                if(iCount != null) iSaleCount -= iCount;
            }
        }
        return iSaleCount;
    }

    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getAverageSellingPrice(SSProduct iProduct) {
        return getAverageSellingPrice(iProduct, null, null);
    }

    /**
     *
     * @param iProduct
     * @return
     */
    public static BigDecimal getAverageSellingPrice(SSProduct iProduct, Date iFrom, Date iTo) {
        List<SSInvoice>       iInvoices = SSDB.getInstance().getInvoices();
        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();

        BigDecimal iSum   = new BigDecimal(0);
        Integer    iCount = 0;
        for (SSInvoice iInvoice : iInvoices) {
            if( iFrom == null || iTo == null || SSInvoiceMath.inPeriod(iInvoice, iFrom, iTo)){

                List<SSSaleRow> iRows = SSInvoiceMath.getRowsForProduct(iInvoice, iProduct);

                for (SSSaleRow iRow : iRows) {
                    Integer    iQuantity  = iRow.getQuantity();
                    BigDecimal iUnitprice = iRow.getUnitprice();
                    BigDecimal iDiscount  = iRow.getNormalizedDiscount();

                    if(iQuantity == null || iUnitprice == null ) continue;

                    BigDecimal iValue = iUnitprice.multiply(new BigDecimal(iQuantity));

                    if(iDiscount != null) iValue = iValue.subtract( iValue.multiply(iDiscount) );

                    iValue = SSInvoiceMath.convertToLocal(iInvoice,  iValue);

                    iSum = iSum.add(iValue);


                    iCount = iCount + iQuantity;
                }

            }
        }
        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            if( iFrom == null || iTo == null || SSCreditInvoiceMath.inPeriod(iCreditInvoice, iFrom, iTo)){

                List<SSSaleRow> iRows = SSCreditInvoiceMath.getRowsForProduct(iCreditInvoice, iProduct);

                for (SSSaleRow iRow : iRows) {
                    Integer    iQuantity  = iRow.getQuantity();
                    BigDecimal iUnitprice = iRow.getUnitprice();
                    BigDecimal iDiscount  = iRow.getNormalizedDiscount();

                    if(iQuantity == null || iUnitprice == null ) continue;

                    BigDecimal iValue = iUnitprice.multiply(new BigDecimal(iQuantity));

                    if(iDiscount != null) iValue = iValue.subtract( iValue.multiply(iDiscount) );

                    iValue = SSCreditInvoiceMath.convertToLocal(iCreditInvoice,  iValue);

                    iSum = iSum.subtract(iValue);

                    iCount = iCount - iQuantity;
                }
            }
        }

        if( iCount == 0) return iProduct.getSellingPrice();

        if( iSum.signum() == 0 ) return iSum;

        return iSum.divide(new BigDecimal(iCount), 20, RoundingMode.HALF_UP);
    }

}

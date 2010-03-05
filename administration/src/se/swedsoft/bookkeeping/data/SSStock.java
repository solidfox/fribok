package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.*;
import se.swedsoft.bookkeeping.calc.util.SSFilter;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-sep-19
 * Time: 16:46:21
 */
public class SSStock {


    // Antal på lagret
    private Map<SSProduct, Integer> iQuantity;
    // Antal reserverade
    private Map<SSProduct, Integer> iReserved;
    // Antal beställda
    private Map<SSProduct, Integer> iOrdered;

    /**
     *
     * @param update
     */
    public SSStock(boolean update) {
        iQuantity = new HashMap<SSProduct, Integer>();
        iReserved = new HashMap<SSProduct, Integer>();
        iOrdered  = new HashMap<SSProduct, Integer>();

        if(update) update();
    }

    /**
     *
     */
    public SSStock() {
        this(false);
    }

    /**
     *
     */
    public void update(){
        List<SSOrder>                 iOrders                 = SSDB.getInstance().getOrders() ;
        List<SSInvoice>               iInvoices               = SSDB.getInstance().getInvoices();
        List<SSCreditInvoice>         iCreditInvoices         = SSDB.getInstance().getCreditInvoices();
        List<SSPurchaseOrder>         iPurchaseOrders         = SSDB.getInstance().getPurchaseOrders();
        List<SSSupplierInvoice>       iSupplierInvoices       = SSDB.getInstance().getSupplierInvoices();
        List<SSSupplierCreditInvoice> iSupplierCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();
        List<SSInventory>             iInventories            = SSDB.getInstance().getInventories();
        List<SSIndelivery>            iIndeliveries           = SSDB.getInstance().getIndeliveries();
        List<SSOutdelivery>           iOutdeliveries          = SSDB.getInstance().getOutdeliveries();

        iOrders         = SSOrderMath        .getOrdersWithoutInvoice(iOrders);
        iPurchaseOrders = SSPurchaseOrderMath.getOrdersWithoutInvoice(iPurchaseOrders);

         iInvoices = SSFilterFactory.doFilter(iInvoices, new SSFilter<SSInvoice>() {
            public boolean applyFilter(SSInvoice iInvoice) {
                return iInvoice.isStockInfluencing();
            }
        });

        iCreditInvoices = SSFilterFactory.doFilter(iCreditInvoices, new SSFilter<SSCreditInvoice>() {
            public boolean applyFilter(SSCreditInvoice iCreditInvoice) {
                return iCreditInvoice.isStockInfluencing();
            }
        });

        iPurchaseOrders = SSFilterFactory.doFilter(iPurchaseOrders, new SSFilter<SSPurchaseOrder>() {
            public boolean applyFilter(SSPurchaseOrder iPurchaseOrder) {
                return iPurchaseOrder.isStockInfluencing() ;
            }
        });

        iSupplierInvoices = SSFilterFactory.doFilter(iSupplierInvoices, new SSFilter<SSSupplierInvoice>() {
            public boolean applyFilter(SSSupplierInvoice iSupplierInvoice) {
                return iSupplierInvoice.isStockInfluencing();
            }
        });

        iSupplierCreditInvoices = SSFilterFactory.doFilter(iSupplierCreditInvoices, new SSFilter<SSSupplierCreditInvoice>() {
            public boolean applyFilter(SSSupplierCreditInvoice iSupplierCreditInvoice) {
                return iSupplierCreditInvoice.isStockInfluencing();
            }
        });

        calculate(iOrders, iInvoices, iCreditInvoices, iPurchaseOrders, iSupplierInvoices, iSupplierCreditInvoices, iInventories, iIndeliveries, iOutdeliveries);
    }

    /**
     *
     * @param iDate
     */
    public void update(final Date iDate){
        List<SSOrder>                 iOrders                 = SSDB.getInstance().getOrders() ;
        List<SSInvoice>               iInvoices               = SSDB.getInstance().getInvoices();
        List<SSCreditInvoice>         iCreditInvoices         = SSDB.getInstance().getCreditInvoices();
        List<SSPurchaseOrder>         iPurchaseOrders         = SSDB.getInstance().getPurchaseOrders();
        List<SSSupplierInvoice>       iSupplierInvoices       = SSDB.getInstance().getSupplierInvoices();
        List<SSSupplierCreditInvoice> iSupplierCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();
        List<SSInventory>             iInventories            = SSDB.getInstance().getInventories();
        List<SSIndelivery>            iIndeliveries           = SSDB.getInstance().getIndeliveries();
        List<SSOutdelivery>           iOutdeliveries          = SSDB.getInstance().getOutdeliveries();

        iOrders         = SSOrderMath        .getOrdersWithoutInvoice(iOrders);
        iPurchaseOrders = SSPurchaseOrderMath.getOrdersWithoutInvoice(iPurchaseOrders);

        iOrders = SSFilterFactory.doFilter(iOrders, new SSFilter<SSOrder>() {
            public boolean applyFilter(SSOrder iOrder) {
                return SSOrderMath.inPeriod(iOrder, iDate);
            }
        });

        iInvoices = SSFilterFactory.doFilter(iInvoices, new SSFilter<SSInvoice>() {
            public boolean applyFilter(SSInvoice iInvoice) {
                return iInvoice.isStockInfluencing() && SSInvoiceMath.inPeriod(iInvoice,  iDate);
            }
        });

        iCreditInvoices = SSFilterFactory.doFilter(iCreditInvoices, new SSFilter<SSCreditInvoice>() {
            public boolean applyFilter(SSCreditInvoice iCreditInvoice) {
                return iCreditInvoice.isStockInfluencing() && SSInvoiceMath.inPeriod(iCreditInvoice,  iDate);
            }
        });

        iPurchaseOrders = SSFilterFactory.doFilter(iPurchaseOrders, new SSFilter<SSPurchaseOrder>() {
            public boolean applyFilter(SSPurchaseOrder iPurchaseOrder) {
                return iPurchaseOrder.isStockInfluencing() && SSPurchaseOrderMath.inPeriod(iPurchaseOrder, iDate);
            }
        });

        iSupplierInvoices = SSFilterFactory.doFilter(iSupplierInvoices, new SSFilter<SSSupplierInvoice>() {
            public boolean applyFilter(SSSupplierInvoice iSupplierInvoice) {
                return iSupplierInvoice.isStockInfluencing() && SSSupplierInvoiceMath.inPeriod(iSupplierInvoice, iDate);
            }
        });

        iSupplierCreditInvoices = SSFilterFactory.doFilter(iSupplierCreditInvoices, new SSFilter<SSSupplierCreditInvoice>() {
            public boolean applyFilter(SSSupplierCreditInvoice iSupplierCreditInvoice) {
                return iSupplierCreditInvoice.isStockInfluencing() && SSSupplierInvoiceMath.inPeriod(iSupplierCreditInvoice, iDate);
            }
        });

        iInventories = SSFilterFactory.doFilter(iInventories, new SSFilter<SSInventory>() {
            public boolean applyFilter(SSInventory iInventory) {
                return SSInventoryMath.inPeriod(iInventory,  iDate);
            }
        });

        iIndeliveries = SSFilterFactory.doFilter(iIndeliveries, new SSFilter<SSIndelivery>() {
            public boolean applyFilter(SSIndelivery iIndelivery) {
                return SSIndeliveryMath.inPeriod(iIndelivery,  iDate);
            }
        });

        iOutdeliveries = SSFilterFactory.doFilter(iOutdeliveries, new SSFilter<SSOutdelivery>() {
            public boolean applyFilter(SSOutdelivery iOutdelivery) {
                return SSOutdeliveryMath.inPeriod(iOutdelivery,  iDate);
            }
        });


        calculate(iOrders, iInvoices, iCreditInvoices, iPurchaseOrders, iSupplierInvoices, iSupplierCreditInvoices, iInventories, iIndeliveries, iOutdeliveries);
    }


    /**
     *
     * @param iFrom
     * @param iTo
     */
    public void update(final Date iFrom, final Date iTo){
        List<SSOrder>                 iOrders                 = SSDB.getInstance().getOrders() ;
        List<SSInvoice>               iInvoices               = SSDB.getInstance().getInvoices();
        List<SSCreditInvoice>         iCreditInvoices         = SSDB.getInstance().getCreditInvoices();
        List<SSPurchaseOrder>         iPurchaseOrders         = SSDB.getInstance().getPurchaseOrders();
        List<SSSupplierInvoice>       iSupplierInvoices       = SSDB.getInstance().getSupplierInvoices();
        List<SSSupplierCreditInvoice> iSupplierCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();
        List<SSInventory>             iInventories            = SSDB.getInstance().getInventories();
        List<SSIndelivery>            iIndeliveries           = SSDB.getInstance().getIndeliveries();
        List<SSOutdelivery>           iOutdeliveries          = SSDB.getInstance().getOutdeliveries();

        iOrders         = SSOrderMath        .getOrdersWithoutInvoice(iOrders);
        iPurchaseOrders = SSPurchaseOrderMath.getOrdersWithoutInvoice(iPurchaseOrders);

        iOrders = SSFilterFactory.doFilter(iOrders, new SSFilter<SSOrder>() {
            public boolean applyFilter(SSOrder iOrder) {
                return SSOrderMath.inPeriod(iOrder, iFrom, iTo);
            }
        });

        iInvoices = SSFilterFactory.doFilter(iInvoices, new SSFilter<SSInvoice>() {
            public boolean applyFilter(SSInvoice iInvoice) {
                return iInvoice.isStockInfluencing() && SSInvoiceMath.inPeriod(iInvoice,  iFrom, iTo);
            }
        });

        iCreditInvoices = SSFilterFactory.doFilter(iCreditInvoices, new SSFilter<SSCreditInvoice>() {
            public boolean applyFilter(SSCreditInvoice iCreditInvoice) {
                return iCreditInvoice.isStockInfluencing() && SSInvoiceMath.inPeriod(iCreditInvoice,  iFrom, iTo);
            }
        });

        iPurchaseOrders = SSFilterFactory.doFilter(iPurchaseOrders, new SSFilter<SSPurchaseOrder>() {
            public boolean applyFilter(SSPurchaseOrder iPurchaseOrder) {
                return iPurchaseOrder.isStockInfluencing() && SSPurchaseOrderMath.inPeriod(iPurchaseOrder,  iFrom, iTo);
            }
        });

        iSupplierInvoices = SSFilterFactory.doFilter(iSupplierInvoices, new SSFilter<SSSupplierInvoice>() {
            public boolean applyFilter(SSSupplierInvoice iSupplierInvoice) {
                return iSupplierInvoice.isStockInfluencing() && SSSupplierInvoiceMath.inPeriod(iSupplierInvoice,  iFrom, iTo);
            }
        });

        iSupplierCreditInvoices = SSFilterFactory.doFilter(iSupplierCreditInvoices, new SSFilter<SSSupplierCreditInvoice>() {
            public boolean applyFilter(SSSupplierCreditInvoice iSupplierCreditInvoice) {
                return iSupplierCreditInvoice.isStockInfluencing() && SSSupplierInvoiceMath.inPeriod(iSupplierCreditInvoice,  iFrom, iTo);
            }
        });

        iInventories = SSFilterFactory.doFilter(iInventories, new SSFilter<SSInventory>() {
            public boolean applyFilter(SSInventory iInventory) {
                return SSInventoryMath.inPeriod(iInventory,  iFrom, iTo);
            }
        });

        iIndeliveries = SSFilterFactory.doFilter(iIndeliveries, new SSFilter<SSIndelivery>() {
            public boolean applyFilter(SSIndelivery iIndelivery) {
                return SSIndeliveryMath.inPeriod(iIndelivery,  iFrom, iTo);
            }
        });

        iOutdeliveries = SSFilterFactory.doFilter(iOutdeliveries, new SSFilter<SSOutdelivery>() {
            public boolean applyFilter(SSOutdelivery iOutdelivery) {
                return SSOutdeliveryMath.inPeriod(iOutdelivery,  iFrom, iTo);
            }
        });


        calculate(iOrders, iInvoices, iCreditInvoices, iPurchaseOrders, iSupplierInvoices, iSupplierCreditInvoices, iInventories, iIndeliveries, iOutdeliveries);
    }




    /**
     *
     * @param iOrders
     * @param iInvoices
     * @param iCreditInvoices
     * @param iPurchaseOrders
     * @param iSupplierInvoices
     * @param iSupplierCreditInvoices
     * @param iIndeliveries
     * @param iOutdeliveries
     */
    private void calculate(List<SSOrder> iOrders, List<SSInvoice> iInvoices, List<SSCreditInvoice> iCreditInvoices, List<SSPurchaseOrder> iPurchaseOrders, List<SSSupplierInvoice> iSupplierInvoices, List<SSSupplierCreditInvoice> iSupplierCreditInvoices, List<SSInventory> iInventories, List<SSIndelivery> iIndeliveries, List<SSOutdelivery> iOutdeliveries){
        List<SSProduct> iProducts = new LinkedList<SSProduct>(SSDB.getInstance().getProducts());

        Map<String, Integer> iOrderCount = SSOrderMath.getStockInfluencing(iOrders);

        Map<String, Integer> iInvoiceCount = SSInvoiceMath.getStockInfluencing(iInvoices);

        Map<String, Integer> iCreditInvoiceCount = SSCreditInvoiceMath.getStockInfluencing(iCreditInvoices);

        Map<String, Integer> iPurchaseOrderCount = SSPurchaseOrderMath.getStockInfluencing(iPurchaseOrders);

        Map<String, Integer> iSupplierInvoiceCount = SSSupplierInvoiceMath.getStockInfluencing(iSupplierInvoices);

        Map<String, Integer> iSupplierCreditInvoiceCount = SSSupplierCreditInvoiceMath.getStockInfluencing(iSupplierCreditInvoices);

        Map<String, Integer> iInventoryCount = SSInventoryMath.getStockInfluencing(iInventories);

        Map<String, Integer> iIndeliveryCount = SSIndeliveryMath.getStockInfluencing(iIndeliveries);

        Map<String, Integer> iOutdeliveryCount = SSOutdeliveryMath.getStockInfluencing(iOutdeliveries);

        for (SSProduct iProduct : iProducts) {

            if(!iProduct.isStockProduct()) continue;

            Integer iQuantity = 0;
            Integer iReserved = 0;
            Integer iOrdered  = 0;

            // Order: + reserverad om den ej är fakturerad
            /*for (SSOrder iOrder : iOrders) {
                iReserved = iReserved + SSOrderMath.getProductCount(iOrder, iProduct);
            }*/
            iReserved = iOrderCount.get(iProduct.getNumber()) == null ? iReserved : iReserved + iOrderCount.get(iProduct.getNumber());

            // Faktura: - på lagret
            /*for (SSInvoice iInvoice : iInvoices) {
                iQuantity = iQuantity - SSSaleMath.getProductCount(iInvoice, iProduct);
            }*/
            iQuantity = iInvoiceCount.get(iProduct.getNumber()) == null ? iQuantity : iQuantity - iInvoiceCount.get(iProduct.getNumber());

            // Kreditfaktura: + på lagret
            /*for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
                iQuantity = iQuantity + SSSaleMath.getProductCount(iCreditInvoice,  iProduct);
            }*/
            iQuantity = iCreditInvoiceCount.get(iProduct.getNumber()) == null ? iQuantity : iQuantity + iCreditInvoiceCount.get(iProduct.getNumber());

            // Inköpsorder: + Ordered om ej fakturerad
            /*for (SSPurchaseOrder iPurchaseOrder : iPurchaseOrders) {
                iOrdered = iOrdered + SSPurchaseOrderMath.getProductCount(iPurchaseOrder,  iProduct);
            }*/
            iOrdered = iPurchaseOrderCount.get(iProduct.getNumber()) == null ? iOrdered : iOrdered + iPurchaseOrderCount.get(iProduct.getNumber());

            // Leverantörsfakturor: + på lagret
            /*for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {
                iQuantity = iQuantity + SSSupplierInvoiceMath.getProductCount(iSupplierInvoice,  iProduct);
            }*/
            iQuantity = iSupplierInvoiceCount.get(iProduct.getNumber()) == null ? iQuantity : iQuantity + iSupplierInvoiceCount.get(iProduct.getNumber());

            // Leverantörskreditfaktura: - på lagret
            /*for (SSSupplierCreditInvoice iSupplierCreditInvoice : iSupplierCreditInvoices) {
                iQuantity = iQuantity - SSSupplierInvoiceMath.getProductCount(iSupplierCreditInvoice,  iProduct);
            }*/
            iQuantity = iSupplierCreditInvoiceCount.get(iProduct.getNumber()) == null ? iQuantity : iQuantity - iSupplierCreditInvoiceCount.get(iProduct.getNumber());

            // Inventering + på lager
            /*for (SSInventory iInventory : iInventories) {
                iQuantity = iQuantity + iInventory.getChange(iProduct);
            }*/
            iQuantity = iInventoryCount.get(iProduct.getNumber()) == null ? iQuantity : iQuantity + iInventoryCount.get(iProduct.getNumber());

            // Inleverans + på lager
            /*for (SSIndelivery iIndelivery : iIndeliveries) {
                iQuantity = iQuantity + iIndelivery.getChange(iProduct);
            }*/
            iQuantity = iIndeliveryCount.get(iProduct.getNumber()) == null ? iQuantity : iQuantity + iIndeliveryCount.get(iProduct.getNumber());

            // Utleverans - på lager
            /*for (SSOutdelivery iOutdelivery : iOutdeliveries) {
                iQuantity = iQuantity - iOutdelivery.getChange(iProduct);
            }*/
            iQuantity = iOutdeliveryCount.get(iProduct.getNumber()) == null ? iQuantity : iQuantity - iOutdeliveryCount.get(iProduct.getNumber());

            this.iQuantity.put(iProduct, iQuantity);
            this.iOrdered .put(iProduct, iOrdered);
            this.iReserved.put(iProduct, iReserved);
        }

        iProducts = null;
        iOrderCount = null;
        iInvoiceCount = null;
        iCreditInvoiceCount = null;
        iPurchaseOrderCount = null;
        iSupplierInvoiceCount = null;
        iSupplierCreditInvoiceCount = null;
        iInventoryCount = null;
        iIndeliveryCount = null;
        iOutdeliveryCount = null;
    }


    /**
     * Returns the current number of products in the stock
     *
     * @param iProduct
     * @return the quantity
     */
    public Integer getQuantity(SSProduct iProduct){
        return iQuantity.containsKey(iProduct) ? iQuantity.get(iProduct) : 0;
    }

    /**
     * Returns the current number of ordered products in the stock
     *
     * @param iProduct
     * @return the num ordered
     */
    public Integer getOrdered(SSProduct iProduct){
        return iOrdered.containsKey(iProduct) ? iOrdered.get(iProduct) : 0;
    }

    /**
     * Returns the current number of reserved products
     *
     * @param iProduct
     * @return the num reserved
     */
    public Integer getReserved(SSProduct iProduct){
        return iReserved.containsKey(iProduct) ? iReserved.get(iProduct) : 0;
    }

    /**
     *
     * @param iProduct
     * @return
     */
    public Integer getAvaiable(SSProduct iProduct) {
        return getQuantity(iProduct) - getReserved(iProduct);
    }
}

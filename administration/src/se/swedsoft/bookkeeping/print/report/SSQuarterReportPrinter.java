package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.report.sales.SSSalePrinterUtils;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSCustomerMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;
import java.text.DateFormatSymbols;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSQuarterReportPrinter extends SSPrinter {

    private Locale iLocale;

    private Date iFrom;

    private Date iTo;

    private List<SSCustomer> iCustomers;

    private Map<SSCustomer, BigDecimal> iEuSaleCommodity;

    private Map<SSCustomer, BigDecimal> iEuSaleThirdPartCommodity;

    /**
     *
     */
    public SSQuarterReportPrinter( Locale iLocale, Date iFrom, Date iTo){
        super();
        // Get all orders
        this.iCustomers = SSDB.getInstance().getCustomers();
        this.iLocale    = iLocale;
        this.iFrom      = iFrom;
        this.iTo        = iTo;

        ResourceBundle iBundle = ResourceBundle.getBundle("se.swedsoft.bookkeeping.resources.quarterreport");//", iLocale);

        setBundle( iBundle );
        setLocale( iLocale );

        setPageHeader  ("quarterreport.jrxml");
        setDetail      ("quarterreport.jrxml");
        setColumnHeader("quarterreport.jrxml");
        setSummary     ("quarterreport.jrxml");
        setPageFooter  ("quarterreport.jrxml");


        addParameters();

        iEuSaleCommodity          = new HashMap<SSCustomer, BigDecimal>();
        iEuSaleThirdPartCommodity = new HashMap<SSCustomer, BigDecimal>();

        Map<String,List<SSInvoice>> iInvoicesForCustomers = SSInvoiceMath.getInvoicesforCustomers();
        Map<String,List<SSCreditInvoice>> iCreditInvoicesForCustomers = SSCreditInvoiceMath.getCreditInvoicesforCustomers();

        //List<SSInvoice>       iInvoices       = SSDB.getInstance().getInvoices();
        //List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();



        for (SSCustomer iCustomer : iCustomers) {
            List<SSInvoice>       iInvoicesForCustomer       = iInvoicesForCustomers.get(iCustomer.getNumber());
            List<SSCreditInvoice> iCreditInvoicesForCustomer = iCreditInvoicesForCustomers.get(iCustomer.getNumber());

            iEuSaleCommodity         .put(iCustomer, new BigDecimal(0.0));
            iEuSaleThirdPartCommodity.put(iCustomer, new BigDecimal(0.0));

            if(iInvoicesForCustomer != null){
                for (SSInvoice iInvoice : iInvoicesForCustomer) {

                    if( ! SSInvoiceMath.inPeriod(iInvoice,  iFrom, iTo) ) continue;

                    BigDecimal iTotalSum = SSInvoiceMath.getTotalSum(iInvoice);

                    iTotalSum = SSInvoiceMath.convertToLocal(iInvoice,  iTotalSum);

                    if(iInvoice.getEuSaleCommodity()){
                        BigDecimal iSum = iEuSaleCommodity.get(iCustomer);

                        iEuSaleCommodity.put(iCustomer, iSum.add( iTotalSum ));
                    }
                    if(iInvoice.getEuSaleThirdPartCommodity()){
                        BigDecimal iSum = iEuSaleThirdPartCommodity.get(iCustomer);

                        iEuSaleThirdPartCommodity.put(iCustomer, iSum.add( iTotalSum ));
                    }
                }
            }
            if(iCreditInvoicesForCustomer != null){
                for (SSCreditInvoice iCreditInvoice : iCreditInvoicesForCustomer) {

                    if( ! SSCreditInvoiceMath.inPeriod(iCreditInvoice,  iFrom, iTo) ) continue;

                    BigDecimal iTotalSum = SSCreditInvoiceMath.getTotalSum(iCreditInvoice);
    
                    iTotalSum = SSCreditInvoiceMath.convertToLocal(iCreditInvoice,  iTotalSum);

                    if(iCreditInvoice.getEuSaleCommodity()){
                        BigDecimal iSum = iEuSaleCommodity.get(iCustomer);

                        iEuSaleCommodity.put(iCustomer, iSum.subtract(iTotalSum ));
                    }

                    if(iCreditInvoice.getEuSaleThirdPartCommodity()){
                        BigDecimal iSum = iEuSaleThirdPartCommodity.get(iCustomer);

                        iEuSaleThirdPartCommodity.put(iCustomer, iSum.subtract( iTotalSum ));
                    }
                }
            }
        }
    }





    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return iBundle.getString("quarterreport.title");
    }

    /**
     *
     */
    private void addParameters(){
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSSalePrinterUtils.addParametersForCompany(iCompany, this);



        // Sale parameters
        addParameter("number"   , iCompany.getVATNumber());
        addParameter("date"     , getPeriodText() );
        addParameter("quater"   , getQuarterText() );
    }

    /**
     *
     * @return
     */
    private String getQuarterText() {
        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iFrom);

        String iYear  = Integer.toString( iCalendar.get(Calendar.YEAR )    ).substring(2);
        String iMonth = Integer.toString( iCalendar.get(Calendar.MONTH) / 3 + 1 );


        return iYear + "-" + iMonth;
    }

    /**
     *
     * @return
     */
    private String getPeriodText(){
        String [] iMonths = new DateFormatSymbols().getMonths();

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iFrom);

        String iMonthFrom = iMonths[ iCalendar.get(Calendar.MONTH)];

        iCalendar.setTime(iTo);

        String iMonthTo = iMonths[ iCalendar.get(Calendar.MONTH)];

        String iYear = Integer.toString(iCalendar.get(Calendar.YEAR));

        return iMonthFrom + " - " + iMonthTo + " " + iYear;

    }


    /**
     *
     * @return
     */
    @Override
    protected SSDefaultTableModel getModel() {

        SSDefaultTableModel<SSCustomer> iModel = new SSDefaultTableModel<SSCustomer>() {


            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSCustomer iCustomer = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iCustomer.getNumber();
                        break;
                    case 1:
                        value = iCustomer.getName();
                        break;
                    case 2:
                        value = iCustomer.getVATNumber();
                        break;
                    case 3:
                        value = iEuSaleCommodity.get(iCustomer);
                        break;
                    case 4:
                        value = iEuSaleThirdPartCommodity.get(iCustomer);
                        break;
                }
                return value;
            }
        };

        iModel.addColumn("customer.number");
        iModel.addColumn("customer.name");
        iModel.addColumn("customer.vatnumber");
        iModel.addColumn("customer.eusalecommodity");
        iModel.addColumn("customer.eusalethirdpartcommodity");


        Collections.sort(iCustomers, new Comparator<SSCustomer>() {
            public int compare(SSCustomer o1, SSCustomer o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });

        iModel.setObjects(iCustomers);


        return iModel;
    }






}

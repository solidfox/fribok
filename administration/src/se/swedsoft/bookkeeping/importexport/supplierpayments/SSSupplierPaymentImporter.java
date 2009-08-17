package se.swedsoft.bookkeeping.importexport.supplierpayments;

import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.PaymentMethod;
import se.swedsoft.bookkeeping.importexport.supplierpayments.poster.*;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.SSOutpayment;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSOutpaymentRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DateFormat;

/**
 * User: Andreas Lago
 * Date: 2006-aug-28
 * Time: 11:57:35
 */
public class SSSupplierPaymentImporter {

    /**
     *
     * @param iFile
     */
    public static List<SSOutpayment> Import(File iFile) throws SSImportException {
        List<String> iLines = new LinkedList<String>();

        try {
            BufferedReader iReader = new BufferedReader( new FileReader(iFile) );

            String iLine;
            while( (iLine = iReader.readLine()) != null){
                iLines.add(iLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<LBinPost> iPosts = new LinkedList<LBinPost>();
        for (String iLine : iLines) {

            LBinLine iReader = new LBinLine(iLine);

            String iPostTyp = iReader.readString(1, 2);

            LBinPost iPost = getPost(iPostTyp);

            if(iPost != null){
                //      System.out.println(iLine);
                iPost.read(iReader);

                iPosts.add(iPost);
            } else {
                System.out.println("Oidentifierad posttyp: " + iPostTyp);
            }
        }
        List<SSOutpayment> iOutpayments = new LinkedList<SSOutpayment>();

        SSOutpayment iOutpayment = null;
        SSSupplierInvoice iSupplierInvoice;

        for (LBinPost iPost : iPosts) {
            if(iPost instanceof LBinPostTK11){
                LBinPostTK11 iPostTK11 = (LBinPostTK11) iPost;

                Date iPaymentDate = iPostTK11.getPaymentDate();

                DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);


                iOutpayment = new SSOutpayment();
                iOutpayment.setText("Leverantörsbetalning");
                iOutpayment.setDate( iPaymentDate );
                iOutpayment.setText("Leverantörsbetalning " + iFormat.format(iPaymentDate));

                iOutpayments.add(iOutpayment);
            }

            if(iOutpayment == null) continue;

            if(iPost instanceof LBinPostTK14){
                LBinPostTK14 iPostTK14 = (LBinPostTK14) iPost;

                String  iReference  = iPostTK14.getReference().trim();
                Integer iInvoiceNr  = iPostTK14.getInvoiceNr();

                iSupplierInvoice = SSSupplierInvoiceMath.getSupplierInvoiceByNumber( SSDB.getInstance().getSupplierInvoices(), iInvoiceNr );

                if(iSupplierInvoice == null){
                    iSupplierInvoice = SSSupplierInvoiceMath.getSupplierInvoiceByReference( SSDB.getInstance().getSupplierInvoices(), iReference );
                }

                if( iSupplierInvoice !=  null){
                    SSOutpaymentRow iRow = new SSOutpaymentRow();

                    iRow.setSupplierInvoice(iSupplierInvoice);

                    iRow.setValue( iPostTK14.getValue()  );

                    iOutpayment.getRows().add(iRow);

                }  else {
                    throw new SSImportException(SSBundle.getBundle(), "supplierpaymentimport.error.invalidreference", iReference);
                }

            }

            if(iPost instanceof LBinPostTK54){
                LBinPostTK54 iPostTK54 = (LBinPostTK54) iPost;

                String  iReference  = iPostTK54.getReference().trim();
                Integer iInvoiceNr  = iPostTK54.getInvoiceNr();

                iSupplierInvoice = SSSupplierInvoiceMath.getSupplierInvoiceByNumber( SSDB.getInstance().getSupplierInvoices(), iInvoiceNr );

                if(iSupplierInvoice == null){
                    iSupplierInvoice = SSSupplierInvoiceMath.getSupplierInvoiceByReference( SSDB.getInstance().getSupplierInvoices(), iReference );
                }

                if( iSupplierInvoice !=  null){
                    SSOutpaymentRow iRow = new SSOutpaymentRow();

                    iRow.setSupplierInvoice(iSupplierInvoice);

                    iRow.setValue( iPostTK54.getValue()  );

                    iOutpayment.getRows().add(iRow);

                }  else {
                    throw new SSImportException(SSBundle.getBundle(), "supplierpaymentimport.error.invalidreference", iReference);
                }

            }
        }
        return iOutpayments;
    }



    /**
     *
     * @param iPostTyp
     * @return
     */
    private static LBinPost getPost(String iPostTyp) {
        if( iPostTyp.equals("11") ) return new LBinPostTK11();
        if( iPostTyp.equals("12") ) return new LBinPostTK12();
        if( iPostTyp.equals("13") ) return new LBinPostTK13();
        if( iPostTyp.equals("14") ) return new LBinPostTK14();
        if( iPostTyp.equals("26") ) return new LBinPostTK26();
        if( iPostTyp.equals("27") ) return new LBinPostTK27();
        if( iPostTyp.equals("29") ) return new LBinPostTK29();
        if( iPostTyp.equals("54") ) return new LBinPostTK54();

        return null;
    }


}

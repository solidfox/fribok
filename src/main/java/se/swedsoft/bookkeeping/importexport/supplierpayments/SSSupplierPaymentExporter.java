package se.swedsoft.bookkeeping.importexport.supplierpayments;


import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.PaymentMethod;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.supplierpayments.poster.*;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-aug-28
 * Time: 11:57:35
 */
public class SSSupplierPaymentExporter {
    private SSSupplierPaymentExporter() {}

    /**
     *
     * @param iFile
     * @param iPayments
     * @throws SSExportException
     */
    public static void Export(File iFile, List<SupplierPayment> iPayments) throws SSExportException {

        List<LBinPost> iLines = getPosts(iPayments);

        try {
            BufferedWriter iWriter = new BufferedWriter(new FileWriter(iFile));

            for (LBinPost iPost : iLines) {
                // Make shure we dont add any empty posts
                if (!iPost.isEmpty()) {
                    LBinLine iLine = new LBinLine(80);

                    iPost.write(iLine);

                    iWriter.write(iLine.toString());
                    iWriter.newLine();
                }
            }
            iWriter.flush();
            iWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param iPayments
     * @return
     */
    public static List<LBinPost> getPosts(List<SupplierPayment> iPayments) {
        List<LBinPost> iPosts = new LinkedList<LBinPost>();

        iPosts.addAll(getAvsnitt(iPayments, "SEK"));
        iPosts.addAll(getAvsnitt(iPayments, "EUR"));

        return iPosts;

    }

    /**
     *
     * @param iPayments
     * @param iCurrency
     * @return
     */
    public static List<LBinPost> getAvsnitt(List<SupplierPayment> iPayments, String iCurrency) {
        List<SupplierPayment> iFiltered = SupplierPayment.getPayments(iPayments, iCurrency);

        List<LBinPost> iPosts = new LinkedList<LBinPost>();

        // Cant add empty sections
        if (iPayments.isEmpty()) {
            return iPosts;
        }

        iPosts.add(new LBinPostTK11(iCurrency));
        iPosts.add(new LBinPostTK12());
        iPosts.add(new LBinPostTK13());

        BigDecimal iSum = new BigDecimal(0);

        // Loop through all payments for this currency
        for (SupplierPayment iPayment : iFiltered) {

            PaymentMethod iPaymentMethod = iPayment.getPaymentMethod();

            // Bangiro
            if (iPaymentMethod == PaymentMethod.BANKGIRO) {
                iPosts.add(new LBinPostTK14(iPayment, iPayment.getBankGiro()));
            }
            // Plusgiro
            if (iPaymentMethod == PaymentMethod.PLUSGIRO) {
                if (!iCurrency.equals("SEK")) {
                    throw new SSExportException(SSBundle.getBundle(),
                            "supplierpaymentframe.error.plusgirocurrency");
                }

                iPosts.add(new LBinPostTK54(iPayment));
            }
            // Utbetalningskort
            if (iPaymentMethod == PaymentMethod.CASH) {

                iPosts.add(
                        new LBinPostTK14(iPayment, iPayment.getOutpaymentNumber() + " "));

                iPosts.add(new LBinPostTK26(iPayment));
                iPosts.add(new LBinPostTK27(iPayment));

            }
	    // TK40 - fixme!  Bankkonto- eller löneinsättning
            if (iPaymentMethod == PaymentMethod.KONTO) {

                iPosts.add(
                        new LBinPostTK14(iPayment, iPayment.getBankGiro()));

                iPosts.add(new LBinPostTK40(iPayment));
            }
            iSum = iSum.add(iPayment.getValue());
        }

        iPosts.add(new LBinPostTK29(iFiltered.size(), iSum));

        return iPosts;

    }

}

package se.swedsoft.bookkeeping.importexport.bgmax;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.SSInpaymentRow;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxAvsnitt;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxBetalning;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxFile;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxReferens;
import se.swedsoft.bookkeeping.importexport.bgmax.dialog.BgMaxSelectInvoiceDialog;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-22
 * Time: 15:00:47
 */
public class SSBgMaxImporter {
    private SSBgMaxImporter() {
    }


    /**
     *
     * @param iFile
     * @throws SSImportException
     * @return
     */
    public static BgMaxFile Import(File iFile) throws SSImportException {

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

        BgMaxFile iBgMaxFile = new BgMaxFile();

        iBgMaxFile.parse(iLines);

        System.out.println(iBgMaxFile);

        return iBgMaxFile;
    }

    /**
     *
     * @param iMainFrame
     * @param iBgMaxFile
     * @return
     */
    public static List<SSInpayment> getInpayments(SSMainFrame iMainFrame, BgMaxFile iBgMaxFile){
        BgMaxSelectInvoiceDialog iDialog = new BgMaxSelectInvoiceDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        DateFormat iDateFormat = new SimpleDateFormat("yyyyMMdd");

        List<SSInpayment> iInpayments = new LinkedList<SSInpayment>();

        for (BgMaxAvsnitt iAvsnitt : iBgMaxFile.iAvsnitts) {
            SSInpayment iInpayment = new SSInpayment();

            iInpayment.setText("Bankgiro inbetalning " + iAvsnitt.iLopnummer);
            try {
                iInpayment.setDate( iDateFormat.parse(iAvsnitt.iBetalningsdag) );
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (BgMaxBetalning iBetalning : iAvsnitt.iBetalningar) {

                SSInvoice iInvoice = SSInvoiceMath.getInvoiceByReference(iBetalning.iReferens);

                if(iInvoice == null && !iBetalning.iReferenser.isEmpty()){
                    for (BgMaxReferens iReferens : iBetalning.iReferenser) {
                         iInvoice = SSInvoiceMath.getInvoiceByReference(iReferens.iReferens);
                    }
                }
                if(iInvoice == null){
                    int iResponce = iDialog.showDialog(iBetalning);
                    if( iResponce != JOptionPane.OK_OPTION ) return null;

                    iInvoice = iDialog.getInvoice();
                }
                SSInpaymentRow iInpaymentRow = new SSInpaymentRow();
                iInpaymentRow.setCurrencyRate( new BigDecimal(1.0));
                iInpaymentRow.setInvoice     ( iInvoice );
                iInpaymentRow.setValue       ( iBetalning.getBelopp() );

                iInpayment.getRows().add(iInpaymentRow);


            }

            iInpayments.add(iInpayment);
        }
        return iInpayments;

    }




}

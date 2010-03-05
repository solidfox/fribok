package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 15:42:39
 */
public class SSTenderMath extends SSSaleMath {




    /**
     * Creates a new tender with the number as the lastest tender number + 1
     *
     * @return The tender
     */
    public static SSTender newTender(){
        SSTender iTender  = new SSTender();

        //iTender.doAutoIncrecement();

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            iTender.setDelayInterest   ( iCompany.getDelayInterest()   );
            iTender.setText            ( iCompany.getStandardText( SSStandardText.Tender ));
            iTender.setTaxRate1        ( iCompany.getTaxRate1()   );
            iTender.setTaxRate2        ( iCompany.getTaxRate2()   );
            iTender.setTaxRate3        ( iCompany.getTaxRate3()   );
            iTender.setDefaultAccounts ( iCompany.getDefaultAccounts());
            iTender.setOurContactPerson( iCompany.getContactPerson  () );
            iTender.setPaymentTerm     ( iCompany.getPaymentTerm());
            iTender.setDeliveryTerm    ( iCompany.getDeliveryTerm());
            iTender.setDeliveryWay     ( iCompany.getDeliveryWay());
            iTender.setCurrency        ( iCompany.getCurrency());
        }

        return iTender;
    }




}

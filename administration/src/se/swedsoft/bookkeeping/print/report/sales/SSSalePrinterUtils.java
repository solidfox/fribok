package se.swedsoft.bookkeeping.print.report.sales;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.print.SSPrinter;

/**
 * User: Andreas Lago
 * Date: 2006-maj-22
 * Time: 16:49:39
 */
public class SSSalePrinterUtils {
    private SSSalePrinterUtils() {
    }

    /**
     *
     * @param iCompany
     * @param iPrinter
     */
    public static void addParametersForCompany(SSNewCompany iCompany, SSPrinter iPrinter){
        // Company parameters
        iPrinter.addParameter("company.logo", iCompany.getLogoImage() );

        iPrinter.addParameter("company.name"     , iCompany.getName() );
        iPrinter.addParameter("company.address1" , iCompany.getAddress().getAddress1() );
        iPrinter.addParameter("company.address2" , iCompany.getAddress().getAddress2() );
        iPrinter.addParameter("company.zipcode"  , iCompany.getAddress().getZipCode() );
        iPrinter.addParameter("company.city"     , iCompany.getAddress().getCity() );
        iPrinter.addParameter("company.country"  , iCompany.getAddress().getCountry() );
        iPrinter.addParameter("company.phone"    , iCompany.getPhone() );
        iPrinter.addParameter("company.telefax"  , iCompany.getTelefax() );
        iPrinter.addParameter("company.residence", iCompany.getResidence() );
        iPrinter.addParameter("company.email"    , iCompany.getEMail() );
        iPrinter.addParameter("company.homepage" , iCompany.getHomepage() );
        iPrinter.addParameter("company.reminderfee"   , iCompany.getReminderfee() );
        iPrinter.addParameter("company.delayinterest" , iCompany.getDelayInterest() );


        iPrinter.addParameter("company.plusaccount" , iCompany.getPlusGiroNumber() );
        iPrinter.addParameter("company.bankaccount" , iCompany.getBankGiroNumber() );

        iPrinter.addParameter("company.bic"          , iCompany.getBIC() );
        iPrinter.addParameter("company.iban"         , iCompany.getIBAN() );
        iPrinter.addParameter("company.bank"         , iCompany.getBank() );
        iPrinter.addParameter("company.taxregistered", iCompany.getTaxRegistered() );

        iPrinter.addParameter("company.corporateid" , iCompany.getCorporateID() );
        iPrinter.addParameter("company.vatnr"       , iCompany.getVATNumber() );

        iPrinter.addParameter("company.weightunit"       , iCompany.getWeightUnit() );
        iPrinter.addParameter("company.volumeunit"       , iCompany.getVolumeUnit() );

    }
}

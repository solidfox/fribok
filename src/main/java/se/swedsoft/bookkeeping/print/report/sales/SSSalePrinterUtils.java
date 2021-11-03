package se.swedsoft.bookkeeping.print.report.sales;

import com.google.zxing.WriterException;
import org.fribok.bookkeeping.app.Path;
import org.fribok.bookkeeping.data.util.CreateQRCode;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.print.SSPrinter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * $Id$
 */
public class SSSalePrinterUtils {
    private SSSalePrinterUtils() {}

    /**
     *
     * @param iCompany
     * @param iPrinter
     */
    public static void addParametersForCompany(SSNewCompany iCompany, SSPrinter iPrinter) {
        // Company parameters
        iPrinter.addParameter("company.logo", iCompany.getLogoImage());

        iPrinter.addParameter("company.name", iCompany.getName());
        iPrinter.addParameter("company.address1", iCompany.getAddress().getAddress1());
        iPrinter.addParameter("company.address2", iCompany.getAddress().getAddress2());
        iPrinter.addParameter("company.zipcode", iCompany.getAddress().getZipCode());
        iPrinter.addParameter("company.city", iCompany.getAddress().getCity());
        iPrinter.addParameter("company.country", iCompany.getAddress().getCountry());
        iPrinter.addParameter("company.phone", iCompany.getPhone());
        iPrinter.addParameter("company.telefax", iCompany.getTelefax());
        iPrinter.addParameter("company.residence", iCompany.getResidence());
        iPrinter.addParameter("company.email", iCompany.getEMail());
        iPrinter.addParameter("company.homepage", iCompany.getHomepage());
        iPrinter.addParameter("company.reminderfee", iCompany.getReminderfee());
        iPrinter.addParameter("company.delayinterest", iCompany.getDelayInterest());

        iPrinter.addParameter("company.plusaccount", iCompany.getPlusGiroNumber());
        iPrinter.addParameter("company.bankaccount", iCompany.getBankGiroNumber());

        iPrinter.addParameter("company.bic", iCompany.getBIC());
        iPrinter.addParameter("company.iban", iCompany.getIBAN());
        iPrinter.addParameter("company.bank", iCompany.getBank());
        iPrinter.addParameter("company.taxregistered", iCompany.getTaxRegistered());

        iPrinter.addParameter("company.corporateid", iCompany.getCorporateID());
        iPrinter.addParameter("company.vatnr", iCompany.getVATNumber());

        iPrinter.addParameter("company.weightunit", iCompany.getWeightUnit());
        iPrinter.addParameter("company.volumeunit", iCompany.getVolumeUnit());

    }

    /**
     * 
     * @param urqData
     * @param iPrinter
     */
    public static void addParameterForQRCode(final String uqrData, SSPrinter iPrinter) {
//SSNewCompany iCompany,
//SSInvoice iInvoice,
        File uqrFileDir = new File(Path.get(Path.APP_DATA), "qrcode");
        String iFileName = "qrkod-faktura.png";
        if (!uqrFileDir.exists()) {
            uqrFileDir.mkdirs();
        }
        File uqrFile = new File(uqrFileDir, iFileName);

        try {
            CreateQRCode.createQRCode(uqrData, uqrFile, 200, 200);
        } catch (WriterException we) {
            System.out.println("Something in the uqr data cannot be encoded as QR-code: " + uqrData.toString());
        } catch (UnsupportedEncodingException uee) {
            System.out.println("Unexpected character encoding: " + uqrData.toString());
        }

        iPrinter.addParameter("invoice.qrcode", SSSalePrinterUtils.getImage(uqrFile));
    }

    /**
     * @param iImageFile
     * @return BufferedImage
     */
    public static BufferedImage getImage(final File iImageFile) {
        // The logotype is null
        if (iImageFile == null) {
            return null;
        }

        if (!iImageFile.exists()) {
            return null;
        }

        BufferedImage iImage = null;

        try {
            iImage = ImageIO.read(iImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iImage;

    }

    
    public static String getPrimaryPaymentMethod(final SSNewCompany iCompany) {
        // fixme! - Check SE, NO, DK
        if (!"".equals(iCompany.getBankGiroNumber())) {
            return "BG";
        } else if (!"".equals(iCompany.getPlusGiroNumber())) {
            return "PG";
        } else if (!"".equals(iCompany.getIBAN())) {
            if ("".equals(iCompany.getBIC())) {
                return "BBAN";
            }
            return "IBAN";
        }
        return "IBAN";
    }
    
    public static String getPrimaryPaymentAccount(final SSNewCompany iCompany) {
        // fixme! - Check SE, NO, DK
        if (!"".equals(iCompany.getBankGiroNumber())) {
            return iCompany.getBankGiroNumber();
        } else if (!"".equals(iCompany.getPlusGiroNumber())) {
            return iCompany.getPlusGiroNumber();
        }
        return iCompany.getIBAN();
    }

}

package org.fribok.bookkeeping.data.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
* Use zxing to create QR code of usingqr data for invoices. 
*
* $Id$
*/
public class CreateQRCode {
    /**
     *
     * @param uqrData
     * @param charset
     * @param iFile
     * @param height
     * @param width
     */
    public static void createQRCode(final String uqrData, final File iFile, final int height, final int width) throws WriterException, UnsupportedEncodingException {

        Map<EncodeHintType, ErrorCorrectionLevel> encodeHintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        encodeHintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        // fixme! - We go UTF-8 here specifically (due to spec) from windows-1252, 
        // but in general for v3 this should be changed all over 
        final String uqrEncoding = "UTF-8";
        System.out.println("Original UsingQR data: " + uqrData);

        BitMatrix matrix = new MultiFormatWriter().encode(new String(uqrData.getBytes(uqrEncoding), uqrEncoding), BarcodeFormat.QR_CODE, width, height, encodeHintMap);

	try {
	    MatrixToImageWriter.writeToFile(matrix, "png", iFile);
	} catch (IOException ioe) {

	}

    }

}

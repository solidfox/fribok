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
    public static void createQRCode(final String uqrData, final String charset, final File iFile, final int height, final int width) throws WriterException, UnsupportedEncodingException {

        Map<EncodeHintType, ErrorCorrectionLevel> encodeHintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        encodeHintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

	BitMatrix matrix = new MultiFormatWriter().encode(new String(uqrData.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height, encodeHintMap);

	try {
	    MatrixToImageWriter.writeToFile(matrix, "png", iFile);
	} catch (IOException ioe) {

	}

    }

}

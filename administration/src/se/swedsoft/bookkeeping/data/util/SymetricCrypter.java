package se.swedsoft.bookkeeping.data.util;

import org.apache.xerces.utils.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Convenience class to make it easy to encrypt and decrypt using a symetric algorithm. 
 * Just construct with a password and call the encrypt/decrypt methods.
 */
public class SymetricCrypter
{
    private SecretKeySpec key;
    private Cipher cipher;
    
    public SymetricCrypter(byte[] keyBytes) {
        key = new SecretKeySpec(keyBytes, "DES");
   
       try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
        } catch (NoSuchAlgorithmException e) {
            onError(e);
        } catch (NoSuchPaddingException e) {
            onError(e);
        }
    }
    
    private static void onError( Throwable cause ) {
        throw new CrypterException( "An error occured in Crypter class", cause );
    }
    
    public byte[] encrypt( byte[] clearText ) {
        return runAlgorithm(Cipher.ENCRYPT_MODE, clearText);
    }
    
    public String encrypt( String clearText ) {
        return new String(Base64.encode(encrypt(clearText.getBytes())));
    }
    
    public String decrypt( String cipherText ) {
        return new String(decrypt(Base64.decode(cipherText.getBytes())));
    }
    
    public byte[] decrypt( byte[] cipherText ) {
        return runAlgorithm(Cipher.DECRYPT_MODE, cipherText);
    }

    public byte[] runAlgorithm( int mode, byte[] data ) {
        
        try {
        
            cipher.init(mode, key);
            return cipher.doFinal(data);

        } catch (IllegalBlockSizeException e) {
            onError(e);
        } catch (BadPaddingException e) {
            onError(e);
        } catch (InvalidKeyException e) {
            onError(e);
        }

        return null; // Should never happen
    }

    public static class CrypterException extends RuntimeException
    {
        public CrypterException( String message ) {
            super(message);
        }
        
        public CrypterException( String message, Throwable cause ) {
            super(message, cause);
        }
    }
}

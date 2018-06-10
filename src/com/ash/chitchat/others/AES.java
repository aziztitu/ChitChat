package com.ash.chitchat.others;

/**
 * Created by azizt on 2/23/2017.
 */
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
/**
 Aes encryption
 */
public class AES
{

    private static SecretKeySpec secretKey ;
    private static byte[] key ;

    private static String decryptedString;
    private static String encryptedString;

    public static void setKey(String myKey){


        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            System.out.println(key.length);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            System.out.println(key.length);
            System.out.println(new String(key,"UTF-8"));
            secretKey = new SecretKeySpec(key, "AES");


        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

   /* public static String getDecryptedString() {
        return decryptedString;
    }
    public static void setDecryptedString(String decryptedString) {
        AES.decryptedString = decryptedString;
    }
    public static String getEncryptedString() {
        return encryptedString;
    }
    public static void setEncryptedString(String encryptedString) {
        AES.encryptedString = encryptedString;
    }*/


    public static String encrypt(String strToEncrypt, String key){
        setKey(key);
        return encrypt(strToEncrypt);
    }

    public static String decrypt(String strToDecrypt, String key){
        setKey(key);
        return decrypt(strToDecrypt);
    }

    public static String encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);


//            return (Base64.encodeBase64String(cipher.doFinal(strToEncrypt.trim().getBytes("UTF-8"))));
            return (new Base32().encodeAsString(cipher.doFinal(strToEncrypt.trim().getBytes("UTF-8"))));

        }
        catch (Exception e)
        {

            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;
    }
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            return (new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt.trim()))));
            return (new String(cipher.doFinal(new Base32().decode(strToDecrypt.trim()))));

        }
        catch (Exception e)
        {

            System.out.println("Error while decrypting: "+e.toString());
        }
        return null;
    }

}
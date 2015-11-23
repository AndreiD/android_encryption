package com.androidadvance.encryptedapi.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {

    private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
    private static final String aesEncryptionAlgorithm = "AES";
    private static byte[] ivBytes;
    private static AESHelper instance = null;
    private static String the_secret;
    private MessageDigest md = null;
    private byte[] keyBytes;

    private AESHelper() {
        SecureRandom random = new SecureRandom();
        AESHelper.ivBytes = new byte[16];
        random.nextBytes(AESHelper.ivBytes);
        try {
            md = MessageDigest.getInstance("SHA-256");
            keyBytes = the_secret.getBytes("utf-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        md.update(keyBytes);
        keyBytes = md.digest();

    }

    public static AESHelper get_instance(String sec) {
        the_secret = sec;
        if (instance == null) {
            instance = new AESHelper();
        }
        return instance;
    }


    public byte[] encrypt(byte[] mes) {
        try {
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            SecureRandom random = new SecureRandom();
            AESHelper.ivBytes = new byte[16];
            random.nextBytes(AESHelper.ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, newKey, random);
            byte[] destination = new byte[ivBytes.length + mes.length];
            System.arraycopy(ivBytes, 0, destination, 0, ivBytes.length);
            System.arraycopy(mes, 0, destination, ivBytes.length, mes.length);
            return cipher.doFinal(destination);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] bytes) {
        try {

            byte[] ivB = Arrays.copyOfRange(bytes, 0, 16);
            byte[] codB = Arrays.copyOfRange(bytes, 16, bytes.length);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivB);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return cipher.doFinal(codB);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


}


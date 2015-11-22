package com.androidadvance.encryptedapi.security;

import android.util.Base64;

import java.io.IOException;
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

public class AESCipher {


    private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
    private static final String aesEncryptionAlgorithm = "AES";
    private static byte[] ivBytes;
    private static AESCipher instance = null;
    private static String the_secret = "RFLDSKJ)U$#@FDSKJ@$#@#!@#E_This is a super secret key..."; //<- your job is to hide this.
    private byte[] keyBytes;

    private AESCipher() {
        SecureRandom random = new SecureRandom();
        AESCipher.ivBytes = new byte[16];
        random.nextBytes(AESCipher.ivBytes);

    }

    public static AESCipher getInstance() {
        if (instance == null) {
            instance = new AESCipher();
        }

        return instance;
    }

    public String encrypt_string(final String plain) {
        try {
            return Base64.encodeToString(encrypt(plain.getBytes()), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "encryption failed";
    }

    public String decrypt_string(final String plain) {
        byte[] encryptedBytes;
        try {
            encryptedBytes = decrypt(Base64.decode(plain, 0));
            return new String(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
        return "decryption failed";
    }


    private byte[] encrypt(byte[] mes)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException, IOException {


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        keyBytes = the_secret.getBytes("utf-8");
        md.update(keyBytes);
        keyBytes = md.digest();


        SecretKeySpec newKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
        Cipher cipher = Cipher.getInstance(cipherTransformation);

        SecureRandom random = new SecureRandom();
        AESCipher.ivBytes = new byte[16];
        random.nextBytes(AESCipher.ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, newKey, random);
        byte[] destination = new byte[ivBytes.length + mes.length];
        System.arraycopy(ivBytes, 0, destination, 0, ivBytes.length);
        System.arraycopy(mes, 0, destination, ivBytes.length, mes.length);
        return cipher.doFinal(destination);

    }

    private byte[] decrypt(byte[] bytes)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException, IOException {


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        keyBytes = the_secret.getBytes("utf-8");
        md.update(keyBytes);
        keyBytes = md.digest();

        byte[] ivB = Arrays.copyOfRange(bytes, 0, 16);
        byte[] codB = Arrays.copyOfRange(bytes, 16, bytes.length);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivB);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(codB);
    }
}


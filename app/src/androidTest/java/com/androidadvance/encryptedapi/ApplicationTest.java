package com.androidadvance.encryptedapi;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.androidadvance.encryptedapi.security.AESHelper;
import com.androidadvance.encryptedapi.security.RSAHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;


public class ApplicationTest extends ApplicationTestCase<Application> {
    private Application mApplication;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mApplication = getApplication();
    }


    public final void test_simple_AESEncryption() {
        Log.i("TESTING AES", "__________________________ AES ___________________");
        String secret_message = "this is a secret message!";

        AESHelper myCipher = AESHelper.get_instance("fdssdfawefdsfdasfas");
        //---- AES Encryption -----
        byte[] bytes_to_be_encripted = new byte[0];
        try {
            bytes_to_be_encripted = secret_message.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] encrypted_bytes = myCipher.encrypt(bytes_to_be_encripted);

        //--- AES Decryption -------
        byte[] decrypted_bytes = myCipher.decrypt(encrypted_bytes);

        assertEquals(new String(decrypted_bytes), secret_message);
    }

    public final void test_simple_RSAEncryption() {
        Log.i("TESTING RSA", "__________________________ RSA ___________________");
        String secret_message = "this is another secret message!";
        byte[] bytes_to_be_encrypted = new byte[0];
        try {
            bytes_to_be_encrypted = secret_message.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //---- RSA Encryption -----
        PublicKey publicKey = RSAHelper.get_instance().getPublicKey(getContext());
        byte[] encrypted_bytes = RSAHelper.encrypt(publicKey, bytes_to_be_encrypted);

        //---- RSA Decrypt -----
        PrivateKey privateKey = RSAHelper.get_instance().getPrivateKey(getContext());
        byte[] decrypted_bytes = RSAHelper.decrypt(privateKey, encrypted_bytes);
        assertEquals(new String(decrypted_bytes), secret_message);
    }

    public final void test_1mb_AESEncryption() {
        Log.i("TESTING AES 1mb", "__________________________ AES ON 1mb FILE ___________________");

        AESHelper myCipher = AESHelper.get_instance("34dfsf23rsdafsadfas32r4");
        long start_time = System.currentTimeMillis();
        InputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = getContext().getAssets().open("1mbfile.dic");
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String secret_message = sc.nextLine();

                //---- AES Encryption -----
                byte[] bytes_to_be_encripted = new byte[0];
                try {
                    bytes_to_be_encripted = secret_message.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] encrypted_bytes = myCipher.encrypt(bytes_to_be_encripted);

                //--- AES Decryption -------
                byte[] decrypted_bytes = myCipher.decrypt(encrypted_bytes);

                assertEquals(new String(decrypted_bytes), secret_message);

            }
            if (sc.ioException() != null) {
                Log.e("exception", String.valueOf(sc.ioException()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        Log.d("FINISHED IN ", String.valueOf(System.currentTimeMillis() - start_time) + "ms");

    }
}
package com.androidadvance.encryptedapi;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import com.androidadvance.encryptedapi.security.AESHelper;
import com.androidadvance.encryptedapi.security.RSAHelper;

import java.io.UnsupportedEncodingException;


public class ApplicationTest extends ApplicationTestCase<Application> {
    private Application mApplication;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mApplication = getApplication();
    }



    public final void testAESEncryption() {
        Log.i("TESTING AES", "__________________________ AES ___________________");
        String secret_message = "this is a secret message!";

        AESHelper myCipher = AESHelper.getInstance();
        //---- AES Encryption -----
        byte[] bytes_to_be_encripted = new byte[0];
        try {
            bytes_to_be_encripted = secret_message.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] encrypted_bytes = AESHelper.getInstance().encrypt(bytes_to_be_encripted);

        //--- AES Decryption -------
        byte[] decrypted_bytes = myCipher.decrypt(encrypted_bytes);

        assertEquals(new String(decrypted_bytes), secret_message);
    }

    public final void testRSAEncryption() {
        Log.i("TESTING RSA","__________________________ RSA ___________________");
        String secret_message = "this is another secret message!";
        byte[] bytes_to_be_encrypted = new byte[0];
        try {
            bytes_to_be_encrypted = secret_message.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //---- RSA Encryption -----
        byte[] encrypted_bytes = RSAHelper.encrypt(new RSAHelper().getPublicKey(mContext), bytes_to_be_encrypted);

        //---- RSA Decrypt -----
        byte[] decrypted_bytes = RSAHelper.decrypt(new RSAHelper().getPrivateKey(mContext), encrypted_bytes);
        assertEquals(new String(decrypted_bytes), secret_message);
    }
}
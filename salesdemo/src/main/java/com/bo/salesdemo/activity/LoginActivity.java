package com.bo.salesdemo.activity;

import android.*;
import android.content.Intent;
import android.content.pm.*;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.*;
import androidx.core.content.*;

import com.blotout.analytics.BlotoutAnalytics;
import com.blotout.deviceinfo.permission.*;
import com.bo.salesdemo.R;
import com.bo.salesdemo.activity.CategoryInfoActivity;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setTitle("Login Screen");
        //senableMyLocation();
        //createKeys();
        ///Initialize SDK here
        //BlotoutAnalytics.getInstance().isProductionMode=false;
        //PGH92NV367NA5ZX
        //BlotoutAnalytics.getInstance().initializeAnalyticsEngine(this,"5DNGP7DR2KD9JSY","WYHDWZ8EHZT3SXE");
        HashMap<String,Object> item2 = new HashMap<>();
        item2.put("emailId","ankuradhikari08@gmail.com");
        item2.put("gender","Male");

        //BlotoutAnalytics.getInstance().mapId("abcd","mixpanel",null);
        //BlotoutAnalytics.getInstance().mapId("abcd","google",item2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void createKeys() {
        try {
            GenerateKeys gk = new GenerateKeys(1024);
            gk.createKeys();
            String msg = "hello world";

            ///Server send this base64String
            byte[] publicKeyString = gk.getPublicKey().getEncoded();
            String base64StringGeneratedAtServer = Base64.encodeToString(publicKeyString,Base64.NO_WRAP);

            /////prepare key from base64String at SDK received from server
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(base64StringGeneratedAtServer,Base64.NO_WRAP)));

            ///encryption at SDK end
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptedBase64String = Base64.encodeToString(rsaCipher.doFinal(msg.getBytes("UTF-8")), Base64.NO_WRAP);
            Log.d("encryptmessage",encryptedBase64String);

            byte[] privateKeyString = gk.getPrivateKey().getEncoded();
            String base64PrivateStringGeneratedAtServer = Base64.encodeToString(privateKeyString,Base64.NO_WRAP);
            ////Decryption at server end
            Cipher rsaCipherDecrypt = Cipher.getInstance("RSA");
            rsaCipherDecrypt.init(Cipher.DECRYPT_MODE, gk.getPrivateKey());
            String decryptString = new String(rsaCipherDecrypt.doFinal(Base64.decode(encryptedBase64String,Base64.NO_WRAP)), "UTF-8");
            Log.d("decryptmessage",decryptString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void onLogin(View view) {

        HashMap<String,Object> item2 = new HashMap<>();
        item2.put("emailId","ankuradhikari08@gmail.com");
        item2.put("gender","Male");


        BlotoutAnalytics.getInstance().mapId("abcd","mixpanel",null);
        BlotoutAnalytics.getInstance().mapId("abcd","google",item2);

        BlotoutAnalytics.getInstance().logPHIEvent("testPHIEvent",item2,null);

        BlotoutAnalytics.getInstance().logPIIEvent("testPIIEvent",item2,null);

        BlotoutAnalytics.getInstance().logEvent("InCart",item2);

        BlotoutAnalytics.getInstance().logEvent("LoginView",null);
        BlotoutAnalytics.getInstance().logEvent("Item Selected",null);
        BlotoutAnalytics.getInstance().logEvent("Add To Cart",null);
        BlotoutAnalytics.getInstance().logEvent("List Item Cart View",null);

        BlotoutAnalytics.getInstance().logEvent("LoginView",null);
        Intent intent = new Intent(this, CategoryInfoActivity.class);
        startActivity(intent);

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        } else  {
            // Access to the location has been granted to the app.

        }
    }
}

class GenerateKeys {

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
    }

    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

}

package com.blotout.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.blotout.app.R;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        Button btnDeviceInfo = findViewById(R.id.btn_device_info);
        Button btnCheckReferrer = findViewById(R.id.btn_check_referrer);
        Button btnCheckLicense = findViewById(R.id.btn_check_license);

        btnDeviceInfo.setOnClickListener(this);
        btnCheckReferrer.setOnClickListener(this);
        btnCheckLicense.setOnClickListener(this);


        mContext =this;




        //TODO: Need to do all initialization in application class
        //TODO: @Ankur: Need to discuss
//        BlotoutAnalytics.getInstance().initializeAnalyticsEngine(this);
//        //Custom events
//        HashMap<String, Object> customEventModelDict = new HashMap<>();
//        customEventModelDict.put("amount", 20);
//        customEventModelDict.put("item", "iphone");
//        BlotoutAnalytics.getInstance().logEvent("App Installed", customEventModelDict);

//        //TODO: Do not delete. Storage testing
//        mBOFileSystemManager = BOFileSystemManager.getInstance();
//        mBOFileSystemManager.createRootDirectory();
//        List<File> fileListWithExtn = mBOFileSystemManager.getAllFilesWithExtension
//        (mBOFileSystemManager.getBOSDKRootDirectory());
//        List<File> fileListWithoutExtn =mBOFileSystemManager.getAllFilesWithoutExtension
//        (mBOFileSystemManager.getBOSDKRootDirectory());
//
//        //mBOFileSystemManager.createDirectoryIfRequiredAndReturnPath
//        ("createDirectoryIfRequiredAndReturnPath")
//
//        if (null != fileListWithExtn && fileListWithExtn.size() > 0) {
//
//            String srcPath = fileListWithExtn.get(0).getAbsolutePath();
//            String destPath = mBOFileSystemManager.createDirectoryIfRequiredAndReturnPath(
//                    mBOFileSystemManager.getBOSDKRootDirectory() + "/Hussain123");
//            mBOFileSystemManager.moveFile(srcPath, destPath);
//
//        }


//        Logger.INSTANCE.d(TAG, "" + "Time stamp in mili seconds  "
//                + BODateTimeUtils.get13DigitNumberObjTimeStamp());
//        Logger.INSTANCE.d(TAG, "Time stamp in seconds  " + BODateTimeUtils.getTimestampInSec());



        //BOAppLifeTimeInfo =  BOCommonUtils.getLifeATimeAppInfo(this);
//        BOCommonUtils.getLifeATimeAppInfo(this);
//        BOCommonUtils.getSessionAppInfo(this);
//
//        Logger.INSTANCE.e(TAG, "Ipv4 "+ BOCommonUtils.getIPAddress(true));
//        Logger.INSTANCE.e(TAG, "IPv6 "+ BOCommonUtils.getIPAddress(false));
//        Logger.INSTANCE.e(TAG, "Mac  "+   BOCommonUtils.getMacAddress(this));

        //BOCommonUtils.getWifiBroadcastAddress(this);




    }


    @Override
    public void onResume() {
        super.onResume();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void launchActivity2() {
        Intent intent = new Intent(MainActivity.this, DeviceInfoActivity.class);
        startActivity(intent);
    }

    private void launchReferrerActivity() {
        Intent intent = new Intent(MainActivity.this, ReferrerActivityDetails.class);
        startActivity(intent);
    }

    private void launchCheckLicenseActivity() {
        //Intent intent = new Intent(MainActivity.this, LVLActivity.class);
        //startActivity(intent);
    }


    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.btn_device_info:
                launchActivity2();
                break;
            case R.id.btn_check_referrer:
                launchReferrerActivity();
                break;
            case R.id.btn_check_license:
                launchCheckLicenseActivity();
                break;


        }
    }
}

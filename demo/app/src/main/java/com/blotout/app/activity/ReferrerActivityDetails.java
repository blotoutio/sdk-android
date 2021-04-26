package com.blotout.app.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.blotout.app.R;


public class ReferrerActivityDetails extends Activity {

    private TextView title;
    private TextView content;

    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          //  updateData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrer);
        title = (TextView) findViewById(R.id.app_name_and_version);
        content = (TextView) findViewById(R.id.content);
        initViews();
        //updateData();
    }

    private void initViews() {
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = getString(R.string.version_unavailable);
        }

        title.setText(Html.fromHtml(getString(R.string.app_name_and_version, versionName)));
    }

//    private void updateData() {
//
//        boolean isReferrerDetected = InstallReferrerHelper.isReferrerDetected(
//                getApplicationContext());
//        String firstLaunch = InstallReferrerHelper.getFirstLaunch(getApplicationContext());
//        String referrerDate = InstallReferrerHelper.getReferrerDate(getApplicationContext());
//        String referrerDataRaw = InstallReferrerHelper.getReferrerDataRaw(getApplicationContext());
//        String referrerDataDecoded = InstallReferrerHelper.getReferrerDataDecoded(
//                getApplicationContext());
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("<b>First launch:</b>")
//                .append("<br/>")
//                .append(firstLaunch)
//                .append("<br/><br/>")
//                .append("<b>Referrer detection:</b>")
//                .append("<br/>")
//                .append(referrerDate);
//        if (isReferrerDetected) {
//            sb.append("<br/><br/>")
//                    .append("<b>Raw referrer:</b>")
//                    .append("<br/>")
//                    .append(referrerDataRaw);
//
//            if (referrerDataDecoded != null) {
//                sb.append("<br/><br/>")
//                        .append("<b>Decoded referrer:</b>")
//                        .append("<br/>")
//                        .append(referrerDataDecoded);
//            }
//        }
//
//        content.setText(Html.fromHtml(sb.toString()));
//        content.setMovementMethod(new LinkMovementMethod());
//    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
//        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateReceiver,
//                new IntentFilter(ReferrerReceiver.ACTION_UPDATE_DATA));
        super.onResume();
    }

}

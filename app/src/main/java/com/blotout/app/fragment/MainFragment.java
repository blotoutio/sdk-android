package com.blotout.app.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.blotout.app.R;
import com.blotout.app.adapter.CustomListAdapter;
import com.blotout.deviceinfo.ads.Ad;
import com.blotout.deviceinfo.ads.AdInfo;
import com.blotout.deviceinfo.device.model.App;
import com.blotout.deviceinfo.device.model.Battery;
import com.blotout.deviceinfo.device.model.Device;
import com.blotout.deviceinfo.device.model.Memory;
import com.blotout.deviceinfo.device.model.Network;
import com.blotout.deviceinfo.location.DeviceLocation;
import com.blotout.deviceinfo.location.LocationInfo;
import com.blotout.deviceinfo.permission.PermissionUtils;
import com.blotout.deviceinfo.userapps.UserAppInfo;
import com.blotout.deviceinfo.userapps.UserApps;
import com.blotout.deviceinfo.usercontacts.UserContactInfo;
import com.blotout.deviceinfo.usercontacts.UserContacts;

import java.util.List;

public class MainFragment extends Fragment implements AdInfo.AdIdCallback{

    private int position;
    private Activity mActivity;

    private RecyclerView recyclerView;
    private CustomListAdapter adapter;
    private PermissionUtils permissionUtils;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NonNull
    public static MainFragment newInstance(int pos) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("pos");
        }
        permissionUtils = new PermissionUtils(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.smoothScrollToPosition(0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        askPermission();
    }

    private void askPermission() {
        String permission = null;
        switch (position) {
            case 0:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                break;
            case 6:
                permission = Manifest.permission.READ_PHONE_STATE;
                break;
            case 8:
                permission = Manifest.permission.READ_CONTACTS;
                break;
        }
        if (permission != null) {
            getPermission(permission);
        } else {
            initialize();
        }
    }

    private void getPermission(@NonNull String permission) {
        PermissionUtils permissionUtils = new PermissionUtils(mActivity);
        if (!permissionUtils.isPermissionGranted(permission)) {

        } else {
            initialize();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initialize() {
        new Handler().post(() -> {
            switch (position) {
                case 0: //call location
                    LocationInfo locationInfo = new LocationInfo(mActivity);
                    DeviceLocation location = locationInfo.getLocation();
                    adapter = new CustomListAdapter(mActivity, location);
                    break;
                case 1: //call apps
                    App app = new App(mActivity);
                    adapter = new CustomListAdapter(mActivity, app);

                case 2: //call ads
                    AdInfo adInfo = new AdInfo(mActivity);
                    adInfo.getAndroidAdId(MainFragment.this);

                case 3: //call battery
                    Battery battery = new Battery(mActivity);
                    adapter = new CustomListAdapter(mActivity, battery);

                case 4: //call device
                    Device device = new Device(mActivity);
                    adapter = new CustomListAdapter(mActivity, device);

                case 5: //call memory
                    Memory memory = new Memory(mActivity);
                    adapter = new CustomListAdapter(mActivity, memory);

                case 6: //call network
                    Network network = new Network(mActivity);
                    adapter = new CustomListAdapter(mActivity, network);

                case 7: //call installed apps
                    UserAppInfo userAppInfo = new UserAppInfo(mActivity);
                    List<UserApps> userApps = userAppInfo.getInstalledApps(true);
                    adapter = new CustomListAdapter(mActivity, userApps);

                case 8: //call contacts
                    UserContactInfo userContactInfo = new UserContactInfo(mActivity);
                    List<UserContacts> userContacts = userContactInfo.getContacts();
                    adapter = new CustomListAdapter(mActivity, userContacts);

            }

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null) {
                        recyclerView.setAdapter(adapter);
                    }
                }
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onResponse(Context context, final Ad ad) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new CustomListAdapter(mActivity, ad);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}

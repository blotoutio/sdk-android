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

import java.util.List;

public class MainFragment extends Fragment implements AdInfo.AdIdCallback{

    private int position;
    private Activity mActivity;

    private RecyclerView recyclerView;
    private CustomListAdapter adapter;

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

            initialize();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initialize() {
        new Handler().post(() -> {
            switch (position) {


                case 2: //call ads
                    AdInfo adInfo = new AdInfo(mActivity);
                    adInfo.getAndroidAdId(MainFragment.this);
                case 8: //call contacts
                    /*UserContactInfo userContactInfo = new UserContactInfo(mActivity);
                    List<UserContacts> userContacts = userContactInfo.getContacts();
                    adapter = new CustomListAdapter(mActivity, userContacts);*/

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

package com.bo.salesdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blotout.analytics.BlotoutAnalytics;
import com.bo.salesdemo.R;
import com.bo.salesdemo.adapter.ViewCartRecyclerViewAdapter;
import com.bo.salesdemo.ebay.shopping.api.SimpleItemType;
import com.bo.salesdemo.model.SingleItemModel;


import java.util.ArrayList;
import java.util.List;

public class AddedToBasketActivity extends AppCompatActivity {
    private static final String TAG = "AddedToBasketActivity";

    private SimpleItemType mSimpleItemType;
    private List<SingleItemModel> mSingleItemModelList;
    private ViewCartRecyclerViewAdapter mViewCartRecyclerViewAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_to_basket_activity);
        setTitle("View Cart Screen");
        Context context =this;
        BlotoutAnalytics.getInstance().logEvent("List Item Cart View",null);
        mSingleItemModelList = new ArrayList<>();
//        getIntent().getExtras().getString("PRICE");
//        getIntent().getExtras().getString("THUMBNAIL");
//        getIntent().getExtras().getString("BIDS");
//        getIntent().getExtras().getString("PRODUCT_NAME");


        mRecyclerView = findViewById(R.id.view_cart_lst);

        SingleItemModel singleItemModel = new SingleItemModel();
        singleItemModel.setProductName(getIntent().getStringExtra("PRODUCT_NAME"));
        singleItemModel.setPrice(getIntent().getStringExtra("PRICE"));
        singleItemModel.setBids(getIntent().getStringExtra("BIDS"));
        singleItemModel.setThumbnailUrl(getIntent().getStringExtra("THUMBNAIL"));

        mSingleItemModelList.add(singleItemModel);
       // mSingleItemModelList.add(singleItemModel1);

        //mRecyclerView.setHasFixedSize(true);
        mViewCartRecyclerViewAdapter = new ViewCartRecyclerViewAdapter(context,
                mSingleItemModelList);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mViewCartRecyclerViewAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);



//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_cart_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_buy) {
            AlertDialog alertDialog = createSimpleOkDialog(this, "B-Commerce",
                    "Thank you for shopping. All Items will be delivered to your Home Address.");
            alertDialog.show();

            mSingleItemModelList.clear();
            mViewCartRecyclerViewAdapter.setList(mSingleItemModelList);
        }

        return super.onOptionsItemSelected(item);
    }


    @NonNull
    private AlertDialog createSimpleOkDialog(@NonNull Context context, String title, String message) {

        BlotoutAnalytics.getInstance().logEvent("Purchase Complete",null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context).setTitle(
                title).setMessage(message).setNeutralButton("Ok", null);
        return alertDialog.create();
    }
}

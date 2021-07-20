package com.bo.salesdemo.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.analytics.BlotoutAnalytics;
import com.bo.salesdemo.R;
import com.bo.salesdemo.ebay.shopping.api.AckCodeType;
import com.bo.salesdemo.ebay.shopping.api.CategoryArrayType;
import com.bo.salesdemo.ebay.shopping.api.CategoryType;
import com.bo.salesdemo.ebay.shopping.api.GetCategoryInfoRequestType;
import com.bo.salesdemo.ebay.shopping.api.GetCategoryInfoResponseType;
import com.bo.salesdemo.ebay.shopping.api.ShoppingServiceClient;
import com.bo.salesdemo.ebay.shopping.api.client.ShoppingInterface_XMLClient;

import com.leansoft.nano.log.ALog;
import com.leansoft.nano.ws.SOAPServiceCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubCategoryInfoActivity extends ListActivity implements OnScrollListener {

    private static final String TAG = SubCategoryInfoActivity.class
            .getSimpleName();


    static final int DEFAULT_ENTRIES_PER_PAGE = 20;

    private int lastItem = 0;
    private int totalItemCount = 0;

    private String searchKeywords;

    @Nullable
    private ProgressDialog progressDialog;
    @Nullable
    private String mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info_activity);

        setTitle("Sub-Category Screen");
        mCategoryId = getIntent().getStringExtra("CATEGORY_ID");

        this.getListView().setOnScrollListener(this);
        getCategoryInfo();

    }


    // asynchronously trigger findItemsByKeywords call
    private void getCategoryInfo() {

        progressDialog = ProgressDialog.show(SubCategoryInfoActivity.this,
                "Please wait...", "Retrieving data...", true, true);

        // build a request object
        GetCategoryInfoRequestType request = new GetCategoryInfoRequestType();
        request.categoryID = mCategoryId;
        request.includeSelector = "ChildCategories";

        ShoppingInterface_XMLClient client = ShoppingServiceClient.getSharedClient();
        client.setDebug(true);


        client.getCategoryInfo(request,
                new SOAPServiceCallback<GetCategoryInfoResponseType>() {


                    @Override
                    public void onSuccess(@NonNull GetCategoryInfoResponseType responseObject) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }

                        if (responseObject.ack
                                == AckCodeType.SUCCESS) { // handle sucessful response
                            CategoryArrayType categoryArrayType = responseObject.categoryArray;

                            List<CategoryType> items = (categoryArrayType.category != null)
                                    ? categoryArrayType.category :
                                    new ArrayList<CategoryType>();
                            ItemAdapter itemAdapter = new ItemAdapter(SubCategoryInfoActivity.this,
                                    R
                                            .layout.row, items);
                            SubCategoryInfoActivity.this.setListAdapter(itemAdapter);


                        } else { // handle response resident error
                            String errorMessage = responseObject.errors.get(0).longMessage;
                            ALog.e(TAG, errorMessage);
                            Toast.makeText(getApplicationContext(),
                                    errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }

                    }


                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        Log.e(TAG, "onFailure " + s);

                    }

                    @Override
                    public void onSOAPFault(@NonNull Object o) {
                        Log.e(TAG, "onSOAPFault " + o.toString());
                    }
                });

    }

    private class ItemAdapter extends ArrayAdapter<CategoryType> {

        private List<CategoryType> items;

        public ItemAdapter(@NonNull Context context, int textViewResourceId,
                @NonNull List<CategoryType> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }

            final CategoryType item = items.get(position);
            if (item != null) {
                TextView title = (TextView) v.findViewById(R.id.title);
                TextView price = (TextView) v.findViewById(R.id.price);
                TextView bidType = (TextView) v.findViewById(R.id.bids);
                TextView timeleft = (TextView) v.findViewById(R.id.timeleft);

                if (title != null) {
                    title.setText(item.categoryName);
                }

//                Amount convertedCurrentPrice = item..convertedCurrentPrice;
//                price.setText(eBayUtil.formatCurrencyToString(
//                        convertedCurrentPrice.value,
//                        convertedCurrentPrice.currencyId));

                price.setText(item.categoryParentName);


//                String listingType = item.listingInfo.listingType;
//                if (listingType.equalsIgnoreCase("FixedPrice")
//                        || listingType.equalsIgnoreCase("StoreInventory")) {
//                    bidType.setBackgroundResource(R.drawable.bin_clear);
//                    bidType.setText("");
//                } else {
//                    bidType.setBackgroundResource(R.drawable.light_blue_pixel);
//                    bidType.setText(item.sellingStatus.bidCount + " bids");
//                }
//
//                Duration duration = item.sellingStatus.timeLeft;
//                if (duration.getDays() == 0 && duration.getHours() == 0
//                        && duration.getMinutes() < 10) {
//                    timeleft.setTextColor(Color.RED);
//                } else {
//                    timeleft.setTextColor(Color.BLACK);
//                }
//
//                timeleft.setText(eBayUtil.formatDuration(duration));
//
                ImageView image = (ImageView) v
                        .findViewById(R.id.gallery_icon);
                image.setVisibility(View.GONE);
//                if (image != null) {
//                    if (item.galleryURL != null) {
//                        image.setImageUrl(item.galleryURL);
//                        image.loadImage();
//                    } else {
//                        image.setNoImageDrawable(R.drawable.placeholder);
//                    }
//                }

                // once clicked, navigate to item details page
                v.setOnClickListener(new OnItemClickListener(item.categoryID,item.categoryName, v.getContext()));
            }

            return v;
        }
    }

    private class OnItemClickListener implements OnClickListener {
        private String mSubCategoeyId;
        private String mCategorName;
        private Context mCxt;

        OnItemClickListener(String subCategoryId,String categoryName, Context cxt) {
            mSubCategoeyId = subCategoryId;
            mCxt = cxt;
            mCategorName = categoryName;
        }

        @Override
        public void onClick(View arg0) {
//            ALog.d(TAG, "onItemClick at item " + mCategoryName);
//            Intent intent = new Intent(mCxt, FindingActivity.class);
//            intent.putExtra("CATEGORY_NAME", mCategoryName);
//            startActivity(intent);
            HashMap<String,Object> actiondata =  new HashMap<>();
            actiondata.put("time", new Date());
            actiondata.put("Item Name", mCategorName);

            BlotoutAnalytics.getInstance().logEvent("Category Selected", actiondata);
            ALog.d(TAG, "onItemClick at item " + mSubCategoeyId);
            Intent intent = new Intent(mCxt, Sub2CategoryInfoActivity.class);
            intent.putExtra("CATEGORY_ID", mSubCategoeyId);
            startActivity(intent);
        }
    }

    // for dynamic pagination
    @Override
    public void onScroll(AbsListView view, int firstVisible, int visibleCount,
            int totalCount) {

        lastItem = firstVisible + visibleCount;
        totalItemCount = totalCount;
    }

    // for dynamic pagination
    @Override
    public void onScrollStateChanged(AbsListView arg0, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {

            boolean loadMore = lastItem >= totalItemCount;

            if (loadMore) {
                int nextPage = totalItemCount / DEFAULT_ENTRIES_PER_PAGE + 1;
                //findItemsByKeywords(nextPage, DEFAULT_ENTRIES_PER_PAGE, false);
                Toast.makeText(this, "Loading more ...",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

}

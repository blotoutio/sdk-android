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
import com.bo.salesdemo.activity.FindingActivity;
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

public class Sub2CategoryInfoActivity extends ListActivity implements OnScrollListener {

    private static final String TAG = Sub2CategoryInfoActivity.class
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
        setContentView(R.layout.category2__info_activity);
        setTitle("Sub-Category2 Screen");

        mCategoryId = getIntent().getStringExtra("CATEGORY_ID");

        this.getListView().setOnScrollListener(this);

//        Button searchButton = findViewById(R.id.btn_search);
//        searchButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String keywords = ((EditText) findViewById(R.id.edit_input))
//                        .getText().toString();
//                // validation
//                if (keywords == null | keywords.length() == 0) {
//                    Toast.makeText(getApplicationContext(),
//                            "please enter keyword first!",
//                            Toast.LENGTH_LONG).show();
//                    return;
//                }
//                searchKeywords = keywords;
//                // make a search
//                //findItemsByKeywords(1, DEFAULT_ENTRIES_PER_PAGE, true);
//                getCategoryInfo();
//            }
//        });
        getCategoryInfo();

    }


    // asynchronously trigger findItemsByKeywords call
    private void getCategoryInfo() {

        progressDialog = ProgressDialog.show(Sub2CategoryInfoActivity.this,
                "Please wait...", "Retrieving data...", true, true);

//		if (newSearch) {
//			// reset
//			lastItem = 0;
//			totalItemCount = 0;
//		}

        // build a request object
        GetCategoryInfoRequestType request = new GetCategoryInfoRequestType();
        request.categoryID = mCategoryId;
        request.includeSelector = "ChildCategories";


        //request.keywords = searchKeywords;
//		PaginationInput pi = new PaginationInput();
//        pi.pageNumber = pageNum;
//        pi.entriesPerPage = entriesPerPage;
//        request.paginationInput = pi;

        // show fixed price and auction item only
//		ItemFilter itemFilter = new ItemFilter();
//		itemFilter.name = ItemFilterType.LISTING_TYPE;
//		itemFilter.value = new ArrayList<String>();
//		itemFilter.value.add("FixedPrice");
//		itemFilter.value.add("Auction");
//		request.itemFilter = new ArrayList<ItemFilter>();
//		request.itemFilter.add(itemFilter);

        // Get shared client
//        ShoppingInterface_SOAPClient client = new ShoppingInterface_SOAPClient();
//        client.setDebug(true);
//        //client.setContentType();

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

                        if (responseObject.ack == AckCodeType.SUCCESS) { // handle sucessful response
                            CategoryArrayType categoryArrayType = responseObject.categoryArray;

                                List<CategoryType> items = (categoryArrayType.category != null) ? categoryArrayType.category :
                                        new ArrayList<CategoryType>();
                                ItemAdapter itemAdapter = new ItemAdapter(Sub2CategoryInfoActivity.this, R
                                        .layout.row, items);
                            Sub2CategoryInfoActivity.this.setListAdapter(itemAdapter);



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
                        Log.e(TAG, "onFailure "+ s);

                    }

                    @Override
                    public void onSOAPFault(@NonNull Object o) {
                        Log.e(TAG, "onSOAPFault "+ o.toString());
                    }
                });

//		// make API call and register callbacks
//		client.getCategoryInfo(request, new SOAPServiceCallback<GetCategoriesResponseType>() {
//
//			@Override
//			public void onSuccess(FindItemsByKeywordsResponse responseObject) {
//	    		if (progressDialog != null) {
//    			    progressDialog.dismiss();
//    			    progressDialog = null;
//    		    }
//
//				if (responseObject.ack == AckValue.SUCCESS) { // handle sucessful response
//                    CategoryArrayType searchResult = responseObject.searchResult;
//		            if (newSearch) {
//			            List<SearchItem> items = (searchResult.item != null) ? searchResult.item :
//			            new ArrayList<SearchItem>();
//						ItemAdapter itemAdapter = new ItemAdapter(CategoryInfoActivity.this, R
//						.layout.row, items);
//			            CategoryInfoActivity.this.setListAdapter(itemAdapter);
//		            } else {
//		            	if (searchResult.item != null) {
//		            		ItemAdapter itemAdapter = (ItemAdapter) CategoryInfoActivity.this
//		            		.getListAdapter();
//		            		itemAdapter.items.addAll(searchResult.item);
//		            		itemAdapter.notifyDataSetChanged();
//		            	}
//		            }
//
//				} else { // handle response resident error
//					String errorMessage = responseObject.errorMessage.error.get(0).message;
//					ALog.e(TAG, errorMessage);
//		            Toast.makeText(getApplicationContext(),
//		            		errorMessage,
//		                    Toast.LENGTH_LONG).show();
//				}
//			}
//
//			@Override
//			public void onFailure(Throwable error, String errorMessage) { // http or parsing error
//	    		if (progressDialog != null) {
//    			    progressDialog.dismiss();
//    			    progressDialog = null;
//    		    }
//
//				Toast.makeText(CategoryInfoActivity.this, errorMessage, Toast.LENGTH_LONG)
//				.show();
//
//			}
//
//			@Override
//			public void onSOAPFault(Object soapFault) { // soap fault
//	    		if (progressDialog != null) {
//    			    progressDialog.dismiss();
//    			    progressDialog = null;
//    		    }
//
//			    com.leansoft.nano.soap12.Fault fault = (com.leansoft.nano.soap12.Fault)soapFault;
//				Reasontext reasonText = fault.reason.text.get(0);
//
//				ALog.e(TAG, reasonText.value);
//
//				Toast.makeText(CategoryInfoActivity.this, reasonText.value, Toast.LENGTH_LONG)
//				.show();
//
//			}
//
//		});
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
                v.setOnClickListener(new OnItemClickListener(item.categoryName, v.getContext()));
            }

            return v;
        }
    }

    private class OnItemClickListener implements OnClickListener {
        private String mCategoryName;
        private Context mCxt;

        OnItemClickListener(String categoryName, Context cxt) {
            mCategoryName = categoryName;
            mCxt = cxt;
        }

        @Override
        public void onClick(View arg0) {
            ALog.d(TAG, "onItemClick at item " + mCategoryName);
            HashMap<String,Object> actiondata =  new HashMap<>();
            actiondata.put("time", new Date());
            actiondata.put("Item Name", mCategoryName);

            BlotoutAnalytics.getInstance().logEvent("Category Selected", actiondata);
            Intent intent = new Intent(mCxt, FindingActivity.class);
            intent.putExtra("CATEGORY_NAME", mCategoryName);
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

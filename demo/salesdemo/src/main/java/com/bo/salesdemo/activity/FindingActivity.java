package com.bo.salesdemo.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.bo.salesdemo.ebay.finding.FindingServiceClient;
import com.bo.salesdemo.ebay.finding.api.AckValue;
import com.bo.salesdemo.ebay.finding.api.Amount;
import com.bo.salesdemo.ebay.finding.api.FindItemsByKeywordsRequest;
import com.bo.salesdemo.ebay.finding.api.FindItemsByKeywordsResponse;
import com.bo.salesdemo.ebay.finding.api.ItemFilter;
import com.bo.salesdemo.ebay.finding.api.ItemFilterType;
import com.bo.salesdemo.ebay.finding.api.PaginationInput;
import com.bo.salesdemo.ebay.finding.api.SearchItem;
import com.bo.salesdemo.ebay.finding.api.SearchResult;
import com.bo.salesdemo.ebay.finding.api.client.FindingServicePortType_SOAPClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ebay.util.eBayUtil;
import com.leansoft.nano.custom.types.Duration;
import com.leansoft.nano.log.ALog;
import com.leansoft.nano.soap12.Reasontext;
import com.leansoft.nano.ws.SOAPServiceCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FindingActivity extends ListActivity implements OnScrollListener {
	
	private static final String TAG = FindingActivity.class
			.getSimpleName();

	
	static final int DEFAULT_ENTRIES_PER_PAGE = 20;
	
	private int lastItem = 0;
	private int totalItemCount = 0;
	
	@Nullable
    private String mSearchKeywords;
	
	@Nullable
    private ProgressDialog progressDialog;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_activity);
		setTitle("Product Screen");
		BlotoutAnalytics.getInstance().logEvent("List Item View",null);

		mSearchKeywords = getIntent().getStringExtra("CATEGORY_NAME");
		
		this.getListView().setOnScrollListener(this);
		
//		Button searchButton = (Button) findViewById(R.id.btn_search);
//		searchButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String keywords = ((EditText) findViewById(R.id.edit_input))
//						.getText().toString();
//				// validation
//				if (keywords == null | keywords.length() == 0) {
//		            Toast.makeText(getApplicationContext(),
//		                    "please enter keyword first!",
//		                    Toast.LENGTH_LONG).show();
//		            return;
//				}
//
//				searchKeywords = keywords;
//
//				// make a search
//				findItemsByKeywords(1, DEFAULT_ENTRIES_PER_PAGE, true);
//
//				//findItemsAdvanced(1, DEFAULT_ENTRIES_PER_PAGE, true);
//			}
//		});

		//findItemsAdvanced(1, DEFAULT_ENTRIES_PER_PAGE, true);//


		//findItemsByCategory();

		findItemsByKeywords(1, DEFAULT_ENTRIES_PER_PAGE, true);


		
	}


	// asynchronously trigger findItemsByKeywords call
	private void findItemsByKeywords(int pageNum, int entriesPerPage, final boolean newSearch) {

    	progressDialog = ProgressDialog.show(FindingActivity.this,
        		"Please wait...", "Retrieving data...", true, true);

		if (newSearch) {
			// reset
			lastItem = 0;
			totalItemCount = 0;
		}

		// build a request object
		FindItemsByKeywordsRequest request = new FindItemsByKeywordsRequest();
		request.keywords = mSearchKeywords;
		PaginationInput pi = new PaginationInput();
        pi.pageNumber = pageNum;
        pi.entriesPerPage = entriesPerPage;
        request.paginationInput = pi;

		// show fixed price and auction item only
		ItemFilter itemFilter = new ItemFilter();
		itemFilter.name = ItemFilterType.LISTING_TYPE;
		itemFilter.value = new ArrayList<String>();
		itemFilter.value.add("FixedPrice");
		itemFilter.value.add("Auction");
		request.itemFilter = new ArrayList<ItemFilter>();
		request.itemFilter.add(itemFilter);

		// Get shared client
		FindingServicePortType_SOAPClient client = FindingServiceClient.getSharedClient();
		client.setDebug(true);

		// make API call and register callbacks
		client.findItemsByKeywords(request, new SOAPServiceCallback<FindItemsByKeywordsResponse>() {

			@Override
			public void onSuccess(@NonNull FindItemsByKeywordsResponse responseObject) {
	    		if (progressDialog != null) {
    			    progressDialog.dismiss();
    			    progressDialog = null;
    		    }

				if (responseObject.ack == AckValue.SUCCESS) { // handle sucessful response
		            SearchResult searchResult = responseObject.searchResult;
		            if (newSearch) {
			            List<SearchItem> items = (searchResult.item != null) ? searchResult.item : new ArrayList<SearchItem>();
						ItemAdapter itemAdapter = new ItemAdapter(FindingActivity.this, R.layout.row, items);
			            FindingActivity.this.setListAdapter(itemAdapter);
		            } else {
		            	if (searchResult.item != null) {
		            		ItemAdapter itemAdapter = (ItemAdapter) FindingActivity.this.getListAdapter();
		            		itemAdapter.items.addAll(searchResult.item);
		            		itemAdapter.notifyDataSetChanged();
		            	}
		            }

				} else { // handle response resident error
					String errorMessage = responseObject.errorMessage.error.get(0).message;
					ALog.e(TAG, errorMessage);
		            Toast.makeText(getApplicationContext(),
		            		errorMessage,
		                    Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String errorMessage) { // http or parsing error
	    		if (progressDialog != null) {
    			    progressDialog.dismiss();
    			    progressDialog = null;
    		    }

				Toast.makeText(FindingActivity.this, errorMessage, Toast.LENGTH_LONG).show();

			}

			@Override
			public void onSOAPFault(Object soapFault) { // soap fault
	    		if (progressDialog != null) {
    			    progressDialog.dismiss();
    			    progressDialog = null;
    		    }

			    com.leansoft.nano.soap12.Fault fault = (com.leansoft.nano.soap12.Fault)soapFault;
				Reasontext reasonText = fault.reason.text.get(0);

				ALog.e(TAG, reasonText.value);

				Toast.makeText(FindingActivity.this, reasonText.value, Toast.LENGTH_LONG).show();

			}

		});
	}


//
//	// asynchronously trigger findItemsByKeywords call
//	private void findItemsAdvanced(int pageNum, int entriesPerPage, final boolean newSearch) {
//
//		progressDialog = ProgressDialog.show(FindingActivity.this,
//				"Please wait...", "Retrieving data...", true, true);
//
//		if (newSearch) {
//			// reset
//			lastItem = 0;
//			totalItemCount = 0;
//		}
//
//		// build a request object
//		FindItemsAdvancedRequest request = new FindItemsAdvancedRequest();
//		request.keywords = "T-shirt";
//		PaginationInput pi = new PaginationInput();
//		pi.pageNumber = pageNum;
//		pi.entriesPerPage = entriesPerPage;
//		request.paginationInput = pi;
//
//		// show fixed price and auction item only
//		ItemFilter itemFilter = new ItemFilter();
//		itemFilter.name = ItemFilterType.LISTING_TYPE;
//		itemFilter.value = new ArrayList<String>();
//		itemFilter.value.add("FixedPrice");
//		itemFilter.value.add("Auction");
//		request.itemFilter = new ArrayList<ItemFilter>();
//		request.itemFilter.add(itemFilter);
//
//		// Get shared client
//		FindingServicePortType_SOAPClient client = FindingServiceClient.getSharedClient();
//		client.setDebug(true);
//
//		// make API call and register callbacks
//		client.findItemsAdvanced(request, new SOAPServiceCallback<FindItemsAdvancedResponse>() {
//
//			@Override
//			public void onSuccess(FindItemsAdvancedResponse responseObject) {
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//					progressDialog = null;
//				}
//
//				if (responseObject.ack == AckValue.SUCCESS) { // handle sucessful response
//					SearchResult searchResult = responseObject.searchResult;
//					if (newSearch) {
//						List<SearchItem> items = (searchResult.item != null) ? searchResult.item : new ArrayList<SearchItem>();
//						ItemAdapter itemAdapter = new ItemAdapter(FindingActivity.this, R.layout.row, items);
//						FindingActivity.this.setListAdapter(itemAdapter);
//					} else {
//						if (searchResult.item != null) {
//							ItemAdapter itemAdapter = (ItemAdapter) FindingActivity.this.getListAdapter();
//							itemAdapter.items.addAll(searchResult.item);
//							itemAdapter.notifyDataSetChanged();
//						}
//					}
//
//				} else { // handle response resident error
//					String errorMessage = responseObject.errorMessage.error.get(0).message;
//					ALog.e(TAG, errorMessage);
//					Toast.makeText(getApplicationContext(),
//							errorMessage,
//							Toast.LENGTH_LONG).show();
//				}
//			}
//
//			@Override
//			public void onFailure(Throwable error, String errorMessage) { // http or parsing error
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//					progressDialog = null;
//				}
//
//				Toast.makeText(FindingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//
//			}
//
//			@Override
//			public void onSOAPFault(Object soapFault) { // soap fault
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//					progressDialog = null;
//				}
//
//				com.leansoft.nano.soap12.Fault fault = (com.leansoft.nano.soap12.Fault)soapFault;
//				Reasontext reasonText = fault.reason.text.get(0);
//
//				ALog.e(TAG, reasonText.value);
//
//				Toast.makeText(FindingActivity.this, reasonText.value, Toast.LENGTH_LONG).show();
//
//			}
//
//		});
//	}
//
//	private void findItemsByCategory(){
//
//		progressDialog = ProgressDialog.show(FindingActivity.this,
//				"Please wait...", "Retrieving data...", true, true);
//
//
//		// build a request object
//		FindItemsByCategoryRequest request = new FindItemsByCategoryRequest();
//		List<String> categoryIdList = new ArrayList<>();
//		categoryIdList.add("21567");
//		categoryIdList.add("15687");
//		categoryIdList.add("178006");
//		categoryIdList.add("21568");
//		categoryIdList.add("61770");
//		//request.categoryID = "15687";
//		//request.categoryID = "178006";
//
//
//		// show fixed price and auction item only
//		ItemFilter itemFilter = new ItemFilter();
//		itemFilter.name = ItemFilterType.LISTING_TYPE;
//		itemFilter.value = new ArrayList<String>();
//		itemFilter.value.add("FixedPrice");
//		itemFilter.value.add("Auction");
//		request.itemFilter = new ArrayList<ItemFilter>();
//		request.itemFilter.add(itemFilter);
//
//		// Get shared client
//		FindingServicePortType_SOAPClient client = FindingServiceClient.getSharedClient();
//		client.setDebug(true);
//
//		// make API call and register callbacks
//		client.findItemsByCategory(request, new SOAPServiceCallback<FindItemsByCategoryResponse>() {
//
//			@Override
//			public void onSuccess(FindItemsByCategoryResponse responseObject) {
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//					progressDialog = null;
//				}
//
//				if (responseObject.ack == AckValue.SUCCESS) { // handle sucessful response
////					SearchResult searchResult = responseObject.searchResult;
////					if (newSearch) {
////						List<SearchItem> items = (searchResult.item != null) ? searchResult.item : new ArrayList<SearchItem>();
////						ItemAdapter itemAdapter = new ItemAdapter(FindingActivity.this, R.layout.row, items);
////						FindingActivity.this.setListAdapter(itemAdapter);
////					} else {
////						if (searchResult.item != null) {
////							ItemAdapter itemAdapter = (ItemAdapter) FindingActivity.this.getListAdapter();
////							itemAdapter.items.addAll(searchResult.item);
////							itemAdapter.notifyDataSetChanged();
////						}
////					}
//
//				} else { // handle response resident error
//					String errorMessage = responseObject.errorMessage.error.get(0).message;
//					ALog.e(TAG, errorMessage);
//					Toast.makeText(getApplicationContext(),
//							errorMessage,
//							Toast.LENGTH_LONG).show();
//				}
//			}
//
//			@Override
//			public void onFailure(Throwable error, String errorMessage) { // http or parsing error
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//					progressDialog = null;
//				}
//
//				Toast.makeText(FindingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//
//			}
//
//			@Override
//			public void onSOAPFault(Object soapFault) { // soap fault
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//					progressDialog = null;
//				}
//
//				com.leansoft.nano.soap12.Fault fault = (com.leansoft.nano.soap12.Fault)soapFault;
//				Reasontext reasonText = fault.reason.text.get(0);
//
//				ALog.e(TAG, reasonText.value);
//
//				Toast.makeText(FindingActivity.this, reasonText.value, Toast.LENGTH_LONG).show();
//
//			}
//
//		});
//
//	}



	private class ItemAdapter extends ArrayAdapter<SearchItem> {

		private List<SearchItem> items;

		public ItemAdapter(@NonNull Context context, int textViewResourceId,
				@NonNull List<SearchItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@NonNull
        @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}

			final SearchItem item = items.get(position);
			if (item != null) {
				TextView title = (TextView) v.findViewById(R.id.title);
				TextView price = (TextView) v.findViewById(R.id.price);
				TextView bidType = (TextView) v.findViewById(R.id.bids);
				TextView timeleft = (TextView) v.findViewById(R.id.timeleft);

				if (title != null) {
					title.setText(item.title);
				}

				Amount convertedCurrentPrice = item.sellingStatus.convertedCurrentPrice;
				price.setText(eBayUtil.formatCurrencyToString(
						convertedCurrentPrice.value,
						convertedCurrentPrice.currencyId));

				
				String listingType = item.listingInfo.listingType;
				if (listingType.equalsIgnoreCase("FixedPrice")
						|| listingType.equalsIgnoreCase("StoreInventory")) {
					bidType.setBackgroundResource(R.drawable.bin_clear);
					bidType.setText("");
				} else {
					bidType.setBackgroundResource(R.drawable.light_blue_pixel);
					bidType.setText(item.sellingStatus.bidCount + " bids");
				}

				Duration duration = item.sellingStatus.timeLeft;
				if (duration.getDays() == 0 && duration.getHours() == 0
						&& duration.getMinutes() < 10) {
					timeleft.setTextColor(Color.RED);
				} else {
					timeleft.setTextColor(Color.BLACK);
				}

				timeleft.setText(eBayUtil.formatDuration(duration));

				ImageView image = (ImageView) v
						.findViewById(R.id.gallery_icon);
				if (image != null) {
					if (item.galleryURL != null) {
						//image.setImageUrl(item.galleryURL);
						//image.loadImage();
						Glide.with(getApplicationContext()).load(item.galleryURL)
								.asBitmap()
								.centerCrop()
								.diskCacheStrategy(DiskCacheStrategy.ALL)
								.placeholder(R.drawable.placeholder)
								.into(image);
					} else {
						image.setImageResource(R.drawable.placeholder);
					}
				}
				
				// once clicked, navigate to item details page
				v.setOnClickListener(new OnItemClickListener(item.itemId,item.title, v.getContext()));
			}

			return v;
		}
	}
	
	private class OnItemClickListener implements OnClickListener {
		private String mItemId;
		private Context mCxt;
		private String mCategorName;

		OnItemClickListener(String itemId,String categoryName, Context cxt) {
			mItemId = itemId;
			mCxt = cxt;
			mCategorName = categoryName;
		}

		@Override
		public void onClick(View arg0) {
			ALog.d(TAG, "onItemClick at item " + mItemId);
			HashMap<String,Object> actiondata =  new HashMap<>();
			actiondata.put("time", new Date());
			actiondata.put("Item Name", mCategorName);

			BlotoutAnalytics.getInstance().logEvent("Item Selected", actiondata);

			Intent intent = new Intent(mCxt, DetailActivity.class);
			intent.putExtra("ITEM_ID", mItemId);
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

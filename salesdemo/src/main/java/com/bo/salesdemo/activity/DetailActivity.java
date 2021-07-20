package com.bo.salesdemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blotout.analytics.BlotoutAnalytics;
import com.bo.salesdemo.R;
import com.bo.salesdemo.activity.AddedToBasketActivity;
import com.bo.salesdemo.ebay.shopping.api.AckCodeType;
import com.bo.salesdemo.ebay.shopping.api.BuyerPaymentMethodCodeType;
import com.bo.salesdemo.ebay.shopping.api.GetSingleItemRequestType;
import com.bo.salesdemo.ebay.shopping.api.GetSingleItemResponseType;
import com.bo.salesdemo.ebay.shopping.api.ListingTypeCodeType;
import com.bo.salesdemo.ebay.shopping.api.ShoppingServiceClient;
import com.bo.salesdemo.ebay.shopping.api.SimpleItemType;
import com.bo.salesdemo.ebay.shopping.api.client.ShoppingInterface_XMLClient;
import com.bo.salesdemo.model.SingleItemModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ebay.util.eBayUtil;

import com.leansoft.nano.custom.types.Duration;
import com.leansoft.nano.log.ALog;
import com.leansoft.nano.ws.XMLServiceCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class
            .getSimpleName();

    @Nullable
    private ProgressDialog progressDialog;
    private Context mContext;
    SimpleItemType mSimpleItemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        setTitle("Detail Screen");
        BlotoutAnalytics.getInstance().logEvent("Item Detail View",null);

        HashMap<String,Object> item = new HashMap<>();
        item.put("item","iPhone");
        item.put("color","green");
        BlotoutAnalytics.getInstance().logEvent("Add To Cart",item);

        HashMap<String,Object> item1 = new HashMap<>();
        item1.put("addedToCart","iPhone");
        item1.put("color","green");
        BlotoutAnalytics.getInstance().logEvent("myCart",item1);

        HashMap<String,Object> item2 = new HashMap<>();
        item2.put("product","iPhone");
        item2.put("color","green");
        BlotoutAnalytics.getInstance().logEvent("InCart",item2);

        mContext = this;

        String itemId = getIntent().getStringExtra("ITEM_ID");
        this.getSingleItem(itemId);
    }

    // call Shopping GetSingleItem API
    private void getSingleItem(final String itemId) {

        progressDialog = ProgressDialog.show(DetailActivity.this,
                "Please wait...", "Retrieving data...", true, true);

        GetSingleItemRequestType request = new GetSingleItemRequestType();
        request.itemID = itemId;
        request.includeSelector = "Details,ShippingCosts";

        ShoppingInterface_XMLClient client = ShoppingServiceClient.getSharedClient();
        client.setDebug(true);

        client.getSingleItem(request, new XMLServiceCallback<GetSingleItemResponseType>() {

            @Override
            public void onSuccess(@NonNull GetSingleItemResponseType responseObject) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }

                // need more error handling logic in real app
                if (responseObject.ack != AckCodeType.FAILURE) {
                    mSimpleItemType = responseObject.item;

                    DetailActivity.this.updateUI(mSimpleItemType);

                } else { // response resident error
                    String errorMessage = responseObject.errors.get(0).longMessage;
                    ALog.e(TAG, errorMessage);
                    Toast.makeText(getApplicationContext(),
                            errorMessage,
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Throwable error, @Nullable String errorMessage) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }


                // handle HTTP or parsing error
                if (errorMessage != null) {
                    ALog.e(TAG, errorMessage);
                    Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                } else {
                    ALog.e(TAG, error.getMessage(), error);
                    Toast.makeText(DetailActivity.this, error.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }


        });
    }




    private void updateUI(@NonNull final SimpleItemType item) {

        // item title
        TextView titleView = (TextView) findViewById(R.id.detail_title);
        titleView.setText(item.title);

        // item image
        String picUrl = null;
        ImageView image = (ImageView) findViewById(R.id.detail_image);
        if (item.pictureURL != null && item.pictureURL.size() > 0) {
            picUrl = item.pictureURL.get(0);
//            image.setImageUrl(item.pictureURL.get(0));
//            image.loadImage();

            Glide.with(mContext.getApplicationContext()).load(item.pictureURL.get(0))
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .into(image);

        } else {
            image.setImageResource(R.drawable.placeholder);
        }


        image.setVisibility(View.VISIBLE);

        // item id
        TextView itemIdView = (TextView) findViewById(R.id.detail_itemid);
        itemIdView.setText(Html.fromHtml("<b>Item ID:</b>&nbsp;&nbsp;"
                + item.itemID));

        // start time
        TextView startTimeView = (TextView) findViewById(R.id.detail_starttime);
        startTimeView.setText(Html.fromHtml("<b>Start Time:</b>&nbsp;&nbsp;"
                + item.startTime.toLocaleString()));

        // end time
        TextView endTimeView = (TextView) findViewById(R.id.detail_endtime);
        endTimeView.setText(Html.fromHtml("<b>End Time:</b>&nbsp;&nbsp;"
                + item.endTime.toLocaleString()));

        // condition
        TextView conditionView = (TextView) findViewById(R.id.detail_condition);
        String conditionDisplayName = "NA";
        if (item.conditionDisplayName != null) {
            conditionDisplayName = item.conditionDisplayName;
        }
        conditionView.setText(Html.fromHtml("<b>Condition:</b>&nbsp;&nbsp;"
                + conditionDisplayName));

        // item price
        TextView priceView = (TextView) findViewById(R.id.detail_price);
        String currencyStr = (item.convertedCurrentPrice.currencyID == null ? ""
                : item.convertedCurrentPrice.currencyID.name());
        String Price = "";
        if (item.listingType == ListingTypeCodeType.FIXED_PRICE_ITEM
                || item.listingType == ListingTypeCodeType.STORES_FIXED_PRICE) {
            Price = "Buy It Now";
        } else {
            Price = "Current Bid";
        }
        priceView.setText(Html.fromHtml("<b>"
                + Price
                + ":</b>&nbsp;&nbsp;"
                + eBayUtil.formatCurrencyToString(
                item.convertedCurrentPrice.value, currencyStr)));

        // shipping cost
        String shippingCost = "NA";
        if (item.shippingCostSummary != null
                && item.shippingCostSummary.shippingServiceCost != null) {
            currencyStr = (item.shippingCostSummary.shippingServiceCost.currencyID == null ? ""
                    : item.shippingCostSummary.shippingServiceCost.currencyID
                            .name());
            shippingCost = eBayUtil.formatCurrencyToString(
                    item.shippingCostSummary.shippingServiceCost.value,
                    currencyStr);
        }
        TextView shippingCostView = (TextView) findViewById(R.id.detail_shipping);
        shippingCostView.setText(Html
                .fromHtml("<b>Shipping Cost:</b>&nbsp;&nbsp;" + shippingCost));

        // item location
        TextView locationView = (TextView) findViewById(R.id.detail_location);
        locationView.setText(Html.fromHtml("<b>Location</b>&nbsp;&nbsp;"
                + item.location));

        // listing type
        TextView listingTypeView = (TextView) findViewById(R.id.detail_listingtype);
        listingTypeView
                .setText(Html.fromHtml("<b>Listing Type:</b>&nbsp;&nbsp;"
                        + item.listingType));

        // time left
        TextView timeLeftView = (TextView) findViewById(R.id.detail_timeleft);
        Duration duration = item.timeLeft;
        if (duration.getDays() == 0 && duration.getHours() == 0
                && duration.getMinutes() < 10) {
            timeLeftView.setTextColor(Color.RED);
        } else {
            timeLeftView.setTextColor(Color.BLACK);
        }

        timeLeftView.setText(Html.fromHtml("<b>Time Left:</b>&nbsp;&nbsp;"
                + eBayUtil.formatDuration(item.timeLeft)));

        // payment method
        TextView paymentView = (TextView) findViewById(R.id.detail_payment);
        String payments = "";
        if(item != null && item.paymentMethods != null) {
            for (BuyerPaymentMethodCodeType payment : item.paymentMethods) {
                payments += "," + payment;
            }
        }
        payments = payments.replaceFirst(",", "");
        paymentView.setText(Html.fromHtml("<b>Payment Method:</b>&nbsp;&nbsp;"
                + payments));

        // view on eBay
        Button viewBtn = (Button) findViewById(R.id.btn_view);
        viewBtn.setOnClickListener(new ViewOneBayListener(
                item.viewItemURLForNaturalSearch));
        viewBtn.setVisibility(View.GONE);

        // watch item
        final Button btnAddToBasket = (Button) findViewById(R.id.btn_add_to_basket);
        final List<SingleItemModel> singleItemModelList = new ArrayList<>();

        final SingleItemModel singleItemModel = new SingleItemModel();
        singleItemModel.setProductName(item.title);
        singleItemModel.setPrice(String.valueOf(Html.fromHtml("<b>"
                + Price
                + ":</b>&nbsp;&nbsp;"
                + eBayUtil.formatCurrencyToString(
                item.convertedCurrentPrice.value, currencyStr))));
        singleItemModel.setBids(item.location);
        singleItemModel.setThumbnailUrl(picUrl);

       // singleItemModelList.add(singleItemModel);

        btnAddToBasket.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // addToWatchList(item.itemID);
                //ALog.d(TAG, "onItemClick at item " + mItemId);
                Intent intent = new Intent(getApplicationContext()
                        , AddedToBasketActivity.class);

                intent.putExtra("PRICE",singleItemModel.getPrice());
                intent.putExtra("THUMBNAIL",singleItemModel.getThumbnailUrl());
                intent.putExtra("BIDS",singleItemModel.getBids());
                intent.putExtra("PRODUCT_NAME",singleItemModel.getProductName());

                startActivity(intent);
            }

        });
        btnAddToBasket.setVisibility(View.VISIBLE);
    }

    // view item on eBay moile web
    private class ViewOneBayListener implements OnClickListener {

        private String itemUrl;

        public ViewOneBayListener(String itemUrl) {
            this.itemUrl = itemUrl;
        }

        @Override
        public void onClick(View arg0) {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemUrl));
//            startActivity(browserIntent);

            Intent browserIntent = new Intent(mContext, EbayDetailWebViewActivity.class);
            browserIntent.putExtra("URL", itemUrl);
            startActivity(browserIntent);


        }

    }



}

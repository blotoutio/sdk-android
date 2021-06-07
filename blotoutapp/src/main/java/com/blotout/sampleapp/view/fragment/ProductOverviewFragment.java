

package com.blotout.sampleapp.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.blotout.sampleapp.R;
import com.blotout.sampleapp.domain.mock.FakeWebServer;
import com.blotout.sampleapp.model.CenterRepository;
import com.blotout.sampleapp.util.AppConstants;
import com.blotout.sampleapp.util.Utils;
import com.blotout.sampleapp.util.Utils.AnimationType;
import com.blotout.sampleapp.view.activities.ECartHomeActivity;
import com.blotout.sampleapp.view.adapter.ProductsInCategoryPagerAdapter;

import java.util.Set;

public class ProductOverviewFragment extends Fragment {

    // SimpleRecyclerAdapter adapter;
    private KenBurnsView header;
    private Bitmap bitmap;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_category_details,
                container, false);

        getActivity().setTitle("Products");

        // Simulate Web service calls
        FakeWebServer.getFakeWebServer().getAllProducts(
                AppConstants.CURRENT_CATEGORY);



        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view
                .findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        header = (KenBurnsView) view.findViewById(R.id.htab_header);
        header.setImageResource(R.drawable.header);

        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);

        mToolbar = (Toolbar) view.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        setUpUi();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    Utils.switchContent(R.id.frag_container,
                            Utils.HOME_FRAGMENT,
                            ((ECartHomeActivity) (getContext())),
                            AnimationType.SLIDE_RIGHT);

                }
                return true;
            }
        });

        return view;
    }

    private void setUpUi() {

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        bitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette
                        .getDarkVibrantColor(R.color.primary_700);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout
                        .setStatusBarScrimColor(vibrantDarkColor);
            }
        });

        tabLayout
                .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        viewPager.setCurrentItem(tab.getPosition());

                        switch (tab.getPosition()) {
                            case 0:

                                header.setImageResource(R.drawable.header);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                collapsingToolbarLayout
                                                        .setContentScrimColor(vibrantColor);
                                                collapsingToolbarLayout
                                                        .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });
                                break;
                            case 1:

                                header.setImageResource(R.drawable.header_1);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header_1);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                collapsingToolbarLayout
                                                        .setContentScrimColor(vibrantColor);
                                                collapsingToolbarLayout
                                                        .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });

                                break;
                            case 2:

                                header.setImageResource(R.drawable.header2);

                                Bitmap bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header2);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                collapsingToolbarLayout
                                                        .setContentScrimColor(vibrantColor);
                                                collapsingToolbarLayout
                                                        .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });

                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

    }

    private void setupViewPager(ViewPager viewPager) {
        ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(
                getActivity().getSupportFragmentManager());
        Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .keySet();

        for (String string : keys) {
            adapter.addFrag(new ProductListFragment(string), string);
        }

        viewPager.setAdapter(adapter);
    }



}
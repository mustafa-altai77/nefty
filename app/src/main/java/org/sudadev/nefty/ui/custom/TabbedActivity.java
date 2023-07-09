package org.sudadev.nefty.ui.custom;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.sudadev.nefty.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class TabbedActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    protected FloatingActionButton action_button;

    protected List<Fragment> fragments = new ArrayList<>();
    protected List<String> titles = new ArrayList<>();

    private ViewPager viewPager;
    private ItemsFragmentAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapped);

        ButterKnife.bind(this);
        setupToolbar();

        viewPager = findViewById(R.id.list_viewpager);
        tabLayout = findViewById(R.id.list_tabs);
        action_button = findViewById(R.id.action_button);
        action_button.hide();

        prepareFragments();

        Configuration config = getResources().getConfiguration();
        final boolean isArabic = config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

        if (isArabic) {
            Collections.reverse(fragments);
            Collections.reverse(titles);
        }

        adapter = new ItemsFragmentAdapter(getSupportFragmentManager(), this, fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(titles.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (isArabic) {
            viewPager.setCurrentItem(fragments.size() - 1);
        } else {
            viewPager.setCurrentItem(0);
        }

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        if (adapter.getCount() <= 5) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    protected void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    abstract public void prepareFragments();

    abstract public String getActivityTitle();

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5f);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    class ItemsFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;
        private Context context;

        public ItemsFragmentAdapter(FragmentManager fm, Context context,
                                    List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.context = context;
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}

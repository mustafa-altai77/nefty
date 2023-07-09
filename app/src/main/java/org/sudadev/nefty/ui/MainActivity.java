package org.sudadev.nefty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.sudadev.nefty.BuildConfig;
import org.sudadev.nefty.R;
import org.sudadev.nefty.firebase.SendingFirebaseToken;
import org.sudadev.nefty.models.User;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;
import org.sudadev.nefty.ui.custom.BaseActivity;
import org.sudadev.nefty.ui.custom.MessageDialog;
import org.sudadev.nefty.ui.menu.NavigationDrawerFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.home_view_pager)
    ViewPager viewPager;

    @BindView(R.id.list_tabs)
    TabLayout tabLayout;

    private HomeAdapter adapter;
    private NavigationDrawerFragment navigationDrawerFragment;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setupToolbar();
        setUpDrawer();

        SharedPreferencesLocalStorage storage = new SharedPreferencesLocalStorage(this);
        User user = storage.retrieveUser();
        handleNotificationsIfSet();
        initFCM(user);

        prepareFragments(user);

        adapter = new HomeAdapter(getSupportFragmentManager(), this, fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());

        Configuration config = getResources().getConfiguration();
        final boolean isArabic = config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

        if (isArabic) {
            Collections.reverse(fragments);
            Collections.reverse(titles);
        }

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

        if (adapter.getCount() <= 4) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private void handleNotificationsIfSet() {
        String id = getIntent().getStringExtra("id");
        String data_type = getIntent().getStringExtra("data_type");
        String message = getIntent().getStringExtra("message");

        if (data_type != null && !data_type.isEmpty()) {
//            if (data_type.equalsIgnoreCase("NEW_COMPLAINT") || data_type.equalsIgnoreCase("UPDATED_COMPLAINT")){
//                getComplainById(Integer.valueOf(id));
//            }
//            else if (data_type.equalsIgnoreCase("NEW_CONSULTATION_REQUEST") || data_type.equalsIgnoreCase("UPDATED_CONSULTATION_REQUEST")){
//                getConsultationById(Integer.valueOf(id));
//            }
//            else if (data_type.equalsIgnoreCase("NEW_CONTRACT")){
//                getContractById(Integer.valueOf(id));
//            }
//            else if (data_type.equalsIgnoreCase("NEW_FINANCIAL_REQUEST") || data_type.equalsIgnoreCase("PROCCESSED_FINANCIAL_REQUEST")){
//                getFinancialRequestById(Integer.valueOf(id));
//            }
//            else if (data_type.equalsIgnoreCase("ADDED_MONTHLY_REPORT")){
//                getMonthlyReportById(Integer.valueOf(id));
//            }
//            else if (data_type.equalsIgnoreCase("NEW_MESSAGE")){
//                getMessageById(Integer.valueOf(id));
//            }
            if (data_type.equalsIgnoreCase("NEW_INVOICE") || data_type.equalsIgnoreCase("INVOICE_UPDATED")){
                //getQuyeds(Integer.valueOf(id));
            }
            else if (data_type.equalsIgnoreCase("NEW_ENTRY_ADDED") || data_type.equalsIgnoreCase("ENTRY_PENDING_REVIEW") || data_type.equalsIgnoreCase("ENTRY_UPDATED_REVIEWER")){
                //getQuyeds(Integer.valueOf(id));
            }
            else if(data_type.equalsIgnoreCase("SYSTEM_MESSAGE")) {
                //MessageDialog.showMessageDialog(this, getString(R.string.system_message), message);
            }
        }
    }

    private void prepareFragments(User user) {
//        addFragment(UserGroupsListFragment.newInstance(user.getId()), getString(R.string.groups));
//        addFragment(AccountingDataListFragment.newInstance(user.getId(), Constants.AccountingDataStatusEnum.Accepted.ordinal()), getString(R.string.accounting_data));
//        addFragment(QyedesListFragment.newInstance(user.getId(), Constants.QyedStatusEnum.Accepted), getString(R.string.accounting_report));
    }

    private void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (navigationDrawerFragment != null)
            navigationDrawerFragment.updateName();

        SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(this);
        if (!localStorage.isLogged())
            finish();
    }

    private void setUpDrawer() {
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(
                R.id.navigation_drawer_fragment);
        navigationDrawerFragment.setUpDrawer(R.id.navigation_drawer_fragment, drawerLayout, toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initFCM(User user) {
        if (user != null) {
            FirebaseApp.initializeApp(this);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }

                            String firebaseToken = task.getResult().getToken();
                            new SendingFirebaseToken().send(MainActivity.this, firebaseToken);
                            FirebaseMessaging.getInstance().subscribeToTopic(BuildConfig.Topic);
                        }
                    });
        }
    }

    class HomeAdapter
            extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;
        private Context context;

        public HomeAdapter(FragmentManager fm, Context context, List<Fragment> fragments, List<String> titles) {
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

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
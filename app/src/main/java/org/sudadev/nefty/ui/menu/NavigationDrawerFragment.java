package org.sudadev.nefty.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.sudadev.nefty.R;
import org.sudadev.nefty.models.User;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationDrawerFragment extends Fragment {
    @BindView(R.id.navigation_text)
    TextView navigation_text;
    @BindView(R.id.navigation_value)
    TextView navigation_value;
    @BindView(R.id.account_type_text_view)
    TextView account_type_text_view;
    @BindView(R.id.profile_layout)
    View profile_layout;
    @BindView(R.id.navigation_items_list)
    RecyclerView recyclerView;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    public void updateName() {
        final SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(getActivity());

        if (localStorage.isLogged()) {
            User user = localStorage.retrieveUser();
            navigation_text.setText(user.getFullName());
            navigation_value.setText(user.getEmail());
            //account_type_text_view.setText(Constants.showAccountType(getActivity(), user.getAccountType()));

            if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
                Picasso.with(getActivity())
                        .load(user.getImagePath())
                        .into(profile_image);
            }
        }
    }

    private void initViews(View view) {
        initSideMenuList();

        final SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(getActivity());

        if (localStorage.isLogged()) {
            User user = localStorage.retrieveUser();
            initButtons(user);
            getUserInformation();
        }
        else {
            //navigation_text.setText(R.string.guest);
            navigation_value.setText("");
        }
    }

    private void getUserInformation() {
        final SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(getActivity());
        ServiceGenerator.createService(APIService.class, localStorage.getToken())
                .getUser()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User tempUser = response.body();
                            navigation_value.setText(tempUser.getEmail());
                            navigation_text.setText(tempUser.getFullName());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }

    private void initSideMenuList() {
        OnDrawerCloseListener closeListener = new OnDrawerCloseListener() {
            @Override
            public void closeDrawer() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        };

        RecyclerView.Adapter adapter = new HomeMenuNavigationAdapter(getActivity(), closeListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void initButtons(User user) {
        navigation_text.setText(user.getFullName());
        navigation_value.setText(user.getEmail());

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//                intent.putExtra("User", user);
//                startActivity(intent);
            }
        });
    }

    public void setUpDrawer(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public interface OnDrawerCloseListener {
        void closeDrawer();
    }
}

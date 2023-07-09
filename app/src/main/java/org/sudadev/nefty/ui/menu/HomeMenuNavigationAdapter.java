package org.sudadev.nefty.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.sudadev.nefty.R;
import org.sudadev.nefty.models.User;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;
import org.sudadev.nefty.ui.MainActivity;
import org.sudadev.nefty.ui.anonymouse.ChangePasswordActivity;
import org.sudadev.nefty.ui.anonymouse.SplashActivity;
import org.sudadev.nefty.ui.custom.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeMenuNavigationAdapter extends RecyclerView.Adapter<HomeMenuNavigationAdapter.NavigationViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private NavigationDrawerFragment.OnDrawerCloseListener mDrawerLayout;

    private int[] names =
    {
//        R.string.edit_profile,
//        R.string.complains,
//        R.string.consultation,
//        R.string.contracts,
//        R.string.settings,
        R.string.change_password,
        R.string.signout
    };

    public HomeMenuNavigationAdapter(Context context, NavigationDrawerFragment.OnDrawerCloseListener drawerLayout) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mDrawerLayout = drawerLayout;
    }

    @Override
    public HomeMenuNavigationAdapter.NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_navigation, parent, false);
        HomeMenuNavigationAdapter.NavigationViewHolder viewHolder = new HomeMenuNavigationAdapter.NavigationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeMenuNavigationAdapter.NavigationViewHolder holder, final int position) {
        String name = context.getString(names[position]);

        holder.nameTextView.setText(name);

        final SharedPreferencesLocalStorage storage = new SharedPreferencesLocalStorage(context);
        User user = storage.retrieveUser();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("User", user);
                        context.startActivity(intent);
                        break;
                    }

                    case 1: {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        context.startActivity(intent);
                        break;
                    }

                    case 2: {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        context.startActivity(intent);
                        break;
                    }

                    case 3: {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("UserId", user.getId());
                        context.startActivity(intent);
                        break;
                    }

                    case 4: {
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        break;
                    }

                    case 5: {
                        Intent intent = new Intent(context, ChangePasswordActivity.class);
                        context.startActivity(intent);
                        break;
                    }

                    case 6: {
                        storage.logout();
                        Intent intent = new Intent(context, SplashActivity.class);
                        context.startActivity(intent);
                        ((BaseActivity) context).finish();
                        break;
                    }
                }

                if (mDrawerLayout != null)
                    mDrawerLayout.closeDrawer();
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }


    public static class NavigationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.navigationName)
        TextView nameTextView;

        public NavigationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


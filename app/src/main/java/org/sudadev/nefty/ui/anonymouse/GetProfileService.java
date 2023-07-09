package org.sudadev.nefty.ui.anonymouse;

import android.content.Intent;

import org.sudadev.nefty.models.AccessToken;
import org.sudadev.nefty.models.User;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;
import org.sudadev.nefty.ui.custom.BaseActivity;

import retrofit2.Call;
import retrofit2.Response;

public class GetProfileService {
    public static void getUserInformation(AccessToken accessToken,
                                          BaseActivity context, BaseActivity otherActivity) {
        context.showWaitDialog();
        final SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(context);
        localStorage.setAccessToken(accessToken);

        ServiceGenerator.createService(APIService.class, localStorage.getToken())
                .getUser().enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                context.dismissWaitDialog();

                if (response.isSuccessful()) {
                    User user = response.body();
                    localStorage.storeUser(user);

                    Intent intent = new Intent(context, otherActivity.getClass());
                    context.startActivity(intent);

                    context.finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}

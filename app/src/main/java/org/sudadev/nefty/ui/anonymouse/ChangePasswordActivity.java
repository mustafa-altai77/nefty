package org.sudadev.nefty.ui.anonymouse;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.ErrorUtils;
import org.sudadev.nefty.models.ChangePasswordInputModel;
import org.sudadev.nefty.models.ErrorResponse;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;
import org.sudadev.nefty.ui.custom.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.old_password_edit_text)
    EditText old_password_edit_text;
    @BindView(R.id.password_edit_text)
    EditText password_edit_text;
    @BindView(R.id.password_confirmation_edittext)
    EditText password_confirmation_edittext;
    @BindView(R.id.change_button)
    Button change_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        setupToolbar();

        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = old_password_edit_text.getText().toString();
                String password = password_edit_text.getText().toString();
                String password2 = password_confirmation_edittext.getText().toString();

                if (oldPassword.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this,
                            R.string.fill_all_required, Toast.LENGTH_LONG).show();
                }
                else if (!password.equals(password2)) {
                    Toast.makeText(ChangePasswordActivity.this,
                            R.string.passowrd_not_matched, Toast.LENGTH_LONG).show();
                }
                else {
                    sendChangePasswordRequest(oldPassword, password);
                }
            }
        });
    }

    private void sendChangePasswordRequest(String oldPassword, String password) {
        showWaitDialog();
        final SharedPreferencesLocalStorage storage = new SharedPreferencesLocalStorage(ChangePasswordActivity.this);

        ServiceGenerator.createService(APIService.class, storage.getToken())
                .changePassword(new ChangePasswordInputModel(oldPassword, password))
                .enqueue(new Callback<ErrorResponse>() {
                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                        dismissWaitDialog();
                        if (response.isSuccessful()) { // 200 OK
                            storage.logout();
                            Intent intent = new Intent(ChangePasswordActivity.this, SplashActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            ErrorUtils.showError(response, ChangePasswordActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {

                    }
                });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
}

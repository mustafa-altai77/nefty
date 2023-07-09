package org.sudadev.nefty.ui.anonymouse;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.ErrorUtils;
import org.sudadev.nefty.models.ErrorResponse;
import org.sudadev.nefty.models.ForgetPasswordInputModel;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.ui.custom.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mobile_number_text_view)
    TextView mobile_number_text_view;
    @BindView(R.id.identity_number_text_view)
    TextView identity_number_text_view;

    @BindView(R.id.forget_password_button)
    Button forget_password_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        setupToolbar();

        forget_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identityNumber = identity_number_text_view.getText().toString();
                String mobileNumber = mobile_number_text_view.getText().toString();
                if (identityNumber.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this,
                            R.string.fill_all_required, Toast.LENGTH_LONG).show();
                }
                else {
                    sendForgetPasswordRequest(identityNumber, mobileNumber);
                }
            }
        });
    }

    private void sendForgetPasswordRequest(final String identityNumber, final String mobileNumber) {
        showWaitDialog();
        ServiceGenerator.createService(APIService.class, null)
                .forgetPassword(new ForgetPasswordInputModel(identityNumber, mobileNumber))
                .enqueue(new Callback<ErrorResponse>() {
                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                        dismissWaitDialog();
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(ForgetPasswordActivity.this,
                                    ResetPasswordActivity.class);
                            intent.putExtra("Mobile", mobileNumber);
                            intent.putExtra("IdentityNumber", identityNumber);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            ErrorResponse errorResponse = ErrorUtils.showError(response, ForgetPasswordActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {

                    }
                });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.forget_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5f);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}



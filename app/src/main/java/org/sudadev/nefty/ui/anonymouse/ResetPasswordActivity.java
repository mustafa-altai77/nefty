package org.sudadev.nefty.ui.anonymouse;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.ErrorUtils;
import org.sudadev.nefty.models.AccessToken;
import org.sudadev.nefty.models.ErrorResponse;
import org.sudadev.nefty.models.ForgetPasswordInputModel;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.ui.MainActivity;
import org.sudadev.nefty.ui.custom.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.code_edit_text)
    EditText code_edit_text;
    @BindView(R.id.password_edit_text)
    EditText password_edit_text;
    @BindView(R.id.password_confirmation_edittext)
    EditText password_confirmation_edittext;
    @BindView(R.id.resend_text_view)
    TextView resend_text_view;
    @BindView(R.id.reset_button)
    Button reset_button;
    @BindView(R.id.counter)
    TextView counter;
    @BindView(R.id.resent_layout)
    View resent_layout;
    @BindView(R.id.timer_layout)
    View timer_layout;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        setupToolbar();

        final String mobile = getIntent().getStringExtra("Mobile");
        final String identityNumber = getIntent().getStringExtra("IdentityNumber");

        resent_layout.setVisibility(View.INVISIBLE);
        countDownTimer();

        resent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_layout.setVisibility(View.VISIBLE);
                resent_layout.setVisibility(View.INVISIBLE);
                countDownTimer();
                sendForgetPasswordRequest(mobile, identityNumber);
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = code_edit_text.getText().toString();
                String password = password_edit_text.getText().toString();
                String password2 = password_confirmation_edittext.getText().toString();

                if (mobile.isEmpty() || identityNumber.isEmpty() ||
                        code.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this,
                            R.string.fill_all_required, Toast.LENGTH_LONG).show();
                }
                else if (!password.equals(password2)) {
                    Toast.makeText(ResetPasswordActivity.this,
                            R.string.passowrd_not_matched, Toast.LENGTH_LONG).show();
                }
                else {
                    sendResetPasswordRequest(mobile, identityNumber, code, password);
                }
            }
        });

        resend_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_layout.setVisibility(View.VISIBLE);
                resent_layout.setVisibility(View.INVISIBLE);
                countDownTimer();
                sendForgetPasswordRequest(mobile, identityNumber);
            }
        });
    }

    private void sendForgetPasswordRequest(final String mobile, final String identityNumber) {
        showWaitDialog();
        ServiceGenerator.createService(APIService.class, null)
                .forgetPassword(new ForgetPasswordInputModel(mobile, identityNumber))
                .enqueue(new Callback<ErrorResponse>() {
                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                        dismissWaitDialog();
                        if (response.isSuccessful()) {

                        }
                        else {
                            ErrorResponse errorResponse = ErrorUtils.showError(response, ResetPasswordActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {
                        dismissWaitDialog();
                    }
                });
    }

    void countDownTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                counter.setText(l / 1000 + " " + getString(R.string.seconds));
            }

            @Override
            public void onFinish() {
                resent_layout.setVisibility(View.VISIBLE);
                timer_layout.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void sendResetPasswordRequest(final String mobile, final String identityNumber,
                                          final String code, final String password) {
        showWaitDialog();
        ServiceGenerator.createService(APIService.class, null)
                .resetPassword(new ForgetPasswordInputModel(mobile, identityNumber, code, password))
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        dismissWaitDialog();
                        if (response.isSuccessful()) { // 200 OK
                            final AccessToken accessToken = response.body();
                            if (accessToken != null) {
                                Toast.makeText(ResetPasswordActivity.this,
                                        getString(R.string.reset_password_successfully), Toast.LENGTH_LONG).show();

                                GetProfileService.getUserInformation(accessToken,
                                        ResetPasswordActivity.this, new MainActivity());
                            }
                        }
                        else {
                            ErrorUtils.showError(response, ResetPasswordActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {

                    }
                });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.reset_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5f);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this,
                        ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

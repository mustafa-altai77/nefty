package org.sudadev.nefty.ui.anonymouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import org.sudadev.nefty.R;
import org.sudadev.nefty.models.AccessToken;
import org.sudadev.nefty.models.EmailConfirmationInputModel;
import org.sudadev.nefty.models.MobileConfirmationInputModel;
import org.sudadev.nefty.models.User;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;
import org.sudadev.nefty.ui.MainActivity;
import org.sudadev.nefty.ui.custom.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends BaseActivity {

    @BindView(R.id.otp_view)
    PinEntryEditText otpView;

    @BindView(R.id.otp_label)
    TextView otp_label;

    @BindView(R.id.resend_otp)
    TextView resendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        ButterKnife.bind(this);

        final String email = getIntent().getStringExtra("Email");
        if (email == null) {
            otp_label.setText(R.string.enter_sms_otp);

            resendOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else {
            resendOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        if (otpView != null) {
            otpView.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.toString().length() != 5) {
                        Toast.makeText(OtpActivity.this, R.string.fill_all_required,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        final String email = getIntent().getStringExtra("Email");
                        if (email != null) {
                            getToken(email, str.toString());
                        }

                        String mobile = getIntent().getStringExtra("Mobile");
                        if (mobile != null) {
                            confirmMobile(mobile, str.toString());
                        }
                    }
                }
            });
        }
    }

    private void confirmMobile(String mobile, String otp) {
        showWaitDialog();
        MobileConfirmationInputModel tokenInputModel =
                new MobileConfirmationInputModel(otp);

        final SharedPreferencesLocalStorage storage = new SharedPreferencesLocalStorage(this);
        ServiceGenerator.createService(APIService.class, storage.getToken())
                .confirmMobile(tokenInputModel)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        dismissWaitDialog();

                        if (response.isSuccessful()) {
                            final User user = response.body();
                            if (user != null) {
                                storage.storeUser(user);
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(OtpActivity.this,
                                    getString(R.string.invalid_otp), Toast.LENGTH_LONG).show();
                            otpView.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
    }

    private void getToken(String email, String otp) {
        showWaitDialog();
        EmailConfirmationInputModel tokenInputModel =
                new EmailConfirmationInputModel(email, otp);

        ServiceGenerator.createService(APIService.class, null)
                .confirmEmail(tokenInputModel)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        dismissWaitDialog();

                        if (response.isSuccessful()) {
                            final AccessToken accessToken = response.body();
                            if (accessToken != null) {
                                Toast.makeText(OtpActivity.this,
                                        getString(R.string.login_sucessfully), Toast.LENGTH_LONG).show();

                                GetProfileService.getUserInformation(accessToken,
                                        OtpActivity.this, new MainActivity());
                            }
                        }
                        else {
                            Toast.makeText(OtpActivity.this,
                                    getString(R.string.invalid_otp), Toast.LENGTH_LONG).show();
                            otpView.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
    }
}

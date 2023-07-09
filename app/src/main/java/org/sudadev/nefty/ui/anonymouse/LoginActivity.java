package org.sudadev.nefty.ui.anonymouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.ErrorUtils;
import org.sudadev.nefty.models.AccessToken;
import org.sudadev.nefty.models.ErrorResponse;
import org.sudadev.nefty.models.LoginInputModel;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.ui.MainActivity;
import org.sudadev.nefty.ui.custom.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.identity_number_edit_text)
    EditText identity_numberEditText;

    @BindView(R.id.password_edit_text)
    EditText passwordEditText;

    @BindView(R.id.register_new_account_textview)
    TextView registerAccountButton;

    @BindView(R.id.login_button)
    Button loginButton;

    @BindView(R.id.forget_password_text_view)
    TextView forget_password_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        registerAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forget_password_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String identityNumber = identity_numberEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                LoginInputModel loginInputModel = new LoginInputModel();
                loginInputModel.setIdNumber(identityNumber);
                loginInputModel.setPassword(password);

                FormValidator formValidator = new FormValidator();
                boolean isValidForm = formValidator.isValid();

                if (isValidForm) {
                    showWaitDialog();
                    ServiceGenerator.createService(APIService.class, null)
                            .login(loginInputModel).enqueue(new retrofit2.Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            dismissWaitDialog();
                            if (response.isSuccessful()) { // 200 OK
                                final AccessToken accessToken = response.body();
                                if (accessToken != null) {
                                    Toast.makeText(LoginActivity.this,
                                            getString(R.string.login_sucessfully), Toast.LENGTH_LONG).show();

                                    GetProfileService.getUserInformation(accessToken,
                                            LoginActivity.this, new MainActivity());
                                }
                            }
                            else {
                                ErrorResponse errorResponse = ErrorUtils.showError(response, LoginActivity.this);
                                if (errorResponse != null) {
                                    if (errorResponse.getCode().equals("email_confirmation")) {
                                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                                        intent.putExtra("Mobile", identityNumber);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, R.string.fill_all_required,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class FormValidator {
        public boolean isValid() {
            return validateIdNumber() & validatePassword();
        }

        public boolean validateIdNumber() {
            if (identity_numberEditText.getText().toString().isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

        public boolean validatePassword() {
            if (passwordEditText.getText().toString().isEmpty()) {
                return false;
            } else {
                return true;
            }
        }
    }
}

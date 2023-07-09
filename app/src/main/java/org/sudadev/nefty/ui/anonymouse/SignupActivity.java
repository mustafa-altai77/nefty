package org.sudadev.nefty.ui.anonymouse;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.ErrorUtils;
import org.sudadev.nefty.common.SpanningUtil;
import org.sudadev.nefty.models.RegisterInputModel;
import org.sudadev.nefty.models.User;
import org.sudadev.nefty.networks.APIService;
import org.sudadev.nefty.networks.ServiceGenerator;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;
import org.sudadev.nefty.ui.custom.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.full_name_edittext)
    EditText fullname_edittext;
    @BindView(R.id.mobile_edittext)
    EditText mobile_edittext;
    @BindView(R.id.identity_number_edittext)
    EditText identity_number_edittext;
    @BindView(R.id.email_edittext)
    EditText email_edittext;
    @BindView(R.id.password_edittext)
    EditText password_edittext;
    @BindView(R.id.password_confirmation_edittext)
    EditText password_confirmation_edittext;

    @BindView(R.id.full_name_text_view)
    TextView full_name_text_view;
    @BindView(R.id.email_text_view)
    TextView email_text_view;
    @BindView(R.id.password_text_view)
    TextView password_text_view;
    @BindView(R.id.password_confirmation_text_view)
    TextView password_confirmation_text_view;
    @BindView(R.id.mobile_number_text_view)
    TextView mobile_number_text_view;
    @BindView(R.id.identity_number_text_view)
    TextView identity_number_text_view;
    @BindView(R.id.register_button)
    Button registerButton;
    @BindView(R.id.checkbox_terms)
    CheckBox checkbox_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        checkbox_terms.setMovementMethod(LinkMovementMethod.getInstance());
        setupToolbar();

        addAsterisks();
    }

    private void addAsterisks() {
        SpanningUtil.putAsteriskString(full_name_text_view);
        SpanningUtil.putAsteriskString(identity_number_text_view);
        SpanningUtil.putAsteriskString(password_text_view);
        SpanningUtil.putAsteriskString(password_confirmation_text_view);
        SpanningUtil.putAsteriskString(mobile_number_text_view);
    }

    private void register() {
        boolean emptyFields =
                fullname_edittext.getText().toString().isEmpty() ||
                mobile_edittext.getText().toString().isEmpty() ||
                password_edittext.getText().toString().isEmpty() ||
                identity_number_edittext.getText().toString().isEmpty() ||
                password_confirmation_edittext.getText().toString().isEmpty();

        if (emptyFields){
            Toast.makeText(SignupActivity.this, getString(R.string.fill_all_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkbox_terms.isChecked()) {
            Toast.makeText(SignupActivity.this,
                    getString(R.string.you_cannot_register_without_accepting_terms),
                    Toast.LENGTH_SHORT).show();
            return;
        }
//        boolean isValidNumber = mCountryCodePicker.isValidFullNumber();
//        if (!isValidNumber){
//            Toast.makeText(SignupActivity.this, getString(R.string.invalid_mobile), Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (!password_confirmation_edittext.getText().toString().equals(password_edittext.getText().toString())) {
            Toast.makeText(SignupActivity.this, getString(R.string.passowrd_not_matched), Toast.LENGTH_SHORT).show();
            return;
        }

        if( mobile_edittext.getText().toString().length() < 8) {
            Toast.makeText(this, getString(R.string.invalid_mobile), Toast.LENGTH_SHORT).show();
            return;
        }

//        if (!isValidEmailAddress(email_edittext.getText().toString())) {
//            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
//            return;
//        }

        RegisterInputModel model = new RegisterInputModel(
                fullname_edittext.getText().toString(),
                email_edittext.getText().toString(),
                mobile_edittext.getText().toString(),
                password_edittext.getText().toString(),
                identity_number_edittext.getText().toString());

        ServiceGenerator.createService(APIService.class, null)
                .register(model).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this,
                            getString(R.string.register_successfully), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignupActivity.this, OtpActivity.class);
                    intent.putExtra("Mobile", mobile_edittext.getText().toString());
                    startActivity(intent);
                    finish();
                }
                else {
                    ErrorUtils.showError(response, SignupActivity.this);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Signup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5f);
        }

        SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localStorage.isLogged()) {
                    onBackPressed();
                }
                else {
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(this);
        if (localStorage.isLogged()) {
            finish();
        }
        else {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}


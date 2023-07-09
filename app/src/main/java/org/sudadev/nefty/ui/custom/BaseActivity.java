package org.sudadev.nefty.ui.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.LocaleContextWrapper;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    @Override
    protected void attachBaseContext(Context base) {
        SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(base);
        ContextWrapper localeContextWrapper = LocaleContextWrapper.wrap(base, localStorage.getLanguage());
        ContextWrapper calligraphyContextWrapper = CalligraphyContextWrapper.wrap(localeContextWrapper);
        super.attachBaseContext(calligraphyContextWrapper);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showWaitDialog() {
        if (dialog ==null){
            dialog = ProgressDialog.show(this, "",
                    getString(R.string.please_wait), true);
        }else {
            dialog.show();
        }
    }

    public void showWaitDialog(String msg) {
        if (dialog ==null){
            dialog = ProgressDialog.show(this, "",
                    msg, true);
        }else {
            dialog.show();
        }
    }

    public void dismissWaitDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
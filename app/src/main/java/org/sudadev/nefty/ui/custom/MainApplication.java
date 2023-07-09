package org.sudadev.nefty.ui.custom;

import android.app.Application;

import org.sudadev.nefty.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // font
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/din.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}

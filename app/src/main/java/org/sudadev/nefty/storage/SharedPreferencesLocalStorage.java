package org.sudadev.nefty.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.sudadev.nefty.models.AccessToken;
import org.sudadev.nefty.models.User;

import java.util.Locale;

public class SharedPreferencesLocalStorage {
    private final Context _context;
    private final SharedPreferences _sharedPreferences;
    private final SharedPreferences.Editor _editor;

    private final String SELECTED_LANGUAGE = "com.zofirm.Selected.Language";
    private final String FileName = "com.zofirm.data.xml";
    private final String AccessTokenField = "com.zofirm.AccessToken";
    private final String UserField = "com.zofirm.UserField";

    public SharedPreferencesLocalStorage(Context context) {
        _context = context;
        _sharedPreferences = _context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        _editor = _sharedPreferences.edit();
    }

    public void setAccessToken(AccessToken accessToken) {
        Gson gson = new Gson();
        String accessTokenAsString = gson.toJson(accessToken);
        _editor.putString(AccessTokenField, accessTokenAsString);
        _editor.commit();
    }

    public void storeUser(User user) {
        Gson gson = new Gson();
        String result = gson.toJson(user);
        _editor.putString(UserField, result);
        _editor.commit();
    }

    public void logout() {
        _editor.remove(AccessTokenField);
        _editor.remove(UserField);
        _editor.commit();
    }

    public User retrieveUser() {
        Gson gson = new Gson();
        String result = _sharedPreferences.getString(UserField, "");
        return gson.fromJson(result, User.class);
    }

    public boolean isLogged() {
        return retrieveUser() != null;
    }

    public String getToken() {
        Gson gson = new Gson();
        String result = _sharedPreferences.getString(AccessTokenField, "");
        if(result.isEmpty())
            return null;
        AccessToken accessToken = gson.fromJson(result, AccessToken.class);
        return "Bearer " + accessToken.getAccessToken();
    }

    public String getLanguage() {
        String defaultLanguage = Locale.getDefault().getLanguage();
        defaultLanguage = "ar";
        return _sharedPreferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    public void setLanguage(String language) {
        _editor.putString(SELECTED_LANGUAGE, language);
        _editor.commit();
    }
}

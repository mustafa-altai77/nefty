package org.sudadev.nefty.networks;

import org.sudadev.nefty.models.AccessToken;
import org.sudadev.nefty.models.ChangePasswordInputModel;
import org.sudadev.nefty.models.EditProfileModel;
import org.sudadev.nefty.models.EmailConfirmationInputModel;
import org.sudadev.nefty.models.ErrorResponse;
import org.sudadev.nefty.models.FileModel;
import org.sudadev.nefty.models.ForgetPasswordInputModel;
import org.sudadev.nefty.models.LoginInputModel;
import org.sudadev.nefty.models.MobileConfirmationInputModel;
import org.sudadev.nefty.models.RegisterInputModel;
import org.sudadev.nefty.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    /////////////////////////////////////////  Account API
    @POST("/auth/register")
    Call<User> register(@Body RegisterInputModel model);

    @POST("/auth/verfied")
    Call<User> confirmMobile(@Body MobileConfirmationInputModel model);

    @POST("/auth")
    @FormUrlEncoded
    Call<AccessToken> login(@Body LoginInputModel model);

    @POST("/api/accounts/confiramtion_email")
    Call<AccessToken> confirmEmail(@Body EmailConfirmationInputModel model);

    @POST("/auth/forget")
    Call<ErrorResponse> forgetPassword(@Body ForgetPasswordInputModel model);

    @POST("/auth/reset")
    Call<AccessToken> resetPassword(@Body ForgetPasswordInputModel model);

    @POST("/api/accounts/change_password")
    Call<ErrorResponse> changePassword(@Body ChangePasswordInputModel model);

    @POST("/api/accounts/firebasetoken")
    Call<String> uploadFirebaseToken(@Query("token") String token,
                                     @Query("os") String os);

    /////////////////////////////////////////  Profile API
    @GET("/auth/owner")
    Call<User> getUser();

    @PUT("/auth/owner")
    Call<User> editProfile(@Body EditProfileModel model);

//    @GET("/api/accounts/notifications")
//    Call<List<NotificationViewModel>> getUserNotifications(@Query("page") int page);
//
//    @PUT("/api/accounts/notifications/{notificationId}")
//    Call<ErrorResponse> setNotificationReadFlag(@Path("notificationId") int notificationId);

    /////////////////////////////////////////  Helper
    @Multipart
    @POST("/api/uploads")
    Call<FileModel> uploadFile(@Part MultipartBody.Part file);
}

package com.example.esbooking;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register.php")
    Call<ResponseBody> registerUser(@Body User user);

    @POST("login.php")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @FormUrlEncoded
    @POST("reset_password.php") // Replace "reset-password" with your actual endpoint
    Call<ResponseBody> sendPasswordResetEmail(@Field("email") String email);


    @FormUrlEncoded
    @POST("verify_otp_for_password_reset.php")
    Call<ResponseBody> verifyOtpForPasswordReset(
            @Field("email") String email,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST("forgetpassword_change_password.php") // Change to your API endpoint for changing password
    Call<ResponseBody> changePassword(
            @Field("email") String email,
            @Field("new_password") String newPassword
    );


}

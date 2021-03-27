package com.example.modawana;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface api_requests {

    @GET("api/users")
    Call<List<ApiClass>> getusers();

    @GET("api/posts")
    Call<List<Get_Grammer>> get_grammer();

    @GET("api/words")
    Call<List<items>> getwords();

    @POST("api/register")
    Call<ApiClass> postusers(@Body ApiClass apiClass);

    @POST("api/login")
    Call<ApiClass> login_user(@Body ApiClass apiClass);

    @DELETE("api/users/{email}")
    Call<Void> delete(@Path("email") String email);

    @POST("api/users/addFavWord")
    Call<post_fav> post_fav (@Body post_fav post_fav);

    @POST("api/comments")
    Call<Post_comment> post_comment(@Body Post_comment comment);

    @POST("api/reply")
    Call<Post_reply> post_reply (@Body Post_reply reply);


}

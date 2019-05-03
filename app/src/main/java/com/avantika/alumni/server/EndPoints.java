package com.avantika.alumni.server;

import com.avantika.alumni.parameters.Assoc_Projects;
import com.avantika.alumni.parameters.Authentication;
import com.avantika.alumni.parameters.Directory;
import com.avantika.alumni.parameters.Events;
import com.avantika.alumni.parameters.IndustryOffers;
import com.avantika.alumni.parameters.Industry_Domains;
import com.avantika.alumni.parameters.News;
import com.avantika.alumni.parameters.Univ_Projects;
import com.avantika.alumni.parameters.WallPosts;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EndPoints {
    @GET("Alumni/actions/authentication.php")
    Call<Authentication> getValidation(@Query("email") String email);

    @GET("Alumni/actions/events.php")
    Call<Events[]> getEvents();

    @GET("Alumni/actions/news.php")
    Call<News[]> getNews();

    @GET("Alumni/actions/directory.php")
    Call<Directory> getDirectory();

    @GET("Alumni/actions/assoc_projects.php")
    Call<Assoc_Projects[]> getAssocProjects();

    @GET("Alumni/actions/univ_projects.php")
    Call<Univ_Projects[]> getUnivProjects();

    @GET("Alumni/actions/allIndustryOffers.php")
    Call<IndustryOffers[]> getAllIndustryOffers();

    @GET("Alumni/actions/recommendedIndustryOffers.php")
    Call<IndustryOffers[]> getRecommendedIndustryOffers(@Query("email") String email);

    @GET("Alumni/actions/getWallPosts.php")
    Call<WallPosts[]> getPosts();

    @GET("Alumni/actions/getDomainList.php")
    Call<Industry_Domains[]> getDomains();

    @FormUrlEncoded
    @POST("Alumni/actions/addQualification.php")
    Call<JSONObject> addQualification(
            @Field("title") String title,
            @Field("startDate") String startDate,
            @Field("endDate") String endDate,
            @Field("email") String email,
            @Field("domainId") String Domain_ID
    );

    @POST("Alumni/actions/saveWallPosts.php")
    Call<WallPosts> savePosts(@Body WallPosts post);
}

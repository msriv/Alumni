package com.avantika.alumni.server;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avantika.alumni.parameters.Authentication;
import com.avantika.alumni.parameters.IndustryOffers;
import com.avantika.alumni.parameters.News;
import com.avantika.alumni.parameters.WallPosts;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;

import static com.avantika.alumni.activities.MainActivity.TAG;
import static com.avantika.alumni.parameters.Intents.ALL_INDUSTRY_ACTION;
import static com.avantika.alumni.parameters.Intents.AUTHENTICATION_ACTION;
import static com.avantika.alumni.parameters.Intents.NEWS_ACTION;
import static com.avantika.alumni.parameters.Intents.POSTS_ACTION;
import static com.avantika.alumni.parameters.Intents.RECOMMENDED_INDUSTRY_ACTION;

public class ServerFetch extends IntentService {

    public ServerFetch(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service");
        String request = intent.getStringExtra("request");
        switch(request){
            case "auth": {
                Log.d(TAG, "auth request for email " + intent.getStringExtra("email"));
                authenticate(intent.getStringExtra("email"));
            }break;
            case "news": {
                Log.d(TAG, "News Call");
                news();
            }break;
            // email = "manas.choubal@avantika.edu.in" and request = "recommendedOffers"
            case "recommendedOffers":{
                Log.d(TAG, "Recommended Offers Call for "+ intent.getStringExtra("email"));
                recommendedOffers(intent.getStringExtra("email"));
            }
            break;
            case "allOffers":{
                Log.d(TAG, "All Offers Call for");
                allOffers();
            }
            break;
            case "posts": {
                Log.d(TAG, "Getting all posts...");
                getPosts();
            }
        }
    }

    private void getPosts() {
        final Call<WallPosts[]> postsCall = ServiceClient.getRetroFit().getPosts();
        try {
            Response<WallPosts[]> response = postsCall.execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                WallPosts[] posts = response.body();
                Log.d(TAG, "Response: " + posts[0].toString());
                Log.d(TAG, "Response: " + posts[0].postPhoto);
                Log.d(TAG, "Response: " + posts[1].postPhoto);
                Intent returningIntent = new Intent(POSTS_ACTION);
                String postsJson = new Gson().toJson(posts);
                returningIntent.putExtra("posts", postsJson);
                sendBroadcast(returningIntent);
            } else {
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void allOffers() {
        final Call<IndustryOffers[]> allOffersCall = ServiceClient.getRetroFit().getAllIndustryOffers();
        try{
            Response<IndustryOffers[]> response = allOffersCall.execute();
            if(response.code() == HttpURLConnection.HTTP_OK){
                IndustryOffers[] offersBody = response.body();
                Log.d(TAG, "Response: " + offersBody[0].toString());
                Intent returningIntent = new Intent(ALL_INDUSTRY_ACTION);
                String offersJson = new Gson().toJson(offersBody);
                returningIntent.putExtra("allOffers",offersJson);
                Log.d(TAG, "All Offers Call");
                sendBroadcast(returningIntent);
            }else{
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void recommendedOffers(String email) {
        final Call<IndustryOffers[]> recommendedOffersCall = ServiceClient.getRetroFit().getRecommendedIndustryOffers(email);
        try{
            Response<IndustryOffers[]> response = recommendedOffersCall.execute();
            if(response.code() == HttpURLConnection.HTTP_OK){
                IndustryOffers[] offersBody = response.body();
                Log.d(TAG, "Response: " + offersBody[0].toString());
                Intent returningIntent = new Intent(RECOMMENDED_INDUSTRY_ACTION);
                String offersJson = new Gson().toJson(offersBody);
                returningIntent.putExtra("recommendedOffers",offersJson);
                Log.d(TAG, "Recommended Offers Call");
                sendBroadcast(returningIntent);
            }else{
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void news() {
        final Call<News> newsCall = ServiceClient.getRetroFit().getNews();
        try{
            Response<News> response = newsCall.execute();
            if(response.code() == HttpURLConnection.HTTP_OK){
                News newsBody = response.body();
                Intent returningIntent = new Intent(NEWS_ACTION);
                String newsJson = new Gson().toJson(newsBody);
                returningIntent.putExtra("news", newsJson);
                Log.d(TAG, "News Call 1");
                sendBroadcast(returningIntent);
            }else {
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void authenticate(String email){
        final Call<Authentication> authCall = ServiceClient.getRetroFit().getValidation(email);
        try {
            Response<Authentication> response = authCall.execute();
            if(response.code() == HttpURLConnection.HTTP_OK){
                Authentication paramAuth = response.body();
                Intent returningIntent = new Intent(AUTHENTICATION_ACTION);
                String profileJson = new Gson().toJson(paramAuth);
                returningIntent.putExtra("profile", profileJson);
                sendBroadcast(returningIntent);
            }else {
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }





}

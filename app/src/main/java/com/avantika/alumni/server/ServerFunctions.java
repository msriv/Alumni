package com.avantika.alumni.server;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avantika.alumni.database.AlumniDatabase;
import com.avantika.alumni.database.DatabaseFunctions;
import com.avantika.alumni.parameters.Assoc_Projects;
import com.avantika.alumni.parameters.Authentication;
import com.avantika.alumni.parameters.Events;
import com.avantika.alumni.parameters.IndustryOffers;
import com.avantika.alumni.parameters.Industry_Domains;
import com.avantika.alumni.parameters.News;
import com.avantika.alumni.parameters.Univ_Projects;
import com.avantika.alumni.parameters.WallPosts;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;

import static com.avantika.alumni.activities.MainActivity.TAG;
import static com.avantika.alumni.parameters.Intents.ADD_QUALIFICATION_ACTION;
import static com.avantika.alumni.parameters.Intents.ALL_INDUSTRY_ACTION;
import static com.avantika.alumni.parameters.Intents.ASSOC_PROJ_ACTION;
import static com.avantika.alumni.parameters.Intents.AUTHENTICATION_ACTION;
import static com.avantika.alumni.parameters.Intents.EVENTS_ACTION;
import static com.avantika.alumni.parameters.Intents.NEWS_ACTION;
import static com.avantika.alumni.parameters.Intents.POSTS_ACTION;
import static com.avantika.alumni.parameters.Intents.RECOMMENDED_INDUSTRY_ACTION;
import static com.avantika.alumni.parameters.Intents.UNIV_PROJ_ACTION;
import static com.avantika.alumni.parameters.SharedPrefFiles.STORAGE_FILE;

public class ServerFunctions extends IntentService {

    public ServerFunctions() {
        super(TAG);
    }

    private AlumniDatabase db;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service");
        db = Room.databaseBuilder(getApplicationContext(),
                AlumniDatabase.class, "alumni.db").fallbackToDestructiveMigration().build();
        String request = intent.getStringExtra("request");
        switch(request){
            case "auth": {
                Log.d(TAG, "auth request for email " + intent.getStringExtra("email"));
                authenticate(intent.getStringExtra("email"));
            }break;
            case "news": {
                Log.d(TAG, "News Call");
                news();
            }
            break;
            case "events": {
                Log.d(TAG, "Events Call");
                events();
            }break;
            case "assoc_projects": {
                Log.d(TAG, "AssocProjects Call");
                assoc_projects();
            }
            break;
            case "univ_projects": {
                Log.d(TAG, "University Call");
                univ_projects();
            }
            break;
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
            break;
            case "domains": {
                Log.d(TAG, "Getting domain list and saving...");
                getDomains();
            }
            break;
            case "addQualification": {
                Log.d(TAG, "Adding Qualification");
                String title = intent.getStringExtra("title");
                Log.d(TAG, "title" + title);
                String startDate = intent.getStringExtra("startDate");
                Log.d(TAG, "startDate" + startDate);
                String endDate = intent.getStringExtra("endDate");
                Log.d(TAG, "endDate" + endDate);
                String domainName = intent.getStringExtra("domainName");
                Log.d(TAG, "domainName" + domainName);
                addQualification(title, startDate, endDate, domainName);
            }
            break;
        }
    }

    private void assoc_projects() {
        final Call<Assoc_Projects[]> assocProjCall = ServiceClient.getRetroFit().getAssocProjects();
        try {
            Response<Assoc_Projects[]> response = assocProjCall.execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                Assoc_Projects[] assocProjectBody = response.body();
                db.assocProjectsDao().deleteAllProjects();
                db.assocProjectsDao().insertAll(assocProjectBody);
                Intent returningIntent = new Intent(ASSOC_PROJ_ACTION);
                String assocProjectJson = new Gson().toJson(assocProjectBody);
                returningIntent.putExtra("projectData", assocProjectJson);
                Log.d(TAG, "Assoc Project Call 1");
                sendBroadcast(returningIntent);
            } else {
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void univ_projects() {
        final Call<Univ_Projects[]> univProjCall = ServiceClient.getRetroFit().getUnivProjects();
        try {
            Response<Univ_Projects[]> response = univProjCall.execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                Univ_Projects[] univProjectBody = response.body();
                db.univProjectsDao().deleteAllProjects();
                db.univProjectsDao().insertAll(univProjectBody);
                Intent returningIntent = new Intent(UNIV_PROJ_ACTION);
                String univProjectJson = new Gson().toJson(univProjectBody);
                returningIntent.putExtra("projectData", univProjectJson);
                Log.d(TAG, "Univ Project Call 1");
                sendBroadcast(returningIntent);
            } else {
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addQualification(String title, String startDate, String endDate, String domainName) {
        SharedPreferences sharedPref = getSharedPreferences(STORAGE_FILE, Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String domainId = db.domainDao().getDomainID(domainName);
        Log.d(TAG, "Domain ID" + domainId);
        final Call<JSONObject> qualificationCall = ServiceClient.getRetroFit().addQualification(title, startDate, endDate, email, domainId);
        try {
            Response<JSONObject> jsonResponse = qualificationCall.execute();
            Log.d(TAG, "Response for Adding Qualification: " + jsonResponse.body());
            if (jsonResponse.code() == HttpURLConnection.HTTP_OK) {
                boolean error = jsonResponse.body().getBoolean("error");
                Intent returningIntent = new Intent(ADD_QUALIFICATION_ACTION);
                returningIntent.putExtra("error", error);
                sendBroadcast(returningIntent);

            } else {
                Log.e(TAG, "HTTP Error" + jsonResponse.code() + " " + jsonResponse.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDomains() {
        final Call<Industry_Domains[]> domainsCall = ServiceClient.getRetroFit().getDomains();
        try {
            Response<Industry_Domains[]> response = domainsCall.execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                Industry_Domains[] domains = response.body();
                String domainsJson = new Gson().toJson(domains);
                Intent dbIntent = new Intent(this, DatabaseFunctions.class);
                dbIntent.putExtra("request", "domainList");
                dbIntent.putExtra("domainList", domainsJson);
                startService(dbIntent);
            } else {
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPosts() {
        final Call<WallPosts[]> postsCall = ServiceClient.getRetroFit().getPosts();
        try {
            Response<WallPosts[]> response = postsCall.execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                WallPosts[] posts = response.body();
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


    private void events() {
        final Call<Events[]> eventsCall = ServiceClient.getRetroFit().getEvents();
        try {
            Response<Events[]> response = eventsCall.execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                Events[] eventsBody = response.body();
                db.eventsDao().deleteAllEvents();
                db.eventsDao().insertAll(eventsBody);
                Intent returningIntent = new Intent(EVENTS_ACTION);
                String eventsJson = new Gson().toJson(eventsBody);
                returningIntent.putExtra("events", eventsJson);
                Log.d(TAG, "Events Call 1");
                sendBroadcast(returningIntent);
            } else {
                Log.e(TAG, "HTTP Error" + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void news() {
        final Call<News[]> newsCall = ServiceClient.getRetroFit().getNews();
        try{
            Response<News[]> response = newsCall.execute();
            if(response.code() == HttpURLConnection.HTTP_OK){
                News[] newsBody = response.body();
                db.newsDao().deleteAllNews();
                db.newsDao().insertAll(newsBody);
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
            Log.d(TAG, "==>Auth Service is called");
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

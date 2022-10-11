package com.gerald.newsapp;

import android.content.Context;
import android.widget.Toast;

import com.gerald.newsapp.Models.NewsApiResponse;
import com.squareup.picasso.Picasso;

import okhttp3.internal.http2.Http2Connection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.*;

public class RequestManager {
    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getNewsHeadlines(OnFetchDataListener, String category, String query)
    {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsApiResponse> call = callNewsApi.callHeadlines("us", category, query, context.getString(R.string.api_key));

        try {
            call.enque(new Callback<NewsApiResponse>(){
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response){
                    if (!response.isSuccessful()){
                        Toast.makeText(context, "Error!!",Toast.LENGTH_SHORT).show();
                    }
                    listener.onFetchData(response.body().getArticles(), response.message());
                }
                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t){
                    listener.onError("Request Failed!");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public RequestManager(Context context) {
        this.context = context;
    }
    public interface CallNewsApi{
        @GET("top-headlines")
        Call<NewsApiResponse> callHeadlines(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String api_key

        );
    }
}

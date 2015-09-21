package com.example.vjobanputra.instagramclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID="7b5b9cc0204b4867b4b07bfee6a99387";

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);

        ListView listViewPhotos = (ListView)findViewById(R.id.listViewPhotos);
        listViewPhotos.setAdapter(aPhotos);

        fetchPopularPhotos();


    }

    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject onePhotoJSON = photosJSON.getJSONObject(i);

                        InstagramPhoto photo = new InstagramPhoto();

                        photo.caption = onePhotoJSON.getJSONObject("caption").getString("text");
                        photo.likesCount = onePhotoJSON.getJSONObject("likes").getInt("count");
                        photo.createdTime = onePhotoJSON.getLong("created_time");
                        // User
                        JSONObject userJSON = onePhotoJSON.getJSONObject("user");
                        photo.username = userJSON.getString("username");
                        photo.profilePicUrl = userJSON.getString("profile_picture");
                        // Image
                        JSONObject standardResolutionImg = onePhotoJSON.getJSONObject("images").getJSONObject("standard_resolution");
                        photo.imageUrl = standardResolutionImg.getString("url");
                        photo.imageHeight = standardResolutionImg.getInt("height");
                        // Comment
                        JSONArray commentJSON = onePhotoJSON.getJSONObject("comments").getJSONArray("data");
                        if (commentJSON != null) {
                            photo.comments = new ArrayList<PhotoComment>();
                            for (int c = 0; i < commentJSON.length() && c <= 2; c++) {
                                PhotoComment pc = new PhotoComment();
                                pc.username = commentJSON.getJSONObject(c).getJSONObject("from").getString("full_name");
                                pc.text = commentJSON.getJSONObject(c).getString("text");
                                photo.comments.add(pc);
                            }
                        }

                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

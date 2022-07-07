package com.example.journeyjournal.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ResourceCursorAdapter;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.journeyjournal.Activities.RemindersActivity;
import com.example.journeyjournal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private Button button;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        String URL = "https://www.googleapis.com/calendar/v3&KEY=AIzaSyBsSZDPhHKUdqchMuM_FzpK71wB8z5kD3k";
        headers.put("CLIENT_ID","581735586314-5jq4jvgeqctgiomh2dg9eqfo2q9960sc.apps.googleusercontent.com");
        client.get(URL, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try{
                    String items = jsonObject.getString("kind");
                    Log.i(TAG,items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i("go next",response);
            }
        });
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReminderActivity();
            }
        });
    }
    // takes to view current goals/ reminders page
    private void goReminderActivity() {
        Intent i = new Intent(getContext(), RemindersActivity.class);
        startActivity(i);
    }


}
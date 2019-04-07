package com.example.android.stackoverflow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.stackoverflow.Adapters.TagRecyclerAdapter;
import com.example.android.stackoverflow.NetworkUtils.ApiUtils;
import com.example.android.stackoverflow.Model.TagItem;
import com.example.android.stackoverflow.Model.TagsResponseObject;
import com.example.android.stackoverflow.NetworkUtils.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInterestActivity extends AppCompatActivity {

    Button submitButton;
    RecyclerView mRecyclerView;
    TagRecyclerAdapter mAdapter;
    LinearLayout selectedTagsLL;
    private ApiInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interest);
        selectedTagsLL = findViewById(R.id.selected_tags_ll);
        mService = ApiUtils.getSOService();

        SharedPreferences sharedPref = getSharedPreferences("tags", Context.MODE_PRIVATE);
        if (!(sharedPref.getString("tag1", "").isEmpty())) {
            Intent in = new Intent(this, QuestionListActivity.class);
            startActivity(in);
            finish();
        }

        submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTagsLL.getChildCount() != 4) {
                    Toast.makeText(UserInterestActivity.this, "Choose 4 tags", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(UserInterestActivity.this, QuestionListActivity.class);
                    saveTags();
                    startActivity(in);
                    finish();
                }
            }
        });

        mRecyclerView = findViewById(R.id.list);
        mAdapter = new TagRecyclerAdapter(this, new ArrayList<TagItem>(0), new TagRecyclerAdapter.TagItemListener() {
            @Override
            public void onTagClick(String name) {

                if (selectedTagsLL.getChildCount() < 4) {
                    //set the properties for button
                    final Button btnTag = new Button(UserInterestActivity.this);
                    btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    btnTag.setText(String.format("%s X", name));
                    btnTag.setTag(name);
                    btnTag.setBackgroundResource(R.drawable.btn_rounded_orange);
                    //add button to the layout
                    selectedTagsLL.addView(btnTag);

                    btnTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedTagsLL.removeView(btnTag);
                        }
                    });

                } else {
                    Toast.makeText(UserInterestActivity.this, "Choose exactly 4 tags", Toast.LENGTH_SHORT).show();
                }

            }

        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


        loadTags();


    }

    private void saveTags() {
        String tag1 = selectedTagsLL.getChildAt(0).getTag().toString().trim();
        String tag2 = selectedTagsLL.getChildAt(1).getTag().toString().trim();
        String tag3 = selectedTagsLL.getChildAt(2).getTag().toString().trim();
        String tag4 = selectedTagsLL.getChildAt(3).getTag().toString().trim();
        SharedPreferences.Editor sharedPref = getSharedPreferences("tags", Context.MODE_PRIVATE).edit();
        sharedPref.putString("tag1", tag1);
        sharedPref.putString("tag2", tag2);
        sharedPref.putString("tag3", tag3);
        sharedPref.putString("tag4", tag4);
        sharedPref.apply();
    }

    private void loadTags() {
        mService.getTags().enqueue(new Callback<TagsResponseObject>() {
            @Override
            public void onResponse(Call<TagsResponseObject> call, Response<TagsResponseObject> response) {
                if (response.isSuccessful()) {
                    mAdapter.updateTags(response.body().getItems());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<TagsResponseObject> call, Throwable t) {
                Toast.makeText(UserInterestActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

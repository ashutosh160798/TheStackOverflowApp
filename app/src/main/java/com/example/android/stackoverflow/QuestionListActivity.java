package com.example.android.stackoverflow;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.stackoverflow.Adapters.QuestionRecyclerAdapter;
import com.example.android.stackoverflow.DatabaseRoom.QuestionDatabase;
import com.example.android.stackoverflow.Model.QuestionItem;
import com.example.android.stackoverflow.Model.QuestionResponseObject;
import com.example.android.stackoverflow.NetworkUtils.ApiInterface;
import com.example.android.stackoverflow.NetworkUtils.ApiUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionListActivity extends AppCompatActivity {

    private static final int MENU_FIRST = Menu.FIRST;
    private static final int MENU_SECOND = Menu.FIRST + 1;
    private static final int MENU_THIRD = Menu.FIRST + 2;
    private static final int MENU_FOURTH = Menu.FIRST + 3;
    private static final String DATABASE_NAME = "questions_db";
    RecyclerView mRecyclerView;
    QuestionRecyclerAdapter mAdapter;
    ProgressDialog progress;
    private SharedPreferences sharedPref;
    private ApiInterface mService;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private String tag;
    private QuestionDatabase questionDatabase;
    private TextView blank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        sharedPref = getSharedPreferences("tags", Context.MODE_PRIVATE);
        boolean hasVisited = sharedPref.getBoolean("HAS_VISITED_BEFORE", false);
        if (!hasVisited) {
            Toast.makeText(this, "Long press Question to save offline.", Toast.LENGTH_SHORT).show();
            sharedPref.edit().putBoolean("HAS_VISITED_BEFORE", true).apply();
        }
        tag = sharedPref.getString("tag1", "");
        mService = ApiUtils.getSOService();
        questionDatabase = Room.databaseBuilder(getApplicationContext(), QuestionDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        blank = findViewById(R.id.blank_tv);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nv);

        sharedPref = getSharedPreferences("tags", Context.MODE_PRIVATE);
        Menu menu = nv.getMenu();
        menu.add(0, MENU_FIRST, Menu.NONE, sharedPref.getString("tag1", ""));
        menu.add(0, MENU_SECOND, Menu.NONE, sharedPref.getString("tag2", ""));
        menu.add(0, MENU_THIRD, Menu.NONE, sharedPref.getString("tag3", ""));
        menu.add(0, MENU_FOURTH, Menu.NONE, sharedPref.getString("tag4", ""));

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                int id = item.getItemId();
                switch (id) {
                    case MENU_FIRST:
                        tag = sharedPref.getString("tag1", "");
                        loadQuestions();
                        break;
                    case MENU_SECOND:
                        tag = sharedPref.getString("tag2", "");
                        loadQuestions();
                        break;
                    case MENU_THIRD:
                        tag = sharedPref.getString("tag3", "");
                        loadQuestions();
                        break;
                    case MENU_FOURTH:
                        tag = sharedPref.getString("tag4", "");
                        loadQuestions();
                        break;
                    default:
                        tag = sharedPref.getString("tag1", "");
                        loadQuestions();
                        return true;
                }
                dl.closeDrawers();

                return true;
            }
        });


        mAdapter = new QuestionRecyclerAdapter(this, new ArrayList<QuestionItem>(), new QuestionRecyclerAdapter.QuestionItemListener() {
            @Override
            public void onQuestionClick(String link) {
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setToolbarColor(QuestionListActivity.this.getResources()
                                .getColor(R.color.colorPrimary))
                        .setShowTitle(true)
                        .build();

                customTabsIntent.launchUrl(QuestionListActivity.this, Uri.parse(link));

            }

            @Override
            public void onLongClicked(final QuestionItem questionItem) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(QuestionListActivity.this);
                if (isNetworkConnected()) {
                    builder.setTitle("Save this question offline?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                questionDatabase.daoAccess().insertQuestion(questionItem);
                                            } catch (SQLiteConstraintException e) {
                                                Log.d("QuestionListActivity:", "Already saved");
                                            }
                                        }
                                    }).start();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    builder.create().show();
                } else {
                    builder.setTitle("Remove this question?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            questionDatabase.daoAccess().deleteQuestion(questionItem);
                                        }
                                    }).start();
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    builder.create().show();
                }

            }
        });
        mRecyclerView = findViewById(R.id.questions_rv);
        mRecyclerView.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        loadQuestions();

        if (!isInternetAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<QuestionItem> questionItems = questionDatabase.daoAccess().fetchAll();
                    if (questionItems.size() != 0) {
                        blank.setVisibility(View.GONE);
                    }
                    mAdapter.updateQuestions(questionItems);
                }
            }).start();
        }
    }

    private void loadQuestions() {

        progress = new ProgressDialog(this);
        progress.setMessage("Loading Questions...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        mService.getQuestions(tag).enqueue(new Callback<QuestionResponseObject>() {
            @Override
            public void onResponse(Call<QuestionResponseObject> call, Response<QuestionResponseObject> response) {
                if (response.isSuccessful()) {
                    mAdapter.updateQuestions(response.body().getItems());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                }
                progress.dismiss();
                mRecyclerView.setVisibility(View.VISIBLE);
                blank.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<QuestionResponseObject> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(QuestionListActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}

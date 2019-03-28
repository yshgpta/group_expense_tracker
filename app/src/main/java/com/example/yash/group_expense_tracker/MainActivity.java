package com.example.yash.group_expense_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
public class MainActivity extends AppCompatActivity {

    CircularProgressButton loadingMe;
    ImageView mainLogo;
    Animation rotate,scale;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().getBooleanExtra("EXIT",false)){
            finish();
        }
        appName = findViewById(R.id.appName);
        mainLogo = findViewById(R.id.mainLogo);

        scale = AnimationUtils.loadAnimation(this,R.anim.scale);
        rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);

        appName.startAnimation(scale);
        mainLogo.startAnimation(rotate);
        loadingMe = (CircularProgressButton)findViewById(R.id.loadingMe);
        loadingMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String, String, String> GetStarted = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if(s.equals("done")){
                            Intent i = new Intent(MainActivity.this, TripSelection.class);
                            startActivity(i);
                        }
                    }
                };

                loadingMe.startAnimation();
                GetStarted.execute();

            }
        });
    }
}

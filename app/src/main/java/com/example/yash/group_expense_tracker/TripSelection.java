package com.example.yash.group_expense_tracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;


public class TripSelection extends AppCompatActivity {

    private TextView tripNameTextView;
    private String TripName;
    DatabaseHelper myDb;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      For Transparent Status bar uncomment below line
//      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView(R.layout.activity_trip_selection);

        myDb = new DatabaseHelper(this);
        cursor = myDb.getAllData();
        if(cursor.moveToFirst()){
            String tripName = cursor.getString(2);
            Intent i = new Intent(TripSelection.this,Home.class);
            startActivity(i);
            return;
        }
        final Button addTripBtn = findViewById(R.id.addTrip);
        addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripNameTextView = findViewById(R.id.tripName);
                TextView warningTextView = findViewById(R.id.warningText);
                if(tripNameTextView.getText().length()==0){
                    warningTextView.setText("Please Enter a valid name of Trip");
                    TastyToast.makeText(TripSelection.this,"Oops..",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }else{
                    warningTextView.setText("");
                    TripName = tripNameTextView.getText().toString();
                    TastyToast.makeText(TripSelection.this,"Trip Selected",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    Intent i = new Intent(TripSelection.this,GroupCreation.class);
                    i.putExtra("TripName",TripName);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}

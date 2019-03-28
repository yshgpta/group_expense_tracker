package com.example.yash.group_expense_tracker;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpensePieGraph extends AppCompatActivity {

    DatabaseHelper myDb;
    Cursor initial_cursor,cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_pie_graph);
        myDb = new DatabaseHelper(getApplicationContext());
        initial_cursor = myDb.getExpenseType();
        List<String> ls = new ArrayList<String>();
        Set<String> set = new HashSet<String>();
        if(initial_cursor.moveToFirst()){
            do{
                String ExpType = initial_cursor.getString(0);
                set.add(ExpType);
            }while (initial_cursor.moveToNext());
        }
        System.out.println(set);
        List<String> Expense_Type = new ArrayList<String>();
        List<Integer> Expense_Value = new ArrayList<Integer>();
        for(String ExpType : set){
            Cursor cursor = myDb.getExpenseTypeByName(ExpType);
            Expense_Type.add(ExpType);
            int sum = 0;
            if(cursor.moveToFirst()){
                do{
                    String expValue = cursor.getString(2);
                    sum = sum + Integer.parseInt(expValue);
                }while (cursor.moveToNext());
            }
            Expense_Value.add(sum);
        }
        String [] ExpenseType_Arr = new String[Expense_Type.size()];
        ExpenseType_Arr = Expense_Type.toArray(ExpenseType_Arr);
        Integer [] ExpenseValue_Arr = new Integer[Expense_Value.size()];
        ExpenseValue_Arr = Expense_Value.toArray(ExpenseValue_Arr);
        List<PieEntry> pieEntries = new ArrayList<PieEntry>();
        for(int i=0;i<ExpenseType_Arr.length;i++){
            pieEntries.add(new PieEntry(ExpenseValue_Arr[i],ExpenseType_Arr[i]));
        }
        cursor = myDb.getAllData();
        cursor.moveToFirst();
        String tripName = cursor.getString(2);
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Value in Rupees");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(15f);
        pieData.setValueTextColor(Color.YELLOW);

        com.github.mikephil.charting.charts.PieChart pieChart = (com.github.mikephil.charting.charts.PieChart)findViewById(R.id.pie_chart);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setData(pieData);
        Legend legend = pieChart.getLegend();
        legend.setTextSize(15f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        Description description = new Description();
        description.setText("Expenses of: "+tripName);
        description.setTextSize(35);
        description.setPosition(800,1700);
        pieChart.setDescription(description);
        pieChart.animateY(1000,Easing.EaseInOutCubic);
        pieChart.invalidate();
    }
}

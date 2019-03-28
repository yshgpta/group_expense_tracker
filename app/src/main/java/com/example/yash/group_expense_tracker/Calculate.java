package com.example.yash.group_expense_tracker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Calculate extends AppCompatActivity {

    ExpandingList expandingList;
    DatabaseHelper myDb;
    Cursor cursor;
    int totalSum=0;
    TextView divAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        expandingList = findViewById(R.id.calculateList);
        myDb = new DatabaseHelper(getApplicationContext());
        cursor = myDb.getAllData();
        cursor.moveToLast();
        String memNum = cursor.getString(0);
        ArrayList<ArrayList<String> > dataList = new ArrayList<ArrayList<String>>();
        cursor = myDb.getAllExpense();
        if(cursor.moveToFirst()){
            do{
                String value = cursor.getString(2);
                totalSum += Integer.parseInt(value);
            }while(cursor.moveToNext());
        }
        int eachToPay = totalSum/Integer.parseInt(memNum);
        divAmt = findViewById(R.id.div_amt);
        divAmt.setText("Divided Amount: Rs "+eachToPay);
        cursor = myDb.getAllData();
        ArrayList<ArrayList<String>> Receiver = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> Doner = new ArrayList<ArrayList<String>>();
        int i=0;
        if(cursor.moveToFirst()){
            do{
                ArrayList<String> dataItem = new ArrayList<String>();
                ArrayList<String> toWhom = new ArrayList<String>();
                String memId = cursor.getString(0);
                String memName = cursor.getString(1);
                dataItem.add(memId+"."+memName);
                toWhom.add(memId+"."+memName);
                Cursor innerCursor = myDb.getExpensebyId(memId);
                int amtPaid = 0;
                if(innerCursor.moveToFirst()){
                    do{
                        String value = innerCursor.getString(2);
                        amtPaid += Integer.parseInt(value);
                    }while (innerCursor.moveToNext());
                    dataItem.add(amtPaid+"");
                }else{
                    dataItem.add(0+"");
                }
                if(amtPaid>=eachToPay){
                    dataItem.add(0+"");
                    dataItem.add((amtPaid-eachToPay)+"");
                    toWhom.add((amtPaid-eachToPay)+"");
                    Receiver.add(toWhom);
                }else{
                    dataItem.add((eachToPay-amtPaid)+"");
                    dataItem.add(0+"");
                    toWhom.add((eachToPay-amtPaid)+"");
                    Doner.add(toWhom);
                }
                dataList.add(dataItem);
            }while (cursor.moveToNext());
            System.out.println("Receiver "+Receiver);
            System.out.println("Doner"+Doner);
//            System.out.println(eachToPay);
//            System.out.println(dataList);
//            System.out.println(dataList.get(0));
            ArrayList<ArrayList<String>> toWhomList = new ArrayList<ArrayList<String>>();
            for(int j=0;j<Doner.size();j++){
                if(Integer.parseInt(Doner.get(j).get(1))==0)
                    continue;
                ArrayList<String> list = new ArrayList<String>();
                for(int k=0;k<Receiver.size();k++){
                    if(Integer.parseInt(Receiver.get(k).get(1))==0)
                        continue;
                    if(Integer.parseInt(Doner.get(j).get(1))==0)
                        break;
                    if(Integer.parseInt(Doner.get(j).get(1))>=Integer.parseInt(Receiver.get(k).get(1))){
                        int value = Integer.parseInt(Doner.get(j).get(1))-Integer.parseInt(Receiver.get(k).get(1));
                        Doner.get(j).set(1,value+"");
                        String str = "Pay Rs "+Receiver.get(k).get(1)+" to "+Receiver.get(k).get(0).substring(2);
                        Receiver.get(k).set(1,"0");
                        list.add(str);
                    }else{
                        int value = Integer.parseInt(Receiver.get(k).get(1))-Integer.parseInt(Doner.get(j).get(1));
                        String str = "Pay Rs "+Doner.get(j).get(1)+" to "+Receiver.get(k).get(0).substring(2);
                        Doner.get(j).set(1,"0");
                        Receiver.get(k).set(1,value+"");
                        list.add(str);
                    }
                }
                toWhomList.add(list);
            }
            ArrayList<ArrayList<String>> payList = new ArrayList<ArrayList<String>>();
            int k=0;
            for(int j=0;j<Integer.parseInt(memNum);j++){
                if(Integer.parseInt(dataList.get(j).get(2))==0){
                    ArrayList<String> demoList = new ArrayList<String>();
                    demoList.add("Don't have to pay anything");
                    payList.add(demoList);
                }else{
                    payList.add(toWhomList.get(k));
                    k++;
                }
            }
            for(int j=0;j<Integer.parseInt(memNum);j++){
                ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout_calculate);
                ((TextView)item.findViewById(R.id.memName)).setText(dataList.get(j).get(0).substring(0,2)+"Member Name: "+dataList.get(j).get(0).substring(2));
                item.createSubItems(payList.get(j).size()+3);
                ((TextView)item.getSubItemView(0).findViewById(R.id.child_item)).setText("Amount Paid: Rs "+dataList.get(j).get(1));
                ((TextView)item.getSubItemView(1).findViewById(R.id.child_item)).setText("Amount To Pay: Rs "+dataList.get(j).get(2));
                ((TextView)item.getSubItemView(2).findViewById(R.id.child_item)).setText("Amount To Receive: Rs "+dataList.get(j).get(3));

                for(int m=3;m<payList.get(j).size()+3;m++){
                    ((TextView)item.getSubItemView(m).findViewById(R.id.child_item)).setText(payList.get(j).get(m-3));
                }
                item.setIndicatorIconRes(R.drawable.ic_person);
                switch (j%10){
                    case 0:
                        item.setIndicatorColorRes(R.color.pink);
                        break;
                    case 1:
                        item.setIndicatorColorRes(R.color.orange);
                        break;
                    case 2:
                        item.setIndicatorColorRes(R.color.yellow);
                        break;
                    case 3:
                        item.setIndicatorColorRes(R.color.lightBlue);
                        break;
                    case 4:
                        item.setIndicatorColorRes(R.color.violet);
                        break;
                    case 5:
                        item.setIndicatorColorRes(R.color.lightGreen);
                        break;
                    case 6:
                        item.setIndicatorColorRes(R.color.red);
                        break;
                    case 7:
                        item.setIndicatorColorRes(R.color.darkBlue);
                        break;
                    case 8:
                        item.setIndicatorColorRes(R.color.darkGreen);
                        break;
                    case 9:
                        item.setIndicatorColorRes(R.color.cyan);
                        break;
                }
            }

            System.out.println("paylist"+payList);
            System.out.println("Towhom"+toWhomList);
        }
    }
}

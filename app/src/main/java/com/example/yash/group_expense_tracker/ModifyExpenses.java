package com.example.yash.group_expense_tracker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class ModifyExpenses extends AppCompatActivity {

    DatabaseHelper myDb;
    Spinner sp_mem,sp_exp;
    TextView expValue,warningText;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_expenses);
        myDb = new DatabaseHelper(this);
        ArrayList<String> nameList = initializeMemSpinner();
        sp_mem = findViewById(R.id.sp_mem);
        sp_exp = findViewById(R.id.sp_exp);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,nameList);
        sp_mem.setAdapter(adapter);
        updateBtn = findViewById(R.id.updateBtn);
        warningText = findViewById(R.id.warningText);
        sp_mem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String memSelected = sp_mem.getSelectedItem().toString();
                ArrayList<String> ExpenseList = initializeExpSpinner(memSelected.charAt(0)+"");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifyExpenses.this,R.layout.spinner_layout,R.id.txt,ExpenseList);
                sp_exp.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayList<String> ExpenseList = initializeExpSpinner("1");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifyExpenses.this,R.layout.spinner_layout,R.id.txt,ExpenseList);
                sp_exp.setAdapter(adapter);
            }
        });
        sp_exp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String memSelected = sp_mem.getSelectedItem().toString();
                String expSelected = sp_exp.getSelectedItem().toString();
                Cursor cursor = myDb.getValueByExpenseNameAndId(expSelected,memSelected.charAt(0)+"");
                while(cursor.moveToNext()){
                    String Value = cursor.getString(2);
                    expValue = findViewById(R.id.expValue);
                    expValue.setHint("Current Value: Rs "+Value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int items = sp_exp.getAdapter().getCount();
                expValue = findViewById(R.id.expValue);
                String value = expValue.getText().toString();
                if(items==0){
                    warningText.setText("You cannot modify this data");
                    return;
                }else{
                    if(value.length()==0){
                        warningText.setText("Please enter a valid value");
                        return;
                    }else{
                        warningText.setText("");
                        String memSelected = sp_mem.getSelectedItem().toString();
                        String expSelected = sp_exp.getSelectedItem().toString();
                        boolean isUpdated = myDb.updateByIdAndExpense(expSelected,memSelected.charAt(0)+"",value);
                        if(isUpdated)
                            TastyToast.makeText(ModifyExpenses.this,"Data Modified",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
                        else
                            TastyToast.makeText(ModifyExpenses.this,"Data Not Modified",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        expValue.setText("");
                        expValue.setHint("Current Value: Rs "+value);
                    }
                }
            }
        });
    }
    public ArrayList<String> initializeMemSpinner(){
        ArrayList<String> list = new ArrayList<String>();
        try{
            Cursor res = myDb.getAllData();
            if(res.getCount()==0){
                return list;
            }else{
                while(res.moveToNext()){
                    String memId = res.getString(0);
                    String memName = res.getString(1);
                    list.add(memId+"."+memName);
                }
            }
        }
        catch (Exception e){
            System.out.println("Error "+e);
        }
        return list;
    }

    public ArrayList<String> initializeExpSpinner(String Id){
        ArrayList<String> list = new ArrayList<String>();
        try{
            Cursor res = myDb.getExpensebyId(Id);
            if(res.getCount()==0){
                expValue = findViewById(R.id.expValue);
                expValue.setHint("Currently having no expense");
                return list;
            }else{
                while(res.moveToNext()){
                    String Expense = res.getString(1);
                    list.add(Expense);
                }
            }
        }
        catch (Exception e){
            System.out.println("Error "+e);
        }
        return list;
    }
}

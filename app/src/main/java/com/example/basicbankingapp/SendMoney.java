package com.example.basicbankingapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;



public class SendMoney extends AppCompatActivity {

    //variables
    private TextInputEditText fromText, amountText;
    private AutoCompleteTextView toText;
    private Button sendBtn;
    private int arraySize;
    private String fromName,id,total_balance;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        //hooks
        fromText = findViewById(R.id.from_person_name);
        toText = findViewById(R.id.to_person_name);
        amountText = findViewById(R.id.amount_to_be_send);
        sendBtn = findViewById(R.id.send_money);


        //Receiving Intent
        Bundle bundle = getIntent().getBundleExtra("sendMoneyData");
        id = bundle.getString("id");
        fromName = bundle.getString("fromName");
        total_balance = bundle.getString("balance");
        arraySize = Integer.valueOf(bundle.getString("size"));
        String[] names = new String[arraySize];
        String[] ids = new String[arraySize];
        String[] balances = new String[arraySize];

        BackgroundProcessing task = new BackgroundProcessing(names,getApplicationContext(),ids,adapter,toText,balances);
        task.execute();
        fromText.setText(fromName);



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountToBeDeducted = amountText.getText().toString();
                String fName = fromText.getText().toString();
                String tName = toText.getText().toString();



                if(TextUtils.isEmpty(amountToBeDeducted) || TextUtils.isEmpty(fName) || TextUtils.isEmpty(tName)){
                    Toast.makeText(getApplicationContext(),"Any Field Can't Be Empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!fName.equals(fromName)){
                    fromText.setError("Name should be " + fromName);
                    return;
                }

                if(tName.equals(fromName)){
                    toText.setError("Name should not be " + fromName);
                    return;
                }

                int  sentAmount = Integer.valueOf(amountToBeDeducted);
                int  totalAmount = Integer.valueOf(total_balance);

                if( sentAmount > totalAmount){
                    amountText.setError("Amount Should be less than or equal to "+ total_balance);
                    return;
                }

                if(sentAmount <= 0 ){
                    amountText.setError("Amount should be greater than 0");
                    return;
                }


                //updating user info table
                int temp = 100;
                for(int i=0;i<arraySize;i++){
                    if(names[i].equals(tName)){
                        temp = i;
                        break;
                    }else{
                        continue;
                    }
                }

                if(temp == 100){
                    toText.setError("Please Select Proper Name");
                    return;
                }

                int updatedAmountToWhomSent =   Integer.valueOf(balances[temp]) + sentAmount;
                UpdateUserInfoHandler newTask = new UpdateUserInfoHandler(ids[temp],String.valueOf(updatedAmountToWhomSent),getApplicationContext());
                newTask.execute();

                //inserting transaction table
                InsertInTransactionDb insertTask = new InsertInTransactionDb(fName,tName,amountToBeDeducted,getApplicationContext());
                insertTask.execute();

                //updating user info table
                int remainingAmount = totalAmount-sentAmount;
                UpdateUserInfoHandler updateTask = new UpdateUserInfoHandler(id,String.valueOf(remainingAmount),getApplicationContext());
                updateTask.execute();





                Toast.makeText(getApplicationContext(),"Successfully Sent",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SendMoney.this,Dashboard.class);
                startActivity(intent);



            }
        });


    }


    class BackgroundProcessing extends AsyncTask<Void,Void,Void>{


        public String[] nameArray;
        public Context context;
        public String[] idArray;
        public String[]  balanceArray;
        public ArrayAdapter<String> mAdapter;
        public AutoCompleteTextView showList;

        public BackgroundProcessing(String[] nameArray,Context context,String[] idArray,ArrayAdapter<String> mAdapter,AutoCompleteTextView showList,String[] balanceArray) {
            this.nameArray = nameArray;
            this.context = context;
            this.idArray = idArray;
            this.mAdapter = mAdapter;
            this.showList = showList;
            this.balanceArray = balanceArray;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AllUsersDatabaseHelper db = new AllUsersDatabaseHelper(context);
            Cursor cursor = db.fetchAllFromUserInfo();

            if(cursor.getCount()>0){
                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToNext();
                    for(int j=0;j<cursor.getColumnCount();j++){
                        if(j==0){
                            idArray[i] = cursor.getString(j);
                        }
                        if(j==1){
                            nameArray[i] = cursor.getString(j);
                        }
                        if(j==2){
                            continue;
                        }
                        if(j==3){
                            continue;
                        }
                        if(j==4){
                            balanceArray[i] = cursor.getString(j);
                        }
                    }

                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,nameArray);
            showList.setAdapter(mAdapter);
        }

    }

    class InsertInTransactionDb extends AsyncTask<Void,Void,Void>{

        public String givenFrom;
        public String givenTo;
        public String amountSent;
        public Context context;

        public InsertInTransactionDb(String givenFrom, String givenTo, String amountSent,Context context) {
            this.givenFrom = givenFrom;
            this.givenTo = givenTo;
            this.amountSent = amountSent;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AllUsersDatabaseHelper db = new AllUsersDatabaseHelper(context);
            boolean isInserted = db.insertToTransacInfo(givenFrom,givenTo,amountSent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Successfully Sent",Toast.LENGTH_SHORT).show();
        }

    }

    class UpdateUserInfoHandler extends AsyncTask<Void,Void,Void>{

        public String userId;
        public String userBalance;
        public Context context;

        public UpdateUserInfoHandler(String userId, String userBalance,Context context) {
            this.userId = userId;
            this.userBalance = userBalance;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AllUsersDatabaseHelper db = new AllUsersDatabaseHelper(context);
            boolean isUpdated = db.updateUserInfo(userId,userBalance);
            return null;
        }




    }


}
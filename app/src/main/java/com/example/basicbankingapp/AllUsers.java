package com.example.basicbankingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class AllUsers extends AppCompatActivity {

    //variables
    private RecyclerView recyclerView;
    private RecyclerView.Adapter customAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ViewDataType> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        //hooks
        recyclerView = findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);


        AllUserAdapter.HandleClickOnCard handleClickOnCard = new AllUserAdapter.HandleClickOnCard() {
            @Override
            public void handleClick(int index) {
                ViewDataType objectToBeSend = arrayList.get(index);
                Intent intent = new Intent(AllUsers.this,ShowUserDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("size",String.valueOf(arrayList.size()));
                bundle.putString("id",objectToBeSend.getId());
                bundle.putString("name",objectToBeSend.getName());
                bundle.putString("email",objectToBeSend.getEmail());
                bundle.putString("gender",objectToBeSend.getGender());
                bundle.putString("balance",objectToBeSend.getBalance());
                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        customAdapter = new AllUserAdapter(arrayList, getApplicationContext(),handleClickOnCard);
        recyclerView.setAdapter(customAdapter);



        //fetching data from database
       FetchFromDatabase task = new FetchFromDatabase(customAdapter,arrayList);
       task.execute(getApplicationContext());
        // fetching data ends



    }








    //Asynctask for fetching data from database
    class FetchFromDatabase extends AsyncTask<Context,ViewDataType,String>{

           private RecyclerView.Adapter adapter;
           private  ArrayList<ViewDataType>  list;


        public FetchFromDatabase(RecyclerView.Adapter adapter, ArrayList<ViewDataType> list) {
            this.adapter = adapter;
            this.list = list;

        }



        @Override
        protected String doInBackground(Context... contexts) {

            AllUsersDatabaseHelper db = new AllUsersDatabaseHelper(contexts[0]);
            Cursor cursor = db.fetchAllFromUserInfo();

            if(cursor.getCount() > 0){

                for(int row=0;row<cursor.getCount();row++){
                    cursor.moveToNext();
                    ViewDataType viewDataType = new ViewDataType();
                    for(int column=0;column<cursor.getColumnCount();column++){
                        switch (column){
                            case 0:
                                viewDataType.setId(cursor.getString(column));
                                break;
                            case 1:
                                viewDataType.setName(cursor.getString(column));
                                break;
                            case 2:
                                viewDataType.setEmail(cursor.getString(column));
                                break;
                            case 3:
                                viewDataType.setGender(cursor.getString(column));
                                break;
                            case 4:
                                viewDataType.setBalance(cursor.getString(column));
                                break;

                        }
                    }
                    publishProgress(viewDataType);


                }

            }

            cursor.close();
            db.close();


            return "All the task is completed";
        }


        @Override
        protected void onProgressUpdate(ViewDataType... values) {
           list.add(values[0]);
           adapter.notifyDataSetChanged();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("MyTag",s);
        }


    }



}
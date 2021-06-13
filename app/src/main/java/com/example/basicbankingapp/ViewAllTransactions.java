package com.example.basicbankingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

import java.util.ArrayList;

public class ViewAllTransactions extends AppCompatActivity {

    //variables
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ViewTransactionDatatype> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_all_transactions);
        recyclerView = findViewById(R.id.transaction_recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ViewTransactionsAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(adapter);

        //fetching from  transacInfo  table started
        FetchFromTransactionDbHandler task = new FetchFromTransactionDbHandler(arrayList,adapter,getApplicationContext());
        task.execute();



    }


    class FetchFromTransactionDbHandler extends AsyncTask<Void,ViewTransactionDatatype,Void> {

        private ArrayList<ViewTransactionDatatype> list;
        private RecyclerView.Adapter adapterAddress;
        private Context context;

        public FetchFromTransactionDbHandler(ArrayList<ViewTransactionDatatype> list, RecyclerView.Adapter adapterAddress, Context context) {
            this.list = list;
            this.adapterAddress = adapterAddress;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AllUsersDatabaseHelper db = new AllUsersDatabaseHelper(context);
            Cursor cursor = db.fetchAllFromTransacInfo();

            if(cursor.getCount() > 0){

                cursor.moveToLast();

                for(int i = cursor.getCount()-1;i >=0;i--){

                        ViewTransactionDatatype viewTransactionDatatype = new ViewTransactionDatatype();
                        for(int j=0;j<cursor.getColumnCount();j++){
                            if(j==0){
                                continue;
                            }
                              switch(j){
                                  case 1:
                                      viewTransactionDatatype.setFromName(cursor.getString(j));
                                      break;
                                  case 2:
                                      viewTransactionDatatype.setToName(cursor.getString(j));
                                      break;
                                  case 3:
                                      viewTransactionDatatype.setAmtTransferred(cursor.getString(j));
                                      break;
                              }
                        }
                        if(i!=0){
                            cursor.moveToPrevious();
                        }
                        publishProgress(viewTransactionDatatype);
                }

                cursor.close();
                db.close();

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(ViewTransactionDatatype... values) {
               list.add(values[0]);
               adapterAddress.notifyDataSetChanged();
        }

    }


}
package com.example.basicbankingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowUserDetail extends AppCompatActivity {

    //variables
    private TextView nameText,emailText,genderText,balanceText;
    private Button sendMoneyBtn;
    private String id;
    private String size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_detail);

        //hooks
        nameText = findViewById(R.id.nameTxt);
        emailText = findViewById(R.id.emailTxt);
        genderText = findViewById(R.id.genderTxt);
        balanceText = findViewById(R.id.balanceTxt);
        sendMoneyBtn = findViewById(R.id.button);


        //Receiving Data from Intent
        Bundle bundle = getIntent().getBundleExtra("data");
        id = bundle.getString("id");
        size = bundle.getString("size");
        nameText.setText(bundle.getString("name"));
        emailText.setText(bundle.getString("email"));
        genderText.setText(bundle.getString("gender"));
        balanceText.setText(bundle.getString("balance")+ " Rs");


    }

    public void goToSendMoney(View view) {

        String name = (String) nameText.getText();
        String balance = (String) balanceText.getText();
        String bal = balance.substring(0,balance.length()-3);

        Intent intent = new Intent(ShowUserDetail.this,SendMoney.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("size",size);
        bundle.putString("fromName",name);
        bundle.putString("balance",bal);
        intent.putExtra("sendMoneyData",bundle);
        startActivity(intent);


    }



}
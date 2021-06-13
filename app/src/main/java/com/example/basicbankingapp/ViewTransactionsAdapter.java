package com.example.basicbankingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewTransactionsAdapter extends RecyclerView.Adapter<ViewTransactionsAdapter.CustomViewHolder> {

    private ArrayList<ViewTransactionDatatype> list;
    private Context context;

    public ViewTransactionsAdapter(ArrayList<ViewTransactionDatatype> list,Context context){
        this.list = list;
        this.context = context;
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView fromText, toText, amountText;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.from);
            toText = itemView.findViewById(R.id.to);
            amountText = itemView.findViewById(R.id.amt);
        }

    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card,parent,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            holder.fromText.setText(list.get(position).getFromName());
            holder.toText.setText(list.get(position).getToName());
            holder.amountText.setText(list.get(position).getAmtTransferred());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }







}

package com.example.basicbankingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.CustomViewHolder> {

    private ArrayList<ViewDataType> list;
    private Context context;
    public static HandleClickOnCard handleClickOnCard;

    public AllUserAdapter(ArrayList<ViewDataType> dataList,Context context,HandleClickOnCard handleClickOnCard) {
        list = dataList;
        this.context = context;
        this.handleClickOnCard = handleClickOnCard;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameText, genderText;
        public ImageView imageView;
        public CardView cardView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.username);
            genderText = itemView.findViewById(R.id.gender);
            imageView = itemView.findViewById(R.id.imageView4);
            cardView = itemView.findViewById(R.id.userCard);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
             handleClickOnCard.handleClick(this.getAdapterPosition());
        }

    }

      public interface HandleClickOnCard {
          void handleClick(int index);
      }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_for_display_of_all_users,parent,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
              holder.nameText.setText(list.get(position).getName());
              holder.genderText.setText(list.get(position).getGender());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }




}

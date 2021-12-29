package com.growindigo.aimt.mynewsapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.growindigo.aimt.mynewsapp.activity.NewsDisplayActivity;
import com.growindigo.aimt.mynewsapp.databinding.HeadlineHomeBinding;
import com.growindigo.aimt.mynewsapp.model.NewsHeadline;

import java.util.List;

public class HomeNewsAdapter extends RecyclerView.Adapter<HomeNewsAdapter.HeadlineViewHolder> {

    private final Context context;
    private List<NewsHeadline> list;

    public HomeNewsAdapter(Context context){
        this.context = context;
    }

    class HeadlineViewHolder extends RecyclerView.ViewHolder{

        HeadlineHomeBinding binding;
        public HeadlineViewHolder(@NonNull HeadlineHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setPage(NewsHeadline headline){
            binding.headlineTxt.setText(headline.getTitle());
            binding.headlineBy.setText(headline.getBy());
            binding.newsTime.setText(headline.getTime());
            // set status as read if updated in db
            if(headline.getStatus() == 0)
                binding.newsStatus.setVisibility(View.INVISIBLE);
            else
                binding.newsStatus.setVisibility(View.VISIBLE);

            // click listener on the whole card
            binding.newsCard.setOnClickListener(v->{
                Intent intent = new Intent(context, NewsDisplayActivity.class);
                intent.putExtra("url", headline.getUrl());
                intent.putExtra("id", headline.getNewsId());
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public HeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HeadlineHomeBinding binding = HeadlineHomeBinding.inflate(((Activity)context).getLayoutInflater(), parent,false);
        return new HeadlineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HeadlineViewHolder holder, int position) {
        holder.setPage(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list == null)
            return 0;
        return list.size();
    }

    public void updateList(List<NewsHeadline> list){
        if(this.list == null)
            this.list = list;
        else{
            this.list.clear();
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }
}

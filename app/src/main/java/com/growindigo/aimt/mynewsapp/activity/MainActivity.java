package com.growindigo.aimt.mynewsapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growindigo.aimt.mynewsapp.adapter.HomeNewsAdapter;
import com.growindigo.aimt.mynewsapp.databinding.ActivityMainBinding;
import com.growindigo.aimt.mynewsapp.view.HeadlineViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init recycler view
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerHome.setLayoutManager(manager);
        binding.recyclerHome.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));

        // set adapter
        HomeNewsAdapter adapter = new HomeNewsAdapter(this);
        binding.recyclerHome.setAdapter(adapter);

        // observe db and load live list
        HeadlineViewModel vm = new HeadlineViewModel(getApplication());
        vm.getList().observe(this, newsHeadlines -> {
            adapter.updateList(newsHeadlines);
            if(newsHeadlines.size() > 0){
                // hide progress bar once list is loaded
                binding.progressBarHome.setVisibility(View.GONE);
                // swipe refresh reset when new list loaded
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        // set default filter empty
        vm.setFilter("");

        binding.homeHead.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // set search text as filter for search
                vm.setFilter(editable.toString());
            }
        });

        // swipe refresh to refresh list from retro
        binding.swipeRefresh.setOnRefreshListener(() -> {
            vm.refreshList();
            // post delayed reset in case list not changed
            new Handler().postDelayed(()->{
                binding.swipeRefresh.setRefreshing(false);
            }, 5000);
        });
    }

}
package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Objects;

public class ActiveListSelectionFragment extends Fragment {
    private ViewListDetailsAdapter mListDetailsAdapter;
    private ActionMode mActionMode;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView mListDetailsRecyclerView;

        rootView = inflater.inflate(R.layout.fragment_clickable_lists_details_rv_container, container, false);

        mListDetailsRecyclerView = rootView.findViewById(R.id.rv_list_details);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mListDetailsRecyclerView.setLayoutManager(linearLayoutManager);
        mListDetailsAdapter = new ViewListDetailsAdapter(getActivity());
        mListDetailsRecyclerView.setAdapter(mListDetailsAdapter);
        registerForContextMenu(mListDetailsRecyclerView);

        ActiveListViewModel mListViewModel;

        mListViewModel = ViewModelProviders.of(this).get(ActiveListViewModel.class);

        mListViewModel.getAllLists().observe(this, new Observer <List<StatisticalList>>() {
            @Override
            public void onChanged(@Nullable final List<StatisticalList> lists) {

                mListDetailsAdapter.setLists(lists);
            }
        });

        return rootView;
    }


}

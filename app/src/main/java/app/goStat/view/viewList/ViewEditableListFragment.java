package app.goStat.view.viewList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.goStat.R;
import app.goStat.view.viewLists.ViewableListAdapter;

public class ViewEditableListFragment extends Fragment {
    private int mListID;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private ViewableListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_editable_list, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        startRecyclerView(rootView);

        TextView mNoDataTextView = rootView.findViewById(R.id.no_data_text_view_instructions);

        getViewModel().getNumberOfDataPointsInList(mListID).observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long numberOfDataPointsInList) {
                if (numberOfDataPointsInList == 0) {
                    mNoDataTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mNoDataTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }

    private void startRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.rv_single_list);
        mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ViewableListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        getViewModel().getListById(mListID).observe(this, mAdapter::submitList);
    }

    public static ViewEditableListFragment newInstance(int listID) {
        ViewEditableListFragment fragment = new ViewEditableListFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private ViewEditableListFragmentViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewEditableListFragmentViewModel.class);
    }

    public void jumpTo(int position) {
        mLinearLayoutManager.scrollToPosition(position);
    }
}

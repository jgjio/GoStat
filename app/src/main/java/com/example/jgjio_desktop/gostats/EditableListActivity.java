package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static android.support.v7.app.AlertDialog.*;

public class EditableListActivity extends AppCompatActivity implements EditableListAdapter.OnLastEditTextOnEnterCallBack {
    private EditableListAdapter mEditableDataRowListRecyclerViewAdapter;
    private RecyclerView mEditableListRecyclerView;


    private double mListId;
    EditableListViewModel editableListViewModel;

    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_editable_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setListID();

        editableListViewModel = ViewModelProviders.of(this).get(EditableListViewModel.class);

        setTitle(editableListViewModel.getListName(mListId));

        configureEditableListRecyclerView();
    }



    //EXTRA_LIST_ID can come from multiple Views so that
    //changing name from one activity and this, may break
    //sending the list from other activities to this
    private boolean setListID() {
        Bundle bundle = getIntent().getExtras();

        if (bundle.getDouble(EXTRA_LIST_ID) !=  0) {
            mListId = bundle.getDouble(EXTRA_LIST_ID);
            return true;
        } else {
           return false;
        }
    }

    private void configureEditableListRecyclerView() {
        mEditableListRecyclerView = findViewById(R.id.rv_editable_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEditableListRecyclerView.setLayoutManager(layoutManager);
        mEditableListRecyclerView.setHasFixedSize(true);
        mEditableDataRowListRecyclerViewAdapter = new EditableListAdapter(this);
        editableListViewModel.getListById((long) mListId).observe(this, mEditableDataRowListRecyclerViewAdapter::submitList);
        mEditableListRecyclerView.setAdapter(mEditableDataRowListRecyclerViewAdapter);

        mEditableDataRowListRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                if (itemCount == 1) {
                    mEditableListRecyclerView.smoothScrollToPosition(mEditableDataRowListRecyclerViewAdapter.getItemCount());
                    mEditableDataRowListRecyclerViewAdapter.setLastEditTextToRequestFocus();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        updateRoom();
        finish();
    }

    @Override
    public void createDataElement() {
        updateRoom();
        DataPoint newDataPoint = new DataPoint(mListId, false, 0.0);
        editableListViewModel.insertDataPoint(newDataPoint);
    }

    //dataPoints that are appended to the list already are updated.
    //We just need to update the changes

    //Also, inserting DataPoints simply replace, because we use OnConflictStrategy.REPLACE in the Dao 
    public void updateRoom() {
        editableListViewModel.insertDataPoints(mEditableDataRowListRecyclerViewAdapter.getNonAppendingUpdates());
    }
}

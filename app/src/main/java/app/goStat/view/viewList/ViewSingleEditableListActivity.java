package app.goStat.view.viewList;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import app.goStat.R;
import app.goStat.view.viewLists.ViewListDetailsAdapter;

public class ViewSingleEditableListActivity extends AppCompatActivity {
    private int mListID;
    //todo change to new package names
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_single_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListID = getIntent().getExtras().getInt(ViewListDetailsAdapter.EXTRA_LIST_ID);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ViewEditableListTemplateFragment.newInstance(mListID))
                    .commitNow();
        }
    }

    //todo sometimes doesn't delete following editList
    @Override
    public void onStart(){
        getViewModel().deleteDisabledDataPoints(mListID);
        super.onStart();
    }

    @Override
    public void onResume(){
        getViewModel().deleteDisabledDataPoints(mListID);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ViewSingleEditableListViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewSingleEditableListViewModel.class);
    }

}

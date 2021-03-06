package app.goStat.view.viewFrequencyTable;

import android.arch.lifecycle.Observer;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import app.goStat.model.FrequencyInterval;
import app.goStat.R;

public class ViewFrequencyTableFragment extends Fragment implements View.OnClickListener {
    private int mListID;
    private String mCopyToClipboardText;
    private View mRootView;
    private Button mCopyToClipboard;
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_view_frequency_table, container, false);
        mListID = getArguments().getInt(EXTRA_LIST_ID);
        setCopyTo();
        mCopyToClipboard = mRootView.findViewById(R.id.copy_to_clipboard_button);
        mCopyToClipboard.setOnClickListener(this);
        configureRecyclerView();
        return mRootView;
    }

    public static ViewFrequencyTableFragment newInstance(int listId) {
        ViewFrequencyTableFragment fragment = new ViewFrequencyTableFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    private void configureRecyclerView() {
        RecyclerView recyclerView = mRootView.findViewById(R.id.rv_interval_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        FrequencyIntervalAdapter adapter = new FrequencyIntervalAdapter();
        recyclerView.setAdapter(adapter);
        getViewModel().getListById(mListID).observe(this, adapter::submitList);
    }

    private ViewFrequencyTableFragmentViewModel getViewModel() {
        return ViewModelProviders.of(this).get(ViewFrequencyTableFragmentViewModel.class);
    }

    private void showCopyToClipboardMessage() {
        Toast toast = Toast.makeText(getActivity(),
                getResources().getString(R.string.toast_copy_to_clipboard_generic),
                Toast.LENGTH_SHORT);
        View view = toast.getView();
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    @Override
    public void onClick(View view) {
        copyToClipboard();
    }

    private void setCopyTo() {
        getViewModel().getTable(mListID).observe(this, new Observer<List<FrequencyInterval>>() {
            @Override
            public void onChanged(@Nullable List<FrequencyInterval> frequencyIntervals) {
                mCopyToClipboardText = "";
                mCopyToClipboardText = getResources().getString(R.string.text_prefix_for_copy_to_clipboard_frequency_table) + "\n";
                for (FrequencyInterval val : frequencyIntervals) {
                    mCopyToClipboardText = mCopyToClipboardText  + getResources().getString(R.string.text_prefix_for_copy_to_clipboard_frequency_interval_line) +
                            val.toString()  +  getResources().getString(R.string.text_infix_for_copy_to_clipboard_frequency_amount_line)+ val.getFrequency() + " \n";
                }
            }
        });
    }

    private void copyToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getResources().getString(R.string.meta_label_copy_to_clipboard_frequency_table), mCopyToClipboardText);
        clipboard.setPrimaryClip(clip);
        showCopyToClipboardMessage();
    }
}

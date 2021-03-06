package app.goStat.view.viewLists;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.goStat.model.DataPoint;
import app.goStat.R;

public class ViewableListAdapter extends PagedListAdapter<DataPoint, ViewableListAdapter.DataPointViewHolder> {

    public ViewableListAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(@NonNull DataPointViewHolder holder, int position) {
        DataPoint dataPoint = getItem(position);
        if (dataPoint != null) holder.bindTo(position, dataPoint);
    }

    private static DiffUtil.ItemCallback<DataPoint> DIFF_CALLBACK = new DiffUtil.ItemCallback<DataPoint>() {
        @Override
        public boolean areItemsTheSame(DataPoint oldDataPoint, DataPoint newDataPoint) {
            return oldDataPoint.getId() == newDataPoint.getId();
        }

        @Override
        public boolean areContentsTheSame(DataPoint oldDataPoint, DataPoint newDataPoint) {
            return oldDataPoint.equals(newDataPoint);
        }
    };

    class DataPointViewHolder extends RecyclerView.ViewHolder {
        TextView viewableDataPoint;
        TextView positionDataPoint;

        public DataPointViewHolder(View itemView) {
            super(itemView);
            viewableDataPoint = itemView.findViewById(R.id.viewable_data_point_text_view);
            positionDataPoint = itemView.findViewById(R.id.item_position);
        }

        void bindTo(int listIndex, DataPoint dataPoint) {
            viewableDataPoint.setText(dataPoint.getValue().toString());
            positionDataPoint.setText(Integer.toString(listIndex + 1)); //statistical ListsLoader start at 1 instead of 0

            if (!dataPoint.isEnabled()) {
                itemView.setBackgroundColor(itemView.getContext().getColor(R.color.highlight));
                viewableDataPoint.setText(R.string.text_data_point_disabled);
            } else {
                itemView.setBackgroundColor(itemView.getContext().getColor(R.color.colorWhite));
            }
        }
    }

    @Override
    public DataPointViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_viewable_data_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup,shouldAttachToParentImmediately);
        DataPointViewHolder viewHolder = new DataPointViewHolder(view);
        return viewHolder;
    }
}

package com.example.jgjio_desktop.gostats;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewListDetailsAdapter extends PagedListAdapter<StatisticalList, ViewListDetailsAdapter.ListDetailViewHolder> {
    public static final String EXTRA_LIST_ID = "com.example.jgjio_desktop.gostats.extra.LIST_ID";
    private Context mContext;
    private int mPositionLongHoldClick;
    private ActionMode mActionMode;

    protected ViewListDetailsAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    @Override
    public ViewListDetailsAdapter.ListDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int templateLayoutID = R.layout.item_list_details;
        Context context = viewGroup.getContext();
        boolean shouldAttachToParentImmediately = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(templateLayoutID, viewGroup, shouldAttachToParentImmediately);
        ViewListDetailsAdapter.ListDetailViewHolder viewHolder = new ViewListDetailsAdapter.ListDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewListDetailsAdapter.ListDetailViewHolder holder, final int position) {
        StatisticalList statisticalList =  getItem(position);

        if (statisticalList != null) {
            holder.bindTo(statisticalList, position);
        } else {
            holder.clear();
        }
    }

    class ListDetailViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout listDetails;

        TextView listName;
        TextView frequencyTableMessage;
        TextView listIDMessage;

        CardView createdByUserMessage;
        CardView createdBySystemMessage;
        CardView editableListMessage;
        CardView lockedMessage;
        CardView frequencyTableInfoMessage;
        CardView staticMessage;

        public ListDetailViewHolder(View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.list_name);
            frequencyTableMessage  = itemView.findViewById(R.id.frequency_table_meta_message);
            createdByUserMessage = itemView.findViewById(R.id.created_by_user_message_container);
            createdBySystemMessage = itemView.findViewById(R.id.created_by_system_message_container);
            editableListMessage = itemView.findViewById(R.id.editable_message_container);
            lockedMessage = itemView.findViewById(R.id.locked_message_container);
            frequencyTableInfoMessage = itemView.findViewById(R.id.frequency_table_meta_message_container);
            staticMessage = itemView.findViewById(R.id.static_message_container);
            listDetails = itemView.findViewById(R.id.list_details_layout);
            listIDMessage = itemView.findViewById(R.id.list_id_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startViewIntent(getItem(getAdapterPosition()).getId(), getItem(getAdapterPosition()).isFrequencyTable());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    mPositionLongHoldClick = getAdapterPosition();

                    if (mActionMode != null) {
                        return false;
                    }

                    mActionMode = view.startActionMode(mActionModeCallBack);
                    view.setSelected(true);
                    return true;
                }
            });


        }

        void bindTo(StatisticalList statisticalList, int position) {
            listName.setText(getItem(position).getName());

            if (statisticalList.isFrequencyTable()) {
                createdByUserMessage.setVisibility(View.GONE);
                createdBySystemMessage.setVisibility(View.VISIBLE);
                editableListMessage.setVisibility(View.GONE);
                lockedMessage.setVisibility(View.VISIBLE);
                frequencyTableInfoMessage.setVisibility(View.VISIBLE);
                staticMessage.setVisibility(View.VISIBLE);
                listName.setTextColor(ContextCompat.getColor(mContext, R.color.colorSecondary));
                frequencyTableMessage.setText("Associated List ID " + Integer.toString(statisticalList.getAssociatedList()));
            } else {
                createdByUserMessage.setVisibility(View.VISIBLE);
                createdBySystemMessage.setVisibility(View.GONE);
                editableListMessage.setVisibility(View.VISIBLE);
                lockedMessage.setVisibility(View.GONE);
                frequencyTableInfoMessage.setVisibility(View.GONE);
                staticMessage.setVisibility(View.GONE);
                listName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }

            listIDMessage.setText("List ID "+ Integer.toString(statisticalList.getId()));
        }

        void clear() {
            //TODO IMPLEMENT
        }

    }


    // a Statistical List may have changed if reloaded from the database,
    // but ID is fixed.
    private static DiffUtil.ItemCallback<StatisticalList> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StatisticalList>() {

                @Override
                public boolean areItemsTheSame(StatisticalList oldStatisticalList, StatisticalList newStatisticalList) {
                    return oldStatisticalList.getId() == newStatisticalList.getId();
                }
                @Override
                public boolean areContentsTheSame(StatisticalList oldStatisticalList,
                                                  StatisticalList newStatisticalList) {
                    return oldStatisticalList.equals(newStatisticalList);
                }
            };

    /*
    * Context menu
    *
    *
    *
    *
    *
     */

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu_list_selection, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_view_list:
                    startViewIntent(getItem(mPositionLongHoldClick).getId(), getItem(mPositionLongHoldClick).isFrequencyTable());
                    mode.finish();
                    return true;
                case R.id.action_delete_list:
                    if (mContext instanceof ViewableListsActivity) {
                        ((ViewableListsActivity)mContext).removeList(getItem(mPositionLongHoldClick));
                    }
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };


    private void startViewIntent(int listID, boolean isFrequencyTable) {
        if(!isFrequencyTable) {
            Intent intent = new Intent(mContext, ViewSingleEditableListActivity.class);
            intent.putExtra(EXTRA_LIST_ID, listID);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, ViewFrequencyTableActivity.class);
            intent.putExtra(EXTRA_LIST_ID, listID);
            mContext.startActivity(intent);
        }
    }
}

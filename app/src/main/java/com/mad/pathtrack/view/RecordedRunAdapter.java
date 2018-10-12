package com.mad.pathtrack.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.pathtrack.R;
import com.mad.pathtrack.model.RecordedRun;

import java.util.List;

public class RecordedRunAdapter extends RecyclerView.Adapter<RecordedRunAdapter.RecordedRunViewHolder> {


    public class RecordedRunViewHolder extends RecyclerView.ViewHolder{
        private final TextView descriptionItemView;

        public RecordedRunViewHolder(View itemView){
            super(itemView);
            descriptionItemView = itemView.findViewById(R.id.item_description);
        }
    }

    private final LayoutInflater mInflater;
    private List<RecordedRun> mAllRuns;

    public RecordedRunAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecordedRunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_item,parent,false);

        return new RecordedRunViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordedRunAdapter.RecordedRunViewHolder holder, int position) {
        if(mAllRuns !=null){
            RecordedRun current = mAllRuns.get(position);
            holder.descriptionItemView.setText(current.getDescription());
        }else{
            holder.descriptionItemView.setText("No data");
        }
    }

    void setRuns(List<RecordedRun> runs){
        mAllRuns = runs;
        notifyDataSetChanged();

    }

    public int getItemCount() {
        if (mAllRuns != null)
            return mAllRuns.size();
        else return 0;
    }
}

package com.mad.pathtrack.view;

import android.content.Context;
import android.content.Intent;
import android.icu.text.Collator;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mad.pathtrack.R;
import com.mad.pathtrack.model.RecordedRun;

import java.util.List;

public class RecordedRunAdapter extends RecyclerView.Adapter<RecordedRunAdapter.RecordedRunViewHolder> {


    public class RecordedRunViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView mDescriptionItemView, mDistanceItemView;
        private int mPosition;
        public static final String TYPE_KEY = "type";
        public static final String ID_KEY = "id";
        public static final String DISPLAY_MODE = "display";



        public RecordedRunViewHolder(View itemView){
            super(itemView);
            mDescriptionItemView = itemView.findViewById(R.id.item_description);
            mDistanceItemView = itemView.findViewById(R.id.item_distance);
            RelativeLayout rl = itemView.findViewById(R.id.item_main_layout);
            rl.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.mPosition = getAdapterPosition();
            RecordedRun run = mAllRuns.get(mPosition);
            Intent intent = new Intent(mContext, MapsActivity.class);
            intent.putExtra(TYPE_KEY, DISPLAY_MODE);
            intent.putExtra(ID_KEY,run.getId());
            mContext.startActivity(intent);

        }


    }

    private final LayoutInflater mInflater;
    private List<RecordedRun> mAllRuns;
    private Context mContext;

    public RecordedRunAdapter(Context context){
        mContext = context;
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
            holder.mDescriptionItemView.setText(current.getDescription());
            holder.mDistanceItemView.setText(Double.toString(current.getDistance()));
        }else{
            holder.mDescriptionItemView.setText("No data");
            holder.mDistanceItemView.setText("No data");
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

package com.monsalud.classblaster.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monsalud.classblaster.Entity.TermEntity;
import com.monsalud.classblaster.R;
import java.util.List;

public class TermRecyclerAdapter extends RecyclerView.Adapter<TermRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private List<TermEntity> mTerms;
    private final LayoutInflater mLayoutInflater;
    private int mTermID;

    public TermRecyclerAdapter(Context context, List<TermEntity> terms) {
        mContext = context;
        mTerms = terms;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_term_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TermEntity term = mTerms.get(position);
        holder.mTextViewTerm.setText(term.getTerm_name());
        holder.mTextViewDates.setText(term.toStringDates());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

//    public void setWords(List<TermEntity> words) {
//        mTerms = words;
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextViewTerm;
        public final TextView mTextViewDates;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTerm = (TextView) itemView.findViewById(R.id.textview_notename);
            mTextViewDates = (TextView) itemView.findViewById(R.id.text_dates);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TermEntity currentTerm = mTerms.get(mCurrentPosition);
                    Intent intent = new Intent(mContext, TermActivity.class);
                    intent.putExtra(TermActivity.TERM_POSITION, mCurrentPosition);
                    intent.putExtra(TermActivity.TERM_ID, currentTerm.getTerm_id());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

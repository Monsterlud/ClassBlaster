package com.monsalud.classblaster.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monsalud.classblaster.Entity.AssessmentEntity;
import com.monsalud.classblaster.Entity.CourseEntity;
import com.monsalud.classblaster.R;

import java.util.List;

public class AssessmentRecyclerAdapter extends RecyclerView.Adapter<AssessmentRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<AssessmentEntity> mAssessments;
    private final LayoutInflater mLayoutInflater;

    public AssessmentRecyclerAdapter(Context context, List<AssessmentEntity> assessments) {
        mContext = context;
        mAssessments= assessments;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_assessment_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssessmentEntity assessment = mAssessments.get(position);
        holder.mTextViewAssessmentName.setText(assessment.getAssessment_title());
        holder.mTextViewAssessmentDate.setText(assessment.toStringAssessmentDate());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextViewAssessmentName;
        public final TextView mTextViewAssessmentDate;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewAssessmentName = (TextView) itemView.findViewById(R.id.text_assessment_name);
            mTextViewAssessmentDate = (TextView) itemView.findViewById(R.id.text_assessment_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AssessmentEntity currentAssessment = mAssessments.get(mCurrentPosition);
                    Intent intent = new Intent(mContext, AssessmentActivity.class);
                    intent.putExtra(AssessmentActivity.ASSESSMENT_POSITION, mCurrentPosition);
                    intent.putExtra(AssessmentActivity.ASSESSMENT_ID, currentAssessment.getAssessment_id());
                    intent.putExtra(AssessmentActivity.ASSESSMENT_TITLE, currentAssessment.getAssessment_title());
                    intent.putExtra(AssessmentActivity.ASSESSMENT_DATE, currentAssessment.getAssessment_end_date());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

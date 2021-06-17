package com.monsalud.classblaster.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monsalud.classblaster.Entity.CourseEntity;
import com.monsalud.classblaster.R;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CourseEntity> mCourses;
    private final LayoutInflater mLayoutInflater;

    public CourseRecyclerAdapter(Context context, List<CourseEntity> courses) {
        mContext = context;
        mCourses= courses;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseEntity course = mCourses.get(position);
        holder.mTextViewCourseName.setText(course.getCourse_name());
        holder.mTextViewCourseDates.setText(course.toStringDates());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextViewCourseName;
        public final TextView mTextViewCourseDates;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewCourseName = (TextView) itemView.findViewById(R.id.text_course_name);
            mTextViewCourseDates = (TextView) itemView.findViewById(R.id.text_course_dates);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseEntity currentCourse = mCourses.get(mCurrentPosition);
                    Intent intent = new Intent(mContext, CourseActivity.class);
                    intent.putExtra(CourseActivity.COURSE_POSITION, mCurrentPosition);
                    intent.putExtra(CourseActivity.COURSE_ID, currentCourse.getCourse_id());
                    intent.putExtra(CourseActivity.COURSE_STATUS, currentCourse.getCourse_status());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

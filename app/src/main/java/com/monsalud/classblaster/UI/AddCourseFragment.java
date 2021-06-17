package com.monsalud.classblaster.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.monsalud.classblaster.R;

import java.util.Calendar;


public class AddCourseFragment extends Fragment {

    Button mCancelBtn;
    Button mAddBtn;

    private OnCancelAddCourseListener mOnCancelAddCourseListener;
    private OnAddCourseListener mOnAddCourseListener;

    public static EditText etCourseName;
    public static TextView dpCourseStartDate;
    public static TextView dpCourseEndDate;
    public static Spinner spinCourseStatus;
    public static EditText etInstructorName;
    public static EditText etInstructorEmail;
    public static EditText etInstructorPhone;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerEnd;



    public static AddCourseFragment newInstance() { return new AddCourseFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {

        //Inflate the fragment
        View view = inflater.inflate(R.layout.fragment_addcourse, container, false);

        //Find all views
        mCancelBtn = view.findViewById(R.id.button_addassessment_cancel);
        mAddBtn = view.findViewById(R.id.button_addassessment_addassessment);
        mCancelBtn.setOnClickListener(buttonCancelListener);
        mAddBtn.setOnClickListener(buttonAddListener);
        etCourseName = view.findViewById(R.id.edittext_addassessment_assessmenttitle);
        etInstructorName = view.findViewById(R.id.et_addcourse_instructorname);
        etInstructorEmail = view.findViewById(R.id.et_addcourse_email);
        etInstructorPhone = view.findViewById(R.id.et_addcourse_phone);

        dpCourseStartDate = view.findViewById(R.id.textview_addassessment_startdatepicker);
        dpCourseEndDate = view.findViewById(R.id.textview_addcourse_enddatepicker);
        spinCourseStatus = (Spinner) view.findViewById(R.id.spinner_addcourse_coursestatus);

        spinCourseStatus.setSelection(0);

        //Set onClickListener for the start date's textview
        dpCourseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Widget_Material,
                        mDateSetListenerStart,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.gravity = Gravity.CENTER;
                params.width = 800;
                params.height = 1400;
                dialog.show();
            }
        });

        //Set the start date's textview with the date chosen with the DatePickerDialog
        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            String startDate;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Adjust month from array position to actual number
                month++;
                if (month < 10) {
                    if(dayOfMonth < 10)
                        startDate = year + "-0" + month + "-0" + dayOfMonth;
                    else
                        startDate = year + "-0" + month + "-" + dayOfMonth;
                }

                //mCurrentCourse.setCourse_start(LocalDate.parse(date));
                updateLabelStart(startDate);
            }
        };

        //Set OnClickListener for the end date's textview
        dpCourseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Widget_Material,
                        mDateSetListenerEnd,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.gravity = Gravity.CENTER;
                params.width = 800;
                params.height = 1400;
                dialog.show();
            }
        });

        //Set the end date's textview with the date chosen with the DatePickerDialog
        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            String endDate;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                if (month < 10) {
                    if(dayOfMonth < 10)
                        endDate = year + "-0" + month + "-0" + dayOfMonth;
                    else
                        endDate = year + "-0" + month + "-" + dayOfMonth;
                }

                //mCurrentCourse.setCourse_end(LocalDate.parse(date));
                updateLabelEnd(endDate);
            }
        };
        return view;
    }

    //Update the Labels (used after the DatePickerDialog chooses new date)
    private void updateLabelStart(String date) {
        dpCourseStartDate.setText(date);
    }
    private void updateLabelEnd(String date) {
        dpCourseEndDate.setText(date);
    }

    //Define Interfaces for the hosting activities to implement
    public interface OnCancelAddCourseListener {
        void onCancel();
    }
    public interface OnAddCourseListener {
        void onAddCourse();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //Check to make sure that the hosting activity is implementing the correct Listeners
        if (context instanceof AddCourseFragment.OnCancelAddCourseListener) {
            mOnCancelAddCourseListener = (AddCourseFragment.OnCancelAddCourseListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnCancelAddCourseListener");
        }
        if (context instanceof AddCourseFragment.OnAddCourseListener) {
            mOnAddCourseListener = (AddCourseFragment.OnAddCourseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnAddCourseListener");
        }
    }

    //Set Listeners to null when detaching fragment from hosting activity
    @Override
    public void onDetach() {
        super.onDetach();
        mOnCancelAddCourseListener = null;
        mOnAddCourseListener = null;
    }

    //Set click actions
    private View.OnClickListener buttonCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnCancelAddCourseListener.onCancel();
        }
    };
    private View.OnClickListener buttonAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnAddCourseListener.onAddCourse();
        }
    };
}
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.monsalud.classblaster.R;

import java.util.Calendar;

public class AddTermFragment extends Fragment {

    Button mCancelBtn;
    Button mAddBtn;

    private OnCancelAddTermListener mOnCancelAddTermListener;
    private OnAddTermListener mOnAddTermListener;

    public static EditText etTermName;
    public static TextView dpTermStartDate;
    public static TextView dpTermEndDate;
    public static RadioButton bIsActive;
    public static RadioButton bIsNotActive;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerEnd;

    public static AddTermFragment newInstance() { return new AddTermFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_addterm, container, false);

        mCancelBtn = view.findViewById(R.id.button_addterm_cancel);
        mCancelBtn.setOnClickListener(buttonCancelListener);
        mAddBtn = view.findViewById(R.id.button_addassessment_addassessment);
        mAddBtn.setOnClickListener(buttonAddListener);
        etTermName = view.findViewById(R.id.edittext_addassessment_assessmenttitle);

        dpTermStartDate = view.findViewById(R.id.textview_addterm_startdatepicker);
        dpTermEndDate = view.findViewById(R.id.textview_addterm_enddatepicker);
        bIsActive = view.findViewById(R.id.radiobutton_addassessment_assessment);
        bIsNotActive = view.findViewById(R.id.radiobutton_addassessment_performance);

        dpTermStartDate.setOnClickListener(new View.OnClickListener() {
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

        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            String startDate;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
        dpTermEndDate.setOnClickListener(new View.OnClickListener() {
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
    private void updateLabelStart(String date) {
        dpTermStartDate.setText(date);
    }
    private void updateLabelEnd(String date) {
        dpTermEndDate.setText(date);
    }
    public interface OnCancelAddTermListener {
        void onCancel();
    }

    public interface OnAddTermListener {
        void onAddTerm();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCancelAddTermListener) {
            mOnCancelAddTermListener = (OnCancelAddTermListener) context;

        } else {
            throw new RuntimeException(context.toString()
            + "must implement OnCancelAddTermListener");
        }
        if (context instanceof OnAddTermListener) {
            mOnAddTermListener = (OnAddTermListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnAddTermListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnCancelAddTermListener = null;
        mOnAddTermListener = null;
    }

    private View.OnClickListener buttonCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnCancelAddTermListener.onCancel();
        }
    };
    private View.OnClickListener buttonAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnAddTermListener.onAddTerm();
        }
    };
}
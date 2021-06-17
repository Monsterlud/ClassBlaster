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

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddAssessmentFragment extends Fragment {

    private Button mCancelBtn;
    private Button mAddBtn;
    
    public static EditText etAssessmentName;
    public static TextView dpAssessmentDate;
    public static RadioButton assessmentType;
    public static RadioButton performanceType;

    private OnCancelAddAssessmentListener mOnCancelAddAssessmentListener;
    private OnAddAssessmentListener mOnAddAssessmentListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static AddAssessmentFragment newInstance() {
        return new AddAssessmentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Inflate the view
        View view = inflater.inflate(R.layout.fragment_addassessment, container, false);

        //Find all views
        mCancelBtn = view.findViewById(R.id.button_addassessment_cancel);
        mAddBtn = view.findViewById(R.id.button_addassessment_addassessment);
        mCancelBtn.setOnClickListener(buttonCancelListener);
        mAddBtn.setOnClickListener(buttonAddListener);
        etAssessmentName = view.findViewById(R.id.edittext_addassessment_assessmenttitle);
        dpAssessmentDate = view.findViewById(R.id.textview_addassessment_startdatepicker);
        assessmentType = view.findViewById(R.id.radiobutton_addassessment_assessment);
        performanceType = view.findViewById(R.id.radiobutton_addassessment_performance);

        //set OnClickListener for the date textview
        dpAssessmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Widget_Material,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.gravity = Gravity.CENTER;
                params.width = 800;
                params.height = 1400;
                dialog.show();
            }
        });

        //Set the date textview with a new date from the DatePickerDialog
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            String assessmentDate;

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Adjust month from array position to actual number
                month++;
                if (month < 10) {
                    if (dayOfMonth < 10)
                        assessmentDate = year + "-0" + month + "-0" + dayOfMonth;
                    else
                        assessmentDate = year + "-0" + month + "-" + dayOfMonth;
                }

                updateLabel(assessmentDate);
            }
        };

        return view;
    }

    //Update the Label (used after the DatePickerDialog chooses new date)
    private void updateLabel(String date) {
        dpAssessmentDate.setText(date);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //Check to make sure that Listeners are implemented by the hosting Activity
        if (context instanceof OnCancelAddAssessmentListener) {
            mOnCancelAddAssessmentListener = (OnCancelAddAssessmentListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnCancelAddAssessmentListener");
        }
        if (context instanceof OnAddAssessmentListener) {
            mOnAddAssessmentListener = (OnAddAssessmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnAddAssessmentListener");
        }

    }

    //Define interfaces for hosting Activities to implement
    public interface OnAddAssessmentListener {
        void onAddAssessment();
    }
    public interface OnCancelAddAssessmentListener {
        void onCancel();
    }

    //Set click actions
    private View.OnClickListener buttonCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnCancelAddAssessmentListener.onCancel();
        }
    };
    private View.OnClickListener buttonAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnAddAssessmentListener.onAddAssessment();
        }
    };
}


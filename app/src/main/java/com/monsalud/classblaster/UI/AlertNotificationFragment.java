package com.monsalud.classblaster.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.monsalud.classblaster.R;

import java.util.Calendar;


public class AlertNotificationFragment extends Fragment {

    private Button mSetAlarm;
    private OnAddAlarmListener mOnAddAlarmListener;
    public static TextView dpAlarmDate;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;


    public static AlertNotificationFragment newInstance() {
        AlertNotificationFragment fragment = new AlertNotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert_notification, container, false);


        mSetAlarm = view.findViewById(R.id.button_notification_setalarm);
        mSetAlarm.setOnClickListener(buttonSetAlarmListener);
        dpAlarmDate = view.findViewById(R.id.textview_notification_date);

        dpAlarmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Widget_Material,
                        mOnDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.gravity = Gravity.CENTER;
                params.width = 800;
                params.height = 1400;
                dialog.show();
            }
        });

        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            String alarmDate;

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Adjust month from array position to actual number
                month++;
                if (month < 10) {
                    if (dayOfMonth < 10)
                        alarmDate = year + "-0" + month + "-0" + dayOfMonth;
                    else
                        alarmDate = year + "-0" + month + "-" + dayOfMonth;
                }

                //mCurrentCourse.setCourse_start(LocalDate.parse(date));
                updateLabel(alarmDate);
            }
        };

        return view;
    }
    private void updateLabel(String date) {
        dpAlarmDate.setText(date);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAddAlarmListener) {
            mOnAddAlarmListener = (OnAddAlarmListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnSetAlarmDateListener");
        }
    }
    private View.OnClickListener buttonSetAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOnAddAlarmListener.onAddAlarm();
        }
    };
        public interface OnAddAlarmListener {
        void onAddAlarm();
    }
}


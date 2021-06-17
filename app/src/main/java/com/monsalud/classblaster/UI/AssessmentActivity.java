package com.monsalud.classblaster.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.monsalud.classblaster.Database.CBRepository;
import com.monsalud.classblaster.Database.ClassBlasterDatabase;
import com.monsalud.classblaster.Entity.AssessmentEntity;
import com.monsalud.classblaster.Entity.CourseEntity;
import com.monsalud.classblaster.R;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class AssessmentActivity extends AppCompatActivity {

    CBRepository mRepository;
    public static final String ASSESSMENT_POSITION = "com.monsalud.classblaster.ASSESSMENT_POSITION";
    public static final String ASSESSMENT_ID = "com.monsalud.classblaster.ASSESSMENT_ID";
    public static final String ASSESSMENT_TITLE = "com.monsalud.classblaster.ASSESSMENT_TITLE";
    public static final String ASSESSMENT_DATE = "com.monslaud.classblaster.ASSESSMENT_DATE";
    private static String TAG = "AssessmentActivity";

    public static int id_currentCourse;
    private AssessmentEntity mCurrentAssessment;
    private List<AssessmentEntity> mAllAssessments;
    private TextView mAssessmentTitle;
    private int mCurrentAssessmentID;
    private TextView mAssessmentID;
    private TextView mAssessmentDate;
    private TextView mAssessmentCourse;
    private String mCurrentAssessmentTitle;
    private String mCurrentAssessmentCourseName;
    private List<CourseEntity> mAllCourses;
    private RadioButton mTypeRadioAssessment;
    private RadioButton mTypeRadioPerformance;
    private AssessmentEntity.AssessmentType mAssessmentType;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int mCurrentAssessmentCourseID;
    private int numAlert = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        /*This alerts the CourseActivity that we have navigated from the AssessmentActivity
        and that the Course that is displayed must be the Course that the Assessment belonged to
         */
        CourseActivity.mCurrentCourseID = -1;

        mRepository = new CBRepository(getApplication());
        Intent intent = getIntent();
        mCurrentAssessmentID = intent.getIntExtra(ASSESSMENT_ID, -1);
        mCurrentAssessmentTitle = intent.getStringExtra(ASSESSMENT_TITLE);

        mAllAssessments = mRepository.getAllAssessments();
        for (AssessmentEntity assessment : mAllAssessments) {
            if (assessment.getAssessment_id() == mCurrentAssessmentID)
                mCurrentAssessment = assessment;
        }
        id_currentCourse = mCurrentAssessment.getCourse_id_fk();
        mAssessmentDate = findViewById(R.id.textview_assesment_date);

        //Set OnClickListener for the assessment date's textview
        mAssessmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AssessmentActivity.this,
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

        //Set the assessment date's textview with the date chosen with the DatePickerDialog
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            String date;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                if (month < 10) {
                    if(dayOfMonth < 10)
                    date = year + "-0" + month + "-0" + dayOfMonth;
                    else
                        date = year + "-0" + month + "-" + dayOfMonth;
                }
                else date = year + "-" + month + "-" + dayOfMonth;
                mCurrentAssessment.setAssessment_end_date(LocalDate.parse(date));
                updateLabel();
            }
        };

        initializeDisplayContents();
    }

    //Update the Label (used after the DatePickerDialog chooses new date)
    private void updateLabel() {
        mAssessmentDate.setText(mCurrentAssessment.toStringAssessmentDate());
    }

    //Deletes the Assessment from the Database then navigates back to the Course
    public void deleteAssessment (View view){
            mRepository.deleteAssessment(mCurrentAssessmentID);
            Intent intent = new Intent(AssessmentActivity.this, CourseActivity.class);
            startActivity(intent);
        }
    //Updates the Assessment with the information currently displayed
    public void updateAssessment (View view){
        if (mTypeRadioAssessment.isChecked()) {
            mAssessmentType = AssessmentEntity.AssessmentType.Objective;
        } else
            mAssessmentType = AssessmentEntity.AssessmentType.Performance;

        AssessmentEntity assessment = new AssessmentEntity(
                Integer.parseInt(String.valueOf(mAssessmentID.getText())),
                mAssessmentType,
                mAssessmentTitle.getText().toString(),
                LocalDate.parse(mAssessmentDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy")),
                mCurrentAssessmentCourseID);


        mRepository.updateAssessment(assessment);
        Intent intent = new Intent(AssessmentActivity.this, CourseActivity.class);
        startActivity(intent);
    }

    private void initializeDisplayContents() {
        mAssessmentID = findViewById(R.id.edit_text_assessment_id);
        mAssessmentTitle = findViewById(R.id.edit_text_assessment_title);
        mAssessmentDate = findViewById(R.id.textview_assesment_date);
        mAssessmentCourse = findViewById(R.id.edit_text_course_name_assessment);
        mTypeRadioAssessment = findViewById(R.id.radiobutton_assessment);
        mTypeRadioPerformance = findViewById(R.id.radiobutton_performance);

        mAssessmentID.setText(String.valueOf(mCurrentAssessmentID));
        mAssessmentTitle.setText(mCurrentAssessmentTitle);
        mAssessmentDate.setText(mCurrentAssessment.toStringAssessmentDate());

        mAllCourses = mRepository.getAllCourses();
        for (CourseEntity course : mAllCourses) {
            if (course.getCourse_id() == id_currentCourse) {
                mCurrentAssessmentCourseName = course.getCourse_name();
                mCurrentAssessmentCourseID = course.getCourse_id();
            }
        }
        mAssessmentCourse.setText(mCurrentAssessmentCourseName);

        mAssessmentType = mCurrentAssessment.getAssessment_type();
        if (mAssessmentType == AssessmentEntity.AssessmentType.Objective) {
            mTypeRadioAssessment.setChecked(true);
        } else
            mTypeRadioPerformance.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alert_assessment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notification_assessmentedate:
                //Creates and sends a broadcast intent with the date from the Assessment
                Intent intent = new Intent(AssessmentActivity.this, CBReceiver.class);
                intent.putExtra("key", "Your assessment for the course \" "
                        + mCurrentAssessmentCourseName + "\" is today!");
                ClassBlasterDatabase.numAlert++;
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentActivity.this, ClassBlasterDatabase.numAlert, intent,0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Instant instant = (LocalDate.parse(mAssessmentDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy"))).atStartOfDay(ZoneId.systemDefault()).toInstant();
                long triggerDateMillis = instant.toEpochMilli();

                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerDateMillis, sender);

                Toast.makeText(getApplicationContext(), "An alert has been set for this assessment date", Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



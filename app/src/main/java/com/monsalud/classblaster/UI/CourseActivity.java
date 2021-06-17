package com.monsalud.classblaster.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class CourseActivity extends AppCompatActivity
        implements AddAssessmentFragment.OnAddAssessmentListener, AddAssessmentFragment.OnCancelAddAssessmentListener{

    private AssessmentRecyclerAdapter mAssessmentRecyclerAdapter;
    private CBRepository mRepository;

    public static final String COURSE_POSITION = "com.monsalud.classblaster.COURSE_POSITION";
    public static final String COURSE_ID = "com.monsalud.classblaster.COURSE_ID";
    public static final String COURSE_STATUS = "com.monsalud.classblaster.COURSE_STATUS";

    public static int id_currentTerm;
    public static int mCurrentCourseID;
    public static CourseEntity mCurrentCourse;
    private List<CourseEntity> mCourses;

    private TextView mTextViewIdNumber;
    private EditText mEditTextCourseName;
    private TextView mEditTextStartCourseDate;
    private TextView mEditTextEndCourseDate;
    private Spinner mSpinnerCourseStatus;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerEnd;
    private Fragment mFragment;

    private EditText mEditTextInstructorName;
    private EditText mEditTextInstructorEmail;
    private EditText mEditTextInstructorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mRepository = new CBRepository(getApplication());
        /* This alerts the TermActivity that we have navigated from the CourseActivity
        and that the Term that is displayed must be the Term that the Course belonged to
         */
        TermActivity.mCurrentTermID = -1;

        mEditTextStartCourseDate = findViewById(R.id.textview_course_start);
        mEditTextEndCourseDate = findViewById(R.id.textview_course_end);

        //Set OnClickListener to the start date's textview
        mEditTextStartCourseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CourseActivity.this,
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

        //Set the course date's textview with the date chosen by the DatePickerDialog
        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
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

                mCurrentCourse.setCourse_start(LocalDate.parse(date));
                updateLabelStart();
            }
        };

        //Set OnClickListener for the end date's textview
        mEditTextEndCourseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CourseActivity.this,
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

        //Set the end date's textview with the date chosen by the DatePickerDialog
        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
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

                mCurrentCourse.setCourse_end(LocalDate.parse(date));
                updateLabelEnd();
            }
        };

        readDisplayStateValues();
        initializeDisplayContents();
    }

    //Update the Label (used after the DatePickerDialog chooses new date)
    private void updateLabelStart() {
        mEditTextStartCourseDate.setText(mCurrentCourse.toStringStartDate());
    }
    private void updateLabelEnd() {
        mEditTextEndCourseDate.setText(mCurrentCourse.toStringEndDate());
    }

    private void readDisplayStateValues() {

        /*Identify if the navigation to this page originated from
        TermActivity or AssessmentActivity then assign the correct CourseID.
        This is necessary for navigating back to the correct Course.
         */
        if (mCurrentCourseID == -1) {
            mCurrentCourseID = AssessmentActivity.id_currentCourse;
        } else if(mCurrentCourseID == -2) {
            mCurrentCourseID = NoteListActivity.id_currentCourse;
        } else
            mCurrentCourseID = getIntent().getIntExtra(COURSE_ID, -1);

        NoteListActivity.mCurrentCourseForNotesID = mCurrentCourseID;

                mCourses = mRepository.getAllCourses();
        for (CourseEntity course : mCourses) {
            if (course.getCourse_id() == mCurrentCourseID) mCurrentCourse = course;
        }
        id_currentTerm = mCurrentCourse.getTerm_id_fk();
    }

        @Override
        protected void onResume() {
            super.onResume();
            //Make sure that the RecyclerAdapter knows that the data set has changed
            mAssessmentRecyclerAdapter.notifyDataSetChanged();
        }


    private void initializeDisplayContents() {

        final RecyclerView recyclerAssessments = (RecyclerView) findViewById(R.id.list_assessments);
        final LinearLayoutManager assessmentsLayoutManager = new LinearLayoutManager(this);
        recyclerAssessments.setLayoutManager(assessmentsLayoutManager);

        List<AssessmentEntity> assessments = mRepository.getAssessmentsForCourse(mCurrentCourseID);
        mAssessmentRecyclerAdapter = new AssessmentRecyclerAdapter(this, assessments);
        recyclerAssessments.setAdapter(mAssessmentRecyclerAdapter);

        //Find all views
        mTextViewIdNumber = findViewById(R.id.textview_COURSEIDNUMBER);
        mEditTextCourseName = findViewById(R.id.edit_text_course_name_assessment);
        mEditTextStartCourseDate = findViewById(R.id.textview_course_start);
        mEditTextEndCourseDate = findViewById(R.id.textview_course_end);
        mSpinnerCourseStatus = findViewById(R.id.spinner_course_status);
        mEditTextInstructorName = findViewById(R.id.edittext_course_instructorname);
        mEditTextInstructorEmail = findViewById(R.id.edittext_course_instructoremail);
        mEditTextInstructorPhone = findViewById(R.id.edittext_course_instructorphone);

        //Set views with the correct values for this Course
        mTextViewIdNumber.setText(String.valueOf(mCurrentCourse.getCourse_id()));
        mEditTextCourseName.setText(mCurrentCourse.getCourse_name());
        mEditTextStartCourseDate.setText(mCurrentCourse.toStringStartDate());
        mEditTextEndCourseDate.setText(mCurrentCourse.toStringEndDate());
        mSpinnerCourseStatus.setAdapter(new ArrayAdapter<CourseEntity.CourseStatus>(this, android.R.layout.simple_spinner_item, CourseEntity.CourseStatus.values()));
        mEditTextInstructorName.setText(mCurrentCourse.getInstructor_name());
        mEditTextInstructorEmail.setText(mCurrentCourse.getInstructor_email());
        mEditTextInstructorPhone.setText(mCurrentCourse.getInstructor_phone());

        CourseEntity.CourseStatus courseStatus = mCurrentCourse.getCourse_status();
        if(courseStatus == CourseEntity.CourseStatus.InProgress) mSpinnerCourseStatus.setSelection(0);
        else if(courseStatus == CourseEntity.CourseStatus.Completed) mSpinnerCourseStatus.setSelection(1);
        else if(courseStatus == CourseEntity.CourseStatus.Dropped) mSpinnerCourseStatus.setSelection(2);
        else mSpinnerCourseStatus.setSelection(3);
    }

    //Called when the notes button is clicked
    public void goToNotes(View view) {
        Intent intent = new Intent(this, NoteListActivity.class);
        intent.putExtra(NoteListActivity.COURSE_NAME, mCurrentCourse.getCourse_name());
        intent.putExtra(NoteListActivity.COURSE_ID, mCurrentCourseID);
        startActivity(intent);
    }

    //Deletes the Course from the database then navigates back to the Term
    public void deleteCourse(View view) {
            mRepository.deleteCourse(mCurrentCourseID);
            Intent intent = new Intent(CourseActivity.this, TermActivity.class);
            startActivity(intent);
    }

    //Updates the Course with the information currently displayed
    public void updateCourse (View view) {
        CourseEntity.CourseStatus courseStatus;
        int position = mSpinnerCourseStatus.getSelectedItemPosition();
        if (position == 0)
            courseStatus = CourseEntity.CourseStatus.InProgress;
        else if (position == 1)
            courseStatus = CourseEntity.CourseStatus.Completed;
        else if (position == 2)
            courseStatus = CourseEntity.CourseStatus.Dropped;
        else
             courseStatus = CourseEntity.CourseStatus.PlanToTake;

        CourseEntity course = new CourseEntity(
                Integer.parseInt(String.valueOf(mTextViewIdNumber.getText())),
                mCurrentCourse.getTerm_id_fk(),
                mEditTextCourseName.getText().toString(),
                LocalDate.parse(mEditTextStartCourseDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy")),
                LocalDate.parse(mEditTextEndCourseDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy")),
                courseStatus,
                mEditTextInstructorName.getText().toString(),
                mEditTextInstructorEmail.getText().toString(),
                mEditTextInstructorPhone.getText().toString());

        mRepository.updateCourse(course);
        Intent intent = new Intent(CourseActivity.this, TermActivity.class);
        startActivity(intent);
    }

    //Navigates to the AddAssessmentFragment
    public void addAssessment(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mFragment = AddAssessmentFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.coursecontainer, mFragment)
                .addToBackStack(null).commit();
    }

    //Called by the AddAssessmentFragment to add that Assessment to the Database
    @Override
    public void onAddAssessment() {
        //Extract which Assessment Type is chosen (assessment or performance)
        AssessmentEntity.AssessmentType assessmentType;
        if(AddAssessmentFragment.assessmentType.isChecked()) assessmentType = AssessmentEntity.AssessmentType.Objective;
        else assessmentType = AssessmentEntity.AssessmentType.Performance;

        //Get the id number of the last term in the arraylist and increment it
        List<AssessmentEntity> allAssessments = mRepository.getAllAssessments();
        int id = allAssessments.get(allAssessments.size() - 1).getAssessment_id();
        id++;

        //Create a Term object using the user-entered information and insert it into the Database
        AssessmentEntity assessment = new AssessmentEntity(
                id,
                assessmentType,
                String.valueOf(AddAssessmentFragment.etAssessmentName.getText()),
                LocalDate.parse(AddAssessmentFragment.dpAssessmentDate.getText()),
                mCurrentCourseID);

        mRepository.insertAssessment(assessment);

        //Close the soft keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        /*Notify the Recycler Adapter that the data set has changed then re-initialize the list
         with the new new Assessment included and go back to the CourseActivity screen
         */
        mAssessmentRecyclerAdapter.notifyDataSetChanged();
        initializeDisplayContents();
        mFragment.getActivity().onBackPressed();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alert_course, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notification_startdate:
                //Creates and sends a broadcast intent with the start date from the Course
                Intent startIntent = new Intent(CourseActivity.this, CBReceiver.class);
                startIntent.putExtra("key", "Today is the start date for the course \""
                        + (mEditTextCourseName.getText()) + "\"");
                ClassBlasterDatabase.numAlert++;
                PendingIntent startSender = PendingIntent.getBroadcast(CourseActivity.this, ClassBlasterDatabase.numAlert, startIntent,0);
                AlarmManager startAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Instant startInstant = (LocalDate.parse(mEditTextStartCourseDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy"))).atStartOfDay(ZoneId.systemDefault()).toInstant();
                long triggerStartDateMillis = startInstant.toEpochMilli();

                startAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerStartDateMillis, startSender);

                Toast.makeText(getApplicationContext(), "An alert has been set for the course start date", Toast.LENGTH_LONG).show();

                return true;

            case R.id.action_notification_enddate:
                //Creates and sends a broadcast intent with the end date from the Course
                Intent endIntent = new Intent(CourseActivity.this, CBReceiver.class);
                endIntent.putExtra("key", "Today is the end date for the course \""
                        + (mEditTextCourseName.getText()) + "\"");
                ClassBlasterDatabase.numAlert++;
                PendingIntent endSender = PendingIntent.getBroadcast(CourseActivity.this, ClassBlasterDatabase.numAlert, endIntent,0);
                AlarmManager endAlertManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Instant endInstant = (LocalDate.parse(mEditTextStartCourseDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy"))).atStartOfDay(ZoneId.systemDefault()).toInstant();
                long triggerEndDateMillis = endInstant.toEpochMilli();

                endAlertManager.set(AlarmManager.RTC_WAKEUP, triggerEndDateMillis, endSender);

                Toast.makeText(getApplicationContext(), "An alert has been set for the course end date", Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Navigates back to the hosting activity
    @Override
    public void onCancel() {
        mFragment.getActivity().onBackPressed();
    }
}

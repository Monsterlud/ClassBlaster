package com.monsalud.classblaster.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.monsalud.classblaster.Database.CBRepository;
import com.monsalud.classblaster.Entity.CourseEntity;
import com.monsalud.classblaster.Entity.TermEntity;
import com.monsalud.classblaster.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class TermActivity extends AppCompatActivity
        implements AddCourseFragment.OnAddCourseListener, AddCourseFragment.OnCancelAddCourseListener{

    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private CBRepository mRepository;

    public static final String TERM_POSITION = "com.monsalud.classblaster.TERM_POSITION";
    public static final String TERM_ID = "com.monsalud.classblaster.TERM_ID";

    public static int mCurrentTermID;
    public static TermEntity mCurrentTerm;
    private List<TermEntity> mAllTerms;
    private TextView mTextViewIdNumber;
    private EditText mEditTextTermName;
    private TextView mEditTextStartTermDate;
    private TextView mEditTextEndTermDate;
    private RadioButton mRadiobuttonTermNotActive;
    private RadioButton mRadiobuttonTermActive;
    private boolean mIsActive;
    private List<CourseEntity> mCourses;
    private Toast mToast;

    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerEnd;
    private Fragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        mRepository = new CBRepository(getApplication());

        mEditTextStartTermDate = findViewById(R.id.textview_term_start_date);
        mEditTextEndTermDate = findViewById(R.id.textview_term_end_date);

        //Set OnClickListener for the start date's textview
        mEditTextStartTermDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TermActivity.this,
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

        //Set start date's textview with the date chosen by the DatePickerDialog
        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            String date;

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                //Change month from the array position to the actual number
                month++;
                if (month < 10) {
                    if (dayOfMonth < 10)
                        date = year + "-0" + month + "-0" + dayOfMonth;
                    else
                        date = year + "-0" + month + "-" + dayOfMonth;
                }

                mCurrentTerm.setTerm_start(LocalDate.parse(date));
                updateLabelStart();
            }
        };

        //Set OnClickListener for the end date's textview
        mEditTextEndTermDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TermActivity.this,
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

        //Set end date's textview with the date chosen by the DatePickerDialog
        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            String date;

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                if (month < 10) {
                    if (dayOfMonth < 10)
                        date = year + "-0" + month + "-0" + dayOfMonth;
                    else
                        date = year + "-0" + month + "-" + dayOfMonth;
                }

                mCurrentTerm.setTerm_end(LocalDate.parse(date));
                updateLabelEnd();
            }
        };

        readDisplayStateValues();
        initializeDisplayContents();
    }

    //Update the Label (used after the DatePickerDialog chooses new date)
    private void updateLabelStart() {
        mEditTextStartTermDate.setText(mCurrentTerm.toStringStartDate());
    }

    private void updateLabelEnd() {
        mEditTextEndTermDate.setText(mCurrentTerm.toStringEndDate());
    }

    private void readDisplayStateValues() {

        if (mCurrentTermID == -1) {
            mCurrentTermID = CourseActivity.id_currentTerm;
        } else
            mCurrentTermID = getIntent().getIntExtra(TERM_ID, -1);

        mAllTerms = mRepository.getAllTerms();
        for (TermEntity term : mAllTerms) {
            if (term.getTerm_id() == mCurrentTermID) mCurrentTerm = term;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mCourseRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContents() {

        //Set RecyclerView with Courses
        final RecyclerView recyclerCourses = (RecyclerView) findViewById(R.id.list_courses);
        final LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(this);
        recyclerCourses.setLayoutManager(coursesLayoutManager);

        mCourses = mRepository.getCoursesForTerm(mCurrentTermID);
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, mCourses);
        recyclerCourses.setAdapter(mCourseRecyclerAdapter);

        //Find all views
        mTextViewIdNumber = findViewById(R.id.text_term_id);
        mEditTextTermName = findViewById(R.id.edit_text_term_name);
        mEditTextStartTermDate = findViewById(R.id.textview_term_start_date);
        mEditTextEndTermDate = findViewById(R.id.textview_term_end_date);
        mRadiobuttonTermActive = findViewById(R.id.radiobutton_term_active);
        mRadiobuttonTermNotActive = findViewById(R.id.radiobutton_term_notactive);

        //Assign mCurrentTerm values to views
        mTextViewIdNumber.setText(String.valueOf(mCurrentTerm.getTerm_id()));
        mEditTextTermName.setText(mCurrentTerm.getTerm_name());
        mEditTextStartTermDate.setText(mCurrentTerm.toStringStartDate());
        mEditTextEndTermDate.setText(mCurrentTerm.toStringEndDate());

        if (mCurrentTerm.isIs_active()) {
            mRadiobuttonTermActive.setChecked(true);
        } else
            mRadiobuttonTermNotActive.setChecked(true);
    }


    //Updates the Term with the information currently displayed
    public void updateTerm(View view) {
        boolean isActive;
        if (mRadiobuttonTermActive.isChecked()) {
            isActive = true;
        } else
            isActive = false;

        TermEntity term = new TermEntity(
                Integer.parseInt(String.valueOf(mTextViewIdNumber.getText())),
                mEditTextTermName.getText().toString(),
                LocalDate.parse(mEditTextStartTermDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy")),
                LocalDate.parse(mEditTextEndTermDate.getText(), DateTimeFormatter.ofPattern("LLLL dd yyyy")),
                isActive);


        mRepository.updateTerm(term);
        Intent intent = new Intent(TermActivity.this, TermListActivity.class);
        startActivity(intent);
    }

    //Deletes the Term on the Database IF THERE ARE NO ASSOCIATED COURSES
    public void deleteTerm(View view) {
        if (mCourses.size() == 0) {
            mRepository.deleteTerm(mCurrentTermID);
            Intent intent = new Intent(TermActivity.this, TermListActivity.class);
            startActivity(intent);
        } else
            mToast = Toast.makeText(TermActivity.this,
                    "YOU CANNOT DELETE A TERM THAT CONTAINS COURSES!",
                    Toast.LENGTH_LONG);
        mToast.show();
    }

    public void addCourse(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mFragment = AddCourseFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.termcontainer, mFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onCancel() {
        mFragment.getActivity().onBackPressed();
    }

    //Adds a Course to the Database and associates it with the current Term
    @Override
    public void onAddCourse() {
        //Extract which Course Status is chosen
        CourseEntity.CourseStatus courseStatus = null;
        int courseStatusPosition = AddCourseFragment.spinCourseStatus.getSelectedItemPosition();
        if(courseStatusPosition == 0) courseStatus = CourseEntity.CourseStatus.InProgress;
        else if(courseStatusPosition == 1) courseStatus = CourseEntity.CourseStatus.Completed;
        else if(courseStatusPosition == 2) courseStatus = CourseEntity.CourseStatus.Dropped;
        else courseStatus = CourseEntity.CourseStatus.PlanToTake;

        //Get the id number of the last term in the arraylist and increment it
        List<CourseEntity> allCourses = mRepository.getAllCourses();
        int id = allCourses.get(allCourses.size() - 1).getCourse_id();
        id++;

        //Create a Course object using the user-entered information and insert it into the Database
        CourseEntity course = new CourseEntity(
                id,
                mCurrentTermID,
                String.valueOf(AddCourseFragment.etCourseName.getText()),
                LocalDate.parse(AddCourseFragment.dpCourseStartDate.getText()),
                LocalDate.parse(AddCourseFragment.dpCourseEndDate.getText()),
                courseStatus,
                String.valueOf(AddCourseFragment.etInstructorName.getText()),
                String.valueOf(AddCourseFragment.etInstructorEmail.getText()),
                String.valueOf(AddCourseFragment.etInstructorPhone.getText())
                );

        mRepository.insertCourse(course);

        //Close the soft keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        /*Notify the Recycler Adapter that the data set has changed then re-initialize the list
         with the new new Course included and go back to the TermActivity screen
         */
        mCourseRecyclerAdapter.notifyDataSetChanged();
        initializeDisplayContents();
        mFragment.getActivity().onBackPressed();
    }
}





package com.monsalud.classblaster.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.monsalud.classblaster.Database.CBRepository;
import com.monsalud.classblaster.Entity.CourseEntity;
import com.monsalud.classblaster.Entity.NoteEntity;
import com.monsalud.classblaster.R;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private CBRepository mRepository;
    public static final String COURSE_NAME = "com.monsalud.classblaster.COURSE_NAME";
    public static final String COURSE_ID = "com.monsalud.classblaster.COURSE_ID";

    private List<NoteEntity> mNotesForCourse;
    public static int id_currentCourse;
    public static int mCurrentCourseForNotesID;
    private CourseEntity mCurrentCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        CourseActivity.mCurrentCourseID = -2;
        mRepository = new CBRepository(getApplication());

        /*Determines where the source of navigation is and, therefore, which Course's
        Note list should be displayed
         */
        if(mCurrentCourseForNotesID == -1)
            id_currentCourse = NoteActivity.id_currentCourse;
        else {
            Intent intent = getIntent();
            id_currentCourse = intent.getIntExtra(COURSE_ID, -1);
        }

        List<CourseEntity> allCourses = mRepository.getAllCourses();

        for(CourseEntity course: allCourses) {
            if(id_currentCourse == course.getCourse_id()) {
                mCurrentCourse = course;
            }
        }

        readDisplayStateValues();
        initializeDisplayContents();
    }

    private void readDisplayStateValues() {
        mNotesForCourse = mRepository.getAllNotesForCourse(id_currentCourse);

        if(mNotesForCourse.size() == 0) {
            List<NoteEntity> allNotes = mRepository.getAllNotes();
            int id = allNotes.get(allNotes.size() - 1).getNote_id();
            id++;
            NoteEntity firstNote = new NoteEntity(id, id_currentCourse, null, null);
            mRepository.insertNote(firstNote);
            mNotesForCourse = mRepository.getAllNotesForCourse(id_currentCourse);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContents() {
        TextView courseName = findViewById(R.id.textview_notelist_coursename);
        courseName.setText(mCurrentCourse.getCourse_name());

        final RecyclerView recyclerNotes = (RecyclerView) findViewById(R.id.list_notes);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerNotes.setLayoutManager(notesLayoutManager);

        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, mNotesForCourse);
        recyclerNotes.setAdapter(mNoteRecyclerAdapter);
    }
}
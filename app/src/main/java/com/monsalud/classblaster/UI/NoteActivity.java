package com.monsalud.classblaster.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.monsalud.classblaster.Database.CBRepository;
import com.monsalud.classblaster.Entity.NoteEntity;
import com.monsalud.classblaster.R;

import java.util.Calendar;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_ID = "com.monsalud.classblaster.NOTE_ID";
    public static final String NOTE_NAME = "com.monsalud.classblaster.NOTE_NAME";
    public static final String NOTE_FIELD = "com.monsalud.classblaster.NOTE_FIELD";
    public static final String NOTE_COURSE_ID = "com.monsalud.classblaster.NOTE_COURSE_ID";
    public static int id_currentCourse;
    private CBRepository mRepository;
    private int mCurrentNoteID;
    private NoteEntity mThisNote;
    private List<NoteEntity> mAllNotes;
    private TextView mNoteID_view;
    private TextView mNoteNAME_view;
    private EditText mNoteFIELD_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        NoteListActivity.mCurrentCourseForNotesID = -1;

        Intent intent = getIntent();
        mCurrentNoteID = intent.getIntExtra(NOTE_ID, -1);
        String noteName = intent.getStringExtra(NOTE_NAME);
        String noteField = intent.getStringExtra(NOTE_FIELD);
        id_currentCourse = intent.getIntExtra(NOTE_COURSE_ID, -1);

        mRepository = new CBRepository(getApplication());
        mAllNotes = mRepository.getAllNotes();
        for(NoteEntity note: mAllNotes) {
            if(mCurrentNoteID == note.getNote_id()) {
                mThisNote = note;
            }
        }

        //Find all views
        mNoteID_view = findViewById(R.id.textview_note_id);
        mNoteNAME_view = findViewById(R.id.edittext_note_notename);
        mNoteFIELD_view = findViewById(R.id.multiline_text_note);

        mNoteID_view.setText(String.valueOf(mCurrentNoteID));
        mNoteNAME_view.setText(noteName);
        mNoteFIELD_view.setText(noteField);
    }
    //Deletes Note from the Database then navigates back to the NoteListActivity
    public void deleteNote(View view) {
        mRepository.deleteNote(mCurrentNoteID);
        Intent intent = new Intent(NoteActivity.this, NoteListActivity.class);
        startActivity(intent);
    }

    //Updates the Note with the information currently displayed
    public void saveNote(View view) {
        NoteEntity note = new NoteEntity(
                Integer.parseInt(String.valueOf(mNoteID_view.getText())),
                mThisNote.getCourse_id_fk(),
                String.valueOf(mNoteNAME_view.getText()),
                String.valueOf(mNoteFIELD_view.getText()));
        mRepository.updateNote(note);
        Intent intent = new Intent(NoteActivity.this, NoteListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sharing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sharing:
                /*Launches the chooser sheet and puts the Extras from the note to be
                diplayed by the chosen app (messages, gmail, etc)
                 */
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "I'm excited to share this note with you!");
                sendIntent.putExtra(Intent.EXTRA_TEXT, mNoteFIELD_view.getText());
                sendIntent.putExtra("sms_body", String.valueOf(mNoteFIELD_view.getText()));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Send note...");
                startActivity(shareIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

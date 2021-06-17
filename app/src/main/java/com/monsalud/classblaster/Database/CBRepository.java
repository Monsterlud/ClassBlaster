package com.monsalud.classblaster.Database;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.monsalud.classblaster.DAO.AssessmentDAO;
import com.monsalud.classblaster.DAO.CourseDAO;
import com.monsalud.classblaster.DAO.NoteDAO;
import com.monsalud.classblaster.DAO.TermDAO;
import com.monsalud.classblaster.Entity.AssessmentEntity;
import com.monsalud.classblaster.Entity.CourseEntity;
import com.monsalud.classblaster.Entity.NoteEntity;
import com.monsalud.classblaster.Entity.TermEntity;

import java.nio.channels.ClosedSelectorException;
import java.util.List;

public class CBRepository {

    //Fields
    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;
    private AssessmentDAO mAssessmentDAO;
    private NoteDAO mNoteDAO;

    private List<TermEntity> mAllTerms;
    private List<CourseEntity> mAllCourses;
    private List<AssessmentEntity> mAllAssessments;
    private List<CourseEntity> mCoursesForTerm;
    private List<AssessmentEntity> mAssessmentsForCourse;
    private List<NoteEntity> mAllNotes;
    private List<NoteEntity> mAllNotesForCourse;
    private TermEntity mTerm;


    //Constructor
    public CBRepository(Application application) {
        ClassBlasterDatabase db = ClassBlasterDatabase.getDatabaseInstance(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
        mNoteDAO = db.noteDAO();

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Methods
    public TermEntity getTerm(int termID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mTerm = mTermDAO.getTerm(termID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mTerm;
    }

    public List<TermEntity> getAllTerms() {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });

        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }


    public List<CourseEntity> getCoursesForTerm(int termID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mCoursesForTerm = mCourseDAO.getCoursesForTerm(termID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mCoursesForTerm;
    }

    public List<CourseEntity> getAllCourses() {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getAllCourses();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    public List<AssessmentEntity> getAssessmentsForCourse(int courseID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentsForCourse = mAssessmentDAO.getAssessmentsForCourse(courseID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAssessmentsForCourse;
    }

    public List<AssessmentEntity> getAllAssessments() {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    public List<NoteEntity> getAllNotesForCourse(int courseID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAllNotesForCourse = mNoteDAO.getAllNotesForCourse(courseID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllNotesForCourse;
    }

    public List<NoteEntity> getAllNotes() {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAllNotes = mNoteDAO.getAllNotes();
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllNotes;
    }


    public void insertTerm(TermEntity term) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.insertTerm(term);
        });
    }

    public void insertCourse(CourseEntity course) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.insertCourse(course);
        });
    }

    public void insertAssessment(AssessmentEntity assessment) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.insertAssessment(assessment);
        });
    }

    public void insertNote(NoteEntity note) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDAO.insertNote(note);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateTerm(TermEntity term) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.updateTerm(term);
        });
    }

    public void updateCourse(CourseEntity course) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.updateCourse(course);
        });
    }

    public void updateAssessment(AssessmentEntity assessment) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.updateAssessment(assessment);
        });
    }

    public void updateNote(NoteEntity note) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDAO.updateNote(note);
        });
    }

    public void deleteTerm(int termID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.deleteTerm(termID);
        });
    }

    public void deleteCourse(int courseID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.deleteCourse(courseID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteAssessment(int assessmentID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.deleteAssessment(assessmentID);
        });
    }

    public void deleteNote(int noteID) {
        ClassBlasterDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDAO.deleteNote(noteID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}









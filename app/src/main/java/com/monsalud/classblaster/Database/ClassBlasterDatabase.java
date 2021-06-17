package com.monsalud.classblaster.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Insert;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.time.LocalDate;

import com.monsalud.classblaster.DAO.AssessmentDAO;
import com.monsalud.classblaster.DAO.CourseDAO;
import com.monsalud.classblaster.DAO.NoteDAO;
import com.monsalud.classblaster.DAO.TermDAO;
import com.monsalud.classblaster.Entity.AssessmentEntity;
import com.monsalud.classblaster.Entity.CourseEntity;
import com.monsalud.classblaster.Entity.NoteEntity;
import com.monsalud.classblaster.Entity.TermEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class, NoteEntity.class}, version = 1)
@TypeConverters(Converters.class)

public abstract class ClassBlasterDatabase extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract NoteDAO noteDAO();

    private static final int NUMBER_OF_THREADS = 4;
    private static final String DATABASE_NAME = "classblasterDB.db";

    public static int numAlert = 0;

    //The Database Executor
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //The Database Instance (Singleton)
    private static volatile ClassBlasterDatabase INSTANCE;

    public static ClassBlasterDatabase getDatabaseInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (ClassBlasterDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ClassBlasterDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        public void onOpen(SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                TermDAO mTermDAO = INSTANCE.termDAO();
                CourseDAO mCourseDAO = INSTANCE.courseDAO();
                AssessmentDAO mAssessmentDAO = INSTANCE.assessmentDAO();
                NoteDAO mNoteDAO = INSTANCE.noteDAO();

                //Delete Terms, Courses, & Assessments for a clean DB every time
                //TO DO: comment out these statements for a persistent DB
                mTermDAO.deleteAllTerms();
                mCourseDAO.deleteAllCourses();
                mAssessmentDAO.deleteAllAssessments();
                mNoteDAO.deleteAllNotes();

                //Populate terms
                TermEntity term = new TermEntity(1, "Spring 2021", LocalDate.parse("2021-01-01"), LocalDate.parse("2021-05-31"), true);
                mTermDAO.insertTerm(term);
                term = new TermEntity(2, "Fall 2021", LocalDate.parse("2021-08-01"), LocalDate.parse("2021-12-31"), false);
                mTermDAO.insertTerm(term);
                term = new TermEntity(3, "Spring 2022", LocalDate.parse("2022-01-01"), LocalDate.parse("2022-05-31"), false);
                mTermDAO.insertTerm(term);
                term = new TermEntity(4, "Fall 2022", LocalDate.parse("2022-08-01"), LocalDate.parse("2022-12-31"), false);
                mTermDAO.insertTerm(term);

                //Populate Courses
                CourseEntity course = new CourseEntity(1, 1, "The Great Chefs of France",
                        LocalDate.parse("2021-01-04"), LocalDate.parse("2021-05-28"), CourseEntity.CourseStatus.InProgress, "Rodger Roberts", "rodger.roberts@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(2, 1, "Regional Chinese Cuisine",
                        LocalDate.parse("2021-01-04"), LocalDate.parse("2021-05-28"), CourseEntity.CourseStatus.InProgress, "Carolyn Sher Decusatis", "carolyn.sher@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(3, 1, "Professional Kitchen Etiquette & Behavior",
                        LocalDate.parse("2021-01-04"), LocalDate.parse("2021-05-28"), CourseEntity.CourseStatus.InProgress, "Rodger Roberts", "rodger.roberts@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(4, 1, "Kitchen Fundamentals",
                        LocalDate.parse("2021-01-04"), LocalDate.parse("2021-05-28"), CourseEntity.CourseStatus.InProgress, "Bill Johnson", "bill.johnson@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(5, 1, "The Basics of Fine Service",
                        LocalDate.parse("2021-01-04"), LocalDate.parse("2021-05-28"), CourseEntity.CourseStatus.Dropped, "Carolyn Sher Decusatis", "carolyn.sher@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(6, 1, "Bread Baking",
                        LocalDate.parse("2021-01-04"), LocalDate.parse("2021-05-28"), CourseEntity.CourseStatus.Completed, "Rodger Roberts", "rodger.roberts@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(7, 1, "Pizza, Pasta, and Italian Sauces",
                        LocalDate.parse("2021-01-04"), LocalDate.parse("2021-05-28"), CourseEntity.CourseStatus.Dropped, "Bill Johnson", "bill.johnson@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(8, 2, "Intermediate Knife Skills",
                        LocalDate.parse("2021-08-02"), LocalDate.parse("2021-12-23"), CourseEntity.CourseStatus.PlanToTake, "Carolyn Sher Decusatis", "carolyn.sher@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(9, 2, "Stocks & Broths: Foundations",
                        LocalDate.parse("2021-08-02"), LocalDate.parse("2021-12-23"), CourseEntity.CourseStatus.PlanToTake, "Rodger Roberts", "rodger.roberts@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(10, 2, "Kitchen Basics: Equipment",
                        LocalDate.parse("2021-08-02"), LocalDate.parse("2021-12-23"), CourseEntity.CourseStatus.PlanToTake, "Bill Johnson", "bill.johnson@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(11, 3, "The Master Sauces",
                        LocalDate.parse("2022-01-03"), LocalDate.parse("2022-05-27"), CourseEntity.CourseStatus.PlanToTake, "Douglas Monsalud", "dmonsal@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(12, 3, "Kitchen Basics: Pastry",
                        LocalDate.parse("2022-01-03"), LocalDate.parse("2022-05-27"), CourseEntity.CourseStatus.Completed, "Carolyn Sher Decusatis", "carolyn.sher@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(13, 3, "Advanced Knife Skills",
                        LocalDate.parse("2022-01-03"), LocalDate.parse("2022-05-27"), CourseEntity.CourseStatus.PlanToTake, "Bill Johnson", "bill.johnson@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(14, 3, "Roasts & Braises: Foundations",
                        LocalDate.parse("2022-01-03"), LocalDate.parse("2022-05-27"), CourseEntity.CourseStatus.PlanToTake, "Carolyn Sher Decusatis", "carolyn.sher@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(15, 4, "Rationale Oven Basics",
                        LocalDate.parse("2022-08-08"), LocalDate.parse("2022-12-23"), CourseEntity.CourseStatus.PlanToTake, "Rodger Roberts", "rodger.roberts@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(16, 4, "Professional Kitchen Inventory & Purchasing",
                        LocalDate.parse("2022-08-08"), LocalDate.parse("2022-12-23"), CourseEntity.CourseStatus.Completed, "Bill Johnson", "bill.johnson@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(17, 4, "Advanced Pastry",
                        LocalDate.parse("2022-08-08"), LocalDate.parse("2022-12-23"), CourseEntity.CourseStatus.PlanToTake, "Carolyn Sher Decusatis", "carolyn.sher@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);
                course = new CourseEntity(18, 4, "Garde Manger",
                        LocalDate.parse("2022-08-08"), LocalDate.parse("2022-12-23"), CourseEntity.CourseStatus.PlanToTake, "Rodger Roberts", "rodger.roberts@wgu.edu", "555-5555");
                mCourseDAO.insertCourse(course);

                //Populate assessments
                AssessmentEntity assessment = new AssessmentEntity(1, AssessmentEntity.AssessmentType.Objective,"Great Chefs Final Exam", LocalDate.parse("2021-05-28"), 1);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(2, AssessmentEntity.AssessmentType.Objective,"Chinese Cuisine Final Exam", LocalDate.parse("2021-05-28"), 2);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(3, AssessmentEntity.AssessmentType.Objective, "Kitchen Etiquette Final Exam", LocalDate.parse("2021-05-28"), 3);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(4, AssessmentEntity.AssessmentType.Performance,"Kitchen Etiquette Performance Exam", LocalDate.parse("2021-05-28"), 3);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(5, AssessmentEntity.AssessmentType.Objective,"Kitchen Fundamentals Final Exam", LocalDate.parse("2021-12-23"), 4);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(6, AssessmentEntity.AssessmentType.Performance, "Knife Skills Performance Exam", LocalDate.parse("2021-12-23"), 8);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(7, AssessmentEntity.AssessmentType.Objective, "Stocks & Broths Final Exam", LocalDate.parse("2021-12-23"), 9);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(8, AssessmentEntity.AssessmentType.Performance, "Stocks & Broths Performance Exam", LocalDate.parse("2021-12-23"), 9);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(9, AssessmentEntity.AssessmentType.Objective, "Kitchen Equipment Final Exam", LocalDate.parse("2021-12-23"), 10);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(10, AssessmentEntity.AssessmentType.Objective, "Master Sauces Final Exam", LocalDate.parse("2022-05-27"), 11);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(11, AssessmentEntity.AssessmentType.Performance, "Master Sauces Performance Exam", LocalDate.parse("2022-05-27"), 11);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(12, AssessmentEntity.AssessmentType.Performance, "Pastry Performance Exam", LocalDate.parse("2022-05-27"), 12);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(13, AssessmentEntity.AssessmentType.Performance, "Advanced Knife Skills Performance Exam", LocalDate.parse("2022-05-27"), 13);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(14, AssessmentEntity.AssessmentType.Objective, "Roasts & Braises Final Exam", LocalDate.parse("2022-05-27"), 14);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(15, AssessmentEntity.AssessmentType.Performance, "Roasts & Braises Performance Exam", LocalDate.parse("2022-05-27"), 14);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(16, AssessmentEntity.AssessmentType.Objective, "Rationale Final Exam", LocalDate.parse("2022-12-23"), 15);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(17, AssessmentEntity.AssessmentType.Objective, "Inventory & Purchasing Final Exam", LocalDate.parse("2022-12-23"), 16);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(18, AssessmentEntity.AssessmentType.Objective, "Advanced Pastry Final Exam", LocalDate.parse("2022-12-23"), 17);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(19, AssessmentEntity.AssessmentType.Performance, "Advanced Pastry Performance Exam", LocalDate.parse("2022-12-23"), 17);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(20, AssessmentEntity.AssessmentType.Objective, "Garde Manger Final Exam", LocalDate.parse("2022-12-23"), 18);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(21, AssessmentEntity.AssessmentType.Performance, "Garde Manger Performance Exam", LocalDate.parse("2022-12-23"), 18);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(22, AssessmentEntity.AssessmentType.Performance, "Fine Service Performance Exam", LocalDate.parse("2021-05-28"), 5);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(23, AssessmentEntity.AssessmentType.Performance, "Bread Baking Performance Exam", LocalDate.parse("2021-05-28"), 6);
                mAssessmentDAO.insertAssessment(assessment);
                assessment = new AssessmentEntity(24, AssessmentEntity.AssessmentType.Performance, "Pizza & Pasta Performance Exam", LocalDate.parse("2021-05-28"), 7);
                mAssessmentDAO.insertAssessment(assessment);

                //Populate Notes
                NoteEntity note = new NoteEntity(1, 1, "Escoffier & his apprentices", "Escoffier wrote the bible of French cooking.");
                mNoteDAO.insertNote(note);
                note = new NoteEntity(2, 1, "Alain Ducasse's Stars", "Alain Ducasse has the most 3-star restaurants in history.");
                mNoteDAO.insertNote(note);
                note = new NoteEntity(3, 2, "Cantonese-Style Dim Sum", "Most dim sum in the USA is from Canton.");
                mNoteDAO.insertNote(note);
                note = new NoteEntity(4, 3, "Respect for the Chef", "Always respond \"yes, chef!\" or \"no, chef!\" ...no talking back or arguing");
                mNoteDAO.insertNote(note);

            });
        }
    };

        @NonNull
        @Override
        protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
            return null;
        }

        @NonNull
        @Override
        protected InvalidationTracker createInvalidationTracker() {
            return null;
        }

        @Override
        public void clearAllTables() {
        }
    }


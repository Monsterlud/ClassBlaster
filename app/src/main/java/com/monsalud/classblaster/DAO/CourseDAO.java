package com.monsalud.classblaster.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.monsalud.classblaster.Entity.CourseEntity;

import java.util.List;

@Dao
public interface CourseDAO {

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID ORDER BY course_id")
    List<CourseEntity> getCourseList(int termID);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID AND course_id = :courseID")
    CourseEntity getCourse(int termID, int courseID);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID")
    List<CourseEntity> getCoursesForTerm(int termID);

    @Query("INSERT INTO course_table (term_id_fk, course_name)\n" +
            "VALUES(:termID, \"Course Name\");")
    void addCourse(int termID);

    @Query("SELECT * FROM course_table")
    List<CourseEntity> getAllCourses();

    @Insert
    void insertCourse(CourseEntity course);

    @Insert
    void insertAll(CourseEntity... course);

    @Update
    void updateCourse(CourseEntity course);

    @Query ("DELETE FROM course_table WHERE course_id = :courseID")
    void deleteCourse(int courseID);

    @Query("DELETE FROM course_table")
    public void deleteAllCourses();
}




package com.monsalud.classblaster.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.monsalud.classblaster.Entity.AssessmentEntity;
import com.monsalud.classblaster.Entity.CourseEntity;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID ORDER BY assessment_id")
    List<AssessmentEntity> getAssessmentList(int courseID);

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID AND assessment_id = :assessmentID")
    AssessmentEntity getAssessment(int courseID, int assessmentID);

    @Query("INSERT INTO assessment_table (course_id_fk, assessment_title)\n" +
            "VALUES(:courseID, \"Assessment Name\");")
    void addAssessment(int courseID);

    @Query("SELECT * FROM assessment_table")
    List<AssessmentEntity> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID")
    List<AssessmentEntity> getAssessmentsForCourse(int courseID);

    @Insert
    void insertAssessment(AssessmentEntity assessment);

    @Insert
    void insertAll(AssessmentEntity assessment);

    @Update
    void updateAssessment(AssessmentEntity assessment);

    @Query ("DELETE FROM assessment_table WHERE assessment_id = :assessmentID")
    void deleteAssessment(int assessmentID);

    @Query("DELETE FROM assessment_table")
    public void deleteAllAssessments();
}




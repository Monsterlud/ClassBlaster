package com.monsalud.classblaster.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.monsalud.classblaster.Database.Converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Entity(
        tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = CourseEntity.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = ForeignKey.CASCADE
        )
)
@TypeConverters(Converters.class)
public class AssessmentEntity {

    //Columns (Fields)
    @PrimaryKey(autoGenerate = true)
    private int assessment_id;

    @ColumnInfo(name = "assessment_type")
    @TypeConverters(Converters.class)
    public AssessmentType assessment_type;

    @ColumnInfo(name = "assessment_title")
    private String assessment_title;

    @ColumnInfo(name = "assessment_end_date")
    private LocalDate assessment_end_date;

    @ColumnInfo (name = "course_id_fk")
    private int course_id_fk;


    //Constructor
    public AssessmentEntity(int assessment_id,
                            AssessmentType assessment_type,
                            String assessment_title,
                            LocalDate assessment_end_date,
                            int course_id_fk) {
        this.assessment_id = assessment_id;
        this.assessment_type = assessment_type;
        this.assessment_title = assessment_title;
        this.assessment_end_date = assessment_end_date;
        this.course_id_fk = course_id_fk;
    }

    //Methods
    public String toStringAssessmentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd yyyy");
        return assessment_end_date.format(formatter);

    }
    public int getCourse_id_fk() {
        return course_id_fk;
    }

    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }



    public AssessmentType getAssessment_type() {
        return assessment_type;
    }

    public void setAssessment_type(AssessmentType assessment_type) {
        this.assessment_type = assessment_type;
    }

    public String getAssessment_title() {
        return assessment_title;
    }

    public void setAssessment_title(String assessment_title) {
        this.assessment_title = assessment_title;
    }

    public LocalDate getAssessment_end_date() {
        return assessment_end_date;
    }

    public void setAssessment_end_date(LocalDate assessment_end_date) {
        this.assessment_end_date = assessment_end_date;
    }

    public static enum AssessmentType {Objective, Performance}
}


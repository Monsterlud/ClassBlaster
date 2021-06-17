package com.monsalud.classblaster.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.monsalud.classblaster.Database.Converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity(
        tableName = "course_table",
        foreignKeys = @ForeignKey(
                entity = TermEntity.class,
                parentColumns = "term_id",
                childColumns = "term_id_fk",
                onDelete = ForeignKey.CASCADE
        )
)
public class CourseEntity {

    //Columns (Fields)
    @PrimaryKey(autoGenerate = true)
    private int course_id;

    @ColumnInfo(name = "term_id_fk")
    private int term_id_fk;

    @ColumnInfo(name = "course_name")
    private String course_name;

    @ColumnInfo(name = "course_start")
    private LocalDate course_start;

    @ColumnInfo(name = "course_end")
    private LocalDate course_end;

    @ColumnInfo(name = "course_status")
    @TypeConverters(Converters.class)
    private CourseStatus course_status;

    @ColumnInfo(name = "instructor_name")
    private String instructor_name;

    @ColumnInfo(name = "instructor_email")
    private String instructor_email;

    @ColumnInfo(name = "instructor_phone")
    private String instructor_phone;





    //Constructor
    public CourseEntity(int course_id,
                        int term_id_fk,
                        String course_name,
                        LocalDate course_start,
                        LocalDate course_end,
                        CourseStatus course_status,
                        String instructor_name,
                        String instructor_email,
                        String instructor_phone)
     {
        this.course_id = course_id;
        this.term_id_fk = term_id_fk;
        this.course_name = course_name;
        this.course_start = course_start;
        this.course_end = course_end;
        this.course_status = course_status;
        this.instructor_name = instructor_name;
        this.instructor_email = instructor_email;
        this.instructor_phone = instructor_phone;
    }

    //Methods
    public String toStringDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd yyyy");
        String courseStart = course_start.format(formatter);
        String courseEnd = course_end.format(formatter);
        return courseStart + " - " + courseEnd;
    }
    public String toStringStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd yyyy");
        return course_start.format(formatter);
    }
    public String toStringEndDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd yyyy");
        return course_end.format(formatter);
    }
    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getTerm_id_fk() {
        return term_id_fk;
    }

    public void setTerm_id_fk(int term_id_fk) {
        this.term_id_fk = term_id_fk;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public LocalDate getCourse_start() {
        return course_start;
    }

    public void setCourse_start(LocalDate course_start) {
        this.course_start = course_start;
    }

    public LocalDate getCourse_end() {
        return course_end;
    }

    public void setCourse_end(LocalDate course_end) {
        this.course_end = course_end;
    }

    public CourseStatus getCourse_status() {
        return course_status;
    }

    public void setCourse_status(CourseStatus course_status) {
        this.course_status = course_status;
    }

    public static enum CourseStatus {InProgress, Completed, Dropped, PlanToTake}

    public String getInstructor_name() {
        return instructor_name;
    }

    public void setInstructor_name(String instructor_name) {
        this.instructor_name = instructor_name;
    }

    public String getInstructor_email() {
        return instructor_email;
    }

    public void setInstructor_email(String instructor_email) {
        this.instructor_email = instructor_email;
    }

    public String getInstructor_phone() {
        return instructor_phone;
    }

    public void setInstructor_phone(String instructor_phone) {
        this.instructor_phone = instructor_phone;
    }
}

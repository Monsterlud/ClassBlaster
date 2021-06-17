package com.monsalud.classblaster.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "note_table",
        foreignKeys = @ForeignKey(
                entity = CourseEntity.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = ForeignKey.CASCADE
        )
)
public class NoteEntity {

    //Columns/Fields
    @PrimaryKey(autoGenerate = true)
    private int note_id;

    @ColumnInfo(name = "course_id_fk")
    private int course_id_fk;

    @ColumnInfo(name = "note_name")
    private String note_name;

    @ColumnInfo(name = "note_field")
    private String note_field;

    //Constructor
    public NoteEntity(int note_id, int course_id_fk, String note_name, String note_field) {
        this.note_id = note_id;
        this.course_id_fk = course_id_fk;
        this.note_name = note_name;
        this.note_field = note_field;
    }

    //Methods
    public String getNote_field() {
        return note_field;
    }

    public void setNote_field(String note_field) {
        this.note_field = note_field;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public int getCourse_id_fk() {
        return course_id_fk;
    }

    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }

    public String getNote_name() {
        return note_name;
    }

    public void setNote_name(String note_name) {
        this.note_name = note_name;
    }
}

package com.monsalud.classblaster.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity(tableName = "term_table")
public class TermEntity {

    //Columns (Fields)
    @PrimaryKey(autoGenerate = true)
    private int term_id;

    @ColumnInfo(name = "term_name")
    private String term_name;

    @ColumnInfo(name = "term_start")
    private LocalDate term_start;

    @ColumnInfo(name="term_end")
    private LocalDate term_end;

   @ColumnInfo(name="active")
    private boolean is_active;

    //Constructor
    public TermEntity(int term_id, String term_name, LocalDate term_start, LocalDate term_end, boolean is_active) {
        this.term_id = term_id;
        this.term_name = term_name;
        this.term_start = term_start;
        this.term_end = term_end;
        this.is_active = is_active;
    }

    //Methods
    public String toStringDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd yyyy");
        String termStart = term_start.format(formatter);
        String termEnd = term_end.format(formatter);
        return termStart + " - " + termEnd;
    }

    public String toStringStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd yyyy");
        return term_start.format(formatter);
    }
    public String toStringEndDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd yyyy");
        return term_end.format(formatter);
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    public LocalDate getTerm_start() {
        return term_start;
    }

    public void setTerm_start(LocalDate term_start) {
        this.term_start = term_start;
    }

    public LocalDate getTerm_end() {
        return term_end;
    }

    public void setTerm_end(LocalDate term_end) {
        this.term_end = term_end;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}

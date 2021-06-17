package com.monsalud.classblaster.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.monsalud.classblaster.Entity.NoteEntity;


import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    void insertNote(NoteEntity note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table")
    List<NoteEntity> getAllNotes();

    @Query("SELECT * FROM note_table WHERE course_id_fk = :courseID")
    List<NoteEntity> getAllNotesForCourse(int courseID);

    @Query("DELETE FROM note_table WHERE note_id = :noteID")
    void deleteNote(int noteID);

    @Update
    void updateNote(NoteEntity note);



}

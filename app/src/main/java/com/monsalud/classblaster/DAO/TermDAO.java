package com.monsalud.classblaster.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.monsalud.classblaster.Entity.TermEntity;

import java.util.List;

@Dao
public interface TermDAO {

    @Query("SELECT * FROM term_table ORDER BY term_id")
    List<TermEntity> getTermList();

    @Query("SELECT * FROM term_table")
    List<TermEntity> getAllTerms();

    @Query ("SELECT * FROM term_table WHERE term_id = :termID")
    TermEntity getTerm(int termID);

    @Insert
    void insertTerm(TermEntity term);

    @Insert
    void insertAll(TermEntity... term);

    @Update
    void updateTerm(TermEntity term);

    @Query ("DELETE FROM term_table WHERE term_id = :termID")
    void deleteTerm(int termID);

    @Query ("DELETE FROM term_table")
    public void deleteAllTerms();
}

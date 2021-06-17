package com.monsalud.classblaster.Database;

import androidx.room.TypeConverter;

import com.monsalud.classblaster.Entity.AssessmentEntity;
import com.monsalud.classblaster.Entity.CourseEntity;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Converters {

    @TypeConverter
    public static LocalDate fromEpochMilliToLocalDate(Long epoch) {
        return Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @TypeConverter
    public static Long localDateToEpochMilli(LocalDate date) {
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    @TypeConverter
    public int fromAssessmentType(AssessmentEntity.AssessmentType assessmentType) {
        return assessmentType.ordinal();
    }
    @TypeConverter
    public AssessmentEntity.AssessmentType toAssessmentType(int ordinal) {
        if (ordinal == 0) return AssessmentEntity.AssessmentType.Objective;
        else return AssessmentEntity.AssessmentType.Performance;
    }

    @TypeConverter
    public int fromCourseStatus(CourseEntity.CourseStatus courseStatus) {
        return courseStatus.ordinal();
    }
    @TypeConverter
    public CourseEntity.CourseStatus fromCourseStatus(int ordinal) {
        if (ordinal == 0) return CourseEntity.CourseStatus.InProgress;
        else if (ordinal == 1) return CourseEntity.CourseStatus.Completed;
        else if (ordinal == 2) return CourseEntity.CourseStatus.Dropped;
        else return CourseEntity.CourseStatus.PlanToTake;
    }
}

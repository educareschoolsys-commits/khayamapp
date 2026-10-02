package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    // Users
    @Query("SELECT * FROM users WHERE (username = :identifier OR fullName = :identifier) AND password = :password LIMIT 1")
    suspend fun authenticate(identifier: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :identifier LIMIT 1")
    suspend fun getUserByUsername(identifier: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    // Students
    @Query("SELECT * FROM students ORDER BY fullName ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Long): StudentEntity?

    @Query("SELECT * FROM students WHERE className = :className ORDER BY rollNo ASC")
    fun getStudentsByClass(className: String): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE parentPhone = :phone OR phone = :phone")
    fun getStudentsByParentPhone(phone: String): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteStudent(id: Long)

    // Teachers
    @Query("SELECT * FROM teachers ORDER BY fullName ASC")
    fun getAllTeachers(): Flow<List<TeacherEntity>>

    @Query("SELECT * FROM teachers WHERE id = :id LIMIT 1")
    suspend fun getTeacherById(id: Long): TeacherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeachers(teachers: List<TeacherEntity>)

    @Update
    suspend fun updateTeacher(teacher: TeacherEntity)

    @Query("DELETE FROM teachers WHERE id = :id")
    suspend fun deleteTeacher(id: Long)

    // Parents
    @Query("SELECT * FROM parents ORDER BY fullName ASC")
    fun getAllParents(): Flow<List<ParentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParents(parents: List<ParentEntity>)

    // Classes & Sections
    @Query("SELECT * FROM class_sections ORDER BY className ASC, section ASC")
    fun getAllClasses(): Flow<List<ClassSectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(classes: List<ClassSectionEntity>)

    // Attendance
    @Query("SELECT * FROM attendance WHERE date = :date AND className = :className ORDER BY rollNo ASC")
    fun getAttendanceByDateAndClass(date: String, className: String): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE date = :date")
    fun getAttendanceForDate(date: String): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceBatch(attendanceList: List<AttendanceEntity>)

    // Fees
    @Query("SELECT * FROM fee_records ORDER BY id DESC")
    fun getAllFeeRecords(): Flow<List<FeeRecordEntity>>

    @Query("SELECT * FROM fee_records WHERE studentId = :studentId ORDER BY id DESC")
    fun getFeesForStudent(studentId: Long): Flow<List<FeeRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeRecord(fee: FeeRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeRecords(fees: List<FeeRecordEntity>)

    @Update
    suspend fun updateFeeRecord(fee: FeeRecordEntity)

    @Query("DELETE FROM fee_records WHERE id = :id")
    suspend fun deleteFeeRecord(id: Long)

    // Exams
    @Query("SELECT * FROM exams ORDER BY id DESC")
    fun getAllExams(): Flow<List<ExamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: ExamEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExams(exams: List<ExamEntity>)

    // Exam Marks
    @Query("SELECT * FROM exam_marks WHERE examId = :examId AND className = :className ORDER BY rollNo ASC")
    fun getMarksForExamAndClass(examId: Long, className: String): Flow<List<ExamMarkEntity>>

    @Query("SELECT * FROM exam_marks WHERE studentId = :studentId ORDER BY examId DESC")
    fun getMarksForStudent(studentId: Long): Flow<List<ExamMarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamMark(mark: ExamMarkEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamMarks(marks: List<ExamMarkEntity>)

    // Timetables
    @Query("SELECT * FROM timetables WHERE className = :className ORDER BY periodNumber ASC")
    fun getTimetableForClass(className: String): Flow<List<TimetableEntity>>

    @Query("SELECT * FROM timetables WHERE teacherName = :teacherName ORDER BY periodNumber ASC")
    fun getTimetableForTeacher(teacherName: String): Flow<List<TimetableEntity>>

    @Query("SELECT * FROM timetables ORDER BY className ASC, periodNumber ASC")
    fun getAllTimetables(): Flow<List<TimetableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetable(timetable: TimetableEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetables(timetables: List<TimetableEntity>)

    // Homework
    @Query("SELECT * FROM homework WHERE className = :className ORDER BY id DESC")
    fun getHomeworkForClass(className: String): Flow<List<HomeworkEntity>>

    @Query("SELECT * FROM homework ORDER BY id DESC")
    fun getAllHomework(): Flow<List<HomeworkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(homework: HomeworkEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeworkList(homeworkList: List<HomeworkEntity>)

    @Query("DELETE FROM homework WHERE id = :id")
    suspend fun deleteHomework(id: Long)

    // Announcements
    @Query("SELECT * FROM announcements ORDER BY id DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<AnnouncementEntity>)

    @Query("DELETE FROM announcements WHERE id = :id")
    suspend fun deleteAnnouncement(id: Long)

    // Admissions
    @Query("SELECT * FROM admissions ORDER BY id DESC")
    fun getAllAdmissions(): Flow<List<AdmissionApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmission(admission: AdmissionApplicationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmissions(admissions: List<AdmissionApplicationEntity>)

    @Update
    suspend fun updateAdmission(admission: AdmissionApplicationEntity)

    // Gallery
    @Query("SELECT * FROM gallery_items ORDER BY id DESC")
    fun getAllGalleryItems(): Flow<List<GalleryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItem(item: GalleryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItems(items: List<GalleryItemEntity>)

    // Achievements
    @Query("SELECT * FROM achievements ORDER BY id DESC")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: AchievementEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    // Settings
    @Query("SELECT * FROM school_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<SchoolSettingsEntity?>

    @Query("SELECT * FROM school_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsDirect(): SchoolSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: SchoolSettingsEntity)
}

package com.example.data.repository

import com.example.data.local.SchoolDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class SchoolRepository(private val dao: SchoolDao) {

    // Auth
    suspend fun authenticate(identifier: String, password: String): UserEntity? {
        return dao.authenticate(identifier.trim(), password.trim())
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return dao.getUserByUsername(username.trim())
    }

    fun getAllUsers(): Flow<List<UserEntity>> = dao.getAllUsers()

    suspend fun addUser(user: UserEntity): Long = dao.insertUser(user)

    // Settings
    val settings: Flow<SchoolSettingsEntity?> = dao.getSettings()

    suspend fun updateSettings(settings: SchoolSettingsEntity) {
        dao.insertOrUpdateSettings(settings)
    }

    // Students
    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudents()

    fun getStudentsByClass(className: String): Flow<List<StudentEntity>> = dao.getStudentsByClass(className)

    fun getStudentsByParentPhone(phone: String): Flow<List<StudentEntity>> = dao.getStudentsByParentPhone(phone)

    suspend fun getStudentById(id: Long): StudentEntity? = dao.getStudentById(id)

    suspend fun addStudent(student: StudentEntity): Long = dao.insertStudent(student)

    suspend fun updateStudent(student: StudentEntity) = dao.updateStudent(student)

    suspend fun deleteStudent(id: Long) = dao.deleteStudent(id)

    // Teachers
    val allTeachers: Flow<List<TeacherEntity>> = dao.getAllTeachers()

    suspend fun getTeacherById(id: Long): TeacherEntity? = dao.getTeacherById(id)

    suspend fun addTeacher(teacher: TeacherEntity): Long = dao.insertTeacher(teacher)

    suspend fun updateTeacher(teacher: TeacherEntity) = dao.updateTeacher(teacher)

    suspend fun deleteTeacher(id: Long) = dao.deleteTeacher(id)

    // Parents
    val allParents: Flow<List<ParentEntity>> = dao.getAllParents()

    // Classes
    val allClasses: Flow<List<ClassSectionEntity>> = dao.getAllClasses()

    // Attendance
    fun getAttendanceByDateAndClass(date: String, className: String): Flow<List<AttendanceEntity>> =
        dao.getAttendanceByDateAndClass(date, className)

    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceEntity>> =
        dao.getAttendanceForStudent(studentId)

    fun getAttendanceForDate(date: String): Flow<List<AttendanceEntity>> =
        dao.getAttendanceForDate(date)

    suspend fun saveAttendanceBatch(records: List<AttendanceEntity>) =
        dao.insertAttendanceBatch(records)

    suspend fun markSingleAttendance(record: AttendanceEntity) =
        dao.insertAttendance(record)

    // Fees
    val allFeeRecords: Flow<List<FeeRecordEntity>> = dao.getAllFeeRecords()

    fun getFeesForStudent(studentId: Long): Flow<List<FeeRecordEntity>> =
        dao.getFeesForStudent(studentId)

    suspend fun addFeeRecord(fee: FeeRecordEntity): Long = dao.insertFeeRecord(fee)

    suspend fun updateFeeRecord(fee: FeeRecordEntity) = dao.updateFeeRecord(fee)

    suspend fun deleteFeeRecord(id: Long) = dao.deleteFeeRecord(id)

    // Exams
    val allExams: Flow<List<ExamEntity>> = dao.getAllExams()

    suspend fun addExam(exam: ExamEntity): Long = dao.insertExam(exam)

    fun getMarksForExamAndClass(examId: Long, className: String): Flow<List<ExamMarkEntity>> =
        dao.getMarksForExamAndClass(examId, className)

    fun getMarksForStudent(studentId: Long): Flow<List<ExamMarkEntity>> =
        dao.getMarksForStudent(studentId)

    suspend fun saveExamMarks(marks: List<ExamMarkEntity>) = dao.insertExamMarks(marks)

    suspend fun addExamMark(mark: ExamMarkEntity): Long = dao.insertExamMark(mark)

    // Timetable
    val allTimetables: Flow<List<TimetableEntity>> = dao.getAllTimetables()

    fun getTimetableForClass(className: String): Flow<List<TimetableEntity>> =
        dao.getTimetableForClass(className)

    fun getTimetableForTeacher(teacherName: String): Flow<List<TimetableEntity>> =
        dao.getTimetableForTeacher(teacherName)

    suspend fun addTimetable(timetable: TimetableEntity): Long = dao.insertTimetable(timetable)

    // Homework
    val allHomework: Flow<List<HomeworkEntity>> = dao.getAllHomework()

    fun getHomeworkForClass(className: String): Flow<List<HomeworkEntity>> =
        dao.getHomeworkForClass(className)

    suspend fun addHomework(homework: HomeworkEntity): Long = dao.insertHomework(homework)

    suspend fun deleteHomework(id: Long) = dao.deleteHomework(id)

    // Announcements
    val allAnnouncements: Flow<List<AnnouncementEntity>> = dao.getAllAnnouncements()

    suspend fun addAnnouncement(announcement: AnnouncementEntity): Long =
        dao.insertAnnouncement(announcement)

    suspend fun deleteAnnouncement(id: Long) = dao.deleteAnnouncement(id)

    // Admissions
    val allAdmissions: Flow<List<AdmissionApplicationEntity>> = dao.getAllAdmissions()

    suspend fun submitAdmission(admission: AdmissionApplicationEntity): Long =
        dao.insertAdmission(admission)

    suspend fun updateAdmissionStatus(admission: AdmissionApplicationEntity) =
        dao.updateAdmission(admission)

    suspend fun convertAdmissionToStudent(admission: AdmissionApplicationEntity): Long {
        val student = StudentEntity(
            admissionNo = admission.applicationNumber,
            rollNo = (100 + (1..899).random()).toString(),
            fullName = admission.studentName,
            fatherName = admission.fatherName,
            motherName = admission.motherName,
            dob = admission.dob,
            gender = admission.gender,
            className = admission.applyingClass,
            section = "A",
            phone = admission.phone,
            whatsapp = admission.whatsapp,
            address = admission.address,
            admissionDate = admission.submissionDate,
            previousSchool = admission.previousSchool,
            status = "Active",
            parentPhone = admission.phone
        )
        val studentId = dao.insertStudent(student)
        dao.updateAdmission(admission.copy(status = "APPROVED"))
        return studentId
    }

    // Gallery
    val allGalleryItems: Flow<List<GalleryItemEntity>> = dao.getAllGalleryItems()

    suspend fun addGalleryItem(item: GalleryItemEntity): Long = dao.insertGalleryItem(item)

    // Achievements
    val allAchievements: Flow<List<AchievementEntity>> = dao.getAllAchievements()

    suspend fun addAchievement(achievement: AchievementEntity): Long = dao.insertAchievement(achievement)
}

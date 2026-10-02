package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    PRINCIPAL,
    ADMIN_STAFF,
    TEACHER,
    PARENT,
    STUDENT
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String, // phone or email
    val password: String,
    val fullName: String,
    val role: UserRole,
    val linkedStudentId: Long? = null,
    val linkedTeacherId: Long? = null,
    val linkedParentPhone: String? = null
)

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val admissionNo: String,
    val rollNo: String,
    val fullName: String,
    val fatherName: String,
    val motherName: String = "",
    val dob: String,
    val gender: String, // Male, Female
    val className: String, // e.g. "Class 8", "Class 9", "Class 10"
    val section: String = "A",
    val phone: String,
    val whatsapp: String = "",
    val address: String,
    val admissionDate: String,
    val previousSchool: String = "",
    val status: String = "Active", // Active, Inactive, Alumni
    val parentPhone: String = ""
)

@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val qualification: String,
    val designation: String, // e.g. "Senior Science Teacher", "Head of Mathematics"
    val assignedClasses: String, // comma separated e.g. "Class 8, Class 9, Class 10"
    val assignedSubjects: String, // comma separated e.g. "Mathematics, Physics"
    val joiningDate: String,
    val status: String = "Active"
)

@Entity(tableName = "parents")
data class ParentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val phone: String,
    val email: String = "",
    val whatsapp: String = "",
    val occupation: String = "",
    val address: String = ""
)

@Entity(tableName = "class_sections")
data class ClassSectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val className: String,
    val section: String,
    val roomNumber: String,
    val classTeacherName: String,
    val totalCapacity: Int = 40
)

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val rollNo: String,
    val className: String,
    val section: String,
    val date: String, // Format: YYYY-MM-DD
    val status: String, // "PRESENT", "ABSENT", "LEAVE"
    val remarks: String = ""
)

@Entity(tableName = "fee_records")
data class FeeRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val rollNo: String,
    val className: String,
    val section: String,
    val monthYear: String, // e.g. "October 2026"
    val tuitionFee: Double,
    val examFee: Double = 0.0,
    val admissionFee: Double = 0.0,
    val transportFee: Double = 0.0,
    val otherCharges: Double = 0.0,
    val discount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val balance: Double = 0.0,
    val status: String = "UNPAID", // "PAID", "PARTIAL", "UNPAID"
    val paymentDate: String = "",
    val receiptNumber: String = ""
)

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val examName: String, // "First Term Examination 2026", "Final Term Examination"
    val session: String = "2026-2027",
    val startDate: String,
    val endDate: String,
    val classesCovered: String // "All Classes" or specific
)

@Entity(tableName = "exam_marks")
data class ExamMarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val examId: Long,
    val examName: String,
    val studentId: Long,
    val studentName: String,
    val rollNo: String,
    val className: String,
    val section: String,
    val subjectName: String,
    val totalMarks: Double = 100.0,
    val obtainedMarks: Double,
    val grade: String = "A",
    val remarks: String = "Good performance"
)

@Entity(tableName = "timetables")
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val className: String,
    val section: String,
    val dayOfWeek: String, // "Monday", "Tuesday", etc.
    val periodNumber: Int,
    val startTime: String,
    val endTime: String,
    val subjectName: String,
    val teacherName: String,
    val roomNumber: String = "Room 101"
)

@Entity(tableName = "homework")
data class HomeworkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val className: String,
    val section: String,
    val subjectName: String,
    val teacherName: String,
    val title: String,
    val description: String,
    val assignedDate: String,
    val dueDate: String,
    val status: String = "Active"
)

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val targetRole: String = "ALL", // "ALL", "STUDENT", "PARENT", "TEACHER", "CLASS"
    val targetClass: String = "",
    val date: String,
    val type: String = "GENERAL", // "GENERAL", "EXAM", "FEE", "HOLIDAY", "EMERGENCY"
    val isImportant: Boolean = false
)

@Entity(tableName = "admissions")
data class AdmissionApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val applicationNumber: String,
    val studentName: String,
    val fatherName: String,
    val motherName: String = "",
    val dob: String,
    val gender: String,
    val applyingClass: String,
    val phone: String,
    val whatsapp: String = "",
    val address: String,
    val previousSchool: String = "",
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val submissionDate: String
)

@Entity(tableName = "gallery_items")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "SPORTS", "ASSEMBLY", "ANNUAL_FUNCTION", "ACADEMIC", "COMPETITION"
    val date: String,
    val description: String
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentName: String,
    val className: String,
    val achievementTitle: String,
    val competitionName: String,
    val position: String, // "1st Position", "Gold Medal", etc.
    val date: String
)

@Entity(tableName = "school_settings")
data class SchoolSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val schoolName: String = "Educare School System Chagmalai",
    val urduSlogan: String = "ہم تراشتے ہیں آپ کے بچوں کا مستقبل",
    val englishSlogan: String = "We Shape Your Children's Future",
    val address: String = "Main Campus, Chagmalai, Khyber Pakhtunkhwa",
    val phone: String = "+92 300 1234567",
    val whatsapp: String = "+92 300 1234567",
    val email: String = "educareschoolsys@gmail.com",
    val currentSession: String = "2026-2027",
    val principalName: String = "Principal Educare System"
)

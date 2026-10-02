package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppDestination {
    LOGIN,
    ADMIN_DASHBOARD,
    TEACHER_PORTAL,
    PARENT_PORTAL,
    STUDENT_PORTAL,
    PUBLIC_WEBSITE
}

enum class AdminTab {
    OVERVIEW,
    STUDENTS,
    TEACHERS,
    ATTENDANCE,
    FEES,
    EXAMS,
    TIMETABLE,
    HOMEWORK,
    ANNOUNCEMENTS,
    ADMISSIONS,
    REPORTS,
    SETTINGS
}

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SchoolRepository
    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = SchoolRepository(database.schoolDao())
    }

    // Navigation and Auth state
    private val _currentDestination = MutableStateFlow(AppDestination.LOGIN)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _activeAdminTab = MutableStateFlow(AdminTab.OVERVIEW)
    val activeAdminTab: StateFlow<AdminTab> = _activeAdminTab.asStateFlow()

    // Notification message (SnackBar / Toast)
    private val _userFeedbackMessage = MutableSharedFlow<String>()
    val userFeedbackMessage: SharedFlow<String> = _userFeedbackMessage.asSharedFlow()

    // Data streams
    val settings: StateFlow<SchoolSettingsEntity> = repository.settings
        .map { it ?: SchoolSettingsEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SchoolSettingsEntity())

    val students: StateFlow<List<StudentEntity>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val teachers: StateFlow<List<TeacherEntity>> = repository.allTeachers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val classes: StateFlow<List<ClassSectionEntity>> = repository.allClasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val feeRecords: StateFlow<List<FeeRecordEntity>> = repository.allFeeRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val exams: StateFlow<List<ExamEntity>> = repository.allExams
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val timetables: StateFlow<List<TimetableEntity>> = repository.allTimetables
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val homeworkList: StateFlow<List<HomeworkEntity>> = repository.allHomework
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val announcements: StateFlow<List<AnnouncementEntity>> = repository.allAnnouncements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val admissions: StateFlow<List<AdmissionApplicationEntity>> = repository.allAdmissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val galleryItems: StateFlow<List<GalleryItemEntity>> = repository.allGalleryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievements: StateFlow<List<AchievementEntity>> = repository.allAchievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filter states
    val studentSearchQuery = MutableStateFlow("")
    val selectedClassFilter = MutableStateFlow("All Classes")

    // Attendance selection states
    val attendanceDate = MutableStateFlow("2026-10-02")
    val attendanceClass = MutableStateFlow("Class 10")
    val attendanceSection = MutableStateFlow("A")

    // Active dialogs
    val activeFeeReceipt = MutableStateFlow<FeeRecordEntity?>(null)
    val activeResultCardStudentId = MutableStateFlow<Long?>(null)

    // Navigation triggers
    fun navigateTo(dest: AppDestination) {
        _currentDestination.value = dest
    }

    fun setAdminTab(tab: AdminTab) {
        _activeAdminTab.value = tab
    }

    fun showFeedback(msg: String) {
        viewModelScope.launch {
            _userFeedbackMessage.emit(msg)
        }
    }

    // Auth actions - strictly restricted to authorized admin khayamilham@gmail.com
    fun login(identifier: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val cleanId = identifier.trim()
            val cleanPass = pass.trim()
            if (cleanId.equals("khayamilham@gmail.com", ignoreCase = true) && cleanPass == "Educareschool") {
                var user = repository.getUserByUsername("khayamilham@gmail.com")
                if (user == null) {
                    val adminUser = UserEntity(
                        username = "khayamilham@gmail.com",
                        password = "Educareschool",
                        fullName = "Khayam Ilham (Super Admin / Principal)",
                        role = UserRole.PRINCIPAL
                    )
                    repository.addUser(adminUser)
                    user = adminUser
                }
                _currentUser.value = user
                launch(Dispatchers.Main) {
                    _currentDestination.value = AppDestination.ADMIN_DASHBOARD
                    onResult(true, "Welcome Khayam Ilham! Access Granted to Educare School System.")
                }
            } else {
                launch(Dispatchers.Main) {
                    onResult(false, "Access Denied: Only authorized login (khayamilham@gmail.com with password 'Educareschool') can open this system.")
                }
            }
        }
    }

    fun quickRoleLogin(role: UserRole) {
        showFeedback("Direct bypass disabled. Please sign in with khayamilham@gmail.com and password Educareschool.")
    }

    fun logout() {
        _currentUser.value = null
        _currentDestination.value = AppDestination.LOGIN
        showFeedback("Logged out successfully")
    }

    // Student CRUD
    fun saveStudent(student: StudentEntity, isNew: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNew) {
                val newId = repository.addStudent(student)
                // Also create a student user account if needed
                repository.addUser(
                    UserEntity(
                        username = student.rollNo,
                        password = "123",
                        fullName = student.fullName,
                        role = UserRole.STUDENT,
                        linkedStudentId = newId
                    )
                )
                showFeedback("Student ${student.fullName} registered successfully!")
            } else {
                repository.updateStudent(student)
                showFeedback("Student ${student.fullName} updated!")
            }
        }
    }

    fun deleteStudent(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStudent(id)
            showFeedback("Student record removed")
        }
    }

    // Teacher CRUD
    fun saveTeacher(teacher: TeacherEntity, isNew: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNew) {
                val newId = repository.addTeacher(teacher)
                repository.addUser(
                    UserEntity(
                        username = teacher.email.ifBlank { teacher.phone },
                        password = "123",
                        fullName = teacher.fullName,
                        role = UserRole.TEACHER,
                        linkedTeacherId = newId
                    )
                )
                showFeedback("Teacher ${teacher.fullName} registered!")
            } else {
                repository.updateTeacher(teacher)
                showFeedback("Teacher ${teacher.fullName} updated!")
            }
        }
    }

    fun deleteTeacher(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTeacher(id)
            showFeedback("Teacher record removed")
        }
    }

    // Attendance
    fun getAttendanceForDateAndClass(date: String, className: String): Flow<List<AttendanceEntity>> {
        return repository.getAttendanceByDateAndClass(date, className)
    }

    fun saveAttendanceRecords(records: List<AttendanceEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAttendanceBatch(records)
            showFeedback("Attendance recorded successfully for ${records.size} students")
        }
    }

    fun getStudentAttendance(studentId: Long): Flow<List<AttendanceEntity>> {
        return repository.getAttendanceForStudent(studentId)
    }

    // Fees
    fun saveFeeRecord(fee: FeeRecordEntity, isNew: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNew) {
                repository.addFeeRecord(fee)
                showFeedback("Fee voucher generated: ${fee.receiptNumber}")
            } else {
                repository.updateFeeRecord(fee)
                showFeedback("Fee record updated successfully")
            }
        }
    }

    fun recordFeePayment(feeId: Long, paymentAmount: Double, paymentDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val record = feeRecords.value.find { it.id == feeId } ?: return@launch
            val newPaid = record.paidAmount + paymentAmount
            val total = record.tuitionFee + record.examFee + record.admissionFee + record.transportFee + record.otherCharges - record.discount
            val newBalance = (total - newPaid).coerceAtLeast(0.0)
            val newStatus = when {
                newBalance <= 0.0 -> "PAID"
                newPaid > 0.0 -> "PARTIAL"
                else -> "UNPAID"
            }
            val receiptNo = if (record.receiptNumber.isBlank()) "ESS-RCP-${System.currentTimeMillis() % 100000}" else record.receiptNumber
            val updated = record.copy(
                paidAmount = newPaid,
                balance = newBalance,
                status = newStatus,
                paymentDate = paymentDate,
                receiptNumber = receiptNo
            )
            repository.updateFeeRecord(updated)
            activeFeeReceipt.value = updated
            showFeedback("Payment of Rs. $paymentAmount recorded. Receipt: $receiptNo")
        }
    }

    fun getFeesForStudent(studentId: Long): Flow<List<FeeRecordEntity>> {
        return repository.getFeesForStudent(studentId)
    }

    // Exams & Marks
    fun createExam(exam: ExamEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addExam(exam)
            showFeedback("Examination '${exam.examName}' created")
        }
    }

    fun getMarksForExamAndClass(examId: Long, className: String): Flow<List<ExamMarkEntity>> {
        return repository.getMarksForExamAndClass(examId, className)
    }

    fun getMarksForStudent(studentId: Long): Flow<List<ExamMarkEntity>> {
        return repository.getMarksForStudent(studentId)
    }

    fun saveExamMarks(marks: List<ExamMarkEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveExamMarks(marks)
            showFeedback("Marks saved successfully")
        }
    }

    // Homework
    fun createHomework(homework: HomeworkEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHomework(homework)
            showFeedback("Homework assigned for ${homework.className}")
        }
    }

    fun deleteHomework(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHomework(id)
            showFeedback("Homework assignment removed")
        }
    }

    fun getHomeworkForClass(className: String): Flow<List<HomeworkEntity>> {
        return repository.getHomeworkForClass(className)
    }

    // Timetable
    fun addTimetable(timetable: TimetableEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTimetable(timetable)
            showFeedback("Timetable period added")
        }
    }

    fun getTimetableForClass(className: String): Flow<List<TimetableEntity>> {
        return repository.getTimetableForClass(className)
    }

    fun getTimetableForTeacher(teacherName: String): Flow<List<TimetableEntity>> {
        return repository.getTimetableForTeacher(teacherName)
    }

    // Announcements
    fun sendAnnouncement(announcement: AnnouncementEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAnnouncement(announcement)
            showFeedback("Announcement published!")
        }
    }

    fun deleteAnnouncement(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAnnouncement(id)
            showFeedback("Announcement removed")
        }
    }

    // Admissions
    fun submitOnlineAdmission(app: AdmissionApplicationEntity, onSuccess: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.submitAdmission(app)
            showFeedback("Application ${app.applicationNumber} submitted successfully!")
            launch(Dispatchers.Main) {
                onSuccess(app.applicationNumber)
            }
        }
    }

    fun approveAdmission(app: AdmissionApplicationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.convertAdmissionToStudent(app)
            showFeedback("Application ${app.applicationNumber} approved! Student enrolled.")
        }
    }

    fun rejectAdmission(app: AdmissionApplicationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAdmissionStatus(app.copy(status = "REJECTED"))
            showFeedback("Application marked as rejected")
        }
    }

    // Settings
    fun updateSchoolSettings(newSettings: SchoolSettingsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSettings(newSettings)
            showFeedback("School settings updated successfully!")
        }
    }

    // Gallery & Achievements
    fun addGalleryItem(item: GalleryItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGalleryItem(item)
            showFeedback("Photo added to gallery")
        }
    }

    fun addAchievement(achievement: AchievementEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAchievement(achievement)
            showFeedback("Achievement added")
        }
    }
}

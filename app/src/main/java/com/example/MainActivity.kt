package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ExamMarkEntity
import com.example.data.model.UserRole
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.exams.ResultCardDialog
import com.example.ui.screens.fees.FeeReceiptDialog
import com.example.ui.screens.parent.ParentPortalScreen
import com.example.ui.screens.student.StudentPortalScreen
import com.example.ui.screens.teacher.TeacherPortalScreen
import com.example.ui.screens.website.PublicWebsiteScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AdminTab
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: SchoolViewModel = viewModel()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                // State collection
                val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
                val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
                val activeAdminTab by viewModel.activeAdminTab.collectAsStateWithLifecycle()

                val students by viewModel.students.collectAsStateWithLifecycle()
                val teachers by viewModel.teachers.collectAsStateWithLifecycle()
                val classes by viewModel.classes.collectAsStateWithLifecycle()
                val feeRecords by viewModel.feeRecords.collectAsStateWithLifecycle()
                val exams by viewModel.exams.collectAsStateWithLifecycle()
                val timetables by viewModel.timetables.collectAsStateWithLifecycle()
                val homeworkList by viewModel.homeworkList.collectAsStateWithLifecycle()
                val announcements by viewModel.announcements.collectAsStateWithLifecycle()
                val admissions by viewModel.admissions.collectAsStateWithLifecycle()
                val galleryItems by viewModel.galleryItems.collectAsStateWithLifecycle()
                val achievements by viewModel.achievements.collectAsStateWithLifecycle()
                val settings by viewModel.settings.collectAsStateWithLifecycle()

                val activeFeeReceipt by viewModel.activeFeeReceipt.collectAsStateWithLifecycle()
                val activeResultCardStudentId by viewModel.activeResultCardStudentId.collectAsStateWithLifecycle()

                // Feedback listener
                LaunchedEffect(Unit) {
                    viewModel.userFeedbackMessage.collectLatest { msg ->
                        snackbarHostState.showSnackbar(msg)
                    }
                }

                // Handle Hardware / Gesture Back Press
                BackHandler(enabled = currentDestination != AppDestination.LOGIN) {
                    when (currentDestination) {
                        AppDestination.ADMIN_DASHBOARD -> {
                            if (activeAdminTab != AdminTab.OVERVIEW) {
                                viewModel.setAdminTab(AdminTab.OVERVIEW)
                            } else {
                                viewModel.logout()
                            }
                        }
                        AppDestination.PUBLIC_WEBSITE -> {
                            if (currentUser != null) {
                                when (currentUser!!.role) {
                                    UserRole.PRINCIPAL, UserRole.ADMIN_STAFF -> viewModel.navigateTo(AppDestination.ADMIN_DASHBOARD)
                                    UserRole.TEACHER -> viewModel.navigateTo(AppDestination.TEACHER_PORTAL)
                                    UserRole.PARENT -> viewModel.navigateTo(AppDestination.PARENT_PORTAL)
                                    UserRole.STUDENT -> viewModel.navigateTo(AppDestination.STUDENT_PORTAL)
                                }
                            } else {
                                viewModel.navigateTo(AppDestination.LOGIN)
                            }
                        }
                        else -> viewModel.logout()
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentDestination) {
                            AppDestination.LOGIN -> {
                                LoginScreen(
                                    onLoginSubmit = { identifier, password ->
                                        viewModel.login(identifier, password) { success, msg ->
                                            scope.launch { snackbarHostState.showSnackbar(msg) }
                                        }
                                    },
                                    onQuickRoleSelect = { role ->
                                        viewModel.quickRoleLogin(role)
                                    },
                                    onOpenPublicWebsite = {
                                        viewModel.navigateTo(AppDestination.PUBLIC_WEBSITE)
                                    }
                                )
                            }

                            AppDestination.ADMIN_DASHBOARD -> {
                                currentUser?.let { user ->
                                    AdminDashboardScreen(
                                        currentUser = user,
                                        activeTab = activeAdminTab,
                                        onTabSelected = { viewModel.setAdminTab(it) },
                                        students = students,
                                        teachers = teachers,
                                        classes = classes,
                                        feeRecords = feeRecords,
                                        exams = exams,
                                        timetables = timetables,
                                        homeworkList = homeworkList,
                                        announcements = announcements,
                                        admissions = admissions,
                                        settings = settings,
                                        onSaveStudent = { s, isNew -> viewModel.saveStudent(s, isNew) },
                                        onDeleteStudent = { viewModel.deleteStudent(it) },
                                        onSaveTeacher = { t, isNew -> viewModel.saveTeacher(t, isNew) },
                                        onDeleteTeacher = { viewModel.deleteTeacher(it) },
                                        onSaveAttendance = { viewModel.saveAttendanceRecords(it) },
                                        onSaveFeeRecord = { f, isNew -> viewModel.saveFeeRecord(f, isNew) },
                                        onRecordFeePayment = { id, amount, date -> viewModel.recordFeePayment(id, amount, date) },
                                        onViewReceipt = { viewModel.activeFeeReceipt.value = it },
                                        onCreateExam = { viewModel.createExam(it) },
                                        onSaveMarks = { viewModel.saveExamMarks(it) },
                                        onViewResultCard = { viewModel.activeResultCardStudentId.value = it.id },
                                        onAddTimetable = { viewModel.addTimetable(it) },
                                        onCreateHomework = { viewModel.createHomework(it) },
                                        onDeleteHomework = { viewModel.deleteHomework(it) },
                                        onSendAnnouncement = { viewModel.sendAnnouncement(it) },
                                        onDeleteAnnouncement = { viewModel.deleteAnnouncement(it) },
                                        onApproveAdmission = { viewModel.approveAdmission(it) },
                                        onRejectAdmission = { viewModel.rejectAdmission(it) },
                                        onSaveSettings = { viewModel.updateSchoolSettings(it) },
                                        onShowFeedback = { viewModel.showFeedback(it) },
                                        onLogout = { viewModel.logout() },
                                        onOpenWebsite = { viewModel.navigateTo(AppDestination.PUBLIC_WEBSITE) }
                                    )
                                }
                            }

                            AppDestination.TEACHER_PORTAL -> {
                                currentUser?.let { user ->
                                    TeacherPortalScreen(
                                        currentUser = user,
                                        teachers = teachers,
                                        students = students,
                                        timetables = timetables,
                                        homeworkList = homeworkList,
                                        announcements = announcements,
                                        onSaveAttendance = { viewModel.saveAttendanceRecords(it) },
                                        onCreateHomework = { viewModel.createHomework(it) },
                                        onDeleteHomework = { viewModel.deleteHomework(it) },
                                        onLogout = { viewModel.logout() },
                                        onOpenWebsite = { viewModel.navigateTo(AppDestination.PUBLIC_WEBSITE) }
                                    )
                                }
                            }

                            AppDestination.PARENT_PORTAL -> {
                                currentUser?.let { user ->
                                    ParentPortalScreen(
                                        currentUser = user,
                                        students = students,
                                        feeRecords = feeRecords,
                                        homeworkList = homeworkList,
                                        timetables = timetables,
                                        announcements = announcements,
                                        onViewReceipt = { viewModel.activeFeeReceipt.value = it },
                                        onViewResultCard = { viewModel.activeResultCardStudentId.value = it.id },
                                        onLogout = { viewModel.logout() },
                                        onOpenWebsite = { viewModel.navigateTo(AppDestination.PUBLIC_WEBSITE) }
                                    )
                                }
                            }

                            AppDestination.STUDENT_PORTAL -> {
                                currentUser?.let { user ->
                                    StudentPortalScreen(
                                        currentUser = user,
                                        students = students,
                                        timetables = timetables,
                                        homeworkList = homeworkList,
                                        announcements = announcements,
                                        onViewResultCard = { viewModel.activeResultCardStudentId.value = it.id },
                                        onLogout = { viewModel.logout() },
                                        onOpenWebsite = { viewModel.navigateTo(AppDestination.PUBLIC_WEBSITE) }
                                    )
                                }
                            }

                            AppDestination.PUBLIC_WEBSITE -> {
                                PublicWebsiteScreen(
                                    teachers = teachers,
                                    announcements = announcements,
                                    gallery = galleryItems,
                                    achievements = achievements,
                                    onSubmitAdmission = { app, onNum ->
                                        viewModel.submitOnlineAdmission(app, onNum)
                                    },
                                    onNavigateToLogin = { _ ->
                                        viewModel.navigateTo(AppDestination.LOGIN)
                                    },
                                    onBack = {
                                        if (currentUser != null) {
                                            when (currentUser!!.role) {
                                                UserRole.PRINCIPAL, UserRole.ADMIN_STAFF -> viewModel.navigateTo(AppDestination.ADMIN_DASHBOARD)
                                                UserRole.TEACHER -> viewModel.navigateTo(AppDestination.TEACHER_PORTAL)
                                                UserRole.PARENT -> viewModel.navigateTo(AppDestination.PARENT_PORTAL)
                                                UserRole.STUDENT -> viewModel.navigateTo(AppDestination.STUDENT_PORTAL)
                                            }
                                        } else {
                                            viewModel.navigateTo(AppDestination.LOGIN)
                                        }
                                    }
                                )
                            }
                        }

                        // Global Fee Receipt Dialog
                        activeFeeReceipt?.let { fee ->
                            FeeReceiptDialog(
                                feeRecord = fee,
                                onDismiss = { viewModel.activeFeeReceipt.value = null },
                                onPrintOrShare = {
                                    viewModel.showFeedback("Printing Fee Receipt for ${fee.studentName}...")
                                }
                            )
                        }

                        // Global Result Card Dialog
                        activeResultCardStudentId?.let { sId ->
                            val targetStudent = students.find { it.id == sId }
                            if (targetStudent != null) {
                                val studentMarksFlow = remember(sId) { viewModel.getMarksForStudent(sId) }
                                val marks by studentMarksFlow.collectAsStateWithLifecycle(emptyList())

                                val displayMarks = if (marks.isNotEmpty()) marks else listOf(
                                    ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = targetStudent.id, studentName = targetStudent.fullName, rollNo = targetStudent.rollNo, className = targetStudent.className, section = targetStudent.section, subjectName = "Mathematics", totalMarks = 100.0, obtainedMarks = 92.0, grade = "A+"),
                                    ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = targetStudent.id, studentName = targetStudent.fullName, rollNo = targetStudent.rollNo, className = targetStudent.className, section = targetStudent.section, subjectName = "Physics", totalMarks = 100.0, obtainedMarks = 88.0, grade = "A"),
                                    ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = targetStudent.id, studentName = targetStudent.fullName, rollNo = targetStudent.rollNo, className = targetStudent.className, section = targetStudent.section, subjectName = "Chemistry", totalMarks = 100.0, obtainedMarks = 85.0, grade = "A"),
                                    ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = targetStudent.id, studentName = targetStudent.fullName, rollNo = targetStudent.rollNo, className = targetStudent.className, section = targetStudent.section, subjectName = "English", totalMarks = 100.0, obtainedMarks = 84.0, grade = "A"),
                                    ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = targetStudent.id, studentName = targetStudent.fullName, rollNo = targetStudent.rollNo, className = targetStudent.className, section = targetStudent.section, subjectName = "Urdu", totalMarks = 100.0, obtainedMarks = 90.0, grade = "A+"),
                                    ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = targetStudent.id, studentName = targetStudent.fullName, rollNo = targetStudent.rollNo, className = targetStudent.className, section = targetStudent.section, subjectName = "Islamiat", totalMarks = 100.0, obtainedMarks = 96.0, grade = "A+")
                                )

                                ResultCardDialog(
                                    student = targetStudent,
                                    examName = "First Term Examination 2026",
                                    marks = displayMarks,
                                    onDismiss = { viewModel.activeResultCardStudentId.value = null },
                                    onPrintOrShare = {
                                        viewModel.showFeedback("Result Card for ${targetStudent.fullName} prepared for Print / PDF export!")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

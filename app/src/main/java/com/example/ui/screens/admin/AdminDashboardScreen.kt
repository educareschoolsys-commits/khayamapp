package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.*
import com.example.ui.components.EducareBadge
import com.example.ui.components.EducareHeader
import com.example.ui.components.StatCard
import com.example.ui.screens.admissions.AdmissionManagementScreen
import com.example.ui.screens.announcements.AnnouncementsScreen
import com.example.ui.screens.attendance.AttendanceScreen
import com.example.ui.screens.exams.ExamManagementScreen
import com.example.ui.screens.fees.FeeManagementScreen
import com.example.ui.screens.homework.HomeworkScreen
import com.example.ui.screens.reports.ReportsScreen
import com.example.ui.screens.settings.AdminSettingsScreen
import com.example.ui.screens.students.StudentManagementScreen
import com.example.ui.screens.teachers.TeacherManagementScreen
import com.example.ui.screens.timetable.TimetableScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminTab

@Composable
fun AdminDashboardScreen(
    currentUser: UserEntity,
    activeTab: AdminTab,
    onTabSelected: (AdminTab) -> Unit,
    students: List<StudentEntity>,
    teachers: List<TeacherEntity>,
    classes: List<ClassSectionEntity>,
    feeRecords: List<FeeRecordEntity>,
    exams: List<ExamEntity>,
    timetables: List<TimetableEntity>,
    homeworkList: List<HomeworkEntity>,
    announcements: List<AnnouncementEntity>,
    admissions: List<AdmissionApplicationEntity>,
    settings: SchoolSettingsEntity,
    onSaveStudent: (StudentEntity, Boolean) -> Unit,
    onDeleteStudent: (Long) -> Unit,
    onSaveTeacher: (TeacherEntity, Boolean) -> Unit,
    onDeleteTeacher: (Long) -> Unit,
    onSaveAttendance: (List<AttendanceEntity>) -> Unit,
    onSaveFeeRecord: (FeeRecordEntity, Boolean) -> Unit,
    onRecordFeePayment: (Long, Double, String) -> Unit,
    onViewReceipt: (FeeRecordEntity) -> Unit,
    onCreateExam: (ExamEntity) -> Unit,
    onSaveMarks: (List<ExamMarkEntity>) -> Unit,
    onViewResultCard: (StudentEntity) -> Unit,
    onAddTimetable: (TimetableEntity) -> Unit,
    onCreateHomework: (HomeworkEntity) -> Unit,
    onDeleteHomework: (Long) -> Unit,
    onSendAnnouncement: (AnnouncementEntity) -> Unit,
    onDeleteAnnouncement: (Long) -> Unit,
    onApproveAdmission: (AdmissionApplicationEntity) -> Unit,
    onRejectAdmission: (AdmissionApplicationEntity) -> Unit,
    onSaveSettings: (SchoolSettingsEntity) -> Unit,
    onShowFeedback: (String) -> Unit,
    onLogout: () -> Unit,
    onOpenWebsite: () -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                EducareHeader(
                    currentUser = currentUser,
                    onLogout = onLogout,
                    onOpenWebsite = onOpenWebsite
                )

                // Scrollable Admin Tabs
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(AdminTab.values()) { tab ->
                            FilterChip(
                                selected = activeTab == tab,
                                onClick = { onTabSelected(tab) },
                                label = {
                                    Text(
                                        text = tab.name.replace("_", " "),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EducareNavyPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("admin_tab_${tab.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (activeTab) {
                AdminTab.OVERVIEW -> AdminOverviewView(
                    students = students,
                    teachers = teachers,
                    classes = classes,
                    feeRecords = feeRecords,
                    admissions = admissions,
                    exams = exams,
                    announcements = announcements,
                    onNavigateToTab = onTabSelected
                )
                AdminTab.STUDENTS -> StudentManagementScreen(
                    students = students,
                    onSaveStudent = onSaveStudent,
                    onDeleteStudent = onDeleteStudent,
                    onViewResultCard = onViewResultCard
                )
                AdminTab.TEACHERS -> TeacherManagementScreen(
                    teachers = teachers,
                    onSaveTeacher = onSaveTeacher,
                    onDeleteTeacher = onDeleteTeacher
                )
                AdminTab.ATTENDANCE -> AttendanceScreen(
                    students = students,
                    onSaveAttendance = onSaveAttendance
                )
                AdminTab.FEES -> FeeManagementScreen(
                    feeRecords = feeRecords,
                    students = students,
                    onSaveFeeRecord = onSaveFeeRecord,
                    onRecordPayment = onRecordFeePayment,
                    onViewReceipt = onViewReceipt
                )
                AdminTab.EXAMS -> ExamManagementScreen(
                    exams = exams,
                    students = students,
                    onCreateExam = onCreateExam,
                    onSaveMarks = onSaveMarks,
                    onViewResultCard = onViewResultCard
                )
                AdminTab.TIMETABLE -> TimetableScreen(
                    timetables = timetables,
                    onAddTimetable = onAddTimetable
                )
                AdminTab.HOMEWORK -> HomeworkScreen(
                    homeworkList = homeworkList,
                    canCreate = true,
                    onCreateHomework = onCreateHomework,
                    onDeleteHomework = onDeleteHomework
                )
                AdminTab.ANNOUNCEMENTS -> AnnouncementsScreen(
                    announcements = announcements,
                    canCreate = true,
                    onSendAnnouncement = onSendAnnouncement,
                    onDeleteAnnouncement = onDeleteAnnouncement
                )
                AdminTab.ADMISSIONS -> AdmissionManagementScreen(
                    admissions = admissions,
                    onApprove = onApproveAdmission,
                    onReject = onRejectAdmission
                )
                AdminTab.REPORTS -> ReportsScreen(
                    students = students,
                    teachers = teachers,
                    fees = feeRecords,
                    admissions = admissions,
                    onShowFeedback = onShowFeedback
                )
                AdminTab.SETTINGS -> AdminSettingsScreen(
                    settings = settings,
                    onSaveSettings = onSaveSettings,
                    onShowFeedback = onShowFeedback
                )
            }
        }
    }
}

@Composable
private fun AdminOverviewView(
    students: List<StudentEntity>,
    teachers: List<TeacherEntity>,
    classes: List<ClassSectionEntity>,
    feeRecords: List<FeeRecordEntity>,
    admissions: List<AdmissionApplicationEntity>,
    exams: List<ExamEntity>,
    announcements: List<AnnouncementEntity>,
    onNavigateToTab: (AdminTab) -> Unit
) {
    val totalCollected = feeRecords.sumOf { it.paidAmount }
    val totalPending = feeRecords.sumOf { it.balance }
    val pendingAdmissions = admissions.count { it.status == "PENDING" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Institutional Dashboard Heading
        Text(
            text = "Institutional KPIs & Executive Summary",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EducareNavyPrimary
            )
        )
        Text(
            text = "Real-time school metrics for Educare Chagmalai Campus",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Row 1: Students & Teachers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Total Students",
                value = "${students.size}",
                icon = Icons.Default.Groups,
                accentColor = EducareNavyPrimary,
                subtitle = "Active Enrolled",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Faculty & Staff",
                value = "${teachers.size}",
                icon = Icons.Default.Badge,
                accentColor = EducareGoldDark,
                subtitle = "Qualified Teachers",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 2: Fees
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Fee Collection",
                value = "Rs. ${"%.0f".format(totalCollected)}",
                icon = Icons.Default.AccountBalance,
                accentColor = EducareSuccess,
                subtitle = "Month of Oct 2026",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Outstanding Fees",
                value = "Rs. ${"%.0f".format(totalPending)}",
                icon = Icons.Default.MoneyOff,
                accentColor = EducareError,
                subtitle = "Pending Balance",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 3: Today's Attendance & Admissions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Today's Attendance",
                value = "94.5%",
                icon = Icons.Default.FactCheck,
                accentColor = EducareSkyBlue,
                subtitle = "Present: 95 | Leave: 3",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "New Admissions",
                value = "$pendingAdmissions Pending",
                icon = Icons.Default.HowToReg,
                accentColor = EducareWarning,
                subtitle = "${admissions.size} Total Received",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Administrative Jump Buttons
        Text("Quick Administration Actions", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminActionChip("Mark Attendance", Icons.Default.FactCheck, Modifier.weight(1f)) {
                onNavigateToTab(AdminTab.ATTENDANCE)
            }
            AdminActionChip("Collect Fee", Icons.Default.Paid, Modifier.weight(1f)) {
                onNavigateToTab(AdminTab.FEES)
            }
            AdminActionChip("Student Roster", Icons.Default.PersonSearch, Modifier.weight(1f)) {
                onNavigateToTab(AdminTab.STUDENTS)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Upcoming Exams Section
        Text("Upcoming Examinations", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        exams.forEach { ex ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(ex.examName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text("Session: ${ex.session} | Classes: ${ex.classesCovered}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    }
                    EducareBadge(text = "${ex.startDate}", type = "INFO")
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Recent Circulars & Broadcasts
        Text("Latest School Announcements", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        announcements.take(2).forEach { ann ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(ann.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        EducareBadge(text = ann.targetRole, type = "INFO")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(ann.message, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155)))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AdminActionChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = EducareNavyPrimary.copy(alpha = 0.08f),
            contentColor = EducareNavyPrimary
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(title, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), maxLines = 1)
        }
    }
}

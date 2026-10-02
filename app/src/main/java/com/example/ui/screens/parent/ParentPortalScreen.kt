package com.example.ui.screens.parent

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.*
import com.example.ui.components.EducareBadge
import com.example.ui.components.EducareHeader
import com.example.ui.theme.*

enum class ParentTab {
    PROFILE,
    ATTENDANCE,
    FEES,
    RESULTS,
    HOMEWORK,
    TIMETABLE
}

@Composable
fun ParentPortalScreen(
    currentUser: UserEntity,
    students: List<StudentEntity>,
    feeRecords: List<FeeRecordEntity>,
    homeworkList: List<HomeworkEntity>,
    timetables: List<TimetableEntity>,
    announcements: List<AnnouncementEntity>,
    onViewReceipt: (FeeRecordEntity) -> Unit,
    onViewResultCard: (StudentEntity) -> Unit,
    onLogout: () -> Unit,
    onOpenWebsite: () -> Unit
) {
    var activeTab by remember { mutableStateOf(ParentTab.PROFILE) }

    // Multi-children handling for this parent!
    val children = remember(students, currentUser) {
        val parentPhone = currentUser.linkedParentPhone ?: "03331234567"
        val matched = students.filter { it.parentPhone == parentPhone || it.phone == parentPhone }
        if (matched.isNotEmpty()) matched else students.take(2)
    }

    var selectedChildIndex by remember { mutableIntStateOf(0) }
    val currentChild = children.getOrNull(selectedChildIndex) ?: children.firstOrNull()

    val childFees = remember(feeRecords, currentChild) {
        if (currentChild == null) emptyList()
        else feeRecords.filter { it.studentId == currentChild.id || it.rollNo == currentChild.rollNo }
    }

    val childHomework = remember(homeworkList, currentChild) {
        if (currentChild == null) emptyList()
        else homeworkList.filter { it.className.equals(currentChild.className, ignoreCase = true) }
    }

    val childTimetable = remember(timetables, currentChild) {
        if (currentChild == null) emptyList()
        else timetables.filter { it.className.equals(currentChild.className, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            EducareHeader(
                currentUser = currentUser,
                onLogout = onLogout,
                onOpenWebsite = onOpenWebsite
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == ParentTab.PROFILE,
                    onClick = { activeTab = ParentTab.PROFILE },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") }
                )
                NavigationBarItem(
                    selected = activeTab == ParentTab.ATTENDANCE,
                    onClick = { activeTab = ParentTab.ATTENDANCE },
                    icon = { Icon(Icons.Default.EventAvailable, contentDescription = null) },
                    label = { Text("Attendance") }
                )
                NavigationBarItem(
                    selected = activeTab == ParentTab.FEES,
                    onClick = { activeTab = ParentTab.FEES },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
                    label = { Text("Fees") }
                )
                NavigationBarItem(
                    selected = activeTab == ParentTab.RESULTS,
                    onClick = { activeTab = ParentTab.RESULTS },
                    icon = { Icon(Icons.Default.School, contentDescription = null) },
                    label = { Text("Results") }
                )
                NavigationBarItem(
                    selected = activeTab == ParentTab.HOMEWORK,
                    onClick = { activeTab = ParentTab.HOMEWORK },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                    label = { Text("Diary") }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Sibling / Child Switcher Tabs (Supports Multi-Child Families)
            if (children.size > 1) {
                Text(
                    text = "Select Child:",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    children.forEachIndexed { idx, child ->
                        FilterChip(
                            selected = selectedChildIndex == idx,
                            onClick = { selectedChildIndex = idx },
                            label = { Text("${child.fullName} (${child.className})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EducareNavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (currentChild == null) {
                Text("No children linked to this parent account.")
            } else {
                when (activeTab) {
                    ParentTab.PROFILE -> ParentChildProfileView(
                        child = currentChild,
                        onViewResult = { onViewResultCard(currentChild) }
                    )
                    ParentTab.ATTENDANCE -> ParentAttendanceView(child = currentChild)
                    ParentTab.FEES -> ParentFeeLedgerView(fees = childFees, onViewReceipt = onViewReceipt)
                    ParentTab.RESULTS -> ParentResultView(
                        child = currentChild,
                        onViewResultCard = { onViewResultCard(currentChild) }
                    )
                    ParentTab.HOMEWORK -> ParentHomeworkView(homework = childHomework)
                    ParentTab.TIMETABLE -> ParentTimetableView(schedule = childTimetable)
                }
            }
        }
    }
}

@Composable
private fun ParentChildProfileView(
    child: StudentEntity,
    onViewResult: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, EducareGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = EducareNavyPrimary, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(child.fullName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("${child.className} (${child.section}) | Roll No: ${child.rollNo}", style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.Bold))
                        Text("Adm No: ${child.admissionNo}", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = EducareOutline.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(10.dp))

                Text("Academic Session: 2026-2027", style = MaterialTheme.typography.bodySmall)
                Text("Father Name: ${child.fatherName}", style = MaterialTheme.typography.bodySmall)
                Text("Registered Address: ${child.address}", style = MaterialTheme.typography.bodySmall)
                Text("Enrollment Status: ${child.status}", style = MaterialTheme.typography.bodySmall.copy(color = EducareSuccess, fontWeight = FontWeight.Bold))

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onViewResult,
                    colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                    modifier = Modifier.fillMaxWidth().testTag("parent_view_result_card")
                ) {
                    Icon(Icons.Default.Assignment, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View Official Examination Result Card")
                }
            }
        }
    }
}

@Composable
private fun ParentAttendanceView(child: StudentEntity) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Monthly Attendance Summary", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Attendance Rate: 96%", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EducareSuccess))
                    Text("Total Days: 25 | Present: 24 | Leave: 1", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Recent Attendance Logs", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            Triple("2026-10-02", "PRESENT", "On Time"),
            Triple("2026-10-01", "PRESENT", "On Time"),
            Triple("2026-09-30", "PRESENT", "On Time"),
            Triple("2026-09-29", "LEAVE", "Leave Application Approved"),
            Triple("2026-09-28", "PRESENT", "On Time")
        ).forEach { item ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.first, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text(item.third, style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                    }
                    EducareBadge(text = item.second, type = item.second)
                }
            }
        }
    }
}

@Composable
private fun ParentFeeLedgerView(
    fees: List<FeeRecordEntity>,
    onViewReceipt: (FeeRecordEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Fee Challans & Receipts", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        if (fees.isEmpty()) {
            Text("No fee vouchers generated yet.", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fees, key = { it.id }) { fee ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(fee.monthYear, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                EducareBadge(text = fee.status, type = fee.status)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            val total = fee.tuitionFee + fee.examFee - fee.discount
                            Text("Total: Rs. ${"%.0f".format(total)} | Paid: Rs. ${"%.0f".format(fee.paidAmount)} | Balance: Rs. ${"%.0f".format(fee.balance)}", style = MaterialTheme.typography.bodySmall)

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onViewReceipt(fee) },
                                colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("View Official Receipt")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ParentResultView(child: StudentEntity, onViewResultCard: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Examination Results Portal", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                Text("View official report card with subject breakdown and grades.", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onViewResultCard,
                    colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.School, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("First Term Examination Result Card")
                }
            }
        }
    }
}

@Composable
private fun ParentHomeworkView(homework: List<HomeworkEntity>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Daily Class Homework & Diary", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        if (homework.isEmpty()) {
            Text("No homework assigned.", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(homework, key = { it.id }) { hw ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(hw.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text("${hw.subjectName} | Due: ${hw.dueDate}", style = MaterialTheme.typography.labelSmall.copy(color = EducareGoldDark, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(hw.description, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155)))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ParentTimetableView(schedule: List<TimetableEntity>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Class Schedule & Periods", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(schedule, key = { it.id }) { p ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("P${p.periodNumber}: ${p.subjectName}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Teacher: ${p.teacherName}", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                        }
                        Text("${p.startTime} - ${p.endTime}", style = MaterialTheme.typography.labelSmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

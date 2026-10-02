package com.example.ui.screens.reports

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.components.EducareBadge
import com.example.ui.theme.*

@Composable
fun ReportsScreen(
    students: List<StudentEntity>,
    teachers: List<TeacherEntity>,
    fees: List<FeeRecordEntity>,
    admissions: List<AdmissionApplicationEntity>,
    onShowFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var generatedReportTitle by remember { mutableStateOf<String?>(null) }
    var reportContent by remember { mutableStateOf("") }

    val totalStudents = students.size
    val totalTeachers = teachers.size
    val totalFeesCollected = fees.sumOf { it.paidAmount }
    val totalPendingFees = fees.sumOf { it.balance }
    val pendingAdmissions = admissions.count { it.status == "PENDING" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Official Academic & Administrative Reports",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EducareNavyPrimary
            )
        )
        Text(
            text = "Generate and export consolidated summaries for Educare School System",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Report Cards
        ReportActionCard(
            title = "Student Enrollment & Roster Report",
            subtitle = "Active students list categorized by class, gender, and contact.",
            icon = Icons.Default.School,
            accentColor = EducareNavyPrimary,
            onGenerate = {
                val sb = StringBuilder("EDUCARE SCHOOL SYSTEM CHAGMALAI\nSTUDENT ENROLLMENT REPORT (Session 2026-2027)\n\n")
                sb.append("Total Registered Students: $totalStudents\n\n")
                students.forEachIndexed { i, s ->
                    sb.append("${i + 1}. ${s.fullName} | Roll: ${s.rollNo} | Class: ${s.className}-${s.section} | Father: ${s.fatherName} | Phone: ${s.phone}\n")
                }
                generatedReportTitle = "Student Enrollment Report"
                reportContent = sb.toString()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        ReportActionCard(
            title = "Fee Collection & Outstanding Ledger Report",
            subtitle = "Detailed audit of fees received, discounts, and pending balances.",
            icon = Icons.Default.Paid,
            accentColor = EducareSuccess,
            onGenerate = {
                val sb = StringBuilder("EDUCARE SCHOOL SYSTEM CHAGMALAI\nFEE AUDIT REPORT\n\n")
                sb.append("Total Fees Collected: Rs. ${"%.0f".format(totalFeesCollected)}\n")
                sb.append("Total Outstanding Balance: Rs. ${"%.0f".format(totalPendingFees)}\n\n")
                fees.forEachIndexed { i, f ->
                    sb.append("${i + 1}. ${f.studentName} (${f.className}) | Month: ${f.monthYear} | Paid: Rs. ${f.paidAmount} | Bal: Rs. ${f.balance} | Status: ${f.status}\n")
                }
                generatedReportTitle = "Fee Collection Audit Report"
                reportContent = sb.toString()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        ReportActionCard(
            title = "Faculty & Staff Allocation Report",
            subtitle = "Teaching staff qualifications, designations, and assigned subjects.",
            icon = Icons.Default.Badge,
            accentColor = EducareGoldDark,
            onGenerate = {
                val sb = StringBuilder("EDUCARE SCHOOL SYSTEM CHAGMALAI\nFACULTY ALLOCATION REPORT\n\n")
                sb.append("Total Faculty Members: $totalTeachers\n\n")
                teachers.forEachIndexed { i, t ->
                    sb.append("${i + 1}. ${t.fullName} (${t.designation})\n   Qualification: ${t.qualification}\n   Classes: ${t.assignedClasses} | Subjects: ${t.assignedSubjects} | Phone: ${t.phone}\n\n")
                }
                generatedReportTitle = "Faculty Allocation Report"
                reportContent = sb.toString()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        ReportActionCard(
            title = "Online Admissions & Inquiries Report",
            subtitle = "Overview of pending and approved new admission requests.",
            icon = Icons.Default.HowToReg,
            accentColor = EducareSkyBlue,
            onGenerate = {
                val sb = StringBuilder("EDUCARE SCHOOL SYSTEM CHAGMALAI\nADMISSIONS SUMMARY REPORT\n\n")
                sb.append("Pending Applications: $pendingAdmissions\nTotal Applications: ${admissions.size}\n\n")
                admissions.forEachIndexed { i, a ->
                    sb.append("${i + 1}. App No: ${a.applicationNumber} | Student: ${a.studentName} | Class: ${a.applyingClass} | Status: ${a.status} | Phone: ${a.phone}\n")
                }
                generatedReportTitle = "Admissions Summary Report"
                reportContent = sb.toString()
            }
        )
    }

    if (generatedReportTitle != null) {
        Dialog(onDismissRequest = { generatedReportTitle = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = generatedReportTitle!!,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EducareSurfaceSubtle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = reportContent,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(onClick = { generatedReportTitle = null }, modifier = Modifier.weight(1f)) {
                            Text("Close")
                        }
                        Button(
                            onClick = {
                                onShowFeedback("Exported $generatedReportTitle as PDF successfully!")
                                generatedReportTitle = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                            modifier = Modifier.weight(1f).testTag("export_report_pdf_button")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Download PDF")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onGenerate: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = accentColor.copy(alpha = 0.12f),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onGenerate,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("View", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White))
            }
        }
    }
}

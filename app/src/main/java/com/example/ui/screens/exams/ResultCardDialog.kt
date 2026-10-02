package com.example.ui.screens.exams

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.ExamMarkEntity
import com.example.data.model.StudentEntity
import com.example.ui.components.EducareBadge
import com.example.ui.theme.*

@Composable
fun ResultCardDialog(
    student: StudentEntity,
    examName: String,
    marks: List<ExamMarkEntity>,
    onDismiss: () -> Unit,
    onPrintOrShare: () -> Unit
) {
    val totalMax = marks.sumOf { it.totalMarks }
    val totalObtained = marks.sumOf { it.obtainedMarks }
    val percentage = if (totalMax > 0) (totalObtained / totalMax) * 100.0 else 0.0
    val overallGrade = when {
        percentage >= 90.0 -> "A+"
        percentage >= 80.0 -> "A"
        percentage >= 70.0 -> "B"
        percentage >= 60.0 -> "C"
        percentage >= 50.0 -> "D"
        else -> "F"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                // Header with Official Logo & School Identity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, EducareGold, CircleShape),
                        color = Color.White
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_logo),
                            contentDescription = "Educare Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Educare School System",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EducareNavyPrimary
                            )
                        )
                        Text(
                            text = "Chagmalai Campus",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = EducareGoldDark
                            )
                        )
                        Text(
                            text = "ہم تراشتے ہیں آپ کے بچوں کا مستقبل",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EducareNavyDark
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_result_card")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = EducareOutline)

                // Title Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = EducareNavyPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "ACADEMIC RESULT CARD - $examName",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Student Profile Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EducareSurfaceSubtle, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Student Photo Placeholder with Badge
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(1.dp, EducareOutline, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Student Photo",
                            tint = EducareNavyLight,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = student.fullName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EducareNavyPrimary
                            )
                        )
                        Text(
                            text = "Father: ${student.fatherName}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569))
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = "Class: ${student.className} (${student.section})",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = "Roll No: ${student.rollNo}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EducareGoldDark
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Subject Marks Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EducareNavyPrimary.copy(alpha = 0.08f), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subject", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.8f))
                    Text("Max", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                    Text("Obt.", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                    Text("Grade", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                }

                // Marks rows
                marks.forEachIndexed { index, mark ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (index % 2 == 0) Color.White else EducareSurfaceSubtle)
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(mark.subjectName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), modifier = Modifier.weight(1.8f))
                        Text("${mark.totalMarks.toInt()}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                        Text("${mark.obtainedMarks.toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                        Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center) {
                            EducareBadge(text = mark.grade, type = if (mark.grade in listOf("A+", "A", "B")) "SUCCESS" else "WARNING")
                        }
                    }
                }

                HorizontalDivider(color = EducareNavyPrimary, thickness = 1.dp)

                // Total Summary Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EducareNavyPrimary.copy(alpha = 0.1f), RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Grand Total", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary), modifier = Modifier.weight(1.8f))
                    Text("${totalMax.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                    Text("${totalObtained.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary), modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                    Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center) {
                        EducareBadge(text = overallGrade, type = "SUCCESS")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Percentage and Position
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = EducareSurfaceSubtle,
                    border = BorderStroke(1.dp, EducareOutline.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Percentage", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            Text("${"%.1f".format(percentage)}%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Overall Result", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            Text(if (percentage >= 50.0) "PASSED" else "NEEDS IMPROVEMENT", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = if (percentage >= 50.0) EducareSuccess else EducareError))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Remarks
                Text("Teacher Remarks: 'Demonstrates dedicated focus and active participation in class.'", style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = Color(0xFF334155)))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Principal Remarks: 'Keep up the exemplary high standard of academic excellence!'", style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = EducareNavyPrimary))

                Spacer(modifier = Modifier.height(16.dp))

                // Signatures & School Stamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .border(1.dp, EducareGold, RoundedCornerShape(8.dp))
                                .background(EducareGold.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "EDUCARE\nOFFICIAL\nSEAL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 7.sp,
                                    color = EducareGoldDark,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("School Stamp", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = Color.Gray))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sir Muhammad Tariq",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary)
                        )
                        Box(modifier = Modifier.width(90.dp).height(1.dp).background(Color.Gray))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Principal Signature", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = Color.Gray))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Close")
                    }
                    Button(
                        onClick = onPrintOrShare,
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("print_result_card_action")
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print / PDF")
                    }
                }
            }
        }
    }
}

package com.example.ui.screens.fees

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
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
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
import com.example.data.model.FeeRecordEntity
import com.example.ui.components.EducareBadge
import com.example.ui.theme.*

@Composable
fun FeeReceiptDialog(
    feeRecord: FeeRecordEntity,
    onDismiss: () -> Unit,
    onPrintOrShare: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header with Official Logo & School Name
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

                    Spacer(modifier = Modifier.width(12.dp))

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
                        modifier = Modifier.size(32.dp).testTag("close_receipt_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = EducareOutline)

                // Title Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = EducareNavyPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "OFFICIAL FEE RECEIPT / VOUCHER",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Receipt Info Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Receipt No:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                        Text(
                            text = feeRecord.receiptNumber.ifBlank { "ESS-RCP-2026-098" },
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Billing Month:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                        Text(
                            text = feeRecord.monthYear,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Student Details Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = EducareSurfaceSubtle,
                    border = BorderStroke(1.dp, EducareOutline.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Student Name:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            Text(feeRecord.studentName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Class & Section:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            Text("${feeRecord.className} (${feeRecord.section})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Roll Number:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            Text(feeRecord.rollNo, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Payment Status:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            EducareBadge(text = feeRecord.status, type = feeRecord.status)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fee Breakdown
                Text("Fee Particulars", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                Spacer(modifier = Modifier.height(6.dp))

                FeeRowItem("Monthly Tuition Fee", "Rs. ${"%.0f".format(feeRecord.tuitionFee)}")
                if (feeRecord.examFee > 0) FeeRowItem("Examination Fee", "Rs. ${"%.0f".format(feeRecord.examFee)}")
                if (feeRecord.admissionFee > 0) FeeRowItem("Admission Fee", "Rs. ${"%.0f".format(feeRecord.admissionFee)}")
                if (feeRecord.transportFee > 0) FeeRowItem("Transport Charges", "Rs. ${"%.0f".format(feeRecord.transportFee)}")
                if (feeRecord.discount > 0) FeeRowItem("Fee Concession / Discount", "- Rs. ${"%.0f".format(feeRecord.discount)}", isDiscount = true)

                val grossTotal = feeRecord.tuitionFee + feeRecord.examFee + feeRecord.admissionFee + feeRecord.transportFee + feeRecord.otherCharges - feeRecord.discount

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = EducareOutline)

                FeeRowItem("Total Payable Amount", "Rs. ${"%.0f".format(grossTotal)}", isBold = true)
                FeeRowItem("Amount Received", "Rs. ${"%.0f".format(feeRecord.paidAmount)}", isHighlight = true)
                FeeRowItem("Outstanding Balance", "Rs. ${"%.0f".format(feeRecord.balance)}", isBold = true, isBalance = feeRecord.balance > 0)

                Spacer(modifier = Modifier.height(20.dp))

                // Stamp and Signatures
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .border(1.dp, EducareGold, RoundedCornerShape(8.dp))
                                .background(EducareGold.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "EDUCARE\nOFFICIAL\nSEAL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.sp,
                                    color = EducareGoldDark,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("School Stamp", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color.Gray))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Accounts Incharge",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary)
                        )
                        Box(modifier = Modifier.width(100.dp).height(1.dp).background(Color.Gray))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Authorized Signature", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = Color.Gray))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Chagmalai, KP | Phone: +92 300 1234567 | educareschoolsys@gmail.com",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = Color.Gray),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Close")
                    }
                    Button(
                        onClick = onPrintOrShare,
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("print_receipt_action")
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

@Composable
private fun FeeRowItem(
    label: String,
    value: String,
    isBold: Boolean = false,
    isDiscount: Boolean = false,
    isHighlight: Boolean = false,
    isBalance: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isDiscount) EducareSuccess else Color(0xFF334155)
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isBold || isHighlight) FontWeight.Bold else FontWeight.SemiBold,
                color = when {
                    isDiscount -> EducareSuccess
                    isBalance -> EducareError
                    isHighlight -> EducareNavyPrimary
                    else -> Color(0xFF0F172A)
                }
            )
        )
    }
}

package com.example.ui.screens.fees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FeeRecordEntity
import com.example.data.model.StudentEntity
import com.example.ui.components.EducareBadge
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.components.StatCard
import com.example.ui.theme.*

@Composable
fun FeeManagementScreen(
    feeRecords: List<FeeRecordEntity>,
    students: List<StudentEntity>,
    onSaveFeeRecord: (FeeRecordEntity, Boolean) -> Unit,
    onRecordPayment: (Long, Double, String) -> Unit,
    onViewReceipt: (FeeRecordEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }
    var showCreateFeeDialog by remember { mutableStateOf(false) }
    var payingFeeRecord by remember { mutableStateOf<FeeRecordEntity?>(null) }

    val filteredRecords = remember(feeRecords, selectedFilter, searchQuery) {
        feeRecords.filter { r ->
            val matchesFilter = when (selectedFilter) {
                "PAID" -> r.status == "PAID"
                "PARTIAL" -> r.status == "PARTIAL"
                "UNPAID" -> r.status == "UNPAID"
                else -> true
            }
            val query = searchQuery.trim().lowercase()
            val matchesQuery = query.isEmpty() ||
                    r.studentName.lowercase().contains(query) ||
                    r.rollNo.lowercase().contains(query) ||
                    r.receiptNumber.lowercase().contains(query)
            matchesFilter && matchesQuery
        }
    }

    val totalCollected = feeRecords.sumOf { it.paidAmount }
    val totalPending = feeRecords.sumOf { it.balance }
    val paidCount = feeRecords.count { it.status == "PAID" }
    val unpaidCount = feeRecords.count { it.status != "PAID" }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateFeeDialog = true },
                containerColor = EducareNavyPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("generate_fee_voucher_fab")
            ) {
                Icon(Icons.Default.AddCard, contentDescription = "Generate Fee Voucher")
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Fee Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Collected",
                    value = "Rs. ${"%.0f".format(totalCollected)}",
                    icon = Icons.Default.CheckCircle,
                    accentColor = EducareSuccess,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Pending",
                    value = "Rs. ${"%.0f".format(totalPending)}",
                    icon = Icons.Default.Pending,
                    accentColor = EducareError,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().testTag("fee_search_input"),
                placeholder = { Text("Search by student or receipt no...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EducareNavyPrimary) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("ALL", "PAID", "PARTIAL", "UNPAID").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EducareNavyPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredRecords.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.ReceiptLong,
                    title = "No Fee Records Found",
                    message = "Tap the + button to generate a new fee challan/voucher."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredRecords, key = { it.id }) { fee ->
                        FeeCardItem(
                            fee = fee,
                            onPay = { payingFeeRecord = fee },
                            onReceipt = { onViewReceipt(fee) }
                        )
                    }
                }
            }
        }
    }

    // Payment Dialog
    if (payingFeeRecord != null) {
        RecordPaymentDialog(
            fee = payingFeeRecord!!,
            onDismiss = { payingFeeRecord = null },
            onSubmitPayment = { amount, date ->
                onRecordPayment(payingFeeRecord!!.id, amount, date)
                payingFeeRecord = null
            }
        )
    }

    // Create Fee Voucher Dialog
    if (showCreateFeeDialog) {
        CreateFeeVoucherDialog(
            students = students,
            onDismiss = { showCreateFeeDialog = false },
            onSave = { newFee ->
                onSaveFeeRecord(newFee, true)
                showCreateFeeDialog = false
            }
        )
    }
}

@Composable
private fun FeeCardItem(
    fee: FeeRecordEntity,
    onPay: () -> Unit,
    onReceipt: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("fee_card_${fee.rollNo}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = fee.studentName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${fee.className} (${fee.section}) | Roll: ${fee.rollNo}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
                EducareBadge(text = fee.status, type = fee.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            val grossTotal = fee.tuitionFee + fee.examFee + fee.admissionFee + fee.transportFee + fee.otherCharges - fee.discount

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Amount", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                    Text("Rs. ${"%.0f".format(grossTotal)}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                }
                Column {
                    Text("Paid", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                    Text("Rs. ${"%.0f".format(fee.paidAmount)}", style = MaterialTheme.typography.bodyMedium.copy(color = EducareSuccess, fontWeight = FontWeight.Bold))
                }
                Column {
                    Text("Balance", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                    Text("Rs. ${"%.0f".format(fee.balance)}", style = MaterialTheme.typography.bodyMedium.copy(color = if (fee.balance > 0) EducareError else Color.Gray, fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = EducareOutline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Month: ${fee.monthYear}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (fee.balance > 0) {
                        OutlinedButton(
                            onClick = onPay,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text("Collect Fee")
                        }
                    }
                    Button(
                        onClick = onReceipt,
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Receipt")
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordPaymentDialog(
    fee: FeeRecordEntity,
    onDismiss: () -> Unit,
    onSubmitPayment: (Double, String) -> Unit
) {
    var payAmount by remember { mutableStateOf(fee.balance.toInt().toString()) }
    var payDate by remember { mutableStateOf("2026-10-02") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Collect Fee Payment",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )
                Text(
                    text = "Student: ${fee.studentName} (Roll: ${fee.rollNo})",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Text(
                    text = "Outstanding Balance: Rs. ${"%.0f".format(fee.balance)}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = EducareError, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = payAmount,
                    onValueChange = { payAmount = it },
                    label = { Text("Payment Amount (Rs.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("payment_amount_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = payDate,
                    onValueChange = { payDate = it },
                    label = { Text("Payment Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val amount = payAmount.toDoubleOrNull() ?: 0.0
                            if (amount > 0) {
                                onSubmitPayment(amount, payDate)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("confirm_payment_button")
                    ) {
                        Text("Save & Print")
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateFeeVoucherDialog(
    students: List<StudentEntity>,
    onDismiss: () -> Unit,
    onSave: (FeeRecordEntity) -> Unit
) {
    var selectedStudent by remember { mutableStateOf(students.firstOrNull()) }
    var monthYear by remember { mutableStateOf("October 2026") }
    var tuitionFee by remember { mutableStateOf("3500") }
    var examFee by remember { mutableStateOf("500") }
    var discount by remember { mutableStateOf("0") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = "Generate Fee Voucher",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Student dropdown
                var expandedStudent by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(
                        onClick = { expandedStudent = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(selectedStudent?.let { "${it.fullName} (Roll: ${it.rollNo})" } ?: "Select Student")
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(expanded = expandedStudent, onDismissRequest = { expandedStudent = false }) {
                        students.forEach { s ->
                            DropdownMenuItem(
                                text = { Text("${s.fullName} - ${s.className} (${s.rollNo})") },
                                onClick = {
                                    selectedStudent = s
                                    expandedStudent = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = monthYear,
                    onValueChange = { monthYear = it },
                    label = { Text("Billing Month") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = tuitionFee,
                    onValueChange = { tuitionFee = it },
                    label = { Text("Tuition Fee (Rs.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = examFee,
                    onValueChange = { examFee = it },
                    label = { Text("Exam Fee (Rs.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = discount,
                    onValueChange = { discount = it },
                    label = { Text("Concession / Discount (Rs.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            selectedStudent?.let { s ->
                                val tuition = tuitionFee.toDoubleOrNull() ?: 0.0
                                val exam = examFee.toDoubleOrNull() ?: 0.0
                                val disc = discount.toDoubleOrNull() ?: 0.0
                                val total = (tuition + exam - disc).coerceAtLeast(0.0)

                                onSave(
                                    FeeRecordEntity(
                                        studentId = s.id,
                                        studentName = s.fullName,
                                        rollNo = s.rollNo,
                                        className = s.className,
                                        section = s.section,
                                        monthYear = monthYear,
                                        tuitionFee = tuition,
                                        examFee = exam,
                                        discount = disc,
                                        paidAmount = 0.0,
                                        balance = total,
                                        status = "UNPAID",
                                        receiptNumber = "ESS-RCP-${System.currentTimeMillis() % 100000}"
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Generate")
                    }
                }
            }
        }
    }
}

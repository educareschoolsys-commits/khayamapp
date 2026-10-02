package com.example.data.local

import com.example.data.model.*

object SeedData {
    suspend fun populateInitialData(dao: SchoolDao) {
        // Only seed if empty
        val existingSettings = dao.getSettingsDirect()
        if (existingSettings != null) return

        // 1. School Settings
        val settings = SchoolSettingsEntity(
            id = 1,
            schoolName = "Educare School System Chagmalai",
            urduSlogan = "ہم تراشتے ہیں آپ کے بچوں کا مستقبل",
            englishSlogan = "We Shape Your Children's Future",
            address = "Main Campus, Chagmalai, Khyber Pakhtunkhwa",
            phone = "+92 300 1234567",
            whatsapp = "+92 300 1234567",
            email = "educareschoolsys@gmail.com",
            currentSession = "2026-2027",
            principalName = "Sir Muhammad Tariq"
        )
        dao.insertOrUpdateSettings(settings)

        // 2. Users (Roles: Principal, Admin Staff, Teacher, Parent, Student)
        val users = listOf(
            UserEntity(
                username = "khayamilham@gmail.com",
                password = "Educareschool",
                fullName = "Khayam Ilham (Super Admin / Principal)",
                role = UserRole.PRINCIPAL
            ),
            UserEntity(
                username = "admin@educare.pk",
                password = "123",
                fullName = "Mr. Imran Khan (Admin Office)",
                role = UserRole.ADMIN_STAFF
            ),
            UserEntity(
                username = "teacher@educare.pk",
                password = "123",
                fullName = "Sir Ahmad Ali (Science Faculty)",
                role = UserRole.TEACHER,
                linkedTeacherId = 1L
            ),
            UserEntity(
                username = "parent@educare.pk",
                password = "123",
                fullName = "Haji Abdul Rehman (Parent)",
                role = UserRole.PARENT,
                linkedParentPhone = "03331234567"
            ),
            UserEntity(
                username = "student@educare.pk",
                password = "123",
                fullName = "Muhammad Bilal (Student)",
                role = UserRole.STUDENT,
                linkedStudentId = 1L
            )
        )
        dao.insertUsers(users)

        // 3. Classes and Sections
        val classes = listOf(
            ClassSectionEntity(className = "Class 10", section = "A", roomNumber = "Hall 1", classTeacherName = "Sir Ahmad Ali", totalCapacity = 40),
            ClassSectionEntity(className = "Class 10", section = "B", roomNumber = "Hall 2", classTeacherName = "Sir Rashid Mehmood", totalCapacity = 40),
            ClassSectionEntity(className = "Class 9", section = "A", roomNumber = "Room 201", classTeacherName = "Ms. Fatima Zahra", totalCapacity = 35),
            ClassSectionEntity(className = "Class 9", section = "B", roomNumber = "Room 202", classTeacherName = "Sir Tariq Naveed", totalCapacity = 35),
            ClassSectionEntity(className = "Class 8", section = "A", roomNumber = "Room 101", classTeacherName = "Ms. Ayesha Bibi", totalCapacity = 35),
            ClassSectionEntity(className = "Class 7", section = "A", roomNumber = "Room 102", classTeacherName = "Sir Usman Khalid", totalCapacity = 30),
            ClassSectionEntity(className = "Class 6", section = "A", roomNumber = "Room 103", classTeacherName = "Ms. Zainab Bibi", totalCapacity = 30),
            ClassSectionEntity(className = "Nursery", section = "A", roomNumber = "Junior Block", classTeacherName = "Ms. Maryam Khan", totalCapacity = 25),
            ClassSectionEntity(className = "Prep", section = "A", roomNumber = "Junior Block", classTeacherName = "Ms. Sana Noor", totalCapacity = 25)
        )
        dao.insertClasses(classes)

        // 4. Teachers
        val teachers = listOf(
            TeacherEntity(
                id = 1L,
                fullName = "Sir Ahmad Ali",
                email = "ahmad.ali@educare.pk",
                phone = "03121234567",
                qualification = "M.Sc Physics (Peshawar Univ)",
                designation = "Senior Science Lecturer",
                assignedClasses = "Class 10, Class 9",
                assignedSubjects = "Physics, Mathematics",
                joiningDate = "2021-03-15",
                status = "Active"
            ),
            TeacherEntity(
                id = 2L,
                fullName = "Ms. Fatima Zahra",
                email = "fatima.z@educare.pk",
                phone = "03139876543",
                qualification = "M.A English Literature",
                designation = "Head of English Department",
                assignedClasses = "Class 9, Class 8",
                assignedSubjects = "English, Communication",
                joiningDate = "2022-01-10",
                status = "Active"
            ),
            TeacherEntity(
                id = 3L,
                fullName = "Sir Rashid Mehmood",
                email = "rashid.m@educare.pk",
                phone = "03009988776",
                qualification = "M.Sc Chemistry, B.Ed",
                designation = "Senior Chemistry Specialist",
                assignedClasses = "Class 10, Class 9",
                assignedSubjects = "Chemistry, Biology",
                joiningDate = "2020-08-20",
                status = "Active"
            ),
            TeacherEntity(
                id = 4L,
                fullName = "Sir Tariq Naveed",
                email = "tariq.n@educare.pk",
                phone = "03456789012",
                qualification = "M.S Computer Science",
                designation = "IT & Computer Incharge",
                assignedClasses = "Class 10, Class 9, Class 8",
                assignedSubjects = "Computer Science, Mathematics",
                joiningDate = "2023-02-01",
                status = "Active"
            )
        )
        dao.insertTeachers(teachers)

        // 5. Parents
        val parents = listOf(
            ParentEntity(
                fullName = "Haji Abdul Rehman",
                phone = "03331234567",
                email = "rehman.traders@gmail.com",
                whatsapp = "+92 333 1234567",
                occupation = "Businessman / Merchant",
                address = "Main Bazaar Chagmalai"
            ),
            ParentEntity(
                fullName = "Farooq Ahmad",
                phone = "03219876543",
                email = "farooq.engr@yahoo.com",
                whatsapp = "+92 321 9876543",
                occupation = "Civil Engineer",
                address = "Near Degree College, Chagmalai"
            ),
            ParentEntity(
                fullName = "Tariq Mehmood",
                phone = "03005551234",
                email = "tariq.m@gmail.com",
                whatsapp = "+92 300 5551234",
                occupation = "Government Officer",
                address = "Officers Colony, Chagmalai"
            )
        )
        dao.insertParents(parents)

        // 6. Students
        val students = listOf(
            StudentEntity(
                id = 1L,
                admissionNo = "ADM-2024-101",
                rollNo = "101",
                fullName = "Muhammad Bilal",
                fatherName = "Haji Abdul Rehman",
                motherName = "Bibi Shaheen",
                dob = "2009-08-14",
                gender = "Male",
                className = "Class 10",
                section = "A",
                phone = "03331234567",
                whatsapp = "+92 333 1234567",
                address = "Main Bazaar, Chagmalai",
                admissionDate = "2024-04-01",
                previousSchool = "Educare Middle Wing",
                status = "Active",
                parentPhone = "03331234567"
            ),
            StudentEntity(
                id = 2L,
                admissionNo = "ADM-2024-102",
                rollNo = "102",
                fullName = "Zeeshan Ahmad",
                fatherName = "Haji Abdul Rehman", // sibling!
                motherName = "Bibi Shaheen",
                dob = "2011-05-22",
                gender = "Male",
                className = "Class 9",
                section = "A",
                phone = "03331234567",
                whatsapp = "+92 333 1234567",
                address = "Main Bazaar, Chagmalai",
                admissionDate = "2024-04-01",
                previousSchool = "Educare Middle Wing",
                status = "Active",
                parentPhone = "03331234567"
            ),
            StudentEntity(
                id = 3L,
                admissionNo = "ADM-2024-103",
                rollNo = "103",
                fullName = "Hamza Farooq",
                fatherName = "Farooq Ahmad",
                motherName = "Sultana Begum",
                dob = "2009-11-04",
                gender = "Male",
                className = "Class 10",
                section = "A",
                phone = "03219876543",
                whatsapp = "+92 321 9876543",
                address = "Near Degree College, Chagmalai",
                admissionDate = "2024-04-05",
                previousSchool = "Govt High School",
                status = "Active",
                parentPhone = "03219876543"
            ),
            StudentEntity(
                id = 4L,
                admissionNo = "ADM-2024-104",
                rollNo = "104",
                fullName = "Aiman Tariq",
                fatherName = "Tariq Mehmood",
                motherName = "Tahira Khan",
                dob = "2010-02-18",
                gender = "Female",
                className = "Class 9",
                section = "A",
                phone = "03005551234",
                whatsapp = "+92 300 5551234",
                address = "Officers Colony, Chagmalai",
                admissionDate = "2024-04-10",
                previousSchool = "Army Public School",
                status = "Active",
                parentPhone = "03005551234"
            ),
            StudentEntity(
                id = 5L,
                admissionNo = "ADM-2024-105",
                rollNo = "105",
                fullName = "Zainab Noor",
                fatherName = "Noor Muhammad",
                motherName = "Kulsoom Bibi",
                dob = "2010-09-30",
                gender = "Female",
                className = "Class 9",
                section = "A",
                phone = "03134445566",
                whatsapp = "+92 313 4445566",
                address = "Hospital Road, Chagmalai",
                admissionDate = "2024-04-12",
                previousSchool = "Educare Junior Section",
                status = "Active",
                parentPhone = "03134445566"
            ),
            StudentEntity(
                id = 6L,
                admissionNo = "ADM-2024-106",
                rollNo = "106",
                fullName = "Usman Ghani",
                fatherName = "Ghani Ur Rehman",
                motherName = "Nazia Bibi",
                dob = "2011-12-05",
                gender = "Male",
                className = "Class 8",
                section = "A",
                phone = "03328889900",
                whatsapp = "+92 332 8889900",
                address = "Gulbahar Road, Chagmalai",
                admissionDate = "2024-04-15",
                previousSchool = "Public Model School",
                status = "Active",
                parentPhone = "03328889900"
            )
        )
        dao.insertStudents(students)

        // 7. Attendance (Today & Recent)
        val today = "2026-10-02"
        val attendanceList = listOf(
            AttendanceEntity(studentId = 1L, studentName = "Muhammad Bilal", rollNo = "101", className = "Class 10", section = "A", date = today, status = "PRESENT", remarks = "On time"),
            AttendanceEntity(studentId = 3L, studentName = "Hamza Farooq", rollNo = "103", className = "Class 10", section = "A", date = today, status = "PRESENT", remarks = "On time"),
            AttendanceEntity(studentId = 2L, studentName = "Zeeshan Ahmad", rollNo = "102", className = "Class 9", section = "A", date = today, status = "PRESENT", remarks = "On time"),
            AttendanceEntity(studentId = 4L, studentName = "Aiman Tariq", rollNo = "104", className = "Class 9", section = "A", date = today, status = "LEAVE", remarks = "Sick Leave (Application submitted)"),
            AttendanceEntity(studentId = 5L, studentName = "Zainab Noor", rollNo = "105", className = "Class 9", section = "A", date = today, status = "PRESENT", remarks = "On time"),
            AttendanceEntity(studentId = 6L, studentName = "Usman Ghani", rollNo = "106", className = "Class 8", section = "A", date = today, status = "ABSENT", remarks = "Uninformed absence")
        )
        dao.insertAttendanceBatch(attendanceList)

        // 8. Fee Records & Receipts
        val fees = listOf(
            FeeRecordEntity(
                studentId = 1L,
                studentName = "Muhammad Bilal",
                rollNo = "101",
                className = "Class 10",
                section = "A",
                monthYear = "October 2026",
                tuitionFee = 3500.0,
                examFee = 500.0,
                discount = 200.0,
                paidAmount = 3800.0,
                balance = 0.0,
                status = "PAID",
                paymentDate = "2026-10-01",
                receiptNumber = "ESS-RCP-2026-1001"
            ),
            FeeRecordEntity(
                studentId = 2L,
                studentName = "Zeeshan Ahmad",
                rollNo = "102",
                className = "Class 9",
                section = "A",
                monthYear = "October 2026",
                tuitionFee = 3200.0,
                examFee = 500.0,
                discount = 500.0, // Sibling concession
                paidAmount = 2000.0,
                balance = 1200.0,
                status = "PARTIAL",
                paymentDate = "2026-10-02",
                receiptNumber = "ESS-RCP-2026-1002"
            ),
            FeeRecordEntity(
                studentId = 3L,
                studentName = "Hamza Farooq",
                rollNo = "103",
                className = "Class 10",
                section = "A",
                monthYear = "October 2026",
                tuitionFee = 3500.0,
                examFee = 500.0,
                discount = 0.0,
                paidAmount = 0.0,
                balance = 4000.0,
                status = "UNPAID",
                paymentDate = "",
                receiptNumber = ""
            ),
            FeeRecordEntity(
                studentId = 4L,
                studentName = "Aiman Tariq",
                rollNo = "104",
                className = "Class 9",
                section = "A",
                monthYear = "October 2026",
                tuitionFee = 3200.0,
                examFee = 500.0,
                discount = 0.0,
                paidAmount = 3700.0,
                balance = 0.0,
                status = "PAID",
                paymentDate = "2026-09-30",
                receiptNumber = "ESS-RCP-2026-0994"
            )
        )
        dao.insertFeeRecords(fees)

        // 9. Exams & Marks
        val exam = ExamEntity(
            id = 1L,
            examName = "First Term Examination 2026",
            session = "2026-2027",
            startDate = "2026-09-15",
            endDate = "2026-09-25",
            classesCovered = "Class 6 to Class 10"
        )
        dao.insertExam(exam)

        val marksList = listOf(
            // Bilal (Class 10)
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 1L, studentName = "Muhammad Bilal", rollNo = "101", className = "Class 10", section = "A", subjectName = "Mathematics", totalMarks = 100.0, obtainedMarks = 94.0, grade = "A+", remarks = "Outstanding analytical ability"),
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 1L, studentName = "Muhammad Bilal", rollNo = "101", className = "Class 10", section = "A", subjectName = "Physics", totalMarks = 100.0, obtainedMarks = 91.0, grade = "A+", remarks = "Excellent conceptual grasp"),
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 1L, studentName = "Muhammad Bilal", rollNo = "101", className = "Class 10", section = "A", subjectName = "Chemistry", totalMarks = 100.0, obtainedMarks = 88.0, grade = "A", remarks = "Very thorough in practicals"),
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 1L, studentName = "Muhammad Bilal", rollNo = "101", className = "Class 10", section = "A", subjectName = "English", totalMarks = 100.0, obtainedMarks = 85.0, grade = "A", remarks = "Good vocabulary"),
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 1L, studentName = "Muhammad Bilal", rollNo = "101", className = "Class 10", section = "A", subjectName = "Urdu", totalMarks = 100.0, obtainedMarks = 89.0, grade = "A", remarks = "Neat handwriting"),
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 1L, studentName = "Muhammad Bilal", rollNo = "101", className = "Class 10", section = "A", subjectName = "Islamiat", totalMarks = 100.0, obtainedMarks = 95.0, grade = "A+", remarks = "Brilliant knowledge"),

            // Zeeshan (Class 9)
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 2L, studentName = "Zeeshan Ahmad", rollNo = "102", className = "Class 9", section = "A", subjectName = "Mathematics", totalMarks = 100.0, obtainedMarks = 82.0, grade = "A", remarks = "Good effort"),
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 2L, studentName = "Zeeshan Ahmad", rollNo = "102", className = "Class 9", section = "A", subjectName = "General Science", totalMarks = 100.0, obtainedMarks = 79.0, grade = "B", remarks = "Needs more revision"),
            ExamMarkEntity(examId = 1L, examName = "First Term Examination 2026", studentId = 2L, studentName = "Zeeshan Ahmad", rollNo = "102", className = "Class 9", section = "A", subjectName = "English", totalMarks = 100.0, obtainedMarks = 84.0, grade = "A", remarks = "Very impressive essays")
        )
        dao.insertExamMarks(marksList)

        // 10. Timetables (Monday)
        val timetables = listOf(
            TimetableEntity(className = "Class 10", section = "A", dayOfWeek = "Monday", periodNumber = 1, startTime = "08:00 AM", endTime = "08:45 AM", subjectName = "Mathematics", teacherName = "Sir Ahmad Ali", roomNumber = "Hall 1"),
            TimetableEntity(className = "Class 10", section = "A", dayOfWeek = "Monday", periodNumber = 2, startTime = "08:45 AM", endTime = "09:30 AM", subjectName = "Physics", teacherName = "Sir Ahmad Ali", roomNumber = "Science Lab"),
            TimetableEntity(className = "Class 10", section = "A", dayOfWeek = "Monday", periodNumber = 3, startTime = "09:30 AM", endTime = "10:15 AM", subjectName = "Chemistry", teacherName = "Sir Rashid Mehmood", roomNumber = "Chemistry Lab"),
            TimetableEntity(className = "Class 10", section = "A", dayOfWeek = "Monday", periodNumber = 4, startTime = "10:45 AM", endTime = "11:30 AM", subjectName = "English", teacherName = "Ms. Fatima Zahra", roomNumber = "Hall 1"),
            TimetableEntity(className = "Class 10", section = "A", dayOfWeek = "Monday", periodNumber = 5, startTime = "11:30 AM", endTime = "12:15 PM", subjectName = "Computer Science", teacherName = "Sir Tariq Naveed", roomNumber = "Computer Lab"),
            TimetableEntity(className = "Class 10", section = "A", dayOfWeek = "Monday", periodNumber = 6, startTime = "12:15 PM", endTime = "01:00 PM", subjectName = "Islamiat", teacherName = "Ms. Ayesha Bibi", roomNumber = "Hall 1"),

            TimetableEntity(className = "Class 9", section = "A", dayOfWeek = "Monday", periodNumber = 1, startTime = "08:00 AM", endTime = "08:45 AM", subjectName = "English", teacherName = "Ms. Fatima Zahra", roomNumber = "Room 201"),
            TimetableEntity(className = "Class 9", section = "A", dayOfWeek = "Monday", periodNumber = 2, startTime = "08:45 AM", endTime = "09:30 AM", subjectName = "Mathematics", teacherName = "Sir Ahmad Ali", roomNumber = "Room 201"),
            TimetableEntity(className = "Class 9", section = "A", dayOfWeek = "Monday", periodNumber = 3, startTime = "09:30 AM", endTime = "10:15 AM", subjectName = "Biology", teacherName = "Sir Rashid Mehmood", roomNumber = "Biology Lab"),
            TimetableEntity(className = "Class 9", section = "A", dayOfWeek = "Monday", periodNumber = 4, startTime = "10:45 AM", endTime = "11:30 AM", subjectName = "Urdu", teacherName = "Ms. Fatima Zahra", roomNumber = "Room 201")
        )
        dao.insertTimetables(timetables)

        // 11. Homework
        val homeworkList = listOf(
            HomeworkEntity(
                className = "Class 10",
                section = "A",
                subjectName = "Mathematics",
                teacherName = "Sir Ahmad Ali",
                title = "Quadratic Equations Exercise 1.2",
                description = "Complete questions 1 to 8 in neat homework notebook. Prepare for short quiz.",
                assignedDate = "2026-10-02",
                dueDate = "2026-10-05"
            ),
            HomeworkEntity(
                className = "Class 10",
                section = "A",
                subjectName = "Physics",
                teacherName = "Sir Ahmad Ali",
                title = "Newton's Laws Numerical Problems",
                description = "Solve Chapter 3 numerical problems 3.1 to 3.5 with proper given data and SI units.",
                assignedDate = "2026-10-01",
                dueDate = "2026-10-04"
            ),
            HomeworkEntity(
                className = "Class 9",
                section = "A",
                subjectName = "English",
                teacherName = "Ms. Fatima Zahra",
                title = "Essay: Dignity of Work",
                description = "Write a 250-word essay highlighting dignity of labor with quotations and examples.",
                assignedDate = "2026-10-02",
                dueDate = "2026-10-06"
            )
        )
        dao.insertHomeworkList(homeworkList)

        // 12. Announcements
        val announcements = listOf(
            AnnouncementEntity(
                title = "Annual Science & Tech Fair 2026",
                message = "Educare School System Chagmalai is organizing its Annual Science Exhibition next Friday. All students from Class 6 to 10 must register their project prototypes with Sir Ahmad Ali.",
                targetRole = "ALL",
                targetClass = "",
                date = "2026-10-02",
                type = "GENERAL",
                isImportant = true
            ),
            AnnouncementEntity(
                title = "Parent-Teacher Meeting (PTM)",
                message = "The First Term Result Cards and Academic Progress Report will be distributed in person during the PTM on Saturday from 9:00 AM to 1:00 PM. Parents' presence is highly requested.",
                targetRole = "PARENT",
                targetClass = "",
                date = "2026-10-01",
                type = "EXAM",
                isImportant = true
            ),
            AnnouncementEntity(
                title = "Monthly Fee Submission Advisory",
                message = "Dear Parents, please ensure payment of October 2026 school fees by 10th October to avoid late submission charges. Fee receipts can be collected from accounts office or viewed in the app.",
                targetRole = "PARENT",
                targetClass = "",
                date = "2026-09-29",
                type = "FEE",
                isImportant = false
            ),
            AnnouncementEntity(
                title = "Kashmir Day Special Assembly",
                message = "A commemorative speech and national song competition will be conducted during the morning assembly.",
                targetRole = "ALL",
                targetClass = "",
                date = "2026-09-28",
                type = "HOLIDAY",
                isImportant = false
            )
        )
        dao.insertAnnouncements(announcements)

        // 13. Admissions
        val admissions = listOf(
            AdmissionApplicationEntity(
                applicationNumber = "ADM-2026-081",
                studentName = "Saad Ullah",
                fatherName = "Rahmat Ullah",
                motherName = "Perveen Bibi",
                dob = "2010-06-12",
                gender = "Male",
                applyingClass = "Class 9",
                phone = "03441239876",
                whatsapp = "+92 344 1239876",
                address = "Village Chagmalai North",
                previousSchool = "Al-Qalam Public Academy",
                status = "PENDING",
                submissionDate = "2026-10-01"
            ),
            AdmissionApplicationEntity(
                applicationNumber = "ADM-2026-082",
                studentName = "Ayesha Siddiqa",
                fatherName = "Dr. Mujeeb Ur Rehman",
                motherName = "Dr. Samina",
                dob = "2018-03-25",
                gender = "Female",
                applyingClass = "Class 2",
                phone = "03029988112",
                whatsapp = "+92 302 9988112",
                address = "Hospital Colony, Chagmalai",
                previousSchool = "Beacon School",
                status = "APPROVED",
                submissionDate = "2026-09-28"
            ),
            AdmissionApplicationEntity(
                applicationNumber = "ADM-2026-083",
                studentName = "Danyal Khan",
                fatherName = "Sher Ali Khan",
                motherName = "Gul Meena",
                dob = "2012-09-14",
                gender = "Male",
                applyingClass = "Class 7",
                phone = "03357776655",
                whatsapp = "+92 335 7776655",
                address = "Bazaar Road, Chagmalai",
                previousSchool = "Government Model School",
                status = "PENDING",
                submissionDate = "2026-10-02"
            )
        )
        dao.insertAdmissions(admissions)

        // 14. Gallery & Activities
        val gallery = listOf(
            GalleryItemEntity(
                title = "Annual Science Exhibition & Robotics",
                category = "ACADEMIC",
                date = "2026-09-18",
                description = "Students presenting automated water irrigation and physics models at Educare Chagmalai campus."
            ),
            GalleryItemEntity(
                title = "Inter-House Cricket Championship",
                category = "SPORTS",
                date = "2026-08-28",
                description = "Final match between Jinnah House and Iqbal House on the lush green school grounds."
            ),
            GalleryItemEntity(
                title = "Annual Prize Distribution Ceremony",
                category = "ANNUAL_FUNCTION",
                date = "2026-05-15",
                description = "Celebrating our top academic achievers and board position holders with medals and shields."
            ),
            GalleryItemEntity(
                title = "Daily Morning Assembly & Moral Speeches",
                category = "ASSEMBLY",
                date = "2026-09-10",
                description = "Disciplined student lines, national anthem recitation, and student moral speech."
            )
        )
        dao.insertGalleryItems(gallery)

        // 15. Achievements
        val achievements = listOf(
            AchievementEntity(
                studentName = "Muhammad Bilal",
                className = "Class 10",
                achievementTitle = "1st Position District Science Olympiad",
                competitionName = "KP Science & Innovation Challenge",
                position = "1st Position (Gold Medal)",
                date = "2026-08-10"
            ),
            AchievementEntity(
                studentName = "Aiman Tariq",
                className = "Class 9",
                achievementTitle = "Regional Speech Competition Champion",
                competitionName = "All-District English Declamation Contest",
                position = "Champion Trophy",
                date = "2026-07-22"
            ),
            AchievementEntity(
                studentName = "Hamza Farooq",
                className = "Class 10",
                achievementTitle = "Best All-Round Athlete 2026",
                competitionName = "Educare Sports Gala",
                position = "Player of the Tournament",
                date = "2026-08-29"
            )
        )
        dao.insertAchievements(achievements)
    }
}

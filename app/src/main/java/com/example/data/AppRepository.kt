package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import android.util.Log

class AppRepository(private val dao: AppDao) {

    // Lists as Flows
    val allNotices: Flow<List<Notice>> = dao.getAllNotices()
    val allAssignments: Flow<List<Assignment>> = dao.getAllAssignments()
    val allExams: Flow<List<Exam>> = dao.getAllExams()
    val allAttendance: Flow<List<Attendance>> = dao.getAllAttendance()
    val allFinancialReminders: Flow<List<FinancialReminder>> = dao.getAllFinancialReminders()
    val allCareerOpportunities: Flow<List<CareerOpportunity>> = dao.getAllCareerOpportunities()
    val allDocuments: Flow<List<Document>> = dao.getAllDocuments()
    val allProjects: Flow<List<Project>> = dao.getAllProjects()
    val allCollegeEvents: Flow<List<CollegeEvent>> = dao.getAllCollegeEvents()
    val allTasks: Flow<List<Task>> = dao.getAllTasks()

    // Notices CRUD
    suspend fun insertNotice(notice: Notice) = dao.insertNotice(notice)
    suspend fun updateNotice(notice: Notice) = dao.updateNotice(notice)
    suspend fun deleteNotice(notice: Notice) = dao.deleteNotice(notice)

    // Assignments CRUD
    suspend fun insertAssignment(assignment: Assignment) = dao.insertAssignment(assignment)
    suspend fun updateAssignment(assignment: Assignment) = dao.updateAssignment(assignment)
    suspend fun deleteAssignment(assignment: Assignment) = dao.deleteAssignment(assignment)

    // Exams CRUD
    suspend fun insertExam(exam: Exam) = dao.insertExam(exam)
    suspend fun updateExam(exam: Exam) = dao.updateExam(exam)
    suspend fun deleteExam(exam: Exam) = dao.deleteExam(exam)

    // Attendance CRUD
    suspend fun insertAttendance(attendance: Attendance) = dao.insertAttendance(attendance)
    suspend fun updateAttendance(attendance: Attendance) = dao.updateAttendance(attendance)
    suspend fun deleteAttendance(attendance: Attendance) = dao.deleteAttendance(attendance)

    // Financial Reminders CRUD
    suspend fun insertFinancialReminder(reminder: FinancialReminder) = dao.insertFinancialReminder(reminder)
    suspend fun updateFinancialReminder(reminder: FinancialReminder) = dao.updateFinancialReminder(reminder)
    suspend fun deleteFinancialReminder(reminder: FinancialReminder) = dao.deleteFinancialReminder(reminder)

    // Career Opportunities CRUD
    suspend fun insertCareerOpportunity(opportunity: CareerOpportunity) = dao.insertCareerOpportunity(opportunity)
    suspend fun updateCareerOpportunity(opportunity: CareerOpportunity) = dao.updateCareerOpportunity(opportunity)
    suspend fun deleteCareerOpportunity(opportunity: CareerOpportunity) = dao.deleteCareerOpportunity(opportunity)

    // Documents CRUD
    suspend fun insertDocument(document: Document) = dao.insertDocument(document)
    suspend fun updateDocument(document: Document) = dao.updateDocument(document)
    suspend fun deleteDocument(document: Document) = dao.deleteDocument(document)

    // Projects CRUD
    suspend fun insertProject(project: Project) = dao.insertProject(project)
    suspend fun updateProject(project: Project) = dao.updateProject(project)
    suspend fun deleteProject(project: Project) = dao.deleteProject(project)

    // College Events CRUD
    suspend fun insertCollegeEvent(event: CollegeEvent) = dao.insertCollegeEvent(event)
    suspend fun updateCollegeEvent(event: CollegeEvent) = dao.updateCollegeEvent(event)
    suspend fun deleteCollegeEvent(event: CollegeEvent) = dao.deleteCollegeEvent(event)

    // Tasks CRUD
    suspend fun insertTask(task: Task) = dao.insertTask(task)
    suspend fun updateTask(task: Task) = dao.updateTask(task)
    suspend fun deleteTask(task: Task) = dao.deleteTask(task)

    // Seed database if empty
    suspend fun checkAndSeedDatabase() {
        val noticeCount = allNotices.first().size
        if (noticeCount == 0) {
            Log.d("AppRepository", "Database is empty. Seeding default collegiate files...")
            val now = System.currentTimeMillis()

            // 1. Seed Notices
            insertNotice(Notice(
                title = "📌 Urgent: Mid-Semester Syllabus and Exam Dates",
                body = "The final mid-semester examinations for internal assessment are scheduled starting on July 10th. All students must submit their pending lab files and practical records to their respective mentors. Check the notice board tab for details on room allotments and dynamic seat plans.",
                category = "Academics",
                timestamp = now - 2 * 3600 * 1000,
                isPinned = true
            ))
            insertNotice(Notice(
                title = "🚀 Google Winter Internship Drive 2026 Open!",
                body = "Google is hiring Software Engineering Interns (SWE) for winter 2026. Submit your resume in PDF format in the Document Vault before June 28th. Recommended topics: Distributed systems, Data structures, Algorithms, and Object-Oriented Design. Minimum CGPA required is 8.0.",
                category = "Career",
                timestamp = now - 12 * 3600 * 1000,
                isPinned = true
            ))
            insertNotice(Notice(
                title = "🏆 HackOverflow 2026: Annual Campus Hackathon",
                body = "The Academic Coding Club, in partnership with AWS, is hosting the 36-hour non-stop HackOverflow Hackathon. Registration is open for groups of 2-4. Grand prize: $5,000, plus custom AWS credits. Free stickers, hoodies, and meals for all participants!",
                category = "Events",
                timestamp = now - 24 * 3600 * 1000,
                isPinned = false
            ))
            insertNotice(Notice(
                title = "🏦 Remit College Fees - Semester 5 Portal Open",
                body = "Students are requested to pay the balance installment of academic tuition fees for Semester 5 before July 5th. Overdue notices will trigger standard registration locks on course portals.",
                category = "General",
                timestamp = now - 48 * 3600 * 1000,
                isPinned = false
            ))

            // 2. Seed Assignments
            insertAssignment(Assignment(
                title = "Distributed Systems Project (RPC Server)",
                courseName = "Distributed Systems (CS401)",
                dueDate = now + 4 * 24 * 3600 * 1000,
                isCompleted = false,
                points = 100
            ))
            insertAssignment(Assignment(
                title = "Lexical Analyzer Coding Exercise",
                courseName = "Compiler Design (CS309)",
                dueDate = now + 1 * 24 * 3600 * 1000,
                isCompleted = true,
                points = 50
            ))
            insertAssignment(Assignment(
                title = "API Endpoint Testing Reports",
                courseName = "Software Engineering (CS312)",
                dueDate = now + 8 * 24 * 3600 * 1000,
                isCompleted = false,
                points = 80
            ))

            // 3. Seed Exams
            insertExam(Exam(
                subject = "Compiler Design Theory",
                examDate = now + 5 * 24 * 3600 * 1000,
                type = "Midterm",
                notes = "Focus on parser states: LR(0), SLR(1), clr(1), lalr(1) action-goto tables."
            ))
            insertExam(Exam(
                subject = "Software Security Practicals",
                examDate = now + 12 * 24 * 3600 * 1000,
                type = "Final",
                notes = "Implement buffer overflow exploits, SQLi mitigation filters."
            ))

            // 4. Seed Attendance
            insertAttendance(Attendance(
                courseName = "Distributed Systems (CS401)",
                attended = 18,
                conducted = 20,
                minPercentage = 75
            ))
            insertAttendance(Attendance(
                courseName = "Compiler Design (CS309)",
                attended = 13,
                conducted = 22, // 59% - Shortage!
                minPercentage = 75
            ))
            insertAttendance(Attendance(
                courseName = "Software Engineering (CS312)",
                attended = 23,
                conducted = 24,
                minPercentage = 75
            ))

            // 5. Seed Financial Reminders
            insertFinancialReminder(FinancialReminder(
                title = "Semester 5 Tuition Fee Due",
                type = "Fee",
                amount = 2450.0,
                dueDate = now + 12 * 24 * 3600 * 1000,
                isDone = false
            ))
            insertFinancialReminder(FinancialReminder(
                title = "State Scholarship Renewal Grant",
                type = "Scholarship",
                amount = 1200.0,
                dueDate = now + 24 * 24 * 3600 * 1000,
                isDone = false
            ))

            // 6. Seed Career Opportunities
            insertCareerOpportunity(CareerOpportunity(
                title = "Software Engineering Intern",
                companyOrOrg = "Google",
                type = "Internship",
                details = "Hiring Software Engineering Interns (SWE) for winter 2026. Requires strong data structures, algorithms, system design.",
                location = "Mountain View, CA (Hybrid)",
                deadline = now + 14 * 24 * 3600 * 1000,
                status = "Applied",
                url = "https://careers.google.com"
            ))
            insertCareerOpportunity(CareerOpportunity(
                title = "Associate Product Manager",
                companyOrOrg = "Uber",
                type = "Placement",
                details = "Full-Time PM role. Looking for analytical thinkers, experience building products, UI design intuition.",
                location = "San Francisco, CA (Onsite)",
                deadline = now + 18 * 24 * 3600 * 1000,
                status = "Wishlist",
                url = "https://careers.uber.com"
            ))
            insertCareerOpportunity(CareerOpportunity(
                title = "HackOverflow 2026",
                companyOrOrg = "AWS & Academic Club",
                type = "Hackathon",
                details = "Collegiate 36h hackathon. Teams build modern fullstack web/mobile applications targeting educational efficiency.",
                location = "Campus Tech auditorium",
                deadline = now + 4 * 24 * 3600 * 1000,
                status = "Open"
            ))

            // 7. Seed Documents
            insertDocument(Document(
                name = "My Principal Resume 2026",
                category = "Resume",
                notes = "Has latest Kotlin Android Multiplatform project listed and AWS Cloud practitioner badge.",
                imageUrl = "resume_stub"
            ))
            insertDocument(Document(
                name = "Official Semester 4 Transcript",
                category = "Marksheet",
                notes = "Issued by Registrar office, CGPA 9.1.",
                imageUrl = "transcript_stub"
            ))

            // 8. Seed Projects
            insertProject(Project(
                title = "Distributed Multi-Agent IoT Router",
                techStack = "Kotlin, Ktor, WebSockets, Redis, Docker",
                description = "Horizontal scaling message multiplexer driving dynamic configuration updates in field controllers.",
                githubLink = "https://github.com/student/iot-router",
                portfolioFeatured = true
            ))

            // 9. Seed College Events
            insertCollegeEvent(CollegeEvent(
                title = "Cloud Infrastructure & Serverless Tech",
                description = "Invited lecture series by Lead Solutions Architect from Amazon Web Systems.",
                organizer = "Tech Student Chapter",
                eventDate = now + 3 * 24 * 3600 * 1000,
                venue = "Seminar Hall C",
                rsvped = true
            ))

            // 10. Seed Tasks
            insertTask(Task(
                title = "Collect signature from HOD for internship NOC",
                category = "Career",
                dueDate = now + 2 * 24 * 3600 * 1000,
                isCompleted = false
            ))
            insertTask(Task(
                title = "Review group compiler project presentation slides",
                category = "Academic",
                dueDate = now + 1 * 24 * 3600 * 1000,
                isCompleted = true
            ))
        }
    }
}

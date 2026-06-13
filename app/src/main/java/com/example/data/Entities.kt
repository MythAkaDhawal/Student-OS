package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notices")
data class Notice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val category: String, // "Academics", "Career", "Placement", "Events", "General"
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val imageUrl: String? = null
) : Serializable

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val courseName: String,
    val dueDate: Long,
    val isCompleted: Boolean = false,
    val points: Int = 100
) : Serializable

@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val examDate: Long,
    val type: String, // "Midterm", "Final", "Quiz", "Registration"
    val notes: String = ""
) : Serializable

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseName: String,
    val attended: Int,
    val conducted: Int,
    val minPercentage: Int = 75
) : Serializable {
    val percentage: Float
        get() = if (conducted > 0) (attended.toFloat() / conducted * 100) else 0f
    
    val statusText: String
        get() = if (percentage >= minPercentage) "Safe" else "Shortage"
}

@Entity(tableName = "financial_reminders")
data class FinancialReminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val type: String, // "Fee", "Scholarship"
    val amount: Double,
    val dueDate: Long,
    val isDone: Boolean = false
) : Serializable

@Entity(tableName = "career_opportunities")
data class CareerOpportunity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, // e.g. "Software Engineering Intern", "HackOverflow 2026"
    val companyOrOrg: String, // e.g. "Google", "ACM Chapter"
    val type: String, // "Internship", "Placement", "Hackathon", "Competition"
    val details: String,
    val location: String = "Remote",
    val deadline: Long,
    val status: String = "Open", // "Open", "Wishlist", "Applied", "Interviewing", "Offered", "Rejected"
    val url: String? = null
) : Serializable

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // "Resume", "Certificate", "ID Document", "Marksheet"
    val notes: String = "",
    val imageUrl: String? = null, // Simulated representation or selected drawable res
    val uploadedTimestamp: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val techStack: String,
    val description: String,
    val githubLink: String = "",
    val portfolioFeatured: Boolean = false
) : Serializable

@Entity(tableName = "college_events")
data class CollegeEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val organizer: String,
    val eventDate: Long,
    val venue: String,
    val rsvped: Boolean = false
) : Serializable

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Personal", "Academic", "Career"
    val dueDate: Long,
    val isCompleted: Boolean = false
) : Serializable

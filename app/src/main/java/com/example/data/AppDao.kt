package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- Notice Queries ---
    @Query("SELECT * FROM notices ORDER BY isPinned DESC, timestamp DESC")
    fun getAllNotices(): Flow<List<Notice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: Notice)

    @Update
    suspend fun updateNotice(notice: Notice)

    @Delete
    suspend fun deleteNotice(notice: Notice)

    // --- Assignment Queries ---
    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    // --- Exam Queries ---
    @Query("SELECT * FROM exams ORDER BY examDate ASC")
    fun getAllExams(): Flow<List<Exam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam)

    @Update
    suspend fun updateExam(exam: Exam)

    @Delete
    suspend fun deleteExam(exam: Exam)

    // --- Attendance Queries ---
    @Query("SELECT * FROM attendance ORDER BY courseName ASC")
    fun getAllAttendance(): Flow<List<Attendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Update
    suspend fun updateAttendance(attendance: Attendance)

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)

    // --- Financial Reminder Queries ---
    @Query("SELECT * FROM financial_reminders ORDER BY dueDate ASC")
    fun getAllFinancialReminders(): Flow<List<FinancialReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinancialReminder(reminder: FinancialReminder)

    @Update
    suspend fun updateFinancialReminder(reminder: FinancialReminder)

    @Delete
    suspend fun deleteFinancialReminder(reminder: FinancialReminder)

    // --- Career Opportunity Queries ---
    @Query("SELECT * FROM career_opportunities ORDER BY deadline ASC")
    fun getAllCareerOpportunities(): Flow<List<CareerOpportunity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareerOpportunity(opportunity: CareerOpportunity)

    @Update
    suspend fun updateCareerOpportunity(opportunity: CareerOpportunity)

    @Delete
    suspend fun deleteCareerOpportunity(opportunity: CareerOpportunity)

    // --- Document Queries ---
    @Query("SELECT * FROM documents ORDER BY uploadedTimestamp DESC")
    fun getAllDocuments(): Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)

    @Update
    suspend fun updateDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)

    // --- Project Queries ---
    @Query("SELECT * FROM projects ORDER BY portfolioFeatured DESC, title ASC")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    // --- College Event Queries ---
    @Query("SELECT * FROM college_events ORDER BY eventDate ASC")
    fun getAllCollegeEvents(): Flow<List<CollegeEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollegeEvent(event: CollegeEvent)

    @Update
    suspend fun updateCollegeEvent(event: CollegeEvent)

    @Delete
    suspend fun deleteCollegeEvent(event: CollegeEvent)

    // --- Task Queries ---
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}

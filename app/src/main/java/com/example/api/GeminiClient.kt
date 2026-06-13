package com.example.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// --- Moshi Request & Response Classes ---

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String? = null,
    @Json(name = "inlineData") val inlineData: InlineData? = null
)

@JsonClass(generateAdapter = true)
data class InlineData(
    @Json(name = "mimeType") val mimeType: String,
    @Json(name = "data") val data: String
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class ResponseFormatText(
    @Json(name = "mimeType") val mimeType: String
)

@JsonClass(generateAdapter = true)
data class ResponseFormat(
    @Json(name = "type") val type: String = "OBJECT", // optional
    @Json(name = "text") val text: ResponseFormatText? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @Json(name = "responseMimeType") val responseMimeType: String? = null,
    @Json(name = "temperature") val temperature: Float? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "generationConfig") val generationConfig: GenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content?
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>?
)

// --- Extracted Structured Data Classes ---

@JsonClass(generateAdapter = true)
data class ExtractedAssignment(
    val title: String,
    val courseName: String,
    val daysFromNow: Int
)

@JsonClass(generateAdapter = true)
data class ExtractedTask(
    val title: String,
    val category: String, // "Personal", "Academic", "Career"
    val daysFromNow: Int
)

@JsonClass(generateAdapter = true)
data class ExtractedExam(
    val subject: String,
    val type: String, // "Midterm", "Final", "Quiz", "Registration"
    val notes: String = "",
    val daysFromNow: Int
)

@JsonClass(generateAdapter = true)
data class ExtractedNotice(
    val title: String,
    val body: String,
    val category: String // "Academics", "Career", "Placement", "Events", "General"
)

@JsonClass(generateAdapter = true)
data class ExtractionResult(
    val assignments: List<ExtractedAssignment>? = null,
    val tasks: List<ExtractedTask>? = null,
    val exams: List<ExtractedExam>? = null,
    val notices: List<ExtractedNotice>? = null
)

// --- Retrofit API Service ---

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    val jsonParserMoshi: Moshi = moshi
}

// --- Extraction Wrapper ---

object GeminiExtractor {
    private val systemInstructionContent = Content(
        parts = listOf(
            Part(text = """
                You are Student OS AI, a brilliant university notice parser. Your job is to extract important tasks, deadlines, exams, assignments, or notice files from raw inputs (including emails, text snippets, screenshots, or PDF dumps).
                
                You MUST identify:
                - Assignments: Title, corresponding Course Name, and days from now it is due.
                - Tasks: Title, custom category ("Academic", "Career", "Personal"), and days from now it is due.
                - Exams: Subject Name, exam type ("Midterm", "Final", "Quiz", "Registration"), special notes, and days from now it is scheduled.
                - Notices: Title (summarized), body (refined/polished detail), and category appropriate for the central notice board.
                
                Always compute 'daysFromNow' relative to today. If no date is mentioned but a task is implied, assume daysFromNow is 1. If it mentions a day, solve relative to the current context (assume today is Saturday June 13, 2026).
                
                Return a strictly compliant JSON object mapping to the following schema:
                {
                   "assignments": [{"title": "Title", "courseName": "Course", "daysFromNow": 3}],
                   "tasks": [{"title": "Title", "category": "Academic", "daysFromNow": 1}],
                   "exams": [{"subject": "Subject", "type": "Midterm", "notes": "notes", "daysFromNow": 5}],
                   "notices": [{"title": "Notice Title", "body": "Clean Notice Body", "category": "Academics"}]
                }
                
                Do NOT output any markdown tags like ```json or any trailing descriptions. Output ONLY the raw JSON string.
            """.trimIndent())
        )
    )

    suspend fun extractCollegiateItems(textInput: String, imageBase64: String? = null): ExtractionResult {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey == "MY_GEMINI_API_KEY" || apiKey.isBlank()) {
            throw IllegalStateException("API Key is missing or default. Please configure GEMINI_API_KEY in the Secrets panel.")
        }

        val promptParts = mutableListOf<Part>()
        promptParts.add(Part(text = "Please parse this raw college circular/communication and extract important actions:\n\n$textInput"))
        
        if (imageBase64 != null) {
            promptParts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = imageBase64)))
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = promptParts)),
            generationConfig = GenerationConfig(
                responseMimeType = "application/json",
                temperature = 0.2f
            ),
            systemInstruction = systemInstructionContent
        )

        val response = RetrofitClient.service.generateContent(apiKey, request)
        val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("No extraction text returned from Gemini")

        // Parse extracted structured JSON using Moshi
        val adapter = RetrofitClient.jsonParserMoshi.adapter(ExtractionResult::class.java)
        return adapter.fromJson(responseText) ?: throw Exception("Failed to deserialize structured extraction JSON")
    }
}

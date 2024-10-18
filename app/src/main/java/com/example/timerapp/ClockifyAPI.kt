package com.example.timerapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ClockifyAPI {

    const val API_KEY = "your-clockify-api"
    const val WORKSPACE_ID = "your-workspace-id"

    // Updated function to fetch projects and pass start and end time
    fun fetchProjectsAndSubmitTime(context: Context, startTime: Long, endTime: Long) {
        val apiUrl = "https://api.clockify.me/api/v1/workspaces/$WORKSPACE_ID/projects"
        val request = Request.Builder()
            .url(apiUrl)
            .header("X-Api-Key", API_KEY)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val projects = parseProjects(responseData)
                    (context as Activity).runOnUiThread {
                        showProjectSelectionDialog(context, projects, startTime, endTime)
                    }
                }
            }
        })
    }

    // Parsing the projects (no change here)
    private fun parseProjects(responseData: String?): List<Project> {
        val projectList = mutableListOf<Project>()
        responseData?.let {
            val jsonArray = JSONArray(it)
            for (i in 0 until jsonArray.length()) {
                val projectObject = jsonArray.getJSONObject(i)
                val project = Project(
                    projectObject.getString("id"),
                    projectObject.getString("name")
                )
                projectList.add(project)
            }
        }
        return projectList
    }

    // Updated to pass both start and end times
    private fun showProjectSelectionDialog(context: Context, projects: List<Project>, startTime: Long, endTime: Long) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Project")
        builder.setItems(projects.map { it.name }.toTypedArray()) { _, which ->
            val selectedProject = projects[which]
            submitTimeEntry(selectedProject.id, startTime, endTime)
        }
        builder.show()
    }

    // Updated to submit start and end time, not duration
    private fun submitTimeEntry(projectId: String, startTime: Long, endTime: Long) {
        val apiUrl = "https://api.clockify.me/api/v1/workspaces/$WORKSPACE_ID/time-entries"

        // Format start and end time in ISO 8601 format
        val startISOTime = getISOTimeFromEpoch(startTime)
        val endISOTime = getISOTimeFromEpoch(endTime)

        // Build the time entry JSON payload
        val timeEntryJson = """
            {
                "start": "$startISOTime",
                "end": "$endISOTime",
                "billable": true,
                "projectId": "$projectId",
                "description": "Timer entry"
            }
        """.trimIndent()

        val requestBody = timeEntryJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(apiUrl)
            .header("X-Api-Key", API_KEY)
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Handle success
                }
            }
        })
    }

    // Helper function to convert epoch time to ISO 8601 format (UTC)
    private fun getISOTimeFromEpoch(epochTime: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(epochTime))
    }
}

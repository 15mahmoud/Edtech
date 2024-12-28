package com.example.student_project.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SearchResult(
    val imgId: Int,
    val mentorName: String,
    val jopTitle: String,
    val university: String,
    val rating: Double,
    val availability: List<String>,
    val degreeAndCertificate: String,
    val timeSlots: List<String>,
    val experience: String,
    val hourlyRate: Double,
)

// object will be send from filter to filter result
@Parcelize
data class FiltrationRequest(
    val jopTitle: String,
    val rating: Double,
    val hourlyRate: Double,
) : Parcelable

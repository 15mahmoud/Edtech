package com.example.student_project.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "student")
data class User(
    @PrimaryKey @SerializedName("_id") val id: String,
    val accountType: String,
    val active: Boolean,
    val approved: Boolean,
    val courses: List<String>,
    val createdAt: String,
    // i add additional details
    //how to save it in room
    @Embedded
    val additionalDetails:AdditionalDetails,
    val email: String,
    var firstName: String,
    val image: String,
    var lastName: String,
    val token: String,
    val updatedAt: String,
)

data class AdditionalDetails(
    @SerializedName("_id")
    val idForAdditionalDetails : String,
    var contactNumber: Int?,
    val dateOfBirth: String?,
    var about: String?,
    var gender : String?
)
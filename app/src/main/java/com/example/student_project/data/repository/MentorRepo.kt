package com.example.student_project.data.repository

import com.example.student_project.data.component.Mentor

class MentorRepo {
    companion object {
        val mentorList =
            listOf(
                Mentor(
                    "1",
                    image = "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    mentorName = "Ramadan",
                    jopTitle = "ARCH 117",
                    university = "Oxford",
                    rating = 4.5,
                    availability = listOf("Saturday", "Wednesday", "Friday"),
                    degreeAndCertificate = "Bachelor's in Applied Mathematics",
                    timeSlots = listOf("Evening"),
                    experience = "3-6 Years",
                    hourlyRate = 20.0,
                ),
                Mentor(
                    "2",
                    image = "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    mentorName = "Eleanor Pena",
                    jopTitle = "MATH 116",
                    university = "Kobenhavens",
                    rating = 5.0,
                    availability = listOf("Saturday", "Sunday", "Friday"),
                    degreeAndCertificate = "Master's in Applied Mathematics",
                    timeSlots = listOf("Morning"),
                    experience = "1-3 Years",
                    hourlyRate = 30.0,
                ),
                Mentor(
                    "3",
                    image = "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    mentorName = "Robert Fox",
                    jopTitle = "MATH 116",
                    university = "Oxford",
                    rating = 4.5,
                    availability = listOf("Saturday", "Wednesday", "Friday"),
                    degreeAndCertificate = "Bachelor's in Applied Mathematics",
                    timeSlots = listOf("Evening"),
                    experience = "3-6 Years",
                    hourlyRate = 30.0,
                ),
                Mentor(
                    "4",
                    image = "https://i.redd.it/spgt1hclj2cd1.jpeg",
                    mentorName = "Ihab",
                    jopTitle = "ARCH 117",
                    university = "Oxford",
                    rating = 4.5,
                    availability = listOf("Saturday", "Wednesday", "Friday"),
                    degreeAndCertificate = "Bachelor's in Applied Mathematics",
                    timeSlots = listOf("Evening"),
                    experience = "3-6 Years",
                    hourlyRate = 10.0,
                ),
            )
        val topLiveMentorList =
            listOf(
                Mentor(
                    id = "1",
                    image =
                        "/home/moaz/AndroidStudioProjects/StudentProject/images/search_result_img1.png",
                    mentorName = "Mahmoud",
                    jopTitle = "ARCH 117",
                    university = "Oxford",
                    rating = 4.5,
                    availability = listOf("Saturday", "Wednesday", "Friday"),
                    degreeAndCertificate = "Bachelor's in Applied Mathematics",
                    timeSlots = listOf("Evening"),
                    experience = "1-3 Years",
                    hourlyRate = 20.0,
                )
            )
    }

    suspend fun getMentorList(): List<Mentor> {
        return mentorList
    }

    // we will add this to home screen later
    suspend fun getTopLiveMentorList(): List<Mentor> {
        return topLiveMentorList
    }
}

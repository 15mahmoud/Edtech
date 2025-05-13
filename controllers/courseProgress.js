const mongoose = require("mongoose")
const Section = require("../models/section")
const SubSection = require("../models/subSection")
const CourseProgress = require("../models/courseProgress")
const { convertSecondsToDuration } = require("../utils/secToDuration");

// ================ update Course Progress ================
exports.updateCourseProgress = async (req, res) => {
  const { courseId, subsectionId } = req.body
  const userId = req.user.id

  try {
    // Check if the subsection is valid
    const subsection = await SubSection.findById(subsectionId)
    if (!subsection) {
      return res.status(404).json({ error: "Invalid subsection" })
    }

    // Find the course progress document for the user and course
    let courseProgress = await CourseProgress.findOne({
      courseID: courseId,
      userId: userId,
    })

    if (!courseProgress) {
      // If course progress doesn't exist, create a new one
      return res.status(404).json({
        success: false,
        message: "Course progress Does Not Exist",
      })
    } else {
      // If course progress exists, check if the subsection is already completed
      if (courseProgress.completedVideos.includes(subsectionId)) {
        return res.status(400).json({ error: "Subsection already completed" })
      }

      // Push the subsection into the completedVideos array
      courseProgress.completedVideos.push(subsectionId)
    }

    // Save the updated course progress
    await courseProgress.save()

    return res.status(200).json({ data: "Course progress updated" })
  }
  catch (error) {
    console.error(error)
    return res.status(500).json({ error: "Internal server error" })
  }
}



// ================ get Progress Percentage ================
exports.getProgressPercentage = async (req, res) => {
  const { courseId } = req.body;
  const userId = req.user.id;

  if (!courseId) {
    return res.status(400).json({ error: "Course ID not provided." });
  }

  try {
    // Find the course progress document for the user and course
    let courseProgress = await CourseProgress.findOne({
      courseID: courseId,
      userId: userId,
    })
      .populate({
        path: "courseID",
        populate: {
          path: "courseContent",
        },
      })
      .exec();

    if (!courseProgress) {
      return res
        .status(400)
        .json({ error: "Can not find Course Progress with these IDs." });
    }

    console.log(courseProgress, userId);

    let totalVideos = 0;
    courseProgress.courseID.courseContent?.forEach((sec) => {
      totalVideos += sec.subSection.length || 0;
    });

    let progressPercentage =
      (courseProgress.completedVideos.length / totalVideos) * 100;

    // To make it up to 2 decimal points
    const multiplier = Math.pow(10, 2);
    progressPercentage =
      Math.round(progressPercentage * multiplier) / multiplier;

    return res.status(200).json({
      data: {
        totalLessons: totalVideos,
        completedLessons: courseProgress.completedVideos.length,
        progressPercentage: progressPercentage,
      },
      message: "Successfully fetched Course progress",
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ error: "Internal server error" });
  }
};







exports.getAllCoursesProgress = async (req, res) => {
  const userId = req.user.id;

  try {
    let coursesProgress = await CourseProgress.find({ userId })
      .populate({
        path: "courseID",
        populate: {
          path: "courseContent",
          populate: {
            path: "subSection",
            select: "timeDuration", // جلب الوقت فقط لتقليل البيانات
          },
        },
      })
      .exec();

    if (!coursesProgress || coursesProgress.length === 0) {
      return res
        .status(400)
        .json({ error: "No course progress found for this user." });
    }

    let progressData = coursesProgress.map((courseProgress) => {
      let totalVideos = 0;
      let totalDurationInSeconds = 0;

      courseProgress.courseID.courseContent?.forEach((sec) => {
        totalVideos += sec.subSection.length || 0;
        sec.subSection.forEach((subSec) => {
        
          totalDurationInSeconds += parseInt(subSec.timeDuration, 10) || 0;
        });
      });

      let completedVideos = courseProgress.completedVideos.length;
      let progressPercentage =
        totalVideos > 0 ? (completedVideos / totalVideos) * 100 : 0;

      // تقريب النسبة المئوية إلى منزلتين عشريتين
      progressPercentage = Math.round(progressPercentage * 100) / 100;

      // تحويل الثواني إلى صيغة مناسبة
      const totalDuration = convertSecondsToDuration(totalDurationInSeconds);

      return {
        _id: courseProgress.courseID._id,
        courseName: courseProgress.courseID.courseName,
        totalLessons: totalVideos,
        completedLessons: completedVideos,
        progressPercentage: progressPercentage,
        totalDuration: totalDuration,
        thumbnail: courseProgress.courseID.thumbnail,
      };
    });

    return res.status(200).json({
      data: progressData,
      message: "Successfully fetched all courses progress",
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ error: "Internal server error" });
  }
};





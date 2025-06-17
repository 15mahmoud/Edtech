

const paypal = require("@paypal/checkout-server-sdk");
const instance = require("../config/paypal");
const mailSender = require("../utils/mailSender");
require("dotenv").config();

const User = require("../models/user");
const Course = require("../models/course");
const CourseProgress = require("../models/courseProgress");

const { default: mongoose } = require("mongoose");






exports.capturePayment = async (req, res) => {
  const { coursesId } = req.body;
  const userId = req.user.id;

  if (coursesId.length === 0) {
    return res.json({ success: false, message: "Please provide Course Id" });
  }

  let totalAmount = 0;

  for (const course_id of coursesId) {
    let course;
    try {
      course = await Course.findById(course_id);
      if (!course) {
        return res
          .status(404)
          .json({ success: false, message: "Could not find the course" });
      }

      const uid = new mongoose.Types.ObjectId(userId);
      if (course.studentsEnrolled.includes(uid)) {
        return res
          .status(400)
          .json({ success: false, message: "Student is already Enrolled" });
      }

      totalAmount += course.price;
    } catch (error) {
      console.log(error);
      return res.status(500).json({ success: false, message: error.message });
    }
  }

  try {
    for (const course_id of coursesId) {
      const course = await Course.findById(course_id);

      await Course.findByIdAndUpdate(course_id, {
        $push: { studentsEnrolled: userId },
      });

      const courseProgress = await CourseProgress.create({
        courseID: course_id,
        userId: userId,
        completedVideos: [],
      });

      await User.findByIdAndUpdate(userId, {
        $push: { courses: course_id, courseProgress: courseProgress._id },
      });
    }

    return res.status(200).json({
      success: true,
      data: "Student enrolled successfully without payment",
    });
  } catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "Error while enrolling student" });
  }
};

exports.verifyPayment = async (req, res) => {
  const { coursesId } = req.body;
  const userId = req.user.id;

  if (!coursesId || !userId) {
    return res
      .status(400)
      .json({ success: false, message: "Please provide all necessary data" });
  }

  try {
    for (const courseId of coursesId) {
      const course = await Course.findById(courseId);
      if (!course) {
        return res.status(404).json({
          success: false,
          message: `Course with ID ${courseId} not found`,
        });
      }

      
      const isEnrolled = course.studentsEnrolled.some(
        (id) => id.toString() === userId
      );
      if (!isEnrolled) {
        return res.status(200).json({ success: true, data: false });
      }
    }

    return res.status(200).json({ success: true, data: true });
  } catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "Error while verifying payment" });
  }
};

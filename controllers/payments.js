// const paypal = require("@paypal/checkout-server-sdk");
// const instance = require("../config/paypal");
// const crypto = require("crypto");
// const mailSender = require("../utils/mailSender");
// const {
//   courseEnrollmentEmail,
// } = require("../mail/templates/courseEnrollmentEmail");
// require("dotenv").config();

// const User = require("../models/user");
// const Course = require("../models/course");
// const CourseProgress = require("../models/courseProgress");

// const { default: mongoose } = require("mongoose");

// // ================ capture the payment and initiate the PayPal order ================
// exports.capturePayment = async (req, res) => {
//   const { coursesId } = req.body;
//   const userId = req.user.id;

//   if (coursesId.length === 0) {
//     return res.json({ success: false, message: "Please provide Course Id" });
//   }

//   let totalAmount = 0;

//   for (const course_id of coursesId) {
//     let course;
//     try {
//       course = await Course.findById(course_id);
//       if (!course) {
//         return res
//           .status(404)
//           .json({ success: false, message: "Could not find the course" });
//       }

//       const uid = new mongoose.Types.ObjectId(userId);
//       if (course.studentsEnrolled.includes(uid)) {
//         return res
//           .status(400)
//           .json({ success: false, message: "Student is already Enrolled" });
//       }

//       totalAmount += course.price;
//     } catch (error) {
//       console.log(error);
//       return res.status(500).json({ success: false, message: error.message });
//     }
//   }

//   // Create PayPal order
//   const currency = "USD"; // Use your desired currency
//   const options = {
//     intent: "CAPTURE",
//     purchase_units: [
//       {
//         amount: {
//           currency_code: currency,
//           value: totalAmount,
//         },
//       },
//     ],
//   };

//   // Initiate payment using PayPal
//   try {
//     const paymentResponse = await instance.instance.orders.create(options);
//     const approvalUrl = paymentResponse.links.find(
//       (link) => link.rel === "approve"
//     ).href;

//     // Return the approval URL for the user to approve the payment
//     res.status(200).json({
//       success: true,
//       message: "Payment order created",
//       approvalUrl,
//     });
//   } catch (error) {
//     console.log(error);
//     return res
//       .status(500)
//       .json({ success: false, message: "Could not Initiate Order" });
//   }
// };

// // ================ verify the payment ================
// exports.verifyPayment = async (req, res) => {
//   const { paypal_order_id, paypal_payment_id, coursesId } = req.body;
//   const userId = req.user.id;

//   if (!paypal_order_id || !paypal_payment_id || !coursesId || !userId) {
//     return res
//       .status(400)
//       .json({ success: false, message: "Payment Failed, data not found" });
//   }

//   // Verify PayPal payment
//   try {
//     const request = new paypal.orders.OrdersCaptureRequest(paypal_order_id);
//     const captureResponse = await instance.instance.execute(request);

//     if (captureResponse.result.status === "COMPLETED") {
//       // Enroll student after payment verification
//       await enrollStudents(coursesId, userId, res);
//       return res
//         .status(200)
//         .json({ success: true, message: "Payment Verified" });
//     } else {
//       return res
//         .status(400)
//         .json({ success: false, message: "Payment not completed" });
//     }
//   } catch (error) {
//     console.log(error);
//     return res
//       .status(500)
//       .json({ success: false, message: "Could not verify payment" });
//   }
// };

// // ================ enroll students after payment ================
// const enrollStudents = async (courses, userId, res) => {
//   if (!courses || !userId) {
//     return res
//       .status(400)
//       .json({
//         success: false,
//         message: "Please Provide data for Courses or UserId",
//       });
//   }

//   for (const courseId of courses) {
//     try {
//       const enrolledCourse = await Course.findOneAndUpdate(
//         { _id: courseId },
//         { $push: { studentsEnrolled: userId } },
//         { new: true }
//       );

//       if (!enrolledCourse) {
//         return res
//           .status(500)
//           .json({ success: false, message: "Course not Found" });
//       }

//       const courseProgress = await CourseProgress.create({
//         courseID: courseId,
//         userId: userId,
//         completedVideos: [],
//       });

//       const enrolledStudent = await User.findByIdAndUpdate(
//         userId,
//         {
//           $push: {
//             courses: courseId,
//             courseProgress: courseProgress._id,
//           },
//         },
//         { new: true }
//       );

//       const emailResponse = await mailSender(
//         enrolledStudent.email,
//         `Successfully Enrolled into ${enrolledCourse.courseName}`,
//         courseEnrollmentEmail(
//           enrolledCourse.courseName,
//           `${enrolledStudent.firstName}`
//         )
//       );
//     } catch (error) {
//       console.log(error);
//       return res.status(500).json({ success: false, message: error.message });
//     }
//   }
// };

// exports.sendPaymentSuccessEmail = async (req, res) => {
//   const { orderId, paymentId, amount } = req.body;
//   const userId = req.user.id;

//   if (!orderId || !paymentId || !amount || !userId) {
//     return res
//       .status(400)
//       .json({ success: false, message: "Please provide all the fields" });
//   }

//   try {
//     const enrolledStudent = await User.findById(userId);
//     await mailSender(
//       enrolledStudent.email,
//       `Payment Received`,
//       paymentSuccessEmail(
//         `${enrolledStudent.firstName}`,
//         amount,
//         orderId,
//         paymentId
//       )
//     );
//   } catch (error) {
//     console.log("Error in sending email", error);
//     return res
//       .status(500)
//       .json({ success: false, message: "Could not send email" });
//   }
// };

const paypal = require("@paypal/checkout-server-sdk");
const instance = require("../config/paypal");
const mailSender = require("../utils/mailSender");
const {
  courseEnrollmentEmail,
} = require("../mail/templates/courseEnrollmentEmail");
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
      message: "Student enrolled successfully without payment",
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

      await Course.findByIdAndUpdate(courseId, {
        $push: { studentsEnrolled: userId },
      });

      const courseProgress = await CourseProgress.create({
        courseID: courseId,
        userId: userId,
        completedVideos: [],
      });

      await User.findByIdAndUpdate(userId, {
        $push: { courses: courseId, courseProgress: courseProgress._id },
      });
    }

    return res.status(200).json({
      success: true,
      message: "Student enrolled successfully without payment verification",
    });
  } catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "Error while verifying payment" });
  }
};

const User = require('../models/user')
const Course = require('../models/course')
const RatingAndReview = require('../models/ratingAndReview')
const mongoose = require('mongoose');

// ================ Create Rating ================

exports.createRating = async (req, res) => {
  try {
    const { rating, review, courseId } = req.body;
    const userId = req.user.id;

    if (!rating || !review || !courseId) {
      return res.status(401).json({
        success: false,
        message: "All fields are required",
      });
    }

    // ✅ تحويل rating إلى Number لضمان عدم حدوث أخطاء في الحساب
    const numericRating = Number(rating);
    if (isNaN(numericRating) || numericRating < 1 || numericRating > 5) {
      return res.status(400).json({
        success: false,
        message: "Invalid rating value. Rating must be between 1 and 5.",
      });
    }

    const courseDetails = await Course.findOne({
      _id: courseId,
      studentsEnrolled: userId,
    });

    if (!courseDetails) {
      return res.status(404).json({
        success: false,
        message: "Student is not enrolled in the course",
      });
    }

    const alreadyReviewed = await RatingAndReview.findOne({
      course: courseId,
      user: userId,
    });

    if (alreadyReviewed) {
      return res.status(403).json({
        success: false,
        message: "Course is already reviewed by the user",
      });
    }

    const ratingReview = await RatingAndReview.create({
      user: userId,
      course: courseId,
      rating: numericRating, // ✅ حفظ الرقم بعد التحويل
      review,
    });

    await Course.findByIdAndUpdate(courseId, {
      $push: { ratingAndReviews: ratingReview._id },
    });

    // ✅ تحديث التقييم المتوسط ليكون صحيحاً
    const result = await RatingAndReview.aggregate([
      { $match: { course: new mongoose.Types.ObjectId(courseId) } },
      { $group: { _id: null, averageRating: { $avg: "$rating" } } },
    ]);

    const newAverageRating = result.length > 0 ? result[0].averageRating : 0;

    await Course.findByIdAndUpdate(courseId, {
      averageRating: newAverageRating,
    });

    return res.status(200).json({
      success: true,
      data: {
        ...ratingReview.toObject(),
        newAverageRating,
      },
      
      message: "Rating and Review created Successfully",
    });
  } catch (error) {
    console.error("Error while creating rating and review", error);
    return res.status(500).json({
      success: false,
      error: error.message,
      message: "Error while creating rating and review",
    });
  }
};
// exports.createRating = async (req, res) => {
//   try {
//     // get data
//     const { rating, review, courseId } = req.body;
//     const userId = req.user.id;

//     // validation
//     if (!rating || !review || !courseId) {
//       return res.status(401).json({
//         success: false,
//         message: "All fields are required",
//       });
//     }

//     // check user is enrolled in course
//     const courseDetails = await Course.findOne({
//       _id: courseId,
//       studentsEnrolled: userId,
//     });

//     if (!courseDetails) {
//       return res.status(404).json({
//         success: false,
//         message: "Student is not enrolled in the course",
//       });
//     }

//     // check if user already reviewed
//     const alreadyReviewed = await RatingAndReview.findOne({
//       course: courseId,
//       user: userId,
//     });

//     if (alreadyReviewed) {
//       return res.status(403).json({
//         success: false,
//         message: "Course is already reviewed by the user",
//       });
//     }

//     // create rating entry in DB
//     const ratingReview = await RatingAndReview.create({
//       user: userId,
//       course: courseId,
//       rating,
//       review,
//     });

//     // link this rating to course
//     await Course.findByIdAndUpdate(courseId, {
//       $push: { ratingAndReviews: ratingReview._id },
//     });

//     // update course average rating
//     const result = await RatingAndReview.aggregate([
//       { $match: { course: new mongoose.Types.ObjectId(courseId) } },
//       { $group: { _id: null, averageRating: { $avg: "$rating" } } },
//     ]);

//     let newAverageRating = 0; // Default value
//     if (result.length > 0 && result[0].averageRating) {
//       newAverageRating = result[0].averageRating;
//     }

//     // Update the course with the new average rating
//     await Course.findByIdAndUpdate(courseId, {
//       averageRating: newAverageRating,
//     });

//     return res.status(200).json({
//       success: true,
//       data: ratingReview,
//       averageRating: newAverageRating, // Return the updated average rating
//       message: "Rating and Review created Successfully",
//     });
//   } catch (error) {
//     console.error("Error while creating rating and review", error);
//     return res.status(500).json({
//       success: false,
//       error: error.message,
//       message: "Error while creating rating and review",
//     });
//   }
// };









// ================ Get Average Rating ================

exports.getAverageRating = async (req, res) => {
  try {
    const courseId = req.body.courseId;

    const result = await RatingAndReview.aggregate([
      {
        $match: {
          course: new mongoose.Types.ObjectId(courseId),
        },
      },
      {
        $group: {
          _id: null,
          averageRating: { $avg: "$rating" },
        },
      },
    ]);

    return res.status(200).json({
      success: true,
      averageRating: result.length > 0 ? result[0].averageRating : 0,
      message:
        result.length > 0
          ? ""
          : "Average Rating is 0, no valid ratings given till now",
    });
  } catch (error) {
    console.log(error);
    return res.status(500).json({
      success: false,
      message: error.message,
    });
  }
};

// exports.getAverageRating = async (req, res) => {
//     try {
//             //get course ID
//             const courseId = req.body.courseId;
//             //calculate avg rating

//             const result = await RatingAndReview.aggregate([
//                 {
//                     $match:{
//                         course: new mongoose.Types.ObjectId(courseId),
//                     },
//                 },
//                 {
//                     $group:{
//                         _id:null,
//                         averageRating: { $avg: "$rating"},
//                     }
//                 }
//             ])

//             //return rating
//             if(result.length > 0) {

//                 return res.status(200).json({
//                     success:true,
//                     averageRating: result[0].averageRating,
//                 })

//             }
            
//             //if no rating/Review exist
//             return res.status(200).json({
//                 success:true,
//                 message:'Average Rating is 0, no ratings given till now',
//                 averageRating:0,
//             })
//     }
//     catch(error) {
//         console.log(error);
//         return res.status(500).json({
//             success:false,
//             message:error.message,
//         })
//     }
// }





// ================ Get All Rating And Reviews ================
exports.getAllRatingReview = async(req, res)=>{
    try{
        const allReviews = await RatingAndReview.find({})
        .sort({rating:'desc'})
        .populate({
            path:'user',
            select:'firstName lastName email image'
        })
        .populate({
            path:'course',
            select:'courseName'
        })
        .exec();

        return res.status(200).json({
            success:true,
            data:allReviews,
            message:"All reviews fetched successfully"
        });
    }
    catch(error){
        console.log('Error while fetching all ratings');
        console.log(error);
        return res.status(500).json({
            success: false,
            error: error.message,
            message: 'Error while fetching all ratings',
        })
    }
}

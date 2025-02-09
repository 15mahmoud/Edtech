const Course = require('../models/course');
const User = require('../models/user');
const Category = require('../models/category');
const Section = require('../models/section')
const SubSection = require('../models/subSection')
const CourseProgress = require('../models/courseProgress')

const { uploadImageToCloudinary, deleteResourceFromCloudinary } = require('../utils/imageUploader');
const { convertSecondsToDuration } = require("../utils/secToDuration")



// ================ create new course ================


exports.createCourse = async (req, res) => {
    try {
        let { courseName, courseDescription, whatYouWillLearn, price, category, instructions: _instructions, status, tag: _tag } = req.body;
        const tag = JSON.parse(_tag);
        const instructions = JSON.parse(_instructions);

       
        const thumbnail = req.files?.thumbnailImage;
        if (!thumbnail) {
            return res.status(400).json({
                success: false,
                message: "Thumbnail image is required",
            });
        }

        if (!courseName || !courseDescription || !whatYouWillLearn || !price || !category || !instructions.length || !tag.length) {
            return res.status(400).json({
                success: false,
                message: "All fields are required",
            });
        }

        if (!status || status === undefined) {
            status = "Draft";
        }

        
        const instructorId = req.user.id;

      
        const categoryDetails = await Category.findById(category);
        if (!categoryDetails) {
            return res.status(401).json({
                success: false,
                message: "Category Details not found",
            });
        }

     
        const thumbnailDetails = await uploadImageToCloudinary(thumbnail, process.env.FOLDER_NAME);

        const newCourse = await Course.create({
            courseName,
            courseDescription,
            instructor: instructorId,
            whatYouWillLearn,
            price,
            category: categoryDetails._id,
            tag,
            status,
            instructions,
            thumbnail: thumbnailDetails.secure_url, 
            createdAt: Date.now(),
        });

        
        await User.findByIdAndUpdate(
            instructorId,
            { $push: { courses: newCourse._id } },
            { new: true }
        );

        
        await Category.findByIdAndUpdate(
            { _id: category },
            { $push: { courses: newCourse._id } },
            { new: true }
        );

      
        res.status(200).json({
            success: true,
            data: newCourse,
            message: "New Course created successfully",
        });

    } catch (error) {
        console.log("Error while creating new course", error);
        res.status(500).json({
            success: false,
            error: error.message,
            message: "Error while creating new course",
        });
    }
};

// ================ Show All Courses================

// exports.getAllCourses = async (req, res) => {
//   try {
//     const allCourses = await Course.aggregate([
//       {
//         $lookup: {
//           from: "ratingandreviews", // تأكد من صحة اسم المجموعة
//           localField: "_id",
//           foreignField: "course",
//           as: "ratings",
//         },
//       },
//       {
//         $addFields: {
//           averageRating: { $avg: "$ratings.rating" }, // حساب متوسط التقييم
//         },
//       },
//       {
//         $project: {
//           courseName: 1,
//           courseDescription: 1,
//           price: 1,
//           thumbnail: 1,
//           instructor: 1,
//           studentsEnrolled: 1,
//           averageRating: { $ifNull: ["$averageRating", 0] }, // إذا لم يوجد تقييم، يكون 0
//         },
//       },
//     ]);

//     // **populate instructor** (لجلب بيانات المدرب بعد التجميع)
//     await Course.populate(allCourses, {
//       path: "instructor",
//       select: "firstName lastName email image",
//     });

//     return res.status(200).json({
//       success: true,
//       data: allCourses,
//       message: "Data for all courses fetched successfully",
//     });
//   } catch (error) {
//     console.log("Error while fetching data of all courses", error);
//     res.status(500).json({
//       success: false,
//       error: error.message,
//       message: "Error while fetching data of all courses",
//     });
//   }
// };


exports.getAllCourses = async (req, res) => {
    try {
        const allCourses = await Course.find(
          {},
          {
            courseName: 1,
            courseDescription: 1,
            price: 1,
            thumbnail: 1,
            instructor: 1,
          // ratingAndReviews: 1,
            studentsEnrolled: 1
          }
        )
          .populate({
            path: "instructor",
            select: "firstName lastName email image",
          })
          .exec();

        return res.status(200).json({
            success: true,
            data: allCourses,
            message: "Data for all courses fetched successfully"
        });

    } catch (error) {
        console.log("Error while fetching data of all courses", error);
        res.status(500).json({
            success: false,
            error: error.message,
            message: "Error while fetching data of all courses",
        });
    }
};





// ================ Get Course Details ================


exports.getCourseDetails = async (req, res) => {
    try {
        // get course ID
        const { courseId } = req.body;

        // find course details
        const courseDetails = await Course.findOne({
            _id: courseId,
        })
            .populate({
                path: "instructor",
                populate: {
                    path: "additionalDetails",
                },
            })
            .populate("category")
            .populate("ratingAndReviews")

            .populate({
                path: "courseContent",
                populate: {
                    path: "subSection",
                    select: "-videoUrl",
                },
            })
            .exec()


        //validation
        if (!courseDetails) {
            return res.status(400).json({
                success: false,
                message: `Could not find the course with ${courseId}`,
            });
        }

        // if (courseDetails.status === "Draft") {
        //   return res.status(403).json({
        //     success: false,
        //     message: `Accessing a draft course is forbidden`,
        //   });
        // }

        // console.log('courseDetails -> ', courseDetails)
        let totalDurationInSeconds = 0
        courseDetails.courseContent.forEach((content) => {
            content.subSection.forEach((subSection) => {
                const timeDurationInSeconds = parseInt(subSection.timeDuration)
                totalDurationInSeconds += timeDurationInSeconds
            })
        })

        const totalDuration = convertSecondsToDuration(totalDurationInSeconds)

        //return response
        return res.status(200).json({
          success: true,
          data: {
            ...courseDetails.toObject(),
            totalDuration,
          },
          message: "Fetched course data successfully",
        });
    }

    catch (error) {
        console.log('Error while fetching course details');
        console.log(error);
        return res.status(500).json({
            success: false,
            error: error.message,
            message: 'Error while fetching course details',
        });
    }
}


// ================ Get Full Course Details ================



exports.getFullCourseDetails = async (req, res) => {
  try {
    const { courseId } = req.body;
    const userId = req.user.id;

    const courseDetails = await Course.findOne({ _id: courseId })
      .populate({
        path: "instructor",
        populate: { path: "additionalDetails" },
      })
      .populate("category")
      .populate("ratingAndReviews")
      .populate({
        path: "courseContent",
        populate: { path: "subSection" },
      })
      .exec();

    if (!courseDetails) {
      return res.status(404).json({
        success: false,
        message: `Could not find course with id: ${courseId}`,
      });
    }

    const courseProgressCount = await CourseProgress.findOne({
      courseID: courseId,
      userId: userId,
    });

    // حساب المدة الإجمالية
    let totalDurationInSeconds = 0;
    courseDetails.courseContent.forEach((content) => {
      content.subSection.forEach((subSection) => {
        totalDurationInSeconds += parseInt(subSection.timeDuration);
      });
    });

    const totalDuration = convertSecondsToDuration(totalDurationInSeconds);

    return res.status(200).json({
      success: true,
      data: {
        ...courseDetails.toObject(), // تحويل بيانات الكورس إلى كائن عادي بدون courseDetails
        totalDuration,
        completedVideos: courseProgressCount?.completedVideos || [],
      },
      message: "Fetched course data successfully",
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: error.message,
    });
  }
};



// ================ Edit Course Details ================
exports.editCourse = async (req, res) => {
    try {
        const { courseId } = req.body
        const updates = req.body
        const course = await Course.findById(courseId)

        if (!course) {
            return res.status(404).json({ error: "Course not found" })
        }

        // If Thumbnail Image is found, update it
        if (req.files) {
            // console.log("thumbnail update")
            const thumbnail = req.files.thumbnailImage
            const thumbnailImage = await uploadImageToCloudinary(
                thumbnail,
                process.env.FOLDER_NAME
            )
            course.thumbnail = thumbnailImage.secure_url
        }

        // Update only the fields that are present in the request body
        for (const key in updates) {
            if (updates.hasOwnProperty(key)) {
                if (key === "tag" || key === "instructions") {
                    course[key] = JSON.parse(updates[key])
                } else {
                    course[key] = updates[key]
                }
            }
        }

        // updatedAt
        course.updatedAt = Date.now();

        //   save data
        await course.save()

        const updatedCourse = await Course.findOne({
            _id: courseId,
        })
            .populate({
                path: "instructor",
                populate: {
                    path: "additionalDetails",
                },
            })
            .populate("category")
            .populate("ratingAndReviews")
            .populate({
                path: "courseContent",
                populate: {
                    path: "subSection",
                },
            })
            .exec()

        // success response
        res.status(200).json({
            success: true,
            message: "Course updated successfully",
            data: updatedCourse,
        })
    } catch (error) {
        console.error(error)
        res.status(500).json({
            success: false,
            message: "Error while updating course",
            error: error.message,
        })
    }
}



// ================ Get a list of Course for a given Instructor ================
exports.getInstructorCourses = async (req, res) => {
    try {
        // Get the instructor ID from the authenticated user or request body
        const instructorId = req.user.id

        // Find all courses belonging to the instructor
        const instructorCourses = await Course.find({ instructor: instructorId, }).sort({ createdAt: -1 })


        // Return the instructor's courses
        res.status(200).json({
            success: true,
            data: instructorCourses,
            // totalDurationInSeconds:totalDurationInSeconds,
            message: 'Courses made by Instructor fetched successfully'
        })
    } catch (error) {
        console.error(error)
        res.status(500).json({
            success: false,
            message: "Failed to retrieve instructor courses",
            error: error.message,
        })
    }
}



// ================ Delete the Course ================
exports.deleteCourse = async (req, res) => {
    try {
        const { courseId } = req.body

        // Find the course
        const course = await Course.findById(courseId)
        if (!course) {
            return res.status(404).json({ message: "Course not found" })
        }

        // Unenroll students from the course
        const studentsEnrolled = course.studentsEnrolled
        for (const studentId of studentsEnrolled) {
            await User.findByIdAndUpdate(studentId, {
                $pull: { courses: courseId },
            })
        }

        // delete course thumbnail From Cloudinary
        await deleteResourceFromCloudinary(course?.thumbnail);

        // Delete sections and sub-sections
        const courseSections = course.courseContent
        for (const sectionId of courseSections) {
            // Delete sub-sections of the section
            const section = await Section.findById(sectionId)
            if (section) {
                const subSections = section.subSection
                for (const subSectionId of subSections) {
                    const subSection = await SubSection.findById(subSectionId)
                    if (subSection) {
                        await deleteResourceFromCloudinary(subSection.videoUrl) // delete course videos From Cloudinary
                    }
                    await SubSection.findByIdAndDelete(subSectionId)
                }
            }

            // Delete the section
            await Section.findByIdAndDelete(sectionId)
        }

        // Delete the course
        await Course.findByIdAndDelete(courseId)

        return res.status(200).json({
            success: true,
            message: "Course deleted successfully",
        })

    } catch (error) {
        console.error(error)
        return res.status(500).json({
            success: false,
            message: "Error while Deleting course",
            error: error.message,
        })
    }
}





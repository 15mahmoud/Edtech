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


// exports.getAllCourses = async (req, res) => {
//     try {
//         const allCourses = await Course.find(
//           {},
//           {
//             courseName: 1,
//             courseDescription: 1,
//             price: 1,
//             thumbnail: 1,
//             instructor: 1,
//           // ratingAndReviews: 1,
//             studentsEnrolled: 1
//           }
//         )
//           .populate({
//             path: "instructor",
//             select: "firstName lastName email image",
//           })
//           .exec();

//         return res.status(200).json({
//             success: true,
//             data: allCourses,
//             message: "Data for all courses fetched successfully"
//         });

//     } catch (error) {
//         console.log("Error while fetching data of all courses", error);
//         res.status(500).json({
//             success: false,
//             error: error.message,
//             message: "Error while fetching data of all courses",
//         });
//     }
// };
exports.getAllCourses = async (req, res) => {
  try {
    const allCourses = await Course.aggregate([
      {
        $lookup: {
          from: "ratingandreviews",
          localField: "_id",
          foreignField: "course",
          as: "ratings",
        },
      },
      {
        $addFields: {
          averageRating: {
            $cond: {
              if: { $gt: [{ $size: "$ratings" }, 0] },
              then: { $avg: "$ratings.rating" },
              else: 0,
            },
          },
        },
      },
      {
        $lookup: {
          from: "users",
          localField: "instructor",
          foreignField: "_id",
          as: "instructor",
        },
      },
      {
        $unwind: "$instructor",
      },
      //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      {
        $lookup: {
          from: "categories",
          localField: "category",
          foreignField: "_id",
          as: "category",
        },
      },
      {
        $unwind: {
          path: "$category",
          preserveNullAndEmptyArrays: true,
        },
      },
      //........>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      {
        $project: {
          courseName: 1,
          courseDescription: 1,
          price: 1,
          thumbnail: 1,
          studentsEnrolled: 1,
          averageRating: 1,
          //>>>>>>>>>>>>>>>>>>>>>>>
          category: {
            _id: "$category._id",
            name: "$category.name",
            description: "$category.description",
          },
          //>>>>>>>>>>>>>>>>>>>>>>>
          "instructor.firstName": 1,
          "instructor.lastName": 1,
          "instructor.email": 1,
          "instructor.image": 1,
        },
      },
    ]);

    return res.status(200).json({
      success: true,
      data: allCourses,
      message: "Data for all courses fetched successfully",
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
        populate: [
          { path: "additionalDetails" },
          { path: "courses", select: "courseName thumbnail" }, // تعديل هنا لإحضار الكورسات كـ List of Objects
        ],
      })
      // .populate({
      //   path: "instructor",
      //   populate: { path: "additionalDetails" },
      // })
      .populate({
        path: "category",
        select: "-courses",
      })
      .populate({
        path: "ratingAndReviews",
        populate: {
          path: "user",
          select: "firstName lastName image",
        },
      })
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
        ...courseDetails.toObject(), 
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

exports.getInstructorWithCourses = async (req, res) => {
  try {
    const { instructorId } = req.body;

    if (!instructorId) {
      return res.status(400).json({
        success: false,
        message: "Instructor ID is required",
      });
    }

    let instructor = await User.findOne({
      _id: instructorId,
      accountType: "Instructor",
    }).populate("additionalDetails");

    if (!instructor) {
      return res.status(404).json({
        success: false,
        message: "Instructor not found",
      });
    }

    // تحويل المدرب إلى كائن عادي لحذف `courses`
    instructor = instructor.toObject();
    delete instructor.courses; // حذف الكورسات من بيانات المدرب

    // جلب قائمة الكورسات
    const courses = await Course.find({ instructor: instructorId }).select(
      "courseName _id thumbnail"
    );

    // استخراج الطلاب بدون تكرار
    const studentIds = new Set();
    const coursesWithStudents = await Course.find({
      instructor: instructorId,
    }).select("studentsEnrolled");

    coursesWithStudents.forEach((course) => {
      course.studentsEnrolled.forEach((student) => {
        studentIds.add(student.toString());
      });
    });

    // جلب بيانات الطلاب الفريدة
    const uniqueStudents = await User.find({
      _id: { $in: Array.from(studentIds) },
    }).select("firstName lastName email image _id");

    res.status(200).json({
      success: true,
      data: {
        instructor, // المدرب بدون courses
        courses, // قائمة الكورسات
        students: uniqueStudents, // قائمة الطلاب
      },
      message: "Instructor details, courses, and students fetched successfully",
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({
      success: false,
      message: "Failed to retrieve instructor details, courses, and students",
      error: error.message,
    });
  }
};
// exports.getInstructorWithCourses = async (req, res) => {
//   try {
//     const { instructorId } = req.body; // استلام ID من الـ body

//     // التأكد من تمرير معرف المدرب
//     if (!instructorId) {
//       return res.status(400).json({
//         success: false,
//         message: "Instructor ID is required",
//       });
//     }

//     // البحث عن المدرب مع بياناته وملفه الشخصي وكورسات مختصرة
//     const instructor = await User.findOne({
//       _id: instructorId,
//       accountType: "Instructor",
//     })
//       .populate("additionalDetails") // جلب بيانات Profile
//       .populate({
//         path: "courses",
//         select: "courseName _id thumbnail",
//       });

//     // التحقق مما إذا كان المدرب موجودًا
//     if (!instructor) {
//       return res.status(404).json({
//         success: false,
//         message: "Instructor not found",
//       });
//     }

//     // إرجاع بيانات المدرب فقط (الكورسات موجودة داخل `instructor`)
//     res.status(200).json({
//       success: true,
//       data: instructor, // لا نكرر الكورسات في response
//       message: "Instructor details and courses fetched successfully",
//     });
//   } catch (error) {
//     console.error(error);
//     res.status(500).json({
//       success: false,
//       message: "Failed to retrieve instructor details and courses",
//       error: error.message,
//     });
//   }
// };



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





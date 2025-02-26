const Meeting = require("../models/Meeting");
const User = require("../models/user");
const mongoose = require("mongoose");

/**
 *  Fetch all meetings for the current user
 */
const getAllMeetings = async (req, res) => {
  try {
    let data = [];

    if (req.user.accountType === "Instructor") {
      data = await Meeting.find({ host: req.user.id })
        .populate("host", "firstName lastName email image") // ✅ إضافة `image`
        .select("description date url");
    } else if (req.user.accountType === "Student") {
      const userId = new mongoose.Types.ObjectId(req.user.id);
      data = await Meeting.find({ "participants.user": userId })
        .populate("host", "firstName lastName email image") // ✅ إضافة `image`
        .select("description date url");
    }

    // ✅ إضافة `isEnded` لمعرفة هل الاجتماع انتهى أم لا
    const now = new Date();
    data = data.map((meeting) => {
      const meetingEndTime = new Date(meeting.date);
      meetingEndTime.setHours(meetingEndTime.getHours() + 1); // انتهاء بعد ساعة

      return {
        ...meeting._doc,
        isEnded: now > meetingEndTime, // ✅ إذا تجاوز الوقت الحالي نهاية الاجتماع، يكون منتهيًا
      };
    });

    res.status(200).json({ success: true, data });
  } catch (err) {
    console.error("Error fetching meetings:", err);
    res
      .status(500)
      .json({ success: false, message: "Error fetching meetings" });
  }
};

// const getAllMeetings = async (req, res) => {
//   try {
//     let data = [];

//     if (req.user.accountType === "Instructor") {
//       data = await Meeting.find({ host: req.user._id })
//         .populate("host", "firstName lastName email")
//         .select("description date url");
//     } else if (req.user.accountType === "Student") {
//       const userId = new mongoose.Types.ObjectId(req.user.id);
//       data = await Meeting.find({ "participants.user": userId })
//         .populate("host", "firstName lastName email")
//         .select("description date url");
//     }

//     // ✅ إضافة `isEnded` لمعرفة هل الاجتماع انتهى أم لا
//     const now = new Date();
//     data = data.map((meeting) => {
//       const meetingEndTime = new Date(meeting.date);
//       meetingEndTime.setHours(meetingEndTime.getHours() + 1); // انتهاء بعد ساعة

//       return {
//         ...meeting._doc,
//         isEnded: now > meetingEndTime, // ✅ إذا تجاوز الوقت الحالي نهاية الاجتماع، يكون منتهيًا
//       };
//     });

//     res.status(200).json({ success: true, data });
//   } catch (err) {
//     console.error("Error fetching meetings:", err);
//     res
//       .status(500)
//       .json({ success: false, message: "Error fetching meetings" });
//   }
// };



// const getAllMeetings = async (req, res) => {
//   try {
//     let data = [];

//     if (req.user.accountType === "Instructor") {
//       data = await Meeting.find({ host: req.user._id })
//         .populate("host", "firstName lastName email")
//         .select("-participants"); // استبعاد participants
//     } else if (req.user.accountType === "Student") {
//       const userId = new mongoose.Types.ObjectId(req.user.id);
//       data = await Meeting.find({ "participants.user": userId })
//         .populate("host", "firstName lastName email")
//         .select("-participants"); // استبعاد participants
//     }

//     res.status(200).json({ success: true, data });
//   } catch (err) {
//     console.error("Error fetching meetings:", err);
//     res
//       .status(500)
//       .json({ success: false, message: "Error fetching meetings" });
//   }
// };

/**
 *  Create and schedule a new meeting
 */
const createMeeting = async (req, res) => {
  try {
    const { participants, description, date } = req.body;

    // ✅ تأكد من أن `req.user.id` موجود
    if (!req.user || !req.user.id) {
      return res.status(401).json({ success: false, message: "Unauthorized" });
    }

    if (!participants || participants.length < 1 || !description || !date) {
      return res
        .status(400)
        .json({ success: false, message: "All fields are required" });
    }

    const meetingUrl = `https://meet.jit.si/${Math.random()
      .toString(36)
      .substring(7)}`;

    const newMeeting = new Meeting({
      host: req.user.id, // ✅ استخدام `id` بدلًا من `_id`
      participants: participants.map((id) => ({ user: id })),
      description,
      date,
      url: meetingUrl,
    });

    await newMeeting.save();

    const populatedMeeting = await Meeting.findById(newMeeting._id)
      .populate("host", "firstName lastName email image")
      .select("host description date url createdAt updatedAt")
      .lean();

    if (!populatedMeeting || !populatedMeeting.host) {
      return res
        .status(500)
        .json({ success: false, message: "Host data not found in database" });
    }

    res.status(201).json({
      success: true,
      message: "Meeting created",
      data: populatedMeeting,
    });
  } catch (err) {
    console.error("Error creating meeting:", err);
    res.status(500).json({ success: false, message: "Error creating meeting" });
  }
};






// const createMeeting = async (req, res) => {
//   try {
//     const { participants, description, date } = req.body;

//     // التحقق من البيانات المطلوبة
//     if (!participants || participants.length < 1 || !description || !date) {
//       return res
//         .status(400)
//         .json({ success: false, message: "All fields are required" });
//     }

//     // توليد رابط اجتماع Jitsi تلقائيًا
//     const meetingUrl = `https://meet.jit.si/${Math.random()
//       .toString(36)
//       .substring(7)}`;

//     const newMeeting = new Meeting({
//       host: req.user._id,
//       participants: participants.map((id) => ({ user: id })),
//       description,
//       date,
//       url: meetingUrl, // حفظ رابط Jitsi
//     });

//     await newMeeting.save();

//     // إعادة جلب البيانات بعد الحفظ وضبط populate
//     const populatedMeeting = await Meeting.findById(newMeeting._id)
//       .populate("host", "firstName lastName email")
//       .populate("participants.user", "firstName lastName email image");

//     res.status(201).json({
//       success: true,
//       message: "Meeting created",
//       data: populatedMeeting,
//     });
//   } catch (err) {
//     console.error("Error creating meeting:", err);
//     res.status(500).json({ success: false, message: "Error creating meeting" });
//   }
// };


// const createMeeting = async (req, res) => {
//   try {
//     const { participants, description, date, url } = req.body;

//     // Validate required fields
//     if (
//       !participants ||
//       participants.length < 1 ||
//       !description ||
//       !date ||
//       !url
//     ) {
//       return res
//         .status(400)
//         .json({ success: false, message: "All fields are required" });
//     }

//     const newMeeting = new Meeting({
//       host: req.user._id,
//       participants: participants.map((id) => ({ user: id })),
//       description,
//       date,
//       url,
//     });

//     await newMeeting.save();

//     // إعادة جلب البيانات بعد الحفظ وضبط populate
//     const populatedMeeting = await Meeting.findById(newMeeting._id)
//       .populate("host", "firstName lastName email")
//       .populate("participants.user", "firstName lastName email image");

//     res.status(201).json({
//       success: true,
//       message: "Meeting created",
//       data: populatedMeeting,
//     });
//   } catch (err) {
//     console.error("Error creating meeting:", err);
//     res.status(500).json({ success: false, message: "Error creating meeting" });
//   }
// };

/**
 *  Update a meeting
 */
const updateMeeting = async (req, res) => {
  try {
    const { id } = req.params;
    const { participants, date, description, url, minutes } = req.body;

    const meeting = await Meeting.findById(id);
    if (!meeting) {
      return res
        .status(404)
        .json({ success: false, message: "Meeting not found" });
    }

    // Update fields
    meeting.participants = participants
      ? participants.map((id) => ({ user: id }))
      : meeting.participants;
    meeting.date = date || meeting.date;
    meeting.description = description || meeting.description;
    meeting.url = url || meeting.url;
    meeting.minutes = minutes || meeting.minutes;

    await meeting.save();

    // إعادة جلب البيانات بعد الحفظ وضبط populate
    const populatedMeeting = await Meeting.findById(meeting._id)
      .populate("host", "firstName lastName email")
      .populate("participants.user", "firstName lastName email");

    res.status(200).json({
      success: true,
      message: "Meeting updated",
      meeting: populatedMeeting,
    });
  } catch (err) {
    console.error("Error updating meeting:", err);
    res.status(500).json({ success: false, message: "Error updating meeting" });
  }
};

/**
 *  Update meeting minutes
 */
const updateMinutes = async (req, res) => {
  try {
    const { id } = req.params;
    const { minutes } = req.body;

    const meeting = await Meeting.findByIdAndUpdate(
      id,
      { minutes },
      { new: true }
    )
      .populate("host", "firstName lastName email")
      .populate("participants.user", "firstName lastName email");

    if (!meeting) {
      return res
        .status(404)
        .json({ success: false, message: "Meeting not found" });
    }

    res
      .status(200)
      .json({ success: true, message: "Minutes updated", meeting });
  } catch (err) {
    console.error("Error updating minutes:", err);
    res.status(500).json({ success: false, message: "Error updating minutes" });
  }
};

module.exports = {
  createMeeting,
  getAllMeetings,
  updateMeeting,
  updateMinutes,
};

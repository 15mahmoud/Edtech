const Meeting = require("../models/Meeting");
const User = require("../models/user");

/**
 *  Fetch all meetings for the current user
 */
const getAllMeetings = async (req, res) => {
  try {
    let meetings = [];

    // Check user role and fetch relevant meetings
    if (req.user.accountType === "Instructor") {
      meetings = await Meeting.find({ host: req.user._id })
        .populate("host", "firstName lastName email")
        .populate("participants.user", "firstName lastName email");
    } else if (req.user.accountType === "Student") {
      meetings = await Meeting.find({ "participants.user": req.user._id })
        .populate("host", "firstName lastName email")
        .populate("participants.user", "firstName lastName email");
    }

    res.status(200).json({ success: true, meetings });
  } catch (err) {
    console.error("Error fetching meetings:", err);
    res
      .status(500)
      .json({ success: false, message: "Error fetching meetings" });
  }
};

/**
 *  Create and schedule a new meeting
 */
const createMeeting = async (req, res) => {
  try {
    const { participants, description, date, url } = req.body;

    // Validate required fields
    if (
      !participants ||
      participants.length < 1 ||
      !description ||
      !date ||
      !url
    ) {
      return res
        .status(400)
        .json({ success: false, message: "All fields are required" });
    }

    const newMeeting = new Meeting({
      host: req.user._id,
      participants: participants.map((id) => ({ user: id })),
      description,
      date,
      url,
    });

    await newMeeting.save();

    // إعادة جلب البيانات بعد الحفظ وضبط populate
    const populatedMeeting = await Meeting.findById(newMeeting._id)
      .populate("host", "firstName lastName email")
      .populate("participants.user", "firstName lastName email");

    res.status(201).json({
      success: true,
      message: "Meeting created",
      meeting: populatedMeeting,
    });
  } catch (err) {
    console.error("Error creating meeting:", err);
    res.status(500).json({ success: false, message: "Error creating meeting" });
  }
};

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

    res
      .status(200)
      .json({
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

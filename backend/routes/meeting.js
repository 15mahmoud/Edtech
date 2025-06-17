const express = require("express");
const router = express.Router();
const { auth ,isInstructor } = require("../middleware/auth");
const {
  createMeeting,
  getAllMeetings,
  updateMeeting,
  updateMinutes,
} = require("../controllers/meeting");


router.post("/create", auth,isInstructor, createMeeting);


router.get("/all", auth, getAllMeetings);




router.put("/update/:id", auth, updateMeeting);


router.patch("/update-minutes/:id", auth, updateMinutes);

module.exports = router;

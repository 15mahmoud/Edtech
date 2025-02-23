const express = require("express");
const router = express.Router();
const { auth ,isInstructor } = require("../middleware/auth");
const {
  createMeeting,
  getAllMeetings,
  updateMeeting,
  updateMinutes,
} = require("../controllers/meeting");

// إنشاء اجتماع جديد
router.post("/create", auth,isInstructor, createMeeting);

// جلب جميع الاجتماعات الخاصة بالمستخدم
router.get("/all", auth, getAllMeetings);



// تحديث تفاصيل الاجتماع
router.put("/update/:id", auth, updateMeeting);

// تحديث ملاحظات الاجتماع (Minutes)
router.patch("/update-minutes/:id", auth, updateMinutes);

module.exports = router;

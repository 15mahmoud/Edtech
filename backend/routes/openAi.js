const express = require("express");
const {
  sendMessage,
  getConversation,
} = require("../controllers/openAi.js");
const {
  auth,
  isAdmin,
  isInstructor,
  isStudent,
} = require("../middleware/auth");

const router = express.Router();

router.post("/chat",auth, sendMessage);
router.get("/chat",auth, getConversation);

module.exports = router;

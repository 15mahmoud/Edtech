const express = require("express");
const router = express.Router();
const chatController = require("../controllers/chat");
const { auth } = require("../middleware/auth");


router.post("/createChat", auth, chatController.createNewChat);


router.get("/getAllChats", auth, chatController.fetchAllChats);


router.post("/sendMessage", auth, chatController.sendMessage);


router.post("/getMessages", auth, chatController.fetchMessages);

module.exports = router;

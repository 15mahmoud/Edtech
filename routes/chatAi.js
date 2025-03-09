const express = require("express");
const router = express.Router();
const chatAiController = require("../controllers/chatAi");

router.post("/ask-jarvis", chatAiController.askJarvis);
router.post("/voice-to-text", chatAiController.voiceToText);
router.post("/text-to-speech", chatAiController.textToSpeech);
router.post("/classify", chatAiController.classifyText);
router.post("/summarize", chatAiController.summarizeText);
router.post("/translate", chatAiController.translateText);
router.get("/chatAi", chatAiController.getUserChat);


module.exports = router;

const axios = require("axios");
const ChatAi = require("../models/chatAi");
const User = require("../models/user");

const JARVIS_API_BASE_URL = "http://localhost:5000"; // عدّل العنوان حسب تشغيلك للسيرفر
const API_KEY = "AIzaSyCcjS62p0XxsXYuQfzfDB8Pyd2d-hvlLcc";

// 🟢 إرسال رسالة إلى Jarvis AI وإدارة المحادثات
const askJarvis = async (req, res) => {
  try {
    const { message, userId } = req.body;

    // التأكد من وجود المستخدم
    const user = await User.findById(userId);
    if (!user)
      return res
        .status(404)
        .json({ success: false, message: "User not found" });

    // البحث عن محادثة المستخدم
    let chat = await ChatAi.findOne({ user: userId });
    if (!chat) chat = new ChatAi({ user: userId, messages: [] });

    // إرسال الطلب إلى Jarvis API
    const response = await axios.post(
      `${JARVIS_API_BASE_URL}/conversation`,
      {
        message,
      },
      { headers: { "X-API-Key": API_KEY } }
    );

    // حفظ الرسالة في قاعدة البيانات
    chat.messages.push({ sender: "user", text: message });
    chat.messages.push({ sender: "ai", text: response.data.response });

    await chat.save();

    return res.json({
      success: true,
      response: response.data.response,
      history: chat.messages,
    });
  } catch (error) {
    console.error("Error:", error);
    return res
      .status(500)
      .json({ success: false, message: "Error contacting Jarvis AI" });
  }
};

// 🟢 تحويل الصوت إلى نص باستخدام `/voice-to-text`
const voiceToText = async (req, res) => {
  try {
    const { userId } = req.body;
    const audioFile = req.file; // تأكد من استخدام multer لرفع الملفات

    const response = await axios.post(
      `${JARVIS_API_BASE_URL}/voice-to-text`,
      audioFile,
      {
        headers: {
          "X-API-Key": API_KEY,
          "Content-Type": "multipart/form-data",
        },
      }
    );

    let chat = await ChatAi.findOne({ user: userId });
    if (!chat) chat = new ChatAi({ user: userId, messages: [] });

    chat.messages.push({ sender: "user", text: response.data.transcript });

    await chat.save();

    return res.json({ success: true, transcript: response.data.transcript });
  } catch (error) {
    console.error("Error in voice-to-text:", error);
    return res
      .status(500)
      .json({ success: false, message: "Error processing voice" });
  }
};

// 🟢 تحويل النص إلى صوت باستخدام `/text-to-speech`
const textToSpeech = async (req, res) => {
  try {
    const { text, userId } = req.body;

    const response = await axios.post(
      `${JARVIS_API_BASE_URL}/text-to-speech`,
      { text },
      {
        headers: { "X-API-Key": API_KEY },
      }
    );

    let chat = await ChatAi.findOne({ user: userId });
    if (!chat) chat = new ChatAi({ user: userId, messages: [] });

    chat.messages.push({ sender: "ai", text, audioUrl: response.data });

    await chat.save();

    return res.json({ success: true, audioFile: response.data });
  } catch (error) {
    console.error("Error in text-to-speech:", error);
    return res
      .status(500)
      .json({ success: false, message: "Error generating speech" });
  }
};

// 🟢 تصنيف النصوص باستخدام `/classify`
const classifyText = async (req, res) => {
  try {
    const { text } = req.body;

    const response = await axios.post(
      `${JARVIS_API_BASE_URL}/classify`,
      { text },
      {
        headers: { "X-API-Key": API_KEY },
      }
    );

    return res.json({
      success: true,
      classification: response.data.classification,
    });
  } catch (error) {
    console.error("Error in classify:", error);
    return res
      .status(500)
      .json({ success: false, message: "Error classifying text" });
  }
};

// 🟢 تلخيص النصوص باستخدام `/summarize`
const summarizeText = async (req, res) => {
  try {
    const { text } = req.body;

    const response = await axios.post(
      `${JARVIS_API_BASE_URL}/summarize`,
      { text },
      {
        headers: { "X-API-Key": API_KEY },
      }
    );

    return res.json({ success: true, summary: response.data.summary });
  } catch (error) {
    console.error("Error in summarize:", error);
    return res
      .status(500)
      .json({ success: false, message: "Error summarizing text" });
  }
};

// 🟢 ترجمة النصوص باستخدام `/translate`
const translateText = async (req, res) => {
  try {
    const { text, targetLanguage } = req.body;

    const response = await axios.post(
      `${JARVIS_API_BASE_URL}/translate`,
      { text, targetLanguage },
      {
        headers: { "X-API-Key": API_KEY },
      }
    );

    return res.json({ success: true, translation: response.data.translation });
  } catch (error) {
    console.error("Error in translate:", error);
    return res
      .status(500)
      .json({ success: false, message: "Error translating text" });
  }
};



const getUserChat = async (req, res) => {
  try {
    const { userId } = req.body;

    // التحقق من وجود المستخدم
    const user = await User.findById(userId);
    if (!user)
      return res
        .status(404)
        .json({ success: false, message: "User not found" });

    // جلب محادثات المستخدم
    const chat = await ChatAi.findOne({ user: userId });

    if (!chat) {
      return res.json({ success: true, messages: [] }); // إذا لم يكن هناك محادثات، يرجع مصفوفة فارغة
    }

    return res.json({ success: true, messages: chat.messages });
  } catch (error) {
    console.error("Error fetching chat:", error);
    return res
      .status(500)
      .json({ success: false, message: "Error retrieving chat" });
  }
};

module.exports = {
  askJarvis,
  voiceToText,
  textToSpeech,
  classifyText,
  summarizeText,
  translateText,
  getUserChat,
};

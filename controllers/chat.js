const Chat = require("../models/chat");
const Message = require("../models/message");
const mongoose = require("mongoose");
const User = require("../models/user"); 

/**
 * @name createNewChat
 * @Desc إنشاء محادثة جديدة بين المستخدمين
 */
exports.createNewChat = async (req, res) => {
  try {
    let { participants } = req.body;

    if (!participants || participants.length < 2) {
      return res.status(400).json({
        success: false,
        message: "At least two participants are required",
      });
    }

    // تحويل المعرفات إلى ObjectId
    participants = participants.map((id) => ({
      user: new mongoose.Types.ObjectId(id),
    }));

    const chatExists = await Chat.findOne({
      "users.user": { $all: participants.map((p) => p.user) },
    });

    if (chatExists) {
      return res.status(409).json({
        success: false,
        message: "Chat already exists",
        chat: chatExists,
      });
    }

    const newChat = new Chat({ users: participants });
    await newChat.save();
    await newChat.populate("users.user", "firstName lastName email");

    return res.status(200).json({
      success: true,
      message: "Chat created successfully",
      chat: newChat,
    });
  } catch (err) {
    console.error("Error creating chat:", err);
    return res
      .status(500)
      .json({ success: false, message: "Failed to create chat" });
  }
};

/**
 * @name fetchAllChats
 * @Desc جلب جميع المحادثات الخاصة بالمستخدم
 */
exports.fetchAllChats = async (req, res) => {
  try {
    const chats = await Chat.find({ "users.user": req.user._id })
      .populate("users.user", "firstName lastName email")
      .populate("latestMessage")
      .sort({ updatedAt: -1 });

    return res
      .status(200)
      .json({ success: true, message: "Chats fetched successfully", chats });
  } catch (err) {
    console.error("Error fetching chats:", err);
    return res
      .status(500)
      .json({ success: false, message: "Failed to fetch chats" });
  }
};

/**
 * @name sendMessage
 * @Desc إرسال رسالة داخل محادثة
 */
exports.sendMessage = async (req, res) => {
  try {
    const { chatId, content, senderId } = req.body; // إضافة senderId من الطلب بدلاً من req.user

    if (!chatId || !content || !senderId) {
      // التأكد من وجود senderId
      return res
        .status(400)
        .json({
          success: false,
          message: "Chat ID, content, and sender ID are required",
        });
    }

    const chat = await Chat.findById(chatId);
    if (!chat) {
      return res
        .status(404)
        .json({ success: false, message: "Chat not found" });
    }

    // البحث عن المستخدم باستخدام senderId بدلاً من req.user._id
    const sender = await User.findById(senderId);
    if (!sender) {
      return res
        .status(404)
        .json({ success: false, message: "Sender not found" });
    }

    const message = new Message({
      sender: sender._id, // استخدم sender._id بدلاً من req.user._id
      chat: chatId,
      content,
    });

    await message.save();
    await message.populate("sender", "firstName lastName email");

    // تحديث الرسالة الأخيرة في المحادثة
    chat.latestMessage = message._id;
    await chat.save();

    return res
      .status(200)
      .json({ success: true, message: "Message sent successfully", message });
  } catch (err) {
    console.error("Error sending message:", err);
    return res
      .status(500)
      .json({ success: false, message: "Failed to send message" });
  }
};

/**
 * @name fetchMessages
 * @Desc جلب جميع الرسائل لمحادثة معينة
 */
exports.fetchMessages = async (req, res) => {
  try {
    const { chatId } = req.params;

    if (!chatId) {
      return res
        .status(400)
        .json({ success: false, message: "Chat ID is required" });
    }

    const messages = await Message.find({ chat: chatId })
      .populate("sender", "firstName lastName email")
      .sort({ createdAt: 1 });

    return res.status(200).json({
      success: true,
      message: "Messages fetched successfully",
      messages,
    });
  } catch (err) {
    console.error("Error fetching messages:", err);
    return res
      .status(500)
      .json({ success: false, message: "Failed to fetch messages" });
  }
};


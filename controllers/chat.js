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
    const userId = req.user.id; // المستخدم الحالي من التوكن
    const { participant } = req.body; // المستخدم الآخر

    if (!participant) {
      return res.status(400).json({
        success: false,
        message: "Participant ID is required",
      });
    }

    const participantId = new mongoose.Types.ObjectId(participant);
    const currentUserId = new mongoose.Types.ObjectId(userId);

    // التأكد من أن الشات غير موجود مسبقًا
    const chatExists = await Chat.findOne({
      users: { $all: [currentUserId, participantId] },
    }).populate("users", "firstName lastName email");

    if (chatExists) {
      return res.status(409).json({
        success: false,
        message: "Chat already exists",
        data: chatExists,
      });
    }

    // إنشاء شات جديد بين المستخدمين
    const newChat = new Chat({ users: [currentUserId, participantId] });
    await newChat.save();
    await newChat.populate("users", "firstName lastName email");

    return res.status(201).json({
      success: true,
      message: "Chat created successfully",
      data: newChat,
    });
  } catch (err) {
    console.error("Error creating chat:", err);
    return res.status(500).json({
      success: false,
      message: "Failed to create chat",
    });
  }
};

// exports.createNewChat = async (req, res) => {
//   try {
//     let { participants } = req.body;

//     if (!participants || participants.length < 2) {
//       return res.status(400).json({
//         success: false,
//         message: "At least two participants are required",
//       });
//     }

//     // تحويل المعرفات إلى ObjectId
//     participants = participants.map((id) => ({
//       user: new mongoose.Types.ObjectId(id),
//     }));

//     const chatExists = await Chat.findOne({
//       "users.user": { $all: participants.map((p) => p.user) },
//     });

//     if (chatExists) {
//       return res.status(409).json({
//         success: false,
//         message: "Chat already exists",
//         chat: chatExists,
//       });
//     }

//     const newChat = new Chat({ users: participants });
//     await newChat.save();
//     await newChat.populate("users.user", "firstName lastName email");

//     return res.status(200).json({
//       success: true,
//       message: "Chat created successfully",
//       chat: newChat,
//     });
//   } catch (err) {
//     console.error("Error creating chat:", err);
//     return res
//       .status(500)
//       .json({ success: false, message: "Failed to create chat" });
//   }
// };

/**
 * @name fetchAllChats
 * @Desc جلب جميع المحادثات الخاصة بالمستخدم
 */
exports.fetchAllChats = async (req, res) => {
  try {
    const userId = new mongoose.Types.ObjectId(req.user.id);

    const chats = await Chat.find({ users: userId }) // البحث عن الشاتات التي يوجد فيها المستخدم
      .populate("users", "firstName lastName email image") // إحضار بيانات جميع المستخدمين
      .populate({
        path: "latestMessage",
        populate: { path: "sender", select: "firstName lastName email image" },
      })
      .sort({ updatedAt: -1 });

    // تصفية بيانات المستخدم الآخر فقط لكل شات
    const filteredChats = chats.map((chat) => {
      const otherUser = chat.users.find(
        (user) => user._id.toString() !== userId.toString()
      );
      return {
        _id: chat._id,
        user: otherUser, // المستخدم الآخر فقط
        latestMessage: chat.latestMessage,
        createdAt: chat.createdAt,
        updatedAt: chat.updatedAt,
      };
    });

    return res.status(200).json({
      success: true,
      message: "Chats fetched successfully",
      data: filteredChats,
    });
  } catch (err) {
    console.error("Error fetching chats:", err);
    return res.status(500).json({
      success: false,
      message: "Failed to fetch chats",
    });
  }
};

// exports.fetchAllChats = async (req, res) => {
//   try {
//     const userId = new mongoose.Types.ObjectId(req.user.id); // تحويل الـ ID إلى ObjectId

//     // البحث عن جميع الشاتات التي يكون فيها المستخدم
//     const data = await Chat.find({ users: userId }) // ✅ تعديل الاستعلام ليبحث مباشرة في users
//       .populate("users", "firstName lastName email image") // ✅ إحضار بيانات جميع المستخدمين في الشات
//       .populate({
//         path: "latestMessage",
//         populate: { path: "sender", select: "firstName lastName email image" }, // ✅ جلب بيانات المرسل للرسالة الأخيرة
//       })
//       .sort({ updatedAt: -1 });

//     return res.status(200).json({
//       success: true,
//       message: "Chats fetched successfully",
//       data,
//     });
//   } catch (err) {
//     console.error("Error fetching chats:", err);
//     return res.status(500).json({
//       success: false,
//       message: "Failed to fetch chats",
//     });
//   }
// };

/**
 * @name sendMessage
 * @Desc إرسال رسالة داخل محادثة
 */
exports.sendMessage = async (req, res) => {
  try {
    const { chatId, content } = req.body;
    const senderId = req.user.id; // استخدام الـ ID من التوكن

    if (!chatId || !content) {
      return res.status(400).json({
        success: false,
        message: "Chat ID and content are required",
      });
    }

    // البحث عن الشات والتأكد أن المرسل موجود فيه
    const chat = await Chat.findById(chatId);
    if (!chat) {
      return res
        .status(404)
        .json({ success: false, message: "Chat not found" });
    }

    if (!chat.users.includes(senderId)) {
      return res
        .status(403)
        .json({
          success: false,
          message: "You are not a participant in this chat",
        });
    }

    // إنشاء الرسالة
    const data = new Message({
      sender: senderId,
      chat: chatId,
      content,
    });

    await data.save();
    await data.populate("sender", "firstName lastName email");

    // تحديث آخر رسالة في المحادثة
    chat.latestMessage = data._id;
    await chat.save();

    return res.status(200).json({
      success: true,
      message: "Message sent successfully",
      data,
    });
  } catch (err) {
    console.error("Error sending message:", err);
    return res.status(500).json({
      success: false,
      message: "Failed to send message",
    });
  }
};

// exports.sendMessage = async (req, res) => {
//   try {
//     const { chatId, content, senderId } = req.body; // إضافة senderId من الطلب بدلاً من req.user

//     if (!chatId || !content || !senderId) {
//       // التأكد من وجود senderId
//       return res
//         .status(400)
//         .json({
//           success: false,
//           message: "Chat ID, content, and sender ID are required",
//         });
//     }

//     const chat = await Chat.findById(chatId);
//     if (!chat) {
//       return res
//         .status(404)
//         .json({ success: false, message: "Chat not found" });
//     }

//     // البحث عن المستخدم باستخدام senderId بدلاً من req.user._id
//     const sender = await User.findById(senderId);
//     if (!sender) {
//       return res
//         .status(404)
//         .json({ success: false, message: "Sender not found" });
//     }

//     const message = new Message({
//       sender: sender._id, // استخدم sender._id بدلاً من req.user._id
//       chat: chatId,
//       content,
//     });

//     await message.save();
//     await message.populate("sender", "firstName lastName email");

//     // تحديث الرسالة الأخيرة في المحادثة
//     chat.latestMessage = message._id;
//     await chat.save();

//     return res
//       .status(200)
//       .json({ success: true, message: "Message sent successfully", message });
//   } catch (err) {
//     console.error("Error sending message:", err);
//     return res
//       .status(500)
//       .json({ success: false, message: "Failed to send message" });
//   }
// };

/**
 * @name fetchMessages
 * @Desc جلب جميع الرسائل لمحادثة معينة
 */
exports.fetchMessages = async (req, res) => {
  try {
    const { chatId } = req.body; // أخذ chatId من body

    if (!chatId) {
      return res
        .status(400)
        .json({ success: false, message: "Chat ID is required" });
    }

    const data = await Message.find({ chat: chatId })
      .populate("sender", "firstName lastName email")
      .sort({ createdAt: 1 });

    return res.status(200).json({
      success: true,
      message: "Messages fetched successfully",
      data,
    });
  } catch (err) {
    console.error("Error fetching messages:", err);
    return res
      .status(500)
      .json({ success: false, message: "Failed to fetch messages" });
  }
};
// exports.fetchMessages = async (req, res) => {
//   try {
//     const { chatId } = req.params;

//     if (!chatId) {
//       return res
//         .status(400)
//         .json({ success: false, message: "Chat ID is required" });
//     }

//     const messages = await Message.find({ chat: chatId })
//       .populate("sender", "firstName lastName email")
//       .sort({ createdAt: 1 });

//     return res.status(200).json({
//       success: true,
//       message: "Messages fetched successfully",
//       messages,
//     });
//   } catch (err) {
//     console.error("Error fetching messages:", err);
//     return res
//       .status(500)
//       .json({ success: false, message: "Failed to fetch messages" });
//   }
// };

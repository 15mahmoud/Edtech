const Chat = require("../models/chat");
const Message = require("../models/message");
const mongoose = require("mongoose");
const User = require("../models/user");

exports.createNewChat = async (req, res) => {
  try {
    const userId = req.user.id; 
    const { participant } = req.body; 

    if (!participant) {
      return res.status(400).json({
        success: false,
        message: "Participant ID is required",
      });
    }

    const participantId = new mongoose.Types.ObjectId(participant);
    const currentUserId = new mongoose.Types.ObjectId(userId);

  
    const chatExists = await Chat.findOne({
      users: { $all: [currentUserId, participantId] },
    }).populate("users", "firstName lastName email");

    if (chatExists) {
      //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      return res.status(409).json({
        success: false,
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        message: "Chat already exists",
        data: chatExists,
      });
    }

    
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


exports.fetchAllChats = async (req, res) => {
  try {
    const userId = new mongoose.Types.ObjectId(req.user.id);

    const chats = await Chat.find({ users: userId }) 
      .populate("users", "firstName lastName email image") 
      .populate({
        path: "latestMessage",
        populate: { path: "sender", select: "firstName lastName email image" },
      })
      .sort({ updatedAt: -1 });

    
    const filteredChats = chats.map((chat) => {
      const otherUser = chat.users.find(
        (user) => user._id.toString() !== userId.toString()
      );
      return {
        _id: chat._id,
        user: otherUser, 
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


exports.sendMessage = async (req, res) => {
  try {
    const { chatId, content } = req.body;
    const senderId = req.user.id; 

    if (!chatId || !content) {
      return res.status(400).json({
        success: false,
        message: "Chat ID and content are required",
      });
    }

    
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

    
    const data = new Message({
      sender: senderId,
      chat: chatId,
      content,
    });

    await data.save();
    await data.populate("sender", "firstName lastName email");

    
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


exports.fetchMessages = async (req, res) => {
  try {
    const { chatId } = req.body; 

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


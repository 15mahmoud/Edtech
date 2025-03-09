const mongoose = require("mongoose");

const messageSchema = new mongoose.Schema({
  role: { type: String, enum: ["user", "assistant"], required: true },
  content: { type: String, required: true },
  timestamp: { type: Date, default: Date.now },
});

const conversationSchema = new mongoose.Schema({
  userId: { type: String, required: true }, // يمكن استبداله بـ ObjectId إذا كنت تدير مستخدمين
  messages: [messageSchema], // تخزين الرسائل بداخل كل محادثة
  createdAt: { type: Date, default: Date.now },
});

const Conversation = mongoose.model("Conversation", conversationSchema);

module.exports = Conversation;

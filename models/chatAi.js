const mongoose = require("mongoose");

const chatSchema = new mongoose.Schema(
  {
    user: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true }, // ربط المحادثة بالمستخدم
    messages: [
      {
        sender: { type: String, enum: ["user", "ai"], required: true },
        text: { type: String },
        audioUrl: { type: String }, // لتخزين رابط الصوت إذا كان الرد صوتي
        timestamp: { type: Date, default: Date.now },
      },
    ],
  },
  { timestamps: true }
);

module.exports = mongoose.model("ChatAi", chatSchema);

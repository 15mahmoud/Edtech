const mongoose = require("mongoose");

const chatModel = new mongoose.Schema(
  {
    users: [
      {
        user: { type: mongoose.Schema.Types.ObjectId, ref: "User" },
      },
    ],
    latestMessage: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Message",
    },
  },
  {
    timestamps: true,
  }
);

const Chat = mongoose.model("Chat", chatModel);
module.exports = Chat;

const axios = require("axios");
const Conversation = require("../models/openAi.js");

const OPENROUTER_API_KEY = process.env.OPENROUTER_API_KEY; // تأكد من إضافة API Key في .env

async function chatWithAI(messages) {
  try {
    const response = await axios.post(
      "https://openrouter.ai/api/v1/chat/completions",
      {
        model: "mistralai/mistral-7b-instruct", // يمكنك تغيير النموذج حسب الحاجة
        messages,
      },
      {
        headers: {
          Authorization: `Bearer ${OPENROUTER_API_KEY}`,
          "Content-Type": "application/json",
        },
      }
    );

    return response.data.choices[0].message.content;
  } catch (error) {
    console.error(
      "OpenRouter API Error:",
      error.response?.data || error.message
    );
    throw new Error("Failed to get response from AI.");
  }
}

exports.sendMessage = async (req, res) => {
  try {
    // Extract user ID from the authenticated user (added by the auth middleware)
    const userId = req.user.id;
    const { message } = req.body;

    // Validate input
    if (!message) {
      return res.status(400).json({ error: "Message is required." });
    }

    // Find or create a conversation for the user
    let conversation = await Conversation.findOne({ userId });

    if (!conversation) {
      conversation = new Conversation({ userId, messages: [] });
    }

    // Add user message to the conversation
    conversation.messages.push({ role: "user", content: message });

    // Get AI reply
    const aiReply = await chatWithAI(conversation.messages);
    conversation.messages.push({ role: "assistant", content: aiReply });

    // Save conversation
    await conversation.save();

    res.json({ reply: aiReply, conversation });
  } catch (error) {
    console.error("Error:", error);
    res.status(500).json({
      error: "An error occurred during processing.",
      details: error.message,
    });
  }
};



exports.getConversation = async (req, res) => {
  try {
    // Extract userId from req.user after token verification in `auth` middleware
    const userId = req.user.id;

    // Find the conversation using userId
    const conversation = await Conversation.findOne({ userId });
    if (!conversation) {
      return res.status(404).json({ error: "Conversation not found." });
    }

    res.json(conversation);
  } catch (error) {
    console.error("🔴 Error:", error.message);
    res.status(500).json({ error: "An error occurred while fetching data." });
  }
};

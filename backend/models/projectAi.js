const mongoose = require("mongoose");

const projectAiSchema = new mongoose.Schema(
  {
    repo_url: { type: String, required: true },
    tutorial_files: [
      {
        filename: String,
        content: String,
      },
    ],
    user: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true }, 
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("ProjectAi", projectAiSchema);

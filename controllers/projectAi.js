const axios = require("axios");
const ProjectAi = require("../models/projectAi");

const BASE_AI_URL = "https://code-explainer-xxp3l.ondigitalocean.app";


exports.createProjectAi = async (req, res) => {
  const { repo_url } = req.body;

  if (!repo_url) {
    return res.status(400).json({ error: "repo_url is required" });
  }

  try {
    // Call AI service
    const aiRes = await axios.post(`${BASE_AI_URL}/generate_tutorial/`, {
      repo_url,
    });

    const { tutorial_files } = aiRes.data;

    // Save to DB with user
    const data = await ProjectAi.create({
      repo_url,
      user: req.user.id, // 🔴 ربط المشروع بالمستخدم
      tutorial_files: Object.entries(tutorial_files).map(
        ([filename, content]) => ({
          filename,
          content,
        })
      ),
    });

    res.status(201).json({
      data,
      message: "Project tutorial saved successfully",
    });
  } catch (error) {
    console.error("AI Error:", error.response?.data || error.message);
    res.status(500).json({
      error: "Failed to generate or save tutorial",
      details: error.response?.data || error.message,
    });
  }
};

exports.getAllProjects = async (req, res) => {
  try {
    const userId = req.user.id;

    // 🔴 جلب المشاريع الخاصة بالمستخدم فقط
    const data = await ProjectAi.find({ user: userId }).sort({ createdAt: -1 });

    res.json({ data });
  } catch (error) {
    res.status(500).json({ error: "Failed to fetch projects" });
  }
};

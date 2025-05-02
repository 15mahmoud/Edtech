const express = require("express");
const router = express.Router();

const {auth} = require("../middleware/auth");
const projectAiController = require("../controllers/projectAi");

router.post("/",auth, projectAiController.createProjectAi);
router.get("/",auth, projectAiController.getAllProjects);

module.exports = router;

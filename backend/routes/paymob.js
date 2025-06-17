const express = require("express");
const router = express.Router();
const {
  initiatePayment,
  handleWebhook,
  getTransactionStatus,
} = require("../controllers/paymob");
const { auth, isStudent } = require("../middleware/auth");

// Route to initiate payment
router.post("/initiate-payment", auth, isStudent, initiatePayment);

// Route to handle Paymob webhook
router.post("/webhook", handleWebhook);
router.post("/getTransactionStatus",auth, getTransactionStatus);

module.exports = router;

const axios = require("axios");
const Transaction = require("../models/paymob");
const User = require("../models/user");
require("dotenv").config();

const PAYMOB_API_KEY = process.env.PAYMOB_API_KEY;
const PAYMOB_IFRAME_URL = process.env.PAYMOB_IFRAME_URL;
const PAYMOB_INTEGRATION_ID = process.env.PAYMOB_INTEGRATION_ID;

// Get authentication token from Paymob
exports.getAuthToken = async () => {
  try {
    const response = await axios.post(
      "https://accept.paymob.com/api/auth/tokens",
      {
        api_key: PAYMOB_API_KEY,
      }
    );
    return response.data.token;
  } catch (error) {
    console.error("Error getting auth token from Paymob:", error);
    throw new Error("Failed to get authentication token");
  }
};

// Initiate payment
exports.initiatePayment = async (req, res) => {
  try {
    const { amount, courseId } = req.body;
    const userId = req.user.id;

    if (!amount || !courseId) {
      return res
        .status(400)
        .json({ success: false, message: "Missing payment details" });
    }

    const authToken = await this.getAuthToken();

    const orderResponse = await axios.post(
      "https://accept.paymob.com/api/ecommerce/orders",
      {
        auth_token: authToken,
        delivery_needed: "false",
        amount_cents: amount * 100,
        currency: "EGP",
        items: [],
      }
    );

    const requestData = {
      auth_token: authToken,
      amount_cents: amount * 100,
      expiration: 3600,
      order_id: orderResponse.data.id,
      billing_data: {
        first_name: req.user.firstName || "Default",
        last_name: req.user.lastName || "User",
        email: req.user.email || "default@example.com",
        phone_number: req.user.contactNumber || "0000000000",
        country: "EG",
        city: "Cairo",
        state: "Cairo",
        street: "N/A",
        apartment: "N/A",
        floor: "N/A",
        building: "N/A",
      },
      currency: "EGP",
      integration_id: PAYMOB_INTEGRATION_ID,
    };

    let paymentKeyResponse;
    try {
      paymentKeyResponse = await axios.post(
        "https://accept.paymob.com/api/acceptance/payment_keys",
        requestData
      );
    } catch (error) {
      console.error(
        "Error generating payment key:",
        error.response?.data || error.message
      );
      return res.status(500).json({
        success: false,
        message: "Failed to generate payment key",
        error: error.response?.data || error.message,
      });
    }

    const transaction = await Transaction.create({
      user: userId,
      course: courseId,
      amount,
      transactionId: orderResponse.data.id,
      paymentKey: paymentKeyResponse.data.token,
      status: "pending",
    });

    res.status(200).json({
      success: true,
      
        data: `${PAYMOB_IFRAME_URL.replace(/\?.*$/, "")}?payment_token=${
          paymentKeyResponse.data.token
        }`,
      
    });
  } catch (error) {
    console.error("Error initiating payment:", error);
    res.status(500).json({
      success: false,
      message: "Payment initiation failed",
    });
  }
};



// Handle Paymob Webhook
exports.handleWebhook = async (req, res) => {
  try {
    const { obj } = req.body;

    // تأكد من أن obj موجود وبه id، وأن success ليس undefined
    if (!obj || !obj.id || obj.success === undefined) {
      return res
        .status(400)
        .json({ success: false, message: "Invalid webhook data" });
    }

    // البحث عن المعاملة بناءً على transactionId
    const transaction = await Transaction.findOne({ transactionId: obj.id });
    if (!transaction) {
      return res
        .status(404)
        .json({ success: false, message: "Transaction not found" });
    }

    // تحديث حالة الدفع بناءً على قيمة success
    transaction.status = obj.success ? "paid" : "failed";
    await transaction.save();

    // إذا كانت المعاملة ناجحة، أضف الكورس إلى المستخدم
    if (obj.success) {
      await User.findByIdAndUpdate(transaction.user.toString(), {
        $push: { courses: transaction.course },
      });
    }

    res
      .status(200)
      .json({ success: true, message: "Webhook handled successfully" });
  } catch (error) {
    console.error("Error handling Paymob webhook:", error);
    res
      .status(500)
      .json({ success: false, message: "Webhook processing failed" });
  }
};



exports.getTransactionStatus = async (req, res) => {
  try {
    const { courseId } = req.body; 
    const userId = req.user.id; 

    const transaction = await Transaction.findOne({
      user: userId,
      course: courseId,
    });

    if (!transaction) {
      return res
        .status(404)
        .json({ success: false, message: "Transaction not found" });
    }

    res.status(200).json({ success: true, data: transaction.status });
  } catch (error) {
    res.status(500).json({ success: false, message: "Server error" });
  }
};

// exports.getTransactionStatus = async (req, res) => {
//   try {
//     const { transactionId } = req.body;
//     if (!transactionId) {
//       return res
//         .status(400)
//         .json({ success: false, message: "Transaction ID is required" });
//     }

//     const transaction = await Transaction.findOne({ transactionId });
//     if (!transaction) {
//       return res
//         .status(404)
//         .json({ success: false, message: "Transaction not found" });
//     }

//     res.status(200).json({ success: true, data: transaction.status });
//   } catch (error) {
//     res.status(500).json({ success: false, message: "Server error" });
//   }
// };
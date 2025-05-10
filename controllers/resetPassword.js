const User = require('../models/user');
const mailSender = require('../utils/mailSender');
const crypto = require('crypto');
const bcrypt = require('bcrypt');

// ================ resetPasswordToken ================
// exports.resetPasswordToken = async (req, res) => {
//     try {
//         // extract email 
//         const { email } = req.body;

//         // email validation
//         const user = await User.findOne({ email });

//         if (!user) {
//             return res.status(401).json({
//                 success: false,
//                 message: 'Your Email is not registered with us'
//             });
//         }

//         // generate token
//         const token = crypto.randomBytes(20).toString("hex");

//         // update user by adding token & token expire date
//         const updatedUser = await User.findOneAndUpdate(
//             { email: email },
//             { token: token, resetPasswordTokenExpires: Date.now() + 5 * 60 * 1000 },
//             { new: true }); // by marking true, it will return updated user


//         // create url
//         const url = `https://study-notion-mern-stack.netlify.app/update-password/${token}`;

//         // send email containing url
//         await mailSender(email, 'Password Reset Link', `Password Reset Link : ${url}`);

//         // return succes response
//         res.status(200).json({
//             success: true,
//             message: 'Email sent successfully , Please check your mail box and change password'
//         })
//     }

//     catch (error) {
//         console.log('Error while creating token for reset password');
//         console.log(error)
//         res.status(500).json({
//             success: false,
//             error: error.message,
//             message: 'Error while creating token for reset password'
//         })
//     }
// }


exports.resetPasswordToken = async (req, res) => {
  try {
    const { email } = req.body;
    const user = await User.findOne({ email });

    if (!user) {
      return res.status(401).json({
        success: false,
        message: "Your Email is not registered with us",
      });
    }
    const token = Math.floor(100000 + Math.random() * 900000).toString();
    const updatedUser = await User.findOneAndUpdate(
      { email: email },
      { token: token, resetPasswordTokenExpires: Date.now() + 5 * 60 * 1000 },
      { new: true }
    );
    res.status(200).json({
      success: true,
      message: "Token generated successfully",
      data: token,
    });
  } catch (error) {
    console.log("Error while creating token for reset password");
    console.log(error);
    res.status(500).json({
      success: false,
      error: error.message,
      message: "Error while creating token for reset password",
    });
  }
};


// ================ resetPassword ================
exports.resetPassword = async (req, res) => {
  try {
    const { token, password, confirmPassword, email } = req.body;

    if (!token || !password || !confirmPassword || !email) {
      return res.status(401).json({
        success: false,
        message: "All fields are required!",
      });
    }

    if (password !== confirmPassword) {
      return res.status(401).json({
        success: false,
        message: "Passwords do not match",
      });
    }

    const userDetails = await User.findOne({ email, token });

    if (!userDetails) {
      return res.status(401).json({
        success: false,
        message: "Invalid token or email",
      });
    }

    if (!(userDetails.resetPasswordTokenExpires > Date.now())) {
      return res.status(401).json({
        success: false,
        message: "Token has expired, please regenerate token",
      });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    userDetails.password = hashedPassword;
    userDetails.token = undefined;
    userDetails.resetPasswordTokenExpires = undefined;

    await userDetails.save();

    res.status(200).json({
      success: true,
      message: "Password reset successfully",
    });
  } catch (error) {
    console.log("Error while resetting password:", error);
    res.status(500).json({
      success: false,
      message: "Internal server error",
      error: error.message,
    });
  }
};
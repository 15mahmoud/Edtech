// signup , verifyEmail, login ,  changePassword
const User = require('./../models/user');
const Profile = require('./../models/profile');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
require('dotenv').config();
const cookie = require('cookie');
const mailSender = require('../utils/mailSender');
const { passwordUpdated } = require("../mail/templates/passwordUpdate");
const sendEmail = require("../utils/mailSender.js");
const crypto = require("crypto"); 

// ================ SEND-OTP For Email Verification ================

exports.verifyEmail = async (req, res) => {
  try {
    const { token } = req.query;
    const user = await User.findOne({ verificationToken: token });

    if (!user) {
      return res
        .status(400)
        .json({ success: false, message: "Invalid or expired token" });
    }

    user.isVerified = true;
    user.verificationToken = undefined;
    await user.save();

    res
      .status(200)
      .json({
        success: true,
        message: "Email verified successfully. You can now log in.",
      });
  } catch (error) {
    console.log("Error verifying email");
    console.log(error);
    res.status(500).json({ success: false, message: "Error verifying email" });
  }
};


// ================ SIGNUP ================
exports.signup = async (req, res) => {
  try {
    const {
      firstName,
      lastName,
      email,
      password,
      confirmPassword,
      accountType,
      contactNumber,
    } = req.body;

    if (
      !firstName ||
      !lastName ||
      !email ||
      !password ||
      !confirmPassword ||
      !accountType
    ) {
      return res.status(401).json({
        success: false,
        message: "All fields are required except contactNumber.",
      });
    }

    if (password !== confirmPassword) {
      return res.status(400).json({
        success: false,
        message:
          "Password & Confirm Password do not match, Please try again..!",
      });
    }

    const checkUserAlreadyExits = await User.findOne({ email });

    if (checkUserAlreadyExits) {
      return res.status(400).json({
        success: false,
        message: "User registered already, go to Login Page",
      });
    }

    let hashedPassword = await bcrypt.hash(password, 10);

    const profileDetails = await Profile.create({
      gender: null,
      dateOfBirth: null,
      about: null,
      contactNumber: contactNumber || null, 
    });

    let approved = accountType === "Instructor" ? false : true;

    const verificationToken = crypto.randomBytes(32).toString("hex");

    const userData = await User.create({
      firstName,
      lastName,
      email,
      password: hashedPassword,
      contactNumber: contactNumber || null, 
      accountType,
      additionalDetails: profileDetails._id,
      approved,
      verificationToken,
      isVerified: false,
    });

    const verificationLink = `${process.env.CLIENT_URL}/api/v1/auth/verify-email?token=${verificationToken}`;

    await sendEmail(
      email,
      "Verify Your Email",
      `Click the link to verify your email: ${verificationLink}`
    );

    res.status(200).json({
      success: true,
      message: "User Registered Successfully. Please verify your email.",
    });
  } catch (error) {
    console.log("Error while registering user (signup)", error);
    res.status(500).json({
      success: false,
      error: error.message,
      message: "User cannot be registered, please try again..!",
    });
  }
};

// ================ LOGIN ================
exports.login = async (req, res) => {
  try {
    const { email, password } = req.body;

    if (!email || !password) {
      return res
        .status(400)
        .json({ success: false, message: "All fields are required" });
    }

    let user = await User.findOne({ email }).populate("additionalDetails");

    if (!user) {
      return res
        .status(401)
        .json({ success: false, message: "You are not registered with us" });
    }

    if (!user.isVerified) {
      return res
        .status(401)
        .json({
          success: false,
          message: "Please verify your email before logging in.",
        });
    }

    if (await bcrypt.compare(password, user.password)) {
      const payload = {
        email: user.email,
        id: user._id,
        accountType: user.accountType,
      };

      const token = jwt.sign(payload, process.env.JWT_SECRET, {
        expiresIn: "24h",
      });

      user = user.toObject();
      user.token = token;
      user.password = undefined;

      const cookieOptions = {
        expires: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000),
        httpOnly: true,
      };

      res
        .cookie("token", token, cookieOptions)
        .status(200)
        .json({
          success: true,
          data: user,
          message: "User logged in successfully",
        });
    } else {
      return res
        .status(401)
        .json({ success: false, message: "Password not matched" });
    }
  } catch (error) {
    console.log("Error while Login user");
    console.log(error);
    res.status(500).json({ success: false, message: "Error while Login user" });
  }
};


// ================ CHANGE PASSWORD ================
exports.changePassword = async (req, res) => {
    try {
        // extract data
        const { oldPassword, newPassword, confirmNewPassword } = req.body;

        // validation
        if (!oldPassword || !newPassword || !confirmNewPassword) {
            return res.status(403).json({
                success: false,
                message: 'All fileds are required'
            });
        }

        // get user
        const userDetails = await User.findById(req.user.id);

        // validate old passowrd entered correct or not
        const isPasswordMatch = await bcrypt.compare(
            oldPassword,
            userDetails.password
        )

        // if old password not match 
        if (!isPasswordMatch) {
            return res.status(401).json({
                success: false, message: "Old password is Incorrect"
            });
        }

        // check both passwords are matched
        if (newPassword !== confirmNewPassword) {
            return res.status(403).json({
                success: false,
                message: 'The password and confirm password do not match'
            })
        }


        // hash password
        const hashedPassword = await bcrypt.hash(newPassword, 10);

        // update in DB
        const updatedUserDetails = await User.findByIdAndUpdate(req.user.id,
            { password: hashedPassword },
            { new: true });


        // send email
        try {
            const emailResponse = await mailSender(
                updatedUserDetails.email,
                'Password for your account has been updated',
                passwordUpdated(
                    updatedUserDetails.email,
                    `Password updated successfully for ${updatedUserDetails.firstName} ${updatedUserDetails.lastName}`
                )
            );
            // console.log("Email sent successfully:", emailResponse);
        }
        catch (error) {
            console.error("Error occurred while sending email:", error);
            return res.status(500).json({
                success: false,
                message: "Error occurred while sending email",
                error: error.message,
            });
        }


        // return success response
        res.status(200).json({
            success: true,
            mesage: 'Password changed successfully'
        });
    }

    catch (error) {
        console.log('Error while changing passowrd');
        console.log(error)
        res.status(500).json({
            success: false,
            error: error.message,
            messgae: 'Error while changing passowrd'
        })
    }
}
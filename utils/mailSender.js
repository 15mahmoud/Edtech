const nodemailer = require("nodemailer");

const sendEmail = async (to, subject, text) => {
  try {
    let transporter = nodemailer.createTransport({
      service: "gmail",
      auth: {
        user: process.env.EMAIL_USER, // بريدك الإلكتروني
        pass: process.env.EMAIL_PASS, // كلمة المرور أو App Password
      },
    });

    let mailOptions = {
      from: process.env.EMAIL_USER,
      to: to,
      subject: subject,
      text: text,
    };

    await transporter.sendMail(mailOptions);
    console.log("✅ Verification Email Sent Successfully!");
  } catch (error) {
    console.log("❌ Error sending email:", error);
  }
};

module.exports = sendEmail;

// const nodemailer = require('nodemailer');

// const mailSender = async (email, title, body) => {
//     try {
//         const transporter = nodemailer.createTransport({
//           host: process.env.MAIL_HOST,
//           auth: {
//             user: process.env.MAIL_USER,
//             pass: process.env.MAIL_PASS,
//           },
//         });

//         const info = await transporter.sendMail({
//             from: 'StudyNotion || by mahmoud soliman',
//             to: email,
//             subject: title,
//             html: body
//         });

//         // console.log('Info of sent mail - ', info);
//         return info;
//     }
//     catch (error) {
//         console.log('Error while sending mail (mailSender) - ', email);
//     }
// }

// module.exports = mailSender;

const mongoose = require('mongoose')

const ratingAndReviewSchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: true,
  },
  rating: {
    type: Number,
    reqired: true,
    min: 1,
    max:5
  },
  review: {
    type: String,
    required: true,
  },
  course: {
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    ref: "Course",
    index: true,
  },
  averageRating: {
    type: Number,
    default: 0,
  },
});

module.exports = mongoose.model('RatingAndReview', ratingAndReviewSchema);
// Helper function to convert total seconds to the duration format


function convertSecondsToDuration(totalSeconds) {
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  return `${hours}hour ${minutes}min ${seconds}sec`;
}

module.exports = {
  convertSecondsToDuration,
};
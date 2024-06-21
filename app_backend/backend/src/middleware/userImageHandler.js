const multer = require('multer');

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'images/user/');
  },
  filename: (req, file, cb) => {
    cb(null, 'temp-' + Date.now() + '-' + file.originalname);
  }
});

const userMulter = multer({ storage: storage });

module.exports = userMulter;

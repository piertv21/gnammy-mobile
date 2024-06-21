const multer = require('multer');

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'images/gnam/');
  },
  filename: (req, file, cb) => {
    cb(null, 'temp-' + Date.now() + '-' + file.originalname);
  }
});

const gnamMulter = multer({ storage: storage });

module.exports = gnamMulter;

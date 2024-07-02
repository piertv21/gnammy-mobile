const multer = require('multer');

const createMulter = (destinationPath) => {
  const storage = multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, destinationPath);
    },
    filename: (req, file, cb) => {
      cb(null, 'temp-' + Date.now() + '-' + file.originalname);
    }
  });

  return multer({ storage: storage });
};

const userMulter = createMulter('images/user/');
const gnamMulter = createMulter('images/gnam/');

module.exports = { userMulter, gnamMulter };
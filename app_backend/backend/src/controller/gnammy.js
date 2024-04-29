const httpStatus = require("http-status");
const gnammyRepository = require('../repository/gnammy')

const addUser = (req, res) => {
    const { username, password, rpassword } = req.query;

    // Some checks
    if(password != rpassword) {
        return res.status(httpStatus.BAD_REQUEST)
            .json({ error: 'Le password non coincidono.' });
    }

    // Call the repository
    gnammyRepository.addUser(username, password, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante l\'inserimento dell\'user.' });
        }
        res.status(httpStatus.OK).json({ user });
    });
}

module.exports = {
    addUser
}
const httpStatus = require("http-status");
const gnammyRepository = require('../repository/gnammy')

const addUser = (req, res) => {
    const { username, password } = req.query;

    // Call the repository
    gnammyRepository.addUser(username, password, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante l\'inserimento dell\'user.' });
        }
        res.status(httpStatus.OK).json({ user });
    });
}

const listUsers = (req, res) => {
    // Call the repository
    gnammyRepository.listUsers((err, users) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante il recupero degli user.' });
        }
        res.status(httpStatus.OK).json({ users });
    });
}

const getUser = (req, res) => {
    const { username } = req.params;

    // Call the repository
    gnammyRepository.getUser(username, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante il recupero dell\'user.' });
        }
        res.status(httpStatus.OK).json({ user });
    });
}

const changeUserInfo = (req, res) => {
    const { userId, username, password, image } = req.query;
    // TODO gestisci cambio immagine

    // Call the repository
    gnammyRepository.changeUserInfo(userId, username, password, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante la modifica dell\'user.' });
        }
        res.status(httpStatus.OK).json({ user });
    });
}

const addGnam = (req, res) => {
    const { authorId, title, short_description, full_recipe } = req.query;
    // TODO gestisci aggiunta immagine

    gnammyRepository.addGnam(authorId, title, short_description, full_recipe, (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante l\'inserimento del gnam.' });
        }
        res.status(httpStatus.OK).json({ gnam });
    });
}

const saveGnam = (req, res) => {
    const { userId, gnamId } = req.query;

    gnammyRepository.saveGnam(userId, gnamId, (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante la salvataggio del gnam.' });
        }
        res.status(httpStatus.OK).json({ likes });
    });
}

const searchGnams = (req, res) => {
    const { keywords, dateFrom, dateTo, numberOfLikes } = req.query;
    gnammyRepository.searchGnams(keywords, dateFrom, dateTo, numberOfLikes, (err, gnams) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante la ricerca dei gnam.' });
        }
        res.status(httpStatus.OK).json({ gnams });
    });
}

const getGnam = (req, res) => {
    const { gnamId } = req.params;

    gnammyRepository.getGnam(gnamId, (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante il recupero del gnam.' });
        }
        res.status(httpStatus.OK).json({ gnam });
    });
}

const listGnams = (req, res) => {
    gnammyRepository.listGnams((err, gnams) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante il recupero dei gnam.' });
        }
        res.status(httpStatus.OK).json({ gnams });
    });
}

const toggleFollowUser = (req, res) => {
    const { userId, gnamId } = req.query;

    gnammyRepository.toggleFollowUser(userId, gnamId, (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante la modifica del gnam.' });
        }
        res.status(httpStatus.OK).json({ gnam });
    });
}

const getNewNotifications = (req, res) => {
    const { userId } = req.query;

    gnammyRepository.getNewNotifications(userId, (err, notifications) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante il recupero dei notifiche.' });
        }
        res.status(httpStatus.OK).json({ notifications });
    });
}

const shortListGoals = (req, res) => {
    const { userId } = req.query;

    gnammyRepository.shortListGoals(userId, (err, goals) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante il recupero dei obiettivi.' });
        }
        res.status(httpStatus.OK).json({ goals });
    });
}

const completeListGoals = (req, res) => {
    const { userId } = req.query;

    gnammyRepository.completeListGoals(userId, (err, goals) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante il recupero dei obiettivi.' });
        }
        res.status(httpStatus.OK).json({ goals });
    });
}

const completeGoal = (req, res) => {
    const { userId, goalId } = req.query;

    gnammyRepository.completeGoal(userId, goalId, (err, goal) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante la modifica del obiettivo.' });
        }
        res.status(httpStatus.OK).json({ goal });
    });
}

module.exports = {
    addUser,
    listUsers,
    getUser,
    changeUserInfo,
    addGnam,
    saveGnam,
    searchGnams,
    getGnam,
    listGnams,
    toggleFollowUser,
    getNewNotifications,
    shortListGoals,
    completeListGoals,
    completeGoal,
}
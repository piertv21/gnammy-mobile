const httpStatus = require("http-status");
const gnammyRepository = require('../repository/gnammy')

const addUser = (req, res) => {
    const { username, password } = req.body;

    // Call the repository
    gnammyRepository.addUser(username, password, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the user insertion: ${err}`});
        }
        res.status(httpStatus.OK).json({ user });
    });
}

const listUsers = (req, res) => {
    // Call the repository
    gnammyRepository.listUsers((err, users) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the users retrieval: ${err}`});
        }
        res.status(httpStatus.OK).json({ users });
    });
}

const getUser = (req, res) => {
    const { userId } = req.params;

    // Call the repository
    gnammyRepository.getUser(userId, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the user retrieval: ${err}`});
        }
        res.status(httpStatus.OK).json({ user });
    });
}

const changeUserInfo = (req, res) => {
    const { username, password, image } = req.body;
    const { userId } = req.params;
    // TODO handle image change

    // Call the repository
    gnammyRepository.changeUserInfo(userId, username, password, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the user update: ${err}`});
        }
        res.status(httpStatus.OK).json({ user });
    });
}

const addGnam = (req, res) => {
    const { authorId, title, short_description, full_recipe } = req.body;
    // TODO handle image addition

    gnammyRepository.addGnam(authorId, title, short_description, full_recipe, (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam insertion: ${err}`});
        }
        res.status(httpStatus.OK).json({ gnam });
    });
}

const postLike = (req, res) => {
    const { userId, gnamId } = req.body;

    gnammyRepository.saveGnam(userId, gnamId, (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam saving: ${err}`});
        }
        res.status(httpStatus.OK).json({ likes });
    });
}

const getLike = (req, res) => {
    const { userId, gnamId } = req.body;

    gnammyRepository.didUserLike(userId, gnamId, (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error getting likes: ${err}`});
        }
        res.status(httpStatus.OK).json({ likes });
    });
}

const searchGnams = (req, res) => {
    const { keywords, dateFrom, dateTo, numberOfLikes } = req.body;
    gnammyRepository.searchGnams(keywords, dateFrom, dateTo, numberOfLikes, (err, gnams) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnams search: ${err}`});
        }
        res.status(httpStatus.OK).json({ gnams });
    });
}

const getGnam = (req, res) => {
    const { gnamId } = req.params;

    gnammyRepository.getGnam(gnamId, (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam retrieval: ${err}`});
        }
        res.status(httpStatus.OK).json({ gnam });
    });
}

const listGnams = (req, res) => {
    gnammyRepository.listGnams((err, gnams) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnams retrieval: ${err}`});
        }
        res.status(httpStatus.OK).json({ gnams });
    });
}

const toggleFollowUser = (req, res) => {
    const { sourceUser, targetUser } = req.body;

    gnammyRepository.toggleFollowUser(sourceUser, targetUser, (err, follow) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error in toggle follow: ${err}`});
        }
        res.status(httpStatus.OK).json({ follow });
    });
}

const listFollower = (req, res) => {
    const { userId } = req.params;

    gnammyRepository.listFollower(userId, (err, followers) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam update: ${err}`});
        }
        res.status(httpStatus.OK).json({ followers });
    });
}

const listFollowing = (req, res) => {
    const { userId } = req.params;

    gnammyRepository.listFollowing(userId, (err, following) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam update: ${err}`});
        }
        res.status(httpStatus.OK).json({ following });
    });
}

const doUserFollowUser = (req, res) => {
    const { sourceUser, targetUser } = req.body;

    gnammyRepository.doUserFollowUser(sourceUser, targetUser, (err, doUserFollowUser) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam update: ${err}`});
        }
        res.status(httpStatus.OK).json({ doUserFollowUser });
    });
}

const getNewNotifications = (req, res) => {
    const { userId } = req.body;

    gnammyRepository.getNewNotifications(userId, (err, notifications) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the notifications retrieval: ${err}`});
        }
        res.status(httpStatus.OK).json({ notifications });
    });
}

const shortListGoals = (req, res) => {
    const { limit } = req.params;
    const { userId } = req.body;

    gnammyRepository.shortListGoals(userId, limit, (err, goals) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the goals retrieval: ${err}`});
        }
        res.status(httpStatus.OK).json({ goals });
    });
}

const completeListGoals = (req, res) => {
    const { userId } = req.body;

    gnammyRepository.completeListGoals(userId, (err, goals) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the goals retrieval: ${err}`});
        }
        res.status(httpStatus.OK).json({ goals });
    });
}

const completeGoal = (req, res) => {
    const { userId, goalId } = req.body;

    gnammyRepository.completeGoal(userId, goalId, (err, goal) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the goal update: ${err}`});
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
    postLike,
    getLike,
    searchGnams,
    getGnam,
    listGnams,
    toggleFollowUser,
    getNewNotifications,
    shortListGoals,
    completeListGoals,
    completeGoal,
    doUserFollowUser,
    listFollower,
    listFollowing,
}

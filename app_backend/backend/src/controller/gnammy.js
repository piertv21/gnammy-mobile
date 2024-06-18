const httpStatus = require("http-status");
const gnammyRepository = require('../repository/gnammy')


class NotificationType {
    static Like = new NotificationType('Like');
    static Follow = new NotificationType('Follow');

    constructor(name) {
        if(this.id == undefined) {
            gnammyRepository.getNotificationType(name, (err, notificationType) => {
                if (err) {
                    console.log(err);
                }
                this.id = notificationType.id;
            });
        }
        this.name = name;
    }
}

class GoalType {
    static Post1 = new GoalType('Hai pubblicato il tuo primo gnam');
    static Post10 = new GoalType('Hai pubblicato 10 gnam');
    static Post100 = new GoalType('Hai pubblicato 100 gnam');
    static Save1 = new GoalType('Hai salvato il tuo primo gnam');
    static Save10 = new GoalType('Hai salvato 10 gnam');
    static Save100 = new GoalType('Hai salvato 100 gnam');
    static OwnGnam10 = new GoalType('I tuoi gnam sono stati salvati 10 volte');
    static OwnGnam100 = new GoalType('I tuoi gnam sono stati salvati 100 volte');
    static OwnGnam1000 = new GoalType('I tuoi gnam sono stati salvati 1000 volte');
    static Follower1 = new GoalType('Hai raggiunto 1 follower');
    static Follower10 = new GoalType('Hai raggiunto 10 follower');
    static Follower100 = new GoalType('Hai raggiunto 100 follower');
    static SpecificRecipeLike1 = new GoalType('Questa ricetta è stata salvata 1 volta');
    static SpecificRecipeLike10 = new GoalType('Questa ricetta è stata salvata 10 volte');
    static SpecificRecipeLike100 = new GoalType('Questa ricetta è stata salvata 100 volte');
    static SpecificRecipeShare1 = new GoalType('Questa ricetta è stata condivisa 1 volta');
    static SpecificRecipeShare10 = new GoalType('Questa ricetta è stata condivisa 10 volte');
    static SpecificRecipeShare100 = new GoalType('Questa ricetta è stata condivisa 100 volte');

    constructor(name) {
        if(this.id == undefined) {
            gnammyRepository.getNotificationType(name, (err, notificationType) => {
                if (err) {
                    console.log(err);
                }
                this.id = notificationType.id;
            });
        }
        this.name = name;
    }
}

const addUser = (req, res) => {
    const { username, password } = req.body;

    gnammyRepository.addUser(username, password, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the user insertion: ${err}`});
        }
        // TODO: Qua deve creare tutti i goal che non sono per gli gnam.

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
        // TODO: Qua deve creare tutti i goal per lo gnam appena postato.
        // TODO: Controllare se bisogna aggiornare i goal:
        // - Post1, Post10, Post100
        res.status(httpStatus.OK).json({ gnam });
    });
}

// TODO: Aggiungi shareGnam
// TODO: controllare se bisogna aggiornare i goal:
// - SpecificShare1, SpecificShare10, SpecificShare100

const postLike = (req, res) => {
    const { userId, gnamId } = req.body;

    gnammyRepository.saveGnam(userId, gnamId, (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam like: ${err}`});
        }
        gnammyRepository.getUserIdFromGnamId(gnamId, (err, targetUserId) => {
            if(!err) createNotification(userId, targetUserId, gnamId, NotificationType.Like);
        });
        // TODO: Controllare se bisogna aggiornare i goal:
        // - Save1, Save10, Save100
        // - OwnGnam10, OwnGnam100, OwnGnam1000
        // - SpecificRecipeLike1, SpecificRecipeLike10, SpecificRecipeLike100
        res.status(httpStatus.OK).json({ likes });
    });
}

const deleteLike = (req, res) => {
    const { userId, gnamId } = req.body;

    gnammyRepository.deleteLike(userId, gnamId, (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during like deletion: ${err}`});
        }
        gnammyRepository.getUserIdFromGnamId(gnamId, (err, targetUserId) => {
            if(!err) deleteNotification(userId, targetUserId, gnamId, NotificationType.Like);
        });
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

    gnammyRepository.toggleFollowUser(sourceUser, targetUser, (err, result) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error in toggle follow: ${err}`});
        }
        // TODO: Controllare se bisogna aggiornare i goal:
        // - Follower1, Follower10, Follower100
        if (result.followed) {
            createNotification(sourceUser, targetUser, null, NotificationType.Follow);
        } else {
            deleteNotification(sourceUser, targetUser, null, NotificationType.Follow);
        }
        res.status(httpStatus.OK).json({ result });
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

const listGoals = (req, res) => {
    const { userId } = req.body;

    gnammyRepository.listGoals(userId, (err, goals) => {
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

async function createNotification(sourceUser, targetUser, gnamId, notificationType) {
    gnammyRepository.createNotification(sourceUser, targetUser, gnamId, notificationType);
}

async function deleteNotification(sourceUser, targetUser, gnamId, notificationType) {
    gnammyRepository.deleteNotification(sourceUser, targetUser, gnamId, notificationType);
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
    listGoals: listGoals,
    completeGoal,
    doUserFollowUser,
    listFollower,
    listFollowing,
    deleteLike
}

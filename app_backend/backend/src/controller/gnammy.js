const httpStatus = require("http-status");
const gnammyRepository = require('../repository/gnammy')
const fs = require('fs');
const path = require('path');
const sharp = require('sharp');

class NotificationType {
    static Like = new NotificationType('Like');
    static Follow = new NotificationType('Follow');

    constructor(templateText) {
        if (this.id == undefined) {
            gnammyRepository.getNotificationType(templateText, (err, notificationType) => {
                if (err) {
                    console.log(err);
                }
                this.id = notificationType.id;
            });
        }
        this.name = templateText;
    }
}

class GoalType {
    static userGoals = {
        Post1: new GoalType('Hai pubblicato il tuo primo gnam'),
        Post10: new GoalType('Hai pubblicato 10 gnam'),
        Post100: new GoalType('Hai pubblicato 100 gnam'),
        Save1: new GoalType('Hai salvato il tuo primo gnam'),
        Save10: new GoalType('Hai salvato 10 gnam'),
        Save100: new GoalType('Hai salvato 100 gnam'),
        OwnGnam10: new GoalType('I tuoi gnam sono stati salvati 10 volte'),
        OwnGnam100: new GoalType('I tuoi gnam sono stati salvati 100 volte'),
        OwnGnam1000: new GoalType('I tuoi gnam sono stati salvati 1000 volte'),
        Follower1: new GoalType('Hai raggiunto 1 follower'),
        Follower10: new GoalType('Hai raggiunto 10 follower'),
        Follower100: new GoalType('Hai raggiunto 100 follower'),
    }
    static gnamGoals = {
        SpecificRecipeLike1: new GoalType('Questa ricetta è stata salvata 1 volta'),
        SpecificRecipeLike10: new GoalType('Questa ricetta è stata salvata 10 volte'),
        SpecificRecipeLike100: new GoalType('Questa ricetta è stata salvata 100 volte'),
        SpecificShare1: new GoalType('Questa ricetta è stata condivisa 1 volta'),
        SpecificShare10: new GoalType('Questa ricetta è stata condivisa 10 volte'),
        SpecificShare100: new GoalType('Questa ricetta è stata condivisa 100 volte'),
    }

    constructor(templateText) {
        if (this.id == undefined) {
            gnammyRepository.getGoalType(templateText, (err, goalType) => {
                if (err) {
                    console.log(err);
                }
                this.id = goalType.id;
            });
        }
        this.templateText = templateText;
    }
}

const addUser = (req, res) => {
    const { username, password, location } = req.body;

    gnammyRepository.addUser(username, password, location, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the user insertion: ${err}` });
        }

        if (req.file) {
            const tempPath = path.join('images/user', req.file.filename);
            const newFilename = `${user.id}.jpg`;
            const newPath = path.join('images/user', newFilename);

            sharp(tempPath)
                .toFormat('jpeg')
                .toFile(newPath, (err, info) => {
                    if (err) {
                        console.error('Error during the conversion and rename of the file:', err);
                        return res.status(500).json({ error: 'Error during the user insertion' });
                    }

                    // Rimozione del file temporaneo
                    fs.unlink(tempPath, (unlinkErr) => {
                        if (unlinkErr) {
                            console.error('Error during the removal of the temporary file:', unlinkErr);
                            return res.status(500).json({ error: 'Error during the user insertion' });
                        }
                    });
                });
        }

        Object.values(GoalType.userGoals).forEach(async goalType => {
            await gnammyRepository.createGoal(user.id, goalType);
        });
        res.status(httpStatus.OK).json({ user });
    });
}

const listUsers = (req, res) => {
    gnammyRepository.listUsers((err, users) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the users retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ users });
    });
}

const getUser = (req, res) => {
    const { userId } = req.params;
    gnammyRepository.getUser(userId, (err, user) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the user retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ user });
    });
}

const changeUserInfo = async (req, res) => {
    const { username, password, location } = req.body;
    const { userId } = req.params;

    gnammyRepository.changeUserInfo(userId, username, password, location, async (err, user) => {
        if (err) {
            console.error('Error during the user update:', err);
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the user update: ${err}` });
        }

        if (req.file) {
            const oldFilename = `${userId}.jpg`;
            const oldFilePath = path.join('images/user', oldFilename);

            if (fs.existsSync(oldFilePath)) {
                fs.unlinkSync(oldFilePath);
            }

            const tempPath = req.file.path;
            const newFilename = `${userId}.jpg`;
            const newPath = path.join('images/user', newFilename);

            await sharp(tempPath)
                .toFormat('jpeg')
                .toFile(newPath);

            fs.unlinkSync(tempPath);
        }


        res.status(httpStatus.OK).json({ user });
    });
};



const addGnam = (req, res) => {
    const { authorId, title, short_description, full_recipe } = req.body;

    gnammyRepository.addGnam(authorId, title, short_description, full_recipe, async (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam insertion: ${err}` });
        }
        Object.values(GoalType.gnamGoals).forEach(async goalType => {
            await gnammyRepository.createGoal(authorId, goalType, gnam.id);
        });
        await gnammyRepository.getUserGnams(authorId, (err, gnams) => {
            if (!err) {
                switch (gnams.length) {
                    case 1:
                        gnammyRepository.completeGoal(authorId, GoalType.userGoals.Post1);
                        break;
                    case 10:
                        gnammyRepository.completeGoal(authorId, GoalType.userGoals.Post10);
                        break;
                    case 100:
                        gnammyRepository.completeGoal(authorId, GoalType.userGoals.Post100);
                        break;
                    default:
                        break;
                }
            }
        });

        if (req.file) {
            const tempPath = path.join('images/gnam', req.file.filename);
            const newFilename = `${gnam.id}.jpg`;
            const newPath = path.join('images/gnam', newFilename);

            sharp(tempPath)
                .toFormat('jpeg')
                .toFile(newPath, (err, info) => {
                    if (err) {
                        console.error('Error during the conversion and rename of the file:', err);
                        return res.status(500).json({ error: 'Error during the gnam insertion' });
                    }

                    // Rimozione del file temporaneo
                    fs.unlink(tempPath, (unlinkErr) => {
                        if (unlinkErr) {
                            console.error('Error during the removal of the temporary file:', unlinkErr);
                            return res.status(500).json({ error: 'Error during the gnam insertion' });
                        }
                    });
                });
        }

        res.status(httpStatus.OK).json({ gnam });
    });
}

const shareGnam = (req, res) => {
    const { gnamId } = req.body;

    gnammyRepository.shareGnam(gnamId, async (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam share: ${err}` });
        }

        switch (gnam.shareCount) {
            case 1:
                gnammyRepository.completeGoal(gnam.authorId, GoalType.gnamGoals.SpecificShare1, gnam.id);
                break;
            case 10:
                gnammyRepository.completeGoal(gnam.authorId, GoalType.gnamGoals.SpecificShare10, gnam.id);
                break;
            case 100:
                gnammyRepository.completeGoal(gnam.authorId, GoalType.gnamGoals.SpecificShare100, gnam.id);
                break;
            default:
                break;
        }
        res.status(httpStatus.OK).json({ gnam });
    });
}

const postLike = (req, res) => {
    const { userId, gnamId } = req.body;

    gnammyRepository.saveGnam(userId, gnamId, async (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam like: ${err}` });
        }
        await gnammyRepository.getUserIdFromGnamId(gnamId, async (err, targetUserId) => {
            if (!err) {
                await gnammyRepository.createNotification(userId, targetUserId, gnamId, NotificationType.Like);
                console.log(`User with id ${userId} has liked gnam with id ${gnamId} by user with id ${targetUserId}`);
            }

            await gnammyRepository.getGnamLikesCount(gnamId, async (err, likesCount) => {
                console.log(`Gnam with id ${gnamId}, made by ${targetUserId} has ${likesCount} likes`);
                if (!err) {
                    switch (likesCount) {
                        case 1:
                            await gnammyRepository.completeGoal(targetUserId, GoalType.gnamGoals.SpecificRecipeLike1, gnamId);
                            break;
                        case 10:
                            await gnammyRepository.completeGoal(targetUserId, GoalType.gnamGoals.SpecificRecipeLike10, gnamId);
                            break;
                        case 100:
                            await gnammyRepository.completeGoal(targetUserId, GoalType.gnamGoals.SpecificRecipeLike100, gnamId);
                            break;
                        default:
                            break;
                    }
                }
            });

            await gnammyRepository.getUserReceivedLikesCount(targetUserId, async (err, likesCount) => {
                console.log(`User with id ${targetUserId} has received ${likesCount} likes`);
                if (!err) {
                    switch (likesCount) {
                        case 10:
                            await gnammyRepository.completeGoal(userId, GoalType.userGoals.OwnGnam10);
                            break;
                        case 100:
                            await gnammyRepository.completeGoal(userId, GoalType.userGoals.OwnGnam100);
                            break;
                        case 1000:
                            await gnammyRepository.completeGoal(userId, GoalType.userGoals.OwnGnam1000);
                            break;
                        default:
                            break;
                    }
                }
            });
        });

        await gnammyRepository.getUserGivenLikesCount(userId, async (err, likesCount) => {
            console.log(`User with id ${userId} has given ${likesCount} likes`);
            if (!err) {
                switch (likesCount) {
                    case 1:
                        await gnammyRepository.completeGoal(userId, GoalType.userGoals.Save1);
                        break;
                    case 10:
                        await gnammyRepository.completeGoal(userId, GoalType.userGoals.Save10);
                        break;
                    case 100:
                        await gnammyRepository.completeGoal(userId, GoalType.userGoals.Save100);
                        break;
                    default:
                        break;
                }
            }
        });
        res.status(httpStatus.OK).json({ likes });
    });
}

const deleteLike = (req, res) => {
    const { userId, gnamId } = req.body;

    gnammyRepository.deleteLike(userId, gnamId, (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during like deletion: ${err}` });
        }
        gnammyRepository.getUserIdFromGnamId(gnamId, async (err, targetUserId) => {
            if (!err) await gnammyRepository.deleteNotification(sourceUser, targetUser, gnamId, NotificationType.Like);
        });
        res.status(httpStatus.OK).json({ likes });
    });
}

const getLike = (req, res) => {
    const { userId, gnamId } = req.body;

    gnammyRepository.didUserLike(userId, gnamId, (err, likes) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error getting likes: ${err}` });
        }
        res.status(httpStatus.OK).json({ likes });
    });
}

const searchGnams = (req, res) => {
    const { keywords, dateFrom, dateTo, numberOfLikes } = req.body;
    gnammyRepository.searchGnams(keywords, dateFrom, dateTo, numberOfLikes, (err, gnams) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnams search: ${err}` });
        }
        res.status(httpStatus.OK).json({ gnams });
    });
}

const getGnam = (req, res) => {
    const { gnamId } = req.params;

    gnammyRepository.getGnam(gnamId, (err, gnam) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ gnam });
    });
}

const listGnams = (req, res) => {
    gnammyRepository.listGnams((err, gnams) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnams retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ gnams });
    });
}

const toggleFollowUser = (req, res) => {
    const { sourceUserId, targetUserId } = req.body;

    gnammyRepository.toggleFollowUser(sourceUserId, targetUserId, async (err, result) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error in toggle follow: ${err}` });
        }
        await gnammyRepository.listFollower(targetUserId, (err, followers) => {
            if (!err) {
                switch (followers.length) {
                    case 1:
                        gnammyRepository.completeGoal(targetUserId, GoalType.userGoals.Follower1);
                        break;
                    case 10:
                        gnammyRepository.completeGoal(targetUserId, GoalType.userGoals.Follower10);
                        break;
                    case 100:
                        gnammyRepository.completeGoal(targetUserId, GoalType.userGoals.Follower100);
                        break;
                    default:
                        break;
                }
            }
        });

        await gnammyRepository.listFollower(sourceUserId, (err, followers) => {
            if (!err) {
                switch (followers.length) {
                    case 1:
                        gnammyRepository.completeGoal(sourceUserId, GoalType.userGoals.Follower1);
                        break;
                    case 10:
                        gnammyRepository.completeGoal(sourceUserId, GoalType.userGoals.Follower10);
                        break;
                    case 100:
                        gnammyRepository.completeGoal(sourceUserId, GoalType.userGoals.Follower100);
                        break;
                    default:
                        break;
                }
            }
        });

        if (result.followed) {
            await gnammyRepository.createNotification(sourceUserId, targetUserId, null, NotificationType.Follow);
        } else {
            await gnammyRepository.deleteNotification(sourceUserId, targetUserId, null, NotificationType.Follow);
        }
        res.status(httpStatus.OK).json({ result });
    });
}

const listFollower = (req, res) => {
    const { userId } = req.params;

    gnammyRepository.listFollower(userId, (err, followers) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam update: ${err}` });
        }
        res.status(httpStatus.OK).json({ followers });
    });
}

const listFollowing = (req, res) => {
    const { userId } = req.params;

    gnammyRepository.listFollowing(userId, (err, following) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam update: ${err}` });
        }
        res.status(httpStatus.OK).json({ following });
    });
}

const doUserFollowUser = (req, res) => {
    const { sourceUser, targetUser } = req.body;

    gnammyRepository.doUserFollowUser(sourceUser, targetUser, (err, doUserFollowUser) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam update: ${err}` });
        }
        res.status(httpStatus.OK).json({ doUserFollowUser });
    });
}

const getNewNotifications = (req, res) => {
    const { userId } = req.params;

    gnammyRepository.getNewNotifications(userId, (err, notifications) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the notifications retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ notifications });
    });
}

const shortListGoals = (req, res) => {
    const { limit, userId } = req.params;

    gnammyRepository.shortListGoals(userId, limit, (err, goals) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the goals retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ goals });
    });
}

const listGoals = (req, res) => {
    const { userId } = req.params;

    gnammyRepository.listGoals(userId, (err, goals) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the goals retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ goals });
    });
}

const completeGoal = (req, res) => {
    const { goalId } = req.params;
    const { userId } = req.body;

    gnammyRepository.completeGoal(userId, goalId, (err, goal) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the goal update: ${err}` });
        }
        res.status(httpStatus.OK).json({ goal });
    });
}

const getUserImage = (req, res) => {
    const { userId } = req.params;

    res.sendFile(`/usr/src/app/images/user/${userId}.jpg`);
}

const getGnamImage = (req, res) => {
    const { gnamId } = req.params;

    res.sendFile(`/usr/src/app/images/gnam/${gnamId}.jpg`);
}

const getUserGnams = (req, res) => {
    const { userId } = req.params;

    gnammyRepository.getUserGnams(userId, (err, gnams) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnams retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ gnams });
    });
}

const setNotificationsAsRead = (req, res) => {
    const { notificationId } = req.params;

    gnammyRepository.setNotificationsAsRead(notificationId, (err, notification) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the notification update: ${err}` });
        }
        res.status(httpStatus.OK).json({ notification });
    });
}

const getListOfUsersThatSavedGnam = (req, res) => {
    const { gnamId } = req.params;

    gnammyRepository.getListOfUsersThatSavedGnam(gnamId, (err, users) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: `Error during the gnam retrieval: ${err}` });
        }
        res.status(httpStatus.OK).json({ users });
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
    listGoals: listGoals,
    completeGoal,
    doUserFollowUser,
    listFollower,
    listFollowing,
    deleteLike,
    shareGnam,
    getUserImage,
    getGnamImage,
    getUserGnams,
    setNotificationsAsRead,
    getListOfUsersThatSavedGnam
}

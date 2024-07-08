const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcrypt');
const prisma = new PrismaClient();

/*clyddkkty00007io2hbhqax8m,user,Hai pubblicato 100 gnam
clyddkm5l00017io2kwrs9mwe,user,Hai pubblicato 10 gnam
clyddkm6c00027io2p247m94x,user,I tuoi gnam sono stati salvati 100 volte
clyddkm6o00077io2qjz8zjqp,gnam,Questa ricetta è stata condivisa 100 volte
clyddkm6e00037io2knsfbp0p,user,Hai salvato 10 gnam
clyddkm6o00067io2ts3sdnep,gnam,Questa ricetta è stata salvata 10 volte
clyddkm6n00057io2zhin4n8e,user,I tuoi gnam sono stati salvati 10 volte
clyddkm6m00047io2ir5mx5xm,user,I tuoi gnam sono stati salvati 1000 volte
clyddkm7200097io2poqzya2f,gnam,Questa ricetta è stata condivisa 1 volta
clyddkm6t00087io2mf337jcr,user,Hai salvato il tuo primo gnam
clyddkm78000a7io275q9cu5k,user,Hai pubblicato il tuo primo gnam
clyddkm7r000c7io28qvpndhy,user,Hai raggiunto 1 follower
clyddkm7n000b7io230wqbfwt,gnam,Questa ricetta è stata salvata 1 volta
clyddkm84000d7io29hf8th1h,user,Hai salvato 100 gnam
clyddkm85000e7io2sa9m136d,user,Hai raggiunto 10 follower
clyddkm8b000f7io2s3wfxqhv,gnam,Questa ricetta è stata salvata 100 volte
clyddkm8j000g7io21jcmyagv,user,Hai raggiunto 100 follower
clyddkm9b000i7io2a4p6zrdv,gnam,Questa ricetta è stata condivisa 10 volte

clyddkm8n000h7io2adl11npw,ha messo like al tuo gnam,Like
clyddkm9n000j7io27gft0ex2,ha iniziato a seguirti,Follow

*/

async function login(username, password, callback) {
    try {
        const user = await prisma.user.findFirst({
            where: {
                username: username
            }
        });

        if (user == null) return callback('User not found', null);

        const passwordMatch = await bcrypt.compare(password, user.password);
        if (!passwordMatch) return callback('Password does not match', null);

        callback(null, user);
    } catch (error) {
        callback(error, null);
    }
}

async function addUser(username, password, location, callback) {
    try {
        const hashedPassword = await bcrypt.hash(password, 10);
        const createdUser = await prisma.user.create({
            data: {
                username: username,
                password: hashedPassword,
                location: location,
                imageUri: "not set"
            }
        });
        callback(null, createdUser);
    } catch (error) {
        callback(error, null);
    }
}

async function listUsers(callback) {
    try {
        const users = await prisma.user.findMany();
        callback(null, users);
    } catch (error) {
        callback(error, null);
    }
}

async function getUser(userId, callback) {
    console.log(`getUser: ${userId}`)
    try {
        const user = await prisma.user.findUnique({
            where: {
                id: userId
            }
        });
        callback(null, user);
    } catch (error) {
        callback(error, null);
    }
}

async function updateUserImage(userId, newFilename, callback) {
    try {
        const updatedUser = await prisma.user.update({
            where: {
                id: userId
            },
            data: {
                imageUri: newFilename
            }
        });

        callback(null, updatedUser);
    } catch (error) {
        callback(error, null);
    }
}

async function changeUserInfo(userId, username, password, location, callback) {
    try {
        let hashedPassword = undefined;
        if (username?.length > 255) return callback('Username too long', null);
        if (password?.length > 255) return callback('Password too long', null);

        // If new password is not undefined, hash it
        if (password !== undefined) {
            hashedPassword = await bcrypt.hash(password, 10);
        }

        const data = {};
        if (username !== undefined) {
            data.username = username;
        }
        if (hashedPassword !== undefined) {
            data.password = hashedPassword;
        }
        if (location !== undefined) {
            data.location = location;
        }

        const updatedUser = await prisma.user.update({
            where: {
                id: userId
            },
            data: data
        });

        callback(null, updatedUser);
    } catch (error) {
        callback(error, null);
    }
}


async function addGnam(authorId, title, short_description, full_recipe, callback) {
    try {
        const createdGnam = await prisma.gnam.create({
            data: {
                authorId: authorId,
                title: title,
                description: short_description,
                recipe: full_recipe
            }
        });
        callback(null, createdGnam);
    } catch (error) {
        callback(error, null);
    }
}

async function saveGnam(userId, gnamId, callback) {
    try {
        const likes = await prisma.like.create({
            data: {
                userId: userId,
                gnamId: gnamId
            }
        });
        callback(null, likes);
    } catch (error) {
        callback(error, null);
    }
}

async function didUserLike(userId, gnamId, callback) {
    try {
        const likes = await prisma.like.findFirst({
            where: {
                userId: userId,
                gnamId: gnamId
            }
        });
        callback(null, likes == null ? false : true);
    } catch (error) {
        callback(error, null);
    }
}

async function searchGnams(keywords, dateFrom, dateTo, numberOfLikes, callback) {
    keywords = keywords || '';
    dateFrom = dateFrom || new Date(0);
    dateTo = dateTo || new Date();
    numberOfLikes = numberOfLikes || 0;

    try {
        const gnams = await prisma.gnam.findMany({
            where: {
                OR: [
                    {
                        title: {
                            contains: keywords
                        }
                    },
                    {
                        description: {
                            contains: keywords
                        }
                    },
                    {
                        recipe: {
                            contains: keywords
                        }
                    }
                ],
                createdAt: {
                    gte: new Date(dateFrom),
                    lte: new Date(dateTo)
                }
            },
            include: {
                _count: {
                    select: { likes: true }
                },
                author: {
                    select: {
                        username: true,
                        imageUri: true
                    }
                }
            },
            orderBy: {
                likes: {
                    _count: 'desc'
                }
            }
        });

        const filteredGnams = gnams.filter(gnam => gnam._count.likes >= numberOfLikes);

        const gnamsWithAuthorName = filteredGnams.map(gnam => ({
            id: gnam.id,
            authorId: gnam.authorId,
            title: gnam.title,
            description: gnam.description,
            recipe: gnam.recipe,
            shareCount: gnam.shareCount,
            createdAt: gnam.createdAt,
            authorName: gnam.author.username,
            authorImageUri: gnam.author.imageUri
        }));
        callback(null, gnamsWithAuthorName);
    } catch (error) {
        callback(error, null);
    }
}


async function getGnam(gnamId, callback) {
    try {
        const gnam = await prisma.gnam.findUnique({
            where: {
                id: gnamId
            },
            include: {
                author: {
                    select: {
                        username: true,
                        imageUri: true
                    }
                }
            }
        });
        const gnamWithAuthorName = {
            id: gnam.id,
            authorId: gnam.authorId,
            title: gnam.title,
            description: gnam.description,
            recipe: gnam.recipe,
            shareCount: gnam.shareCount,
            createdAt: gnam.createdAt,
            authorName: gnam.author.username,
            authorImageUri: gnam.author.imageUri
        };
        callback(null, gnamWithAuthorName);
    } catch (error) {
        callback(error, null);
    }
}

async function listGnams(callback) {
    try {
        const gnams = await prisma.gnam.findMany({
            include: {
                author: {
                    select: {
                        username: true,
                        imageUri: true
                    }
                }
            },
            orderBy: {
                createdAt: 'desc'
            }
        });
        const gnamsWithAuthorName = gnams.map(gnam => ({
            id: gnam.id,
            authorId: gnam.authorId,
            title: gnam.title,
            description: gnam.description,
            recipe: gnam.recipe,
            shareCount: gnam.shareCount,
            createdAt: gnam.createdAt,
            authorName: gnam.author.username,
            authorImageUri: gnam.author.imageUri
        }));
        callback(null, gnamsWithAuthorName);
    } catch (error) {
        callback(error, null);
    }
}

async function toggleFollowUser(sourceUser, targetUser, callback) {
    try {
        // Check if the following relationship already exists
        const followUser = await prisma.following.findUnique({
            where: {
                sourceUserId_targetUserId: {
                    sourceUserId: sourceUser,
                    targetUserId: targetUser
                }
            }
        });

        if (followUser) {
            // If it exists, delete the following relationship
            await prisma.following.delete({
                where: {
                    id: followUser.id
                }
            });
            callback(null, { followed: false });
        } else {
            // If it doesn't exist, create the following relationship
            await prisma.following.create({
                data: {
                    sourceUserId: sourceUser,
                    targetUserId: targetUser
                }
            });
            callback(null, { followed: true });
        }
    } catch (error) {
        callback(error, null);
    }
}

async function doUserFollowUser(sourceUser, targetUser, callback) {
    try {
        // Check if the following relationship already exists
        const followUser = await prisma.following.findUnique({
            where: {
                sourceUserId_targetUserId: {
                    sourceUserId: sourceUser,
                    targetUserId: targetUser
                }
            }
        });
        callback(null, followUser == null ? false : true);
    } catch (error) {
        callback(error, null);
    }
}

async function listFollower(userId, callback) {
    try {
        const followers = await prisma.following.findMany({
            where: {
                targetUserId: userId
            },
            include: {
                sourceUser: true
            }
        });

        // Extracting the source users from the following relationships
        const followerUsers = followers.map(follow => follow.sourceUser);

        callback(null, followerUsers);
    } catch (error) {
        callback(error, null);
    }
}

async function listFollowing(userId, callback) {
    try {
        const followings = await prisma.following.findMany({
            where: {
                sourceUserId: userId
            },
            include: {
                targetUser: true
            }
        });

        // Extracting the target users from the following relationships
        const followingUsers = followings.map(follow => follow.targetUser);

        callback(null, followingUsers);
    } catch (error) {
        callback(error, null);
    }
}

async function getNewNotifications(userId, callback) {
    try {
        const notifications = await prisma.notification.findMany({
            where: {
                targetUserId: userId,
                seen: false
            },
            include: {
                sourceUser: true,
                notificationType: true,
                gnam: true
            },
        });

        const finalNotifications = notifications.map(notification => {
            const sourceName = notification.sourceUser.username;
            const gnamId = notification.gnam?.id ?? '';
            const gnamTitle = notification.gnam?.title ?? '';

            return {
                id: notification.id,
                gnamId: gnamId,
                sourceId: notification.sourceUserId,
                content: `${sourceName} ${notification.notificationType.templateText} ${gnamTitle}`,
                createdAt: notification.createdAt,
                sourceImageUri: notification.sourceUser.imageUri
            };
        });

        callback(null, finalNotifications);
    } catch (error) {
        callback(error, null);
    }
}


async function shortListGoals(userId, limit, callback) {
    try {
        const goals = await prisma.goal.findMany({
            where: {
                userId: userId,
                goalType: {
                    isFor: 'user'
                }
            },
            take: limit,
            orderBy: {
                achievedOn: {
                    sort: 'desc',
                    nulls: 'last'
                }
            },
            include: {
                goalType: {
                    select: {
                        templateText: true
                    }
                }
            }
        });

        const formattedGoals = goals.map(goal => ({
            id: goal.id,
            userId: goal.userId,
            content: goal.goalType.templateText
        }));

        callback(null, formattedGoals);
    } catch (error) {
        callback(error, null);
    }
}


async function listGoals(userId, callback) {
    try {
        const goals = await prisma.goal.findMany({
            where: {
                userId: userId
            },
            orderBy: {
                achievedOn: {
                    sort: 'desc',
                    nulls: 'last'
                }
            },
            include: {
                goalType: {
                    select: {
                        templateText: true
                    }
                }
            }
        });

        const formattedGoals = goals.map(goal => ({
            id: goal.id,
            userId: goal.userId,
            content: goal.goalType.templateText,
            gnamId: goal.gnamId
        }));

        callback(null, formattedGoals);
    } catch (error) {
        callback(error, null);
    }
}

async function createNotificationType(name, templateText, callback) {
    try {
        const notificationType = await prisma.notificationType.create({
            data: {
                typeName: name,
                templateText: templateText
            }
        });
        callback(null, notificationType);
    } catch (error) {
        callback(error, null);
    }
}

async function createGoalType(isFor, templateText, callback) {
    try {
        const goalType = await prisma.goalType.create({
            data: {
                isFor: isFor,
                templateText: templateText
            }
        });
        callback(null, goalType);
    } catch (error) {
        callback(error, null);
    }
}

async function getNotificationType(name, callback) {
    try {

        const notificationType = await prisma.notificationType.findUnique({
            where: {
                typeName: name
            }
        });
        callback(null, notificationType);
    } catch (error) {
        callback(error, null);
    }
}

async function createNotification(sourceUser, targetUser, gnamId, notificationType) {
    await prisma.notification.create({
        data: {
            sourceUserId: sourceUser,
            targetUserId: targetUser,
            gnamId: gnamId,
            notificationTypeId: notificationType.id
        }
    });
}

async function deleteNotification(sourceUser, targetUser, gnamId, notificationType) {
    const existingNotification = await prisma.notification.findFirst({
        where: {
            sourceUserId: sourceUser,
            targetUserId: targetUser,
            gnamId: gnamId,
            notificationTypeId: notificationType.id
        }
    });
    if (existingNotification) {
        await prisma.notification.delete({
            where: {
                id: existingNotification.id
            }
        });
    }
}



async function getUserIdFromGnamId(gnamId, callback) {
    try {
        const gnam = await prisma.gnam.findUnique({
            where: {
                id: gnamId
            }
        });
        callback(null, gnam.authorId);
    } catch (error) {
        callback(error, null);
    }
}

async function deleteLike(userId, gnamId, callback) {
    try {
        const likes = await prisma.like.findFirst({
            where: {
                userId: userId,
                gnamId: gnamId
            }
        });
        if (likes == null) return callback(null, null);
        await prisma.like.delete({
            where: {
                id: likes.id
            }
        });
        callback(null, likes);
    } catch (error) {
        callback(error, null);
    }
}

async function createGoal(userId, goalType, gnamId) {
    await prisma.goal.create({
        data: {
            userId: userId,
            goalTypeId: goalType.id,
            gnamId: gnamId
        }
    });
}

async function completeGoal(userId, goalType, gnamId) {
    let goal = null;
    if (gnamId == null) {
        goal = await prisma.goal.findFirst({
            where: {
                userId: userId,
                goalTypeId: goalType.id
            }
        });
    } else {
        goal = await prisma.goal.findFirst({
            where: {
                userId: userId,
                goalTypeId: goalType.id,
                gnamId: gnamId
            }
        });
    }


    if (goal == null) return;

    await prisma.goal.update({
        where: {
            id: goal.id
        },
        data: {
            achievedOn: new Date()
        }
    });
}

async function getUserGnamsCount(userId, callback) {
    try {
        const gnams = await prisma.gnam.findMany({
            where: {
                authorId: userId
            },
            select: {
                id: true
            }
        });
        callback(null, gnams.length);
    } catch (error) {
        callback(error, null);
    }
}

async function getUserGivenLikesCount(userId, callback) {
    try {
        const likes = await prisma.like.findMany({
            where: {
                userId: userId
            },
            select: {
                gnamId: true
            }
        });
        callback(null, likes.length);
    } catch (error) {
        callback(error, null);
    }
}

async function getUserReceivedLikesCount(userId, callback) {
    try {
        const totalLikesCount = await prisma.like.count({
            where: {
                gnam: {
                    authorId: userId
                }
            }
        });

        callback(null, totalLikesCount);
    } catch (error) {
        callback(error, null);
    }
}

async function getGnamLikesCount(gnamId, callback) {
    try {
        const likes = await prisma.like.findMany({
            where: {
                gnamId: gnamId
            },
            select: {
                userId: true
            }
        });
        callback(null, likes.length);
    } catch (error) {
        callback(error, null);
    }
}

async function getGoalType(name, callback) {
    try {
        const goalType = await prisma.goalType.findFirst({
            where: {
                templateText: name
            }
        });
        callback(null, goalType);
    } catch (error) {
        callback(error, null);
    }
}

async function getUserGnams(userId, callback) {
    try {
        const gnams = await prisma.gnam.findMany({
            where: {
                authorId: userId
            },
            include: {
                author: {
                    select: {
                        username: true,
                        imageUri: true
                    }
                }
            }
        });
        const gnamsWithAuthorName = gnams.map(gnam => ({
            id: gnam.id,
            authorId: gnam.authorId,
            title: gnam.title,
            description: gnam.description,
            recipe: gnam.recipe,
            shareCount: gnam.shareCount,
            createdAt: gnam.createdAt,
            authorName: gnam.author.username,
            authorImageUri: gnam.author.imageUri
        }));
        callback(null, gnamsWithAuthorName);
    } catch (error) {
        callback(error, null);
    }
}

async function shareGnam(gnamId, callback) {
    try {
        const updatedGnam = await prisma.gnam.update({
            where: { id: gnamId },
            data: {
                shareCount: {
                    increment: 1
                }
            }
        });

        callback(null, updatedGnam);
    } catch (error) {
        callback(error, null);
    }
}

async function setNotificationsAsRead(notificationId, callback) {
    try {
        await prisma.notification.update({
            where: { id: notificationId },
            data: {
                seen: true
            }
        });

        callback(null, true);
    } catch (error) {
        callback(error, null);
    }
}

async function getListOfUsersThatSavedGnam(gnamId, callback) {
    try {
        const users = await prisma.user.findMany({
            where: {
                likes: {
                    some: {
                        gnamId: gnamId
                    }
                }
            },
        });
        callback(null, users);
    } catch (error) {
        callback(error, null);
    }
}

async function getSavedGnams(userId, callback) {
    try {
        const gnams = await prisma.gnam.findMany({
            where: {
                likes: {
                    some: {
                        userId: userId
                    }
                }
            },
            include: {
                author: {
                    select: {
                        username: true,
                        imageUri: true
                    }
                }
            }
        });
        const gnamsWithAuthorName = gnams.map(gnam => ({
            id: gnam.id,
            authorId: gnam.authorId,
            title: gnam.title,
            description: gnam.description,
            recipe: gnam.recipe,
            shareCount: gnam.shareCount,
            createdAt: gnam.createdAt,
            authorName: gnam.author.username,
            authorImageUri: gnam.author.imageUri
        }));
        callback(null, gnamsWithAuthorName);
    } catch (error) {
        callback(error, null);
    }
}

async function getGnamTimeline(userId, offset, callback) {
    try {
        const gnams = await prisma.gnam.findMany({
            where: {
                authorId: {
                    not: userId
                }
            },
            take: 10,
            skip: offset,
            orderBy: {
                createdAt: 'desc'
            },
            include: {
                author: {
                    select: {
                        username: true,
                        imageUri: true
                    }
                }
            }
        });

        const gnamsWithAuthorName = gnams.map(gnam => ({
            id: gnam.id,
            authorId: gnam.authorId,
            title: gnam.title,
            description: gnam.description,
            recipe: gnam.recipe,
            shareCount: gnam.shareCount,
            createdAt: gnam.createdAt,
            authorName: gnam.author.username,
            authorImageUri: gnam.author.imageUri
        }));
        console.log(gnamsWithAuthorName)
        callback(null, gnamsWithAuthorName);
    } catch (error) {
        callback(error, null);
    }
}


module.exports = {
    login,
    addUser,
    listUsers,
    getUser,
    changeUserInfo,
    addGnam,
    saveGnam,
    getGnam,
    listGnams,
    searchGnams,
    getNewNotifications,
    shortListGoals,
    listGoals,
    completeGoal,
    toggleFollowUser,
    didUserLike,
    doUserFollowUser,
    listFollower,
    listFollowing,
    getNotificationType,
    createNotification,
    getUserIdFromGnamId,
    deleteLike,
    deleteNotification,
    createGoal,
    completeGoal,
    getUserGnamsCount,
    getUserGivenLikesCount,
    getUserReceivedLikesCount,
    getGnamLikesCount,
    getGoalType,
    getUserGnams,
    createGoalType,
    createNotificationType,
    shareGnam,
    setNotificationsAsRead,
    getListOfUsersThatSavedGnam,
    getSavedGnams,
    getGnamTimeline,
    updateUserImage
};

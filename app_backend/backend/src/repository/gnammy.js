const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcrypt');
const prisma = new PrismaClient();

async function addUser(username, password, callback) {
    try {
        const hashedPassword = await bcrypt.hash(password, 10);
        const createdUser = await prisma.user.create({
            data: {
                username: username,
                password: hashedPassword
            }
        });
        callback(null, createdUser);
    } catch (error) {
        callback(error, null);
    }
}

async function listUsers(callback) {
    try {
        const users = await prisma.user.findMany({
            take: 5
        });
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

async function changeUserInfo(userId, username, password, callback) {
    try {
        const hashedPassword = undefined;
        if(username?.length > 255) return callback('Username too long', null);
        if(password?.length > 255) return callback('Password too long', null);
        if(password != undefined) hashedPassword = await bcrypt.hash(password, 10);
        const updatedUser = await prisma.user.update({
            where: {
                id: userId
            },
            data: {
                username: username,
                password: hashedPassword
            }
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
                }
            },
            orderBy: {
                likes: {
                    _count: 'desc'
                }
            }
        });

        const filteredGnams = gnams.filter(gnam => gnam._count.likes >= numberOfLikes);

        callback(null, filteredGnams);
    } catch (error) {
        callback(error, null);
    }
}


async function getGnam(gnamId, callback) {
    try {
        const gnam = await prisma.gnam.findUnique({
            where: {
                id: gnamId
            }
        });
        callback(null, gnam);
    } catch (error) {
        callback(error, null);
    }
}

async function listGnams(callback) {
    try {
        const gnams = await prisma.gnam.findMany({
            take: 5
        });
        callback(null, gnams);
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
                sourceUserId: userId,
                seen: false
            }
        });
        callback(null, notifications);
    } catch (error) {
        callback(error, null);
    }
}

async function shortListGoals(userId, limit, callback) {

}

async function completeListGoals(userId, callback) {

}

async function completeGoal(userId, goalId, callback) {
    try {
        const Goal = await prisma.Goal.findUnique({
            where: {
                id: goalId,
                userId: userId
            }
        });

        if (!Goal) {
            throw new Error('Goal not found');
        }

        await prisma.Goal.update({
            where: {
                id: goalId
            },
            data: {
                completed: true
            }
        });

        callback(null, Goal);
    } catch (error) {
        callback(error, null);
    }
}

module.exports = {
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
    completeListGoals,
    completeGoal,
    toggleFollowUser,
    didUserLike,
    doUserFollowUser,
    listFollower,
    listFollowing
};

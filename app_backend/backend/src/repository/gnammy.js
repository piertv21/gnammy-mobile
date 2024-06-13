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

async function listUsers(limit, callback) {
    try {
        const users = await prisma.user.findMany({
            take: limit
        });
        callback(null, users);
    } catch (error) {
        callback(error, null);
    }
}

async function changeUserInfo(userId, username, password, callback) {
    try {
        const hashedPassword = await bcrypt.hash(password, 10);
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

async function changeUserImage(userId, image, callback) {
    // TODO
}

async function addGnam(authorId, title, short_description, full_recipe, image, callback) {
    try {
        const createdGnam = await prisma.gnam.create({
            data: {
                authorId: authorId,
                title: title,
                short_description: short_description,
                full_recipe: full_recipe
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

async function searchGnams(keywords, dateFrom, dateTo, numberOfLikes, callback) {
    // Controlla e imposta valori predefiniti per i parametri
    keywords = keywords || '';
    dateFrom = dateFrom || new Date(0); // Imposta dateFrom a una data molto vecchia
    dateTo = dateTo || new Date(); // Imposta dateTo alla data corrente
    numberOfLikes = numberOfLikes || 0; // Imposta il numero di like minimo a 0

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
                        short_description: {
                            contains: keywords
                        }
                    },
                    {
                        full_recipe: {
                            contains: keywords
                        }
                    }
                ],
                createdAt: {
                    gte: dateFrom,
                    lte: dateTo
                },
                likes: {
                    gte: numberOfLikes
                }
            },
            orderBy: {
                likes: 'desc'
            }
        });
        callback(null, gnams);
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

async function listGnam(callback) {
    try {
        const gnams = await prisma.gnam.findMany({
            take: 5
        });
        callback(null, gnams);
    } catch (error) {
        callback(error, null);
    }
}

async function toggleFollowUser(userId, gnamId, callback) {
    try {
        const followUser = await prisma.followUser.findUnique({
            where: {
                userId: userId,
                gnamId: gnamId
            }
        });
        if (followUser) {
            await prisma.followUser.delete({
                where: {
                    userId: userId,
                    gnamId: gnamId
                }
            });
        } else {
            await prisma.followUser.create({
                data: {
                    userId: userId,
                    gnamId: gnamId
                }
            });
        }
        callback(null, followUser);
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

async function shortListAchievements(userId, callback) {
    try {
        const userType = await prisma.goals_type.findUnique({
            where: {
                is_for: 'user'
            }
        });

        if (!userType) {
            throw new Error('User type not found');
        }

        const achievements = await prisma.achievement.findMany({
            where: {
                goalTypeId: userType.id,
                userId: userId
            }
        });

        callback(null, achievements);
    } catch (error) {
        callback(error, null);
    }
}

async function completeListAchievements(userId, callback) {
    try {
        const achievement = await prisma.achievement.findUnique({
            where: {
                userId: userId
            }
        });

        callback(null, achievements);
    } catch (error) {
        callback(error, null);
    }
}

async function completeAchievement(userId, achievementId, callback) {
    try {
        const achievement = await prisma.achievement.findUnique({
            where: {
                id: achievementId
            }
        });

        if (!achievement) {
            throw new Error('Achievement not found');
        }

        await prisma.achievement.update({
            where: {
                id: achievementId
            },
            data: {
                completed: true
            }
        });

        callback(null, achievement);
    } catch (error) {
        callback(error, null);
    }
}

module.exports = {
    addUser,
    listUsers
};

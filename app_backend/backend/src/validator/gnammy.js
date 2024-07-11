const Joi = require('joi')

const login = {
    body: Joi.object().keys({
        username: Joi.string().required(),
        password: Joi.string().required()
    }),
}

const addUser = {
    body: Joi.object().keys({
        username: Joi.string().required(),
        password: Joi.string().required(),
        location: Joi.string().optional()
    }),
    file: Joi.object().keys({
        fieldname: Joi.string().required(),
        originalname: Joi.string().required(),
        encoding: Joi.string().required(),
        mimetype: Joi.string().required(),
        destination: Joi.string().required(),
        filename: Joi.string().required(),
        path: Joi.string().required(),
        size: Joi.number().required()
    })
}

const changeUserInfo = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    }),
    body: Joi.object().keys({
        username: Joi.string().optional(),
        password: Joi.string().optional(),
        location: Joi.string().optional()
    }),
    file: Joi.optional(Joi.object().keys({
        fieldname: Joi.string().required(),
        originalname: Joi.string().required(),
        encoding: Joi.string().required(),
        mimetype: Joi.string().required(),
        destination: Joi.string().required(),
        filename: Joi.string().required(),
        path: Joi.string().required(),
        size: Joi.number().required()
    }))
}

const getUser = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const addGnam = {
    body: Joi.object().keys({
        authorId: Joi.string().required(),
        title: Joi.string().required(),
        short_description: Joi.string().required(),
        full_recipe: Joi.string().required(),
    }),
    file: Joi.optional(Joi.object().keys({
        fieldname: Joi.string().required(),
        originalname: Joi.string().required(),
        encoding: Joi.string().required(),
        mimetype: Joi.string().required(),
        destination: Joi.string().required(),
        filename: Joi.string().required(),
        path: Joi.string().required(),
        size: Joi.number().required()
    }))
}

const postLike = {
    body: Joi.object().keys({
        userId: Joi.string().required(),
        gnamId: Joi.string().required(),
    }),
}

const deleteLike = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
        gnamId: Joi.string().required(),
    }),
}

const getLike = {
    body: Joi.object().keys({
        userId: Joi.string().required(),
        gnamId: Joi.string().required(),
    }),
}

const searchGnams = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
        keywords: Joi.string().optional().allow(''),
        dateFrom: Joi.date().optional().allow(''),
        dateTo: Joi.date().optional().allow(''),
        numberOfLikes: Joi.number().optional().allow(''),
    }),
};


const getGnam = {
    params: Joi.object().keys({
        gnamId: Joi.string().required(),
    }),
}

const toggleFollowUser = {
    body: Joi.object().keys({
        sourceUserId: Joi.string().required(),
        targetUserId: Joi.string().required(),
    }),
}

const listFollower = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const listFollowing = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const doUserFollowUser = {
    params: Joi.object().keys({
        sourceUserId: Joi.string().required(),
        targetUserId: Joi.string().required(),
    }),
}

const getNewNotifications = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const shortListGoals = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
        limit: Joi.number().required(),
    })
}

const listGoals = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const completeGoal = {
    params: Joi.object().keys({
        goalId: Joi.string().required(),
    }),
    body: Joi.object().keys({
        userId: Joi.string().required(),
    })
}

const shareGnam = {
    params: Joi.object().keys({
        gnamId: Joi.string().required(),
    })
}

const getUserGnams = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    })
}

const setNotificationsAsRead = {
    params: Joi.object().keys({
        notificationId: Joi.string().required(),
    })
}

const getListOfUsersThatSavedGnam = {
    params: Joi.object().keys({
        gnamId: Joi.string().required(),
    })
}

const getSavedGnams = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    })
}

const getGnamTimeline = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
        offset: Joi.number().required(),
    })
}

module.exports = {
    login,
    addUser,
    getUser,
    changeUserInfo,
    addGnam,
    postLike,
    getLike,
    searchGnams,
    getGnam,
    toggleFollowUser,
    getNewNotifications,
    shortListGoals,
    listGoals,
    completeGoal,
    doUserFollowUser,
    listFollower,
    listFollowing,
    deleteLike,
    shareGnam,
    getUserGnams,
    setNotificationsAsRead,
    getListOfUsersThatSavedGnam,
    getSavedGnams,
    getGnamTimeline
}

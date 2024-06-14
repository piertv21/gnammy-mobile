const Joi = require('joi')

const addUser = {
    query: Joi.object().keys({
        username: Joi.string().required(),
        password: Joi.string().required()
    }),
}

const listUsers = { }

const changeUserInfo = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
        username: Joi.string().optional(),
        password: Joi.string().optional(),
        image: Joi.optional(),
    }),
}

const getUser = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const addGnam = {
    query: Joi.object().keys({
        authorId: Joi.string().required(),
        title: Joi.string().required(),
        short_description: Joi.string().required(),
        full_recipe: Joi.string().required(),
        image: Joi.optional(),
    }),
}

const saveGnam = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
        gnamId: Joi.string().required(),
    }),
}

const searchGnams = {
    query: Joi.object().keys({
        keywords: Joi.string().optional(),
        dateFrom: Joi.date().optional(),
        dateTo: Joi.date().optional(),
        numberOfLikes: Joi.number().optional(),
    }),
}

const getGnam = {
    query: Joi.object().keys({
        gnamId: Joi.string().required(),
    }),
}

const listGnams = { }

const toggleFollowUser = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
        gnamId: Joi.string().required(),
    }),
}

const getNewNotifications = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const shortListGoals = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
    })
}

const completeListGoals = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
    })
}

const completeGoal = {
    query: Joi.object().keys({
        userId: Joi.string().required(),
        goalId: Joi.string().required(),
    })
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
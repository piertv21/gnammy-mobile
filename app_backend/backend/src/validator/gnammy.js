const { query } = require('express')
const Joi = require('joi')

const addUser = {
    body: Joi.object().keys({
        username: Joi.string().required(),
        password: Joi.string().required()
    }),
}

const listUsers = { }

const changeUserInfo = {
    params: Joi.object().keys({
        userId: Joi.string().required(),
    }),
    body: Joi.object().keys({
        username: Joi.string().optional(),
        password: Joi.string().optional(),
        image: Joi.optional(),
    }),
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
        image: Joi.optional(),
    }),
}

const postLike = {
    body: Joi.object().keys({
        userId: Joi.string().required(),
        gnamId: Joi.string().required(),
    }),
}

const deleteLike = {
    body: Joi.object().keys({
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
    body: Joi.object().keys({
        keywords: Joi.string().optional(),
        dateFrom: Joi.date().optional(),
        dateTo: Joi.date().optional(),
        numberOfLikes: Joi.number().optional(),
    }),
}

const getGnam = {
    params: Joi.object().keys({
        gnamId: Joi.string().required(),
    }),
}

const listGnams = { }

const toggleFollowUser = {
    body: Joi.object().keys({
        sourceUser: Joi.string().required(),
        targetUser: Joi.string().required(),
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
    body: Joi.object().keys({
        sourceUser: Joi.string().required(),
        targetUser: Joi.string().required(),
    }),
}

const getNewNotifications = {
    body: Joi.object().keys({
        userId: Joi.string().required(),
    }),
}

const shortListGoals = {
    body: Joi.object().keys({
        userId: Joi.string().required(),
    })
}

const listGoals = {
    params: Joi.object().keys({
        limit: Joi.number().required(),
    }),
    body: Joi.object().keys({
        userId: Joi.string().required(),
    })
}

const completeGoal = {
    body: Joi.object().keys({
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
    postLike,
    getLike,
    searchGnams,
    getGnam,
    listGnams,
    toggleFollowUser,
    getNewNotifications,
    shortListGoals,
    listGoals,
    completeGoal,
    doUserFollowUser,
    listFollower,
    listFollowing,
    deleteLike
}
const Joi = require('joi')

const addUser = {
    query: Joi.object().keys({
        username: Joi.string().required(),
        password: Joi.string().required(),
        rpassword: Joi.string().required()
    }),
}

const listUsers = {
    query: Joi.object().keys({
        limit: Joi.number().required(),
    }),
}

module.exports = {
    addUser,
    listUsers
}
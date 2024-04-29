const Joi = require('joi')

const addUser = {
    query: Joi.object().keys({
        username: Joi.string().required(),
        password: Joi.string().required(),
        rpassword: Joi.string().required()
    }),
}

module.exports = {
    addUser
}
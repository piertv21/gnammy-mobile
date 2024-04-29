const Joi = require('joi')

const listTasks = {
    query: Joi.object().keys({
        sortBy: Joi.string(),
        limit: Joi.number().integer().default(10),
        page: Joi.number().integer().default(1),
    }),
}

const addTask = {
    body: Joi.object().keys({
        title: Joi.string().required(),
        description: Joi.string().required(),
    }),
}

const editTask = {
    body: Joi.object().keys({
        title: Joi.string().required(),
        description: Joi.string().required(),
    }),
    params: Joi.object().keys({
        id: Joi.string().min(36).max(36).required(),
    }),
}

const deleteTask = {
    params: Joi.object().keys({
        id: Joi.string().min(36).max(36).required(),
    }),
}

module.exports = {
    listTasks,
    addTask,
    editTask,
    deleteTask
}
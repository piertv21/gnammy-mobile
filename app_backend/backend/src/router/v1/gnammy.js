const express = require('express')
const validate = require('../../middleware/validator')
const taskValidator = require('../../validator/gnammy')
const taskController = require('../../controller/gnammy')

const router = express.Router()

// Example of middleware
router.use((req, res, next) => {
    console.log(`Route called: ${req.method} - ${req.originalUrl}`)
    next()
})

// Add
router.post('/task/', validate(taskValidator.addTask), taskController.addTask)

// Edit
router.put('/task/:id', validate(taskValidator.editTask), taskController.editTask)

// Delete
router.delete('/task/:id', validate(taskValidator.deleteTask), taskController.deleteTask)

// List all tasks
router.get('/tasks/', validate(taskValidator.listTasks), taskController.listTasks)

module.exports = router
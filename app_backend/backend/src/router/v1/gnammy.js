const express = require('express')
const validate = require('../../middleware/validator')
const gnammyValidator = require('../../validator/gnammy')
const gnammyController = require('../../controller/gnammy')

const router = express.Router()

// Middleware
router.use((req, res, next) => {
    console.log(`Route called: ${req.method} - ${req.originalUrl}`)
    next()
})

// Add user
router.post('/addUser/', validate(gnammyValidator.addUser), gnammyController.addUser)

module.exports = router
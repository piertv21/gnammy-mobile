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

// List users
router.get('/listUsers/', validate(gnammyValidator.listUsers), gnammyController.listUsers)

// Change user info
router.put('/changeUserInfo/', validate(gnammyValidator.changeUserInfo), gnammyController.changeUserInfo)

// Change user image
router.put('/changeUserImage/', validate(gnammyValidator.changeUserImage), gnammyController.changeUserImage)

// Add gnam
router.post('/addGnam/', validate(gnammyValidator.addGnam), gnammyController.addGnam)

// Save gnam (like)
router.post('/saveGnam/', validate(gnammyValidator.saveGnam), gnammyController.saveGnam)

// get gnam
router.get('/getGnam/', validate(gnammyValidator.getGnam), gnammyController.getGnam)

// list gnam
router.get('/listGnam/', validate(gnammyValidator.listGnam), gnammyController.listGnam)

// Search gnams
router.get('/searchGnams/', validate(gnammyValidator.searchGnams), gnammyController.searchGnams)

// Toggle follow user
router.post('/toggleFollowUser/', validate(gnammyValidator.toggleFollowUser), gnammyController.toggleFollowUser)

// Request notifications
router.post('/getNewNotifications/', validate(gnammyValidator.getNewNotifications), gnammyController.getNewNotifications)

// List brief achievements
router.get('/shortListAchievements/', validate(gnammyValidator.shortListAchievements), gnammyController.shortListAchievements)

// List complete achievements
router.get('/completeListAchievements/', validate(gnammyValidator.completeListAchievements), gnammyController.completeListAchievements)

// Add achievement
router.post('/completeAchievement/', validate(gnammyValidator.completeAchievement), gnammyController.completeAchievement)

module.exports = router
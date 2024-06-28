const express = require('express')
const validate = require('../../middleware/validator')
const gnammyValidator = require('../../validator/gnammy')
const gnammyController = require('../../controller/gnammy')
const userMulter = require('../../middleware/userImageHandler')
const gnamMulter = require('../../middleware/gnamImageHandler')

const router = express.Router()
console.log('Router created')

// Middleware
router.use((req, res, next) => {
    console.log(`Route called: ${req.method} - ${req.originalUrl}`)
    next()
})

// Add user
router.post('/user/', userMulter.single('image'), validate(gnammyValidator.addUser), gnammyController.addUser)

// List users
router.get('/user/', gnammyController.listUsers)

// Get user
router.get('/user/:userId', validate(gnammyValidator.getUser), gnammyController.getUser)

// Get user gnams
router.get('/gnamsOf/:userId', validate(gnammyValidator.getUserGnams), gnammyController.getUserGnams)

// Change user info
router.patch('/user/:userId', userMulter.single('image'), validate(gnammyValidator.changeUserInfo), gnammyController.changeUserInfo)

// Save gnam (like)
router.post('/like/', validate(gnammyValidator.postLike), gnammyController.postLike)

// Delete like
router.delete('/like/', validate(gnammyValidator.deleteLike), gnammyController.deleteLike)

// Ritorna booleano se l'utente ha messo like
router.get('/like/', validate(gnammyValidator.getLike), gnammyController.getLike)

// Add gnam
router.post('/gnam/', gnamMulter.single('image'), validate(gnammyValidator.addGnam), gnammyController.addGnam)

// get gnam
router.get('/gnam/:gnamId', validate(gnammyValidator.getGnam), gnammyController.getGnam)

// list gnam
router.get('/gnam/', gnammyController.listGnams)

// Search gnams
router.get('/search/', validate(gnammyValidator.searchGnams), gnammyController.searchGnams)

// Toggle follow user
router.post('/follower/', validate(gnammyValidator.toggleFollowUser), gnammyController.toggleFollowUser)

// get follower of user form userId
router.get('/follower/:userId', validate(gnammyValidator.listFollower), gnammyController.listFollower)

// get follower of user form userId
router.get('/following/:userId', validate(gnammyValidator.listFollowing), gnammyController.listFollowing)

// Gets if users follow another user
router.get('/follower/', validate(gnammyValidator.doUserFollowUser), gnammyController.doUserFollowUser)

// Request notifications
router.get('/notifications/:userId', validate(gnammyValidator.getNewNotifications), gnammyController.getNewNotifications)

// Set notifications as read
router.post('/notification/:notificationId', validate(gnammyValidator.setNotificationsAsRead), gnammyController.setNotificationsAsRead)

// List brief Goals
router.get('/goals/:userId/:limit', validate(gnammyValidator.shortListGoals), gnammyController.shortListGoals)

// List complete Goals
router.get('/goals/:userId/', validate(gnammyValidator.listGoals), gnammyController.listGoals)

// share gnam
router.post('/shareGnam/', validate(gnammyValidator.shareGnam), gnammyController.shareGnam)

// get user image
router.get('/image/user/:userId', gnammyController.getUserImage)

// get gnam image
router.get('/image/gnam/:gnamId', gnammyController.getGnamImage)

// get listOfUsersThatSavedGnam
router.get('/gnam/savedBy/:gnamId', validate(gnammyValidator.getListOfUsersThatSavedGnam), gnammyController.getListOfUsersThatSavedGnam)

module.exports = router
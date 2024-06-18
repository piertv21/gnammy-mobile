const express = require('express')
const validate = require('../../middleware/validator')
const gnammyValidator = require('../../validator/gnammy')
const gnammyController = require('../../controller/gnammy')

const router = express.Router()
console.log('Router created')

// Middleware
router.use((req, res, next) => {
    console.log(`Route called: ${req.method} - ${req.originalUrl}`)
    next()
})

// Add user
router.post('/user/', validate(gnammyValidator.addUser), gnammyController.addUser)

// List users
router.get('/user/', gnammyController.listUsers)

// Get user
router.get('/user/:userId', validate(gnammyValidator.getUser), gnammyController.getUser)

// Change user info
router.patch('/user/:userId', validate(gnammyValidator.changeUserInfo), gnammyController.changeUserInfo)

// Save gnam (like)
router.post('/like/', validate(gnammyValidator.postLike), gnammyController.postLike)

// Delete like
router.delete('/like/', validate(gnammyValidator.deleteLike), gnammyController.deleteLike)

// Ritorna booleano se l'utente ha messo like
router.get('/like/', validate(gnammyValidator.getLike), gnammyController.getLike)

// Add gnam
router.post('/gnam/', validate(gnammyValidator.addGnam), gnammyController.addGnam)

// get gnam
router.get('/gnam/:gnamId', validate(gnammyValidator.getGnam), gnammyController.getGnam)

// list gnam
router.get('/gnam/', validate(gnammyValidator.listGnams), gnammyController.listGnams)

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

// Request notifications, TODO tutto in pratica
router.post('/getNewNotifications/', validate(gnammyValidator.getNewNotifications), gnammyController.getNewNotifications)

// List brief Goals, TODO tutto in pratica
router.get('/goal/:limit', validate(gnammyValidator.shortListGoals), gnammyController.shortListGoals)

// List complete Goals, TODO tutto in pratica
router.get('/goals/', validate(gnammyValidator.completeListGoals), gnammyController.completeListGoals)

// complete goal, TODO tutto in pratica
router.post('/goal/', validate(gnammyValidator.completeGoal), gnammyController.completeGoal)

module.exports = router
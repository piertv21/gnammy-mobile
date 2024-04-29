const express = require('express')
const cors = require('cors')

const port = 3000
const server = express()

server.use(express.json());
server.use(cors());
server.options('*', cors());

const gnammyRouter = require('./router/v1/gnammy')
server.use('/api/v1', gnammyRouter)

server.listen(port)
console.log(`Server started on port ${port}...`)
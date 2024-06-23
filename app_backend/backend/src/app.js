const express = require('express')
const cors = require('cors')

const port = 3000
const server = express()

server.use(express.json());
server.use(cors({
    origin: '*', // O specifica i domini permessi
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type', 'Authorization']
}));
server.options('*', cors());

const gnammyRouter = require('./router/v1/gnammy')
server.use('', gnammyRouter)

server.listen(port)
console.log(`Backend started on port ${port}...`)

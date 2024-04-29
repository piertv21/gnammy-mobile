const { Client } = require('pg');
const { v4: uuidv4 } = require('uuid');

const dbConfig = {
    user: 'postgres',
    host: 'localhost',
    database: 'gnammy',
    password: 'password',
    port: 5432,
};

const addUser = async (client, username) => {
    const client = new Client(dbConfig);
    await client.connect();
    
    const query = {
        text: 'INSERT INTO users (id, username) VALUES ($1, $2)',
        values: [uuidv4(), username],
    };
    

module.exports = {
    addTask,
    editTask,
    deleteTask,
    listTasks,
    initializeDatabase
};
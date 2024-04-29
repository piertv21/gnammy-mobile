const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcrypt');
const prisma = new PrismaClient();

async function addUser(username, password, callback) {
    try {
        const hashedPassword = await bcrypt.hash(password, 10);
        const createdUser = await prisma.user.create({
            data: {
                username: username,
                password: hashedPassword
            }
        });
        callback(null, createdUser);
    } catch (error) {
        callback(error, null);
    }
}

async function listUsers(limit, callback) {
    try {
        const users = await prisma.user.findMany({
            take: limit
        });
        callback(null, users);
    } catch (error) {
        callback(error, null);
    }
}

module.exports = {
    addUser,
    listUsers
};

const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

async function addUser(username, password, callback) {
    try {
        const createdUser = await prisma.users.create({
            data: {
                username: username,
                password: password
            }
        });
        callback(null, createdUser);
    } catch (error) {
        callback(error, null);
    }
}

module.exports = {
    addUser
};

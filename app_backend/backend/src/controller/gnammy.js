const httpStatus = require("http-status");
const taskRepository = require('../repository/gnammy')

let initializedDb = false;

if (!initializedDb) {
    taskRepository.initializeDatabase();
    initializedDb = true;
}

const addTask = (req, res) => {
    taskRepository.addTask(req.body.title, req.body.description, (err, task) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: 'Errore durante l\'inserimento del task.' });
        }
        res.status(httpStatus.OK).json({ data: task });
    });
}

const editTask = (req, res) => {
    taskRepository.editTask(req.params.id, req.body.title, req.body.description, (err, task) => {
        if (err) {
            return res.status(
                err.message == 'Task not found' ? httpStatus.NOT_FOUND :
                    httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: err });
        }
        return res.status(httpStatus.OK).json({ data: task });
    });
}

const deleteTask = (req, res) => {
    taskRepository.deleteTask(req.params.id, (err, task) => {
        if (err) {
            return res.status(err.message == 'Task not found' ? httpStatus.NOT_FOUND :
                httpStatus.INTERNAL_SERVER_ERROR)
                .json({ id: err.id, error: err.message });
        }
        res.status(httpStatus.OK).json({ data: task });
    });
}

const listTasks = (req, res) => {
    taskRepository.listTasks(req.query.limit, req.query.page, req.query.sortBy, (err, tasks) => {
        if (err) {
            return res.status(httpStatus.INTERNAL_SERVER_ERROR)
                .json({ error: err.message });
        }
        res.status(httpStatus.OK).json({ data: tasks });
    })
}

module.exports = {
    listTasks,
    addTask,
    editTask,
    deleteTask
}
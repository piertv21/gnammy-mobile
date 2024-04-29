import express, { Express } from "express";
import cors from "cors";
import TaskRouter from "./router/v1/taskRouter";
import TaskController from "./controller/taskController";
import TaskRepository from "./repository/taskRepository";

const port: string | number = 3000;
const server: Express = express();

const middlewares: any[] = [express.json(), cors()];
server.use(middlewares);
server.options("*", cors());

const taskRepository: TaskRepository = new TaskRepository();
const taskController: TaskController = new TaskController(taskRepository);
const taskRouter: TaskRouter = new TaskRouter(taskController);

server.use("/api/v1", taskRouter.getRouter());

server.listen(port, () => {
  console.log(`Server started on port ${port}...`);
});

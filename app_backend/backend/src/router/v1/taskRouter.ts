import express, { Router } from "express";
import TaskController from "../../controller/taskController";
import validate from "../../middleware/validator";
import ITaskRouter from "./taskRouter.interface";
import { addTask, deleteTask, editTask, listTasks } from "../../validator/task";

class TaskRouter implements ITaskRouter {
  private readonly router: Router;
  private readonly taskController: TaskController;

  constructor(taskController: TaskController) {
    this.taskController = taskController;
    this.router = express.Router();
    this.initRoutes();
  }

  private initRoutes(): void {
    this.router.use((req, res, next) => {
      console.log(`Route called: ${req.method} - ${req.originalUrl}`);
      next();
    });

    // Add
    this.router.post("/task/", validate(addTask), this.taskController.addTask);

    // Edit
    this.router.put(
      "/task/:id",
      validate(editTask),
      this.taskController.editTask
    );

    // Delete
    this.router.delete(
      "/task/:id",
      validate(deleteTask),
      this.taskController.deleteTask
    );

    // List
    this.router.get(
      "/tasks/",
      validate(listTasks),
      this.taskController.listTasks
    );
  }

  public getRouter(): Router {
    return this.router;
  }
}

export default TaskRouter;

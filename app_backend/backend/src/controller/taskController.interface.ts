import { Request, Response } from "express";

interface ITaskController {
  addTask(req: Request, res: Response): void;
  editTask(req: Request, res: Response): void;
  deleteTask(req: Request, res: Response): void;
  listTasks(req: Request, res: Response): void;
}

export default ITaskController;

import { Request, Response } from "express";
import { INTERNAL_SERVER_ERROR, OK, NOT_FOUND, CREATED } from "http-status";
import TaskRepository from "../repository/taskRepository";
import { Task } from "../models/task.interface";
import { Query } from "../models/query.interface";
import ITaskController from "./taskController.interface";
import { Prisma, User } from "@prisma/client";

class TaskController implements ITaskController {
  private readonly taskRepository: TaskRepository;

  constructor(taskRepository: TaskRepository) {
    this.taskRepository = taskRepository;
    this.addTask = this.addTask.bind(this);
    this.editTask = this.editTask.bind(this);
    this.deleteTask = this.deleteTask.bind(this);
    this.listTasks = this.listTasks.bind(this);
  }

  private handleResponse(res: Response, err: Error | null, data: any, action: 'addTask' | 'listTask' | 'editTask' | 'deleteTask'): void {
    if (err) {
      if (err.message === "Task not found") {
        res.status(NOT_FOUND).json({ error: err.message });
      } else {
        res.status(INTERNAL_SERVER_ERROR).json({ error: err.message });
      }
    } else {
      let statusCode: number = OK;
      if(action === 'addTask') {
        statusCode = CREATED;
      }
      res.status(statusCode).json({ data });
    }
  }  

  public addTask(req: Request, res: Response): void {
    const { task, user } = req.body;
    this.taskRepository.addTask(user, task, (err, task) => {
      this.handleResponse(res, err, task, 'addTask');
    });
  }

  public editTask(req: Request, res: Response): void {
    const task: Task = req.body;
    task.id = req.params.id;
    this.taskRepository.editTask(task, (err, task) => {
      this.handleResponse(res, err, task, 'editTask');
    });
  }

  public deleteTask(req: Request, res: Response): void {
    const task: Task = req.body;
    task.id = req.params.id;
    this.taskRepository.deleteTask(task, (err, task) => {
      this.handleResponse(res, err, task, 'deleteTask');
    });
  }

  public listTasks(req: Request, res: Response): void {
    const query = req.query;
    this.taskRepository.listTasks(query, (err, tasks) => {
      this.handleResponse(res, err, tasks, 'listTask');
    });
  }
}

export default TaskController;

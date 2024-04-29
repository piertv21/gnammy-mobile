import { Prisma, PrismaClient, User } from "@prisma/client";
import { Task } from "../models/task.interface";
import { Query } from "../models/query.interface";
import ITaskRepository from "./taskRepository.interface";

class TaskManager implements ITaskRepository {
  private readonly prisma: PrismaClient;

  constructor() {
    this.prisma = new PrismaClient();
  }

  public async addTask(
    user: User,
    task: Task,
    callback: (err: Error | null, task: Task | null) => void
  ): Promise<void> {
    try {
      const createdTask: Task = await this.prisma.task.create({
        data: {
          title: task.title,
          description: task.description,
          authorId: user.id,
        },
      });
      callback(null, createdTask);
    } catch (error) {
      callback(error as Error, null);
    }
  }

  public async editTask(
    task: Task,
    callback: (err: Error | null, result: Task | null) => void
  ): Promise<void> {
    try {
      const updatedTask: Task = await this.prisma.task.update({
        where: { id: task.id },
        data: {
          title: task.title,
          description: task.description,
        },
      });
      callback(null, updatedTask);
    } catch (error) {
      if (
        error instanceof Prisma.PrismaClientKnownRequestError &&
        error.code === "P2025"
      ) {
        callback(new Error("Task not found"), null);
      } else {
        callback(error as Error, null);
      }
    }
  }

  public async deleteTask(
    task: Task,
    callback: (err: Error | null, result: Task | null) => void
  ): Promise<void> {
    try {
      const deletedTask: Task = await this.prisma.task.delete({
        where: { id: task.id },
      });
      callback(null, deletedTask);
    } catch (error) {
      if (
        error instanceof Prisma.PrismaClientKnownRequestError &&
        error.code === "P2025"
      ) {
        callback(new Error("Task not found"), null);
      } else {
        callback(error as Error, null);
      }
    }
  }

  public async listTasks(
    query: Query,
    callback: (err: Error | null, result: any) => void
  ): Promise<void> {
    try {
      const { sortBy, page, limit, user } = query;
      const pageNumber: number = page || 1;
      const limitNumber: number = limit || 10;
      const skip: number = (pageNumber - 1) * limitNumber;

      const tasks: Task[] = await this.prisma.task.findMany({
        where: { authorId: user },
        take: limitNumber,
        
        skip: skip,
        orderBy: sortBy ? { [sortBy]: 'asc' } : { updatedAt: 'desc' },
      });
      
      const totalResults: number = await this.prisma.task.count({
        where: { authorId: user },
      });
      const totalPages: number = Math.ceil(totalResults / limitNumber);

      const response = {
        pagination: {
          totalResults,
          currentPage: pageNumber,
          totalPages,
          resultsPerPage: limitNumber,
        },
        data: tasks,
      };

      callback(null, response);
    } catch (error) {
      callback(error as Error, null);
    }
  }
}

export default TaskManager;

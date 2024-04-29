import { Query } from "../models/query.interface";
import { Task } from "../models/task.interface";
import { User } from "../models/user.interface";

interface ITaskRepository {
  addTask(user: User, task: Task, callback: (err: Error | null, task: any) => void): void;
  editTask(task: Task, callback: (err: any, result: any) => void): void;
  deleteTask(task: Task, callback: (err: any, result: any) => void): void;
  listTasks(
    query: Query,
    callback: (err: Error | null, result: any) => void
  ): void;
}

export default ITaskRepository;

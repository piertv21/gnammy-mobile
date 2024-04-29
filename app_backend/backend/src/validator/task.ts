import Joi from "joi";

export interface TaskSchema {
  [key: string]: Joi.ObjectSchema;
}

const listTasks: TaskSchema = {
  query: Joi.object().keys({
    sortBy: Joi.string(),
    limit: Joi.number().integer().default(10),
    page: Joi.number().integer().default(1),
    user: Joi.string().required(),
  })
};

const addTask: TaskSchema = {
  body: Joi.object().keys({
    task: Joi.object().keys({
      title: Joi.string().required(),
      description: Joi.string().required(),
    }),
    user: Joi.object().keys({
      id: Joi.string().required(),
    }),
  }),
};

const editTask: TaskSchema = {
  body: Joi.object().keys({
    title: Joi.string().required(),
    description: Joi.string().required(),
  }),
  params: Joi.object().keys({
    id: Joi.string().min(36).max(36).required(),
  }),
};

const deleteTask: TaskSchema = {
  params: Joi.object().keys({
    id: Joi.string().min(36).max(36).required(),
  }),
};

export { listTasks, addTask, editTask, deleteTask };

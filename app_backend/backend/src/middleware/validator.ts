import Joi from "joi";
import { Request, Response, NextFunction } from "express";
import httpStatus from "http-status";
import pick from "../utils/pick";
import { TaskSchema } from "../validator/task";

interface ValidSchema {
  params?: Joi.Schema;
  query?: Joi.Schema;
  body?: Joi.Schema;
}

const validate =
  (schema: TaskSchema) => (req: Request, res: Response, next: NextFunction) => {
    const validSchema: ValidSchema = pick(schema, ["params", "query", "body"]);
    const object = pick(req, Object.keys(validSchema)) as {
      [key: string]: any;
    };
    const { value, error } = Joi.compile(validSchema as Joi.ObjectSchema)
      .prefs({ errors: { label: "key" }, abortEarly: false })
      .validate(object);

    if (error) {
      const errorMessage = error.details
        .map((details) => details.message)
        .join(", ");
      return res.status(httpStatus.BAD_REQUEST).json({ error: errorMessage });
    }
    Object.assign(req, value);
    return next();
  };

export default validate;

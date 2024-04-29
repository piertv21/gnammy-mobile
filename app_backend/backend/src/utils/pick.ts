/*
 * This function is used to pick the keys from the object
 * and return a new object with the picked keys.
 */
function pick(
  object: Record<string, any>,
  keys: string[]
): Record<string, any> {
  return keys.reduce(
    (obj, key) => {
      if (object && Object.prototype.hasOwnProperty.call(object, key)) {
        obj[key] = object[key];
      }
      return obj;
    },
    {} as Record<string, any>
  );
}

export default pick;

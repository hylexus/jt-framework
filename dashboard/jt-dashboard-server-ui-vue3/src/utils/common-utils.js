export function isSuccess(resp) {
  return resp !== null && resp !== undefined && resp.code === 0
}

export function isEmpty(obj) {
  return obj === null || obj === undefined || obj === ''
}

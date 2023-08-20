function isSuccess(resp) {
  return resp !== null && resp !== undefined && (resp.code === 0)
}

function isEmpty(obj) {
  return obj === null || obj === undefined || obj === ''
}

export default {
  isSuccess, isEmpty
}

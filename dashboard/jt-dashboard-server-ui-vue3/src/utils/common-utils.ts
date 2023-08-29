const isSuccess = (resp) => {
  return resp !== null && resp !== undefined && resp.code === 0
}

const isEmpty = (obj) => {
  return obj === null || obj === undefined || obj === ''
}

export { isSuccess, isEmpty }

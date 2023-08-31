const isSuccess = (resp: any) => {
  return resp !== null && resp !== undefined && resp.code === 0
}

const isEmpty = (obj: any) => {
  return obj === null || obj === undefined || obj === ''
}

export { isSuccess, isEmpty }

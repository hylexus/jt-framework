import request from '@/utils/request'

export function requestServerInstances(params) {
  return request({
    url: '/api/dashboard/instances/all',
    method: 'get',
    params: params
  })
}

export function requestPlayerUrl(params) {
  return request({
    url: '/api/dashboard/1078/stream-address',
    method: 'post',
    data: params
  })
}

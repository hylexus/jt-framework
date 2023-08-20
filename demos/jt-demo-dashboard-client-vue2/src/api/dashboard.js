import request from '@/utils/request'

export function requestServerMetadata(params) {
  return request({
    url: '/api/dashboard/server-metadata',
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

import request from '@/utils/request'

export function requestTerminalList(params) {
  return request({
    url: `/api/dashboard/proxy/${params.instanceId}/jt808/terminal/list`,
    method: 'get',
    params: params
  })
}

export function sendMsg9101(params) {
  return request({
    url: '/api/jt808/send-msg/9101',
    method: 'post',
    data: params
  })
}

export function sendMsg9102(params) {
  return request({
    url: '/api/jt808/send-msg/9102',
    method: 'post',
    data: params
  })
}

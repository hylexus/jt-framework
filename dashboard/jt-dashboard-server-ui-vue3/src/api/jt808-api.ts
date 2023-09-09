import request from '@/utils/request'

export function requestTerminalList(params: any = {}) {
  return request({
    url: `/api/dashboard/proxy/${params.instanceId}/api/dashboard-client/jt808/sessions`,
    method: 'get',
    params: params
  })
}

export function sendMsg9101(params: any = {}) {
  return request({
    url: '/api/jt808/send-msg/9101',
    method: 'post',
    data: params
  })
}

export function sendMsg9102(params = {}) {
  return request({
    url: '/api/jt808/send-msg/9102',
    method: 'post',
    data: params
  })
}

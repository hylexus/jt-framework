import request from '@/utils/request'

export function requestTerminalList(params: any = {}) {
  return request({
    url: `/api/dashboard/proxy/${params.instanceId}/api/dashboard-client/jt1078/sessions?withSubscribers=true`,
    method: 'get',
    params: params
  })
}

export function unSubscribe(params: any = {}) {
  return request({
    url: `/api/dashboard/proxy/${params.instanceId}/api/dashboard-client/jt1078/subscribers/${params.subscriberId}`,
    method: 'delete'
  })
}

export function unSubscribeSessions(params: any = {}) {
  return request({
    url: `/api/dashboard/proxy/${params.instanceId}/api/dashboard-client/jt1078/sessions/${params.sessionId}`,
    method: 'delete'
  })
}

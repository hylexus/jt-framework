import request from '@/utils/request'

export function requestTerminalList(params: any = {}) {
  return request({
    url: `/api/dashboard/proxy/${params.instanceId}/api/dashboard-client/jt1078/sessions?withSubscribers=true`,
    method: 'get',
    params: params
  })
}

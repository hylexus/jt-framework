export interface Registration {
  name?: string
  baseUrl?: string
  source?: string
  host?: string
  tcpPort?: string
  metadata?: string
}
export interface Config {
  instanceId?: string
  host?: string
  httpPort?: string
  tcpPort?: string
  notes?: string
  registration?: Registration
}

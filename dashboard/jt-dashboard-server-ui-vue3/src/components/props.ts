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
  metrics?: any
  status?: any
  registration?: Registration
}

export enum AudioHints {
  AUTO = 'AUTO',
  SILENCE = 'SILENCE',
  G726_S16_LE_MONO = 'G726_S16_LE_MONO',
  G726_S24_LE_MONO = 'G726_S24_LE_MONO',
  G726_S32_LE_MONO = 'G726_S32_LE_MONO',
  G726_S40_LE_MONO = 'G726_S40_LE_MONO',
  ADPCM_IMA_MONO = 'ADPCM_IMA_MONO',
  G711_A_MONO = 'G711_A_MONO',
  G711_U_MONO = 'G711_U_MONO',
  PCM_S16_LE_MONO = 'PCM_S16_LE_MONO',
  PCM_S24_LE_MONO = 'PCM_S24_LE_MONO',
  PCM_S32_LE_MONO = 'PCM_S32_LE_MONO',
  PCM_S40_LE_MONO = 'PCM_S40_LE_MONO',
}

export enum FlvPlayerStatus {
  OFFLINE = 'Offline',
  CONNECTING = 'Connecting',
  RUNNING = 'Running',
  PAUSE = 'Pause'
}
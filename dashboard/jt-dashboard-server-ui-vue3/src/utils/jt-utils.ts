import { AudioHints } from '@/components/props'

const audioHintDesc = new Map<AudioHints, { label: string }>()
audioHintDesc.set(AudioHints.AUTO, { label: '自动' })
audioHintDesc.set(AudioHints.SILENCE, { label: '静音(服务端忽略音频码流)' })

const audioHintDescription = (input: AudioHints) => {
  return audioHintDesc.get(input)?.label
}

export { audioHintDescription }
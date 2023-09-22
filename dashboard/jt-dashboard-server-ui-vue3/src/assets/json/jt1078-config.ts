// type: 1, 音视频/视频
// type: 2, 音频
export const channelConfig = [
  { channel: 1, location: '驾驶员', type: 1, title: '通道1--驾驶员--音视频/视频' },
  { channel: 2, location: '车辆正前方', type: 1, title: '通道2--车辆正前方--音视频/视频' },
  { channel: 3, location: '车前门', type: 1, title: '通道3--车前门--音视频/视频' },
  { channel: 4, location: '车厢前部', type: 1, title: '通道4--车厢前部--音视频/视频' },
  { channel: 5, location: '车厢后部', type: 1, title: '通道5--车厢后部--音视频/视频' },
  { channel: 6, location: '车后门', type: 1, title: '通道6--车后门--音视频/视频' }
  // {channel: 7, location: '行李舱', type: 1, title: '通道7--行李舱--音视频/视频'},
  // {channel: 8, location: '车辆左侧', type: 1, title: '通道8--车辆左侧--音视频/视频'},
  // {channel: 9, location: '车辆右侧', type: 1, title: '通道9--车辆右侧--音视频/视频'},
  // {channel: 10, location: '车辆正后方', type: 1, title: '通道10--车辆正后方--音视频/视频'},
  // {channel: 11, location: '车厢中部', type: 1, title: '通道11--车厢中部--音视频/视频'},
  // {channel: 12, location: '车中部', type: 1, title: '通道12--车中部--音视频/视频'},
  // {channel: 13, location: '驾驶席车门', type: 1, title: '通道13--驾驶席车门--音视频/视频'},
  // {channel: 33, location: '驾驶员', type: 2, title: '通道33--驾驶员--音频'},
  // {channel: 36, location: '车厢前部', type: 2, title: '通道36--车厢前部--音频'},
  // {channel: 37, location: '车厢后部', type: 2, title: '通道37--车厢后部--音频'},
]

const dataType = [
  { value: 0, label: '0--音视频' },
  { value: 1, label: '1--视频' },
  { value: 2, label: '2--双向对讲' },
  { value: 3, label: '3--监听' },
  { value: 4, label: '4--中心广播' },
  { value: 5, label: '5--透传' }
]

const commandType = [
  { value: 0, label: '0--关闭音视频传输指令' },
  { value: 1, label: '1--切换码流' },
  { value: 2, label: '2--暂停该通道所有流的发送' },
  { value: 3, label: '3--回复暂停前流的发送,与暂停前的流类型一致' },
  { value: 4, label: '4--关闭双向对讲' }
]
const mediaTypeToClose = [
  { value: 0, label: '0--关闭该通道有关的音视频数据' },
  { value: 1, label: '1--只关闭该通道有关的音频,保留该通道有关的视频' },
  { value: 2, label: '2--只关闭该通道有关的视频,保留该通道有关的音频' }
]

const Config = { channelConfig, dataType, commandType, mediaTypeToClose }
export default Config

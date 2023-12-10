package io.github.hylexus.jt.dashboard.common.model.dto.jt808;

import lombok.Data;

import java.time.Duration;

@Data
public class DashboardCommand9102Dto {

    private String sim;

    /**
     * 逻辑通道号
     */
    private Integer channelNumber;

    /**
     * <ul>
     *     <li>0--关闭音视频传输指令</li>
     *     <li>1--切换码流</li>
     *     <li>2--暂停该通道所有流的发送</li>
     *     <li>3--恢复暂停前流的发送,与暂停前的流类型一致</li>
     *     <li>4--关闭双向对讲</li>
     * </ul>
     */
    private Integer command;

    /**
     * <ul>
     *     <li>0--关闭该通道有关的音视频数据</li>
     *     <li>1--只关闭该通道有关的音频,保留该通道有关的视频</li>
     *     <li>2--只关闭该通道有关的视频,保留该通道有关的音频</li>
     * </ul>
     */
    private Integer mediaTypeToClose;

    /**
     * <ul>
     *     <li>0-主码流</li>
     *     <li>1-子码流</li>
     * </ul>
     */
    private Integer streamType;

    private Duration timeout = Duration.ofSeconds(10);
}

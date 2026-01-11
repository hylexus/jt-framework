/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.jt.jt808.samples.xtreamcodec.boot2.message;

import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.xtream.codec.core.type.Preset;
import org.jspecify.annotations.NullMarked;

import java.util.StringJoiner;

/**
 * 终端通用应答 0x0001
 *
 * @author hylexus
 */
@NullMarked
@Jt808RequestBody(drivenBy = @DrivenBy(DrivenBy.Type.XTREAM_CODEC))
public record XtreamMessage0001(
        @Preset.JtStyle.Word(desc = "应答流水号 对应的平台消息的流水号") int serverFlowId,
        @Preset.JtStyle.Word(desc = "应答id 对应的平台消息的ID") int serverMessageId,
        @Preset.JtStyle.Byte(desc = "结果 0:成功/确认; 1:失败; 2:消息有误; 3:不支持") short result
) {

    @Override
    public String toString() {
        return new StringJoiner(", ", XtreamMessage0001.class.getSimpleName() + "[", "]")
                .add("serverFlowId=" + serverFlowId)
                .add("serverMessageId=" + serverMessageId + "(0x" + FormatUtils.toHexString(serverMessageId, 4) + ")")
                .add("result=" + result)
                .toString();
    }
}

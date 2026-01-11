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

package io.github.hylexus.jt.jt808.adapter.xtreamcodec.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.StringJoiner;

@ConfigurationProperties(prefix = "xtream.codec")
public class XtreamCodecProperties {

    @NestedConfigurationProperty
    ExpressionProperties expression = new ExpressionProperties();

    public static class ExpressionProperties {
        private ExpressionType type = ExpressionType.SPEL;

        public ExpressionType getType() {
            return type;
        }

        public ExpressionProperties setType(ExpressionType type) {
            this.type = type;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", ExpressionProperties.class.getSimpleName() + "[", "]")
                    .add("expressionType=" + type)
                    .toString();
        }
    }

    public enum ExpressionType {
        /**
         * 基于 Spring SpEL 的实现
         *
         * @see <a href="https://docs.spring.io/spring-framework/reference/core/expressions.html">https://docs.spring.io/spring-framework/reference/core/expressions.html</a>
         */
        SPEL,
        /**
         * 基于 MVEL 2.x 的实现
         *
         * @see <a href="http://mvel.documentnode.com/">http://mvel.documentnode.com/</a>
         */
        MVEL,
        /**
         * 基于 Aviator 的实现
         *
         * @see <a href="https://github.com/killme2008/aviatorscript">https://github.com/killme2008/aviatorscript</a>
         */
        AVIATOR,
        /**
         * 自定义实现: 需要自定义 {@link io.github.hylexus.xtream.codec.core.XtreamExpressionFactory XtreamExpressionFactory}
         */
        CUSTOM,
    }

    public ExpressionProperties getExpression() {
        return expression;
    }

    public XtreamCodecProperties setExpression(ExpressionProperties expression) {
        this.expression = expression;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", XtreamCodecProperties.class.getSimpleName() + "[", "]")
                .add("expression=" + expression)
                .toString();
    }
}

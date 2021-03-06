/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 heimuheimu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.heimuheimu.naiveasync.consumer;

import java.util.List;

/**
 * 异步消息消费者，通常需配合消费管理者使用（例如：{@link com.heimuheimu.naiveasync.kafka.consumer.KafkaConsumerManager}），
 * 消费者通过 {@link #getMessageClass()} 向消费管理者表明期望接收的消息类型，当管理者接收到该类型消息时，将会调用消费者的 {@link #consume(Object)} 方法。
 *
 * <p>建议通过继承 {@link AbstractMessageConsumer} 或 {@link AbstractBatchMessageConsumer} 来实现异步消息消费者。</p>
 *
 * <p><strong>说明：</strong>实现类必须是线程安全的。</p>
 *
 * @param <T> 消息的 {@code Class} 对象
 * @author heimuheimu
 */
public interface AsyncMessageConsumer<T> {

    /**
     * 获得当前消费者期望接收的消息类型。
     *
     * @return 当前消费者期望接收的消息类型
     */
    Class<T> getMessageClass();

    /**
     * 是否为批量消费模式，如果返回 {@code true}，消费管理者应调用 {@link #consume(List)} 进行消息消费，如果返回 {@code false}，
     * 应调用 {@link #consume(Object)} 进行消息消费。
     *
     * @return 批量消费模式
     */
    boolean isBatchMode();

    /**
     * 对接收到的单条异步消息进行消费，消费管理者将重复推送消费失败的消息，直至消费成功。
     *
     * <p><strong>说明：</strong>该方法抛出异常将被消费管理者认为消费失败。</p>
     *
     * <p><strong>注意：</strong>仅在 {@link #isBatchMode()} 返回 {@code false} 时，此方法才会被消费管理者调用。</p>
     *
     * @param message 接收到的异步消息，不会为 {@code null}
     */
    void consume(T message);

    /**
     * 对接收到的异步消息列表进行消费，消费管理者将重复推送消费失败的消息列表，直至消费成功。
     *
     * <p><strong>说明：</strong>该方法抛出异常将被消费管理者认为消费失败。</p>
     *
     * <p><strong>注意：</strong>仅在 {@link #isBatchMode()} 返回 {@code true} 时，此方法才会被消费管理者调用。</p>
     *
     * @param messageList 接收到的异步消息列表，不会为 {@code null}
     */
    void consume(List<T> messageList);
}

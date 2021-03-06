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

package com.heimuheimu.naiveasync.transcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * 基于 Java 序列化方式实现的消息与字节数组转换器。
 *
 * <p><strong>说明：</strong>{@code SimpleMessageTranscoder} 类是线程安全的，可在多个线程中使用同一个实例。</p>
 *
 * @author heimuheimu
 */
public class SimpleMessageTranscoder implements MessageTranscoder {

    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleMessageTranscoder.class);

    @Override
    public byte[] encode(Object message) throws TranscoderException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(message);
            return bos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("Encode message failed: `" + e.getMessage() + "`. Message: `" + message + "`.", e);
            throw new TranscoderException("Encode message failed: `" + e.getMessage() + "`. Message: `" + message + "`.", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T decode(byte[] src) throws TranscoderException {
        try {
            ByteArrayInputStream valueBis = new ByteArrayInputStream(src);
            ObjectInputStream ois = new ObjectInputStream(valueBis);
            return (T) ois.readObject();
        } catch (Exception e) {
            LOGGER.error("Decode message failed: `" + e.getMessage() + "`. Src: `" + Arrays.toString(src) + "`.", e);
            throw new TranscoderException("Decode message failed: `" + e.getMessage() + "`. Src: `" + Arrays.toString(src) + "`.", e);
        }
    }
}

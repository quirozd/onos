/*
 * Copyright 2015 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.netconf.ctl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.onosproject.netconf.NetconfDeviceInfo;
import org.onosproject.netconf.NetconfDeviceOutputEvent;
import org.onosproject.netconf.NetconfDeviceOutputEventListener;
import org.onosproject.netconf.NetconfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Thread that gets spawned each time a session is established and handles all the input
 * and output from the session's streams to and from the NETCONF device the session is
 * established with.
 */
public class NetconfStreamThread extends Thread implements NetconfStreamHandler {

    private static final Logger log = LoggerFactory
            .getLogger(NetconfStreamThread.class);
    private static final String HELLO = "<hello";
    private static final String END_PATTERN = "]]>]]>";
    private static final String RPC_REPLY = "rpc-reply";
    private static final String RPC_ERROR = "rpc-error";
    private static final String NOTIFICATION_LABEL = "<notification>";
    private static final String MESSAGE_ID = "message-id=";

    private PrintWriter outputStream;
    private final InputStream err;
    private final InputStream in;
    private NetconfDeviceInfo netconfDeviceInfo;
    private NetconfSessionDelegate sessionDelegate;
    private NetconfMessageState state;
    private  List<NetconfDeviceOutputEventListener> netconfDeviceEventListeners
            = Lists.newArrayList();

    public NetconfStreamThread(final InputStream in, final OutputStream out,
                               final InputStream err, NetconfDeviceInfo deviceInfo,
                               NetconfSessionDelegate delegate) {
        this.in = in;
        this.err = err;
        outputStream = new PrintWriter(out);
        netconfDeviceInfo = deviceInfo;
        state = NetconfMessageState.NO_MATCHING_PATTERN;
        sessionDelegate = delegate;
        log.debug("Stream thread for device {} session started", deviceInfo);
        start();
    }

    @Override
    public CompletableFuture<String> sendMessage(String request) {
        log.debug("Sending message {} to device {}", request, netconfDeviceInfo);
        outputStream.print(request);
        outputStream.flush();
        return new CompletableFuture<>();
    }

    public enum NetconfMessageState {

        NO_MATCHING_PATTERN {
            @Override
            NetconfMessageState evaluateChar(char c) {
                if (c == ']') {
                    return FIRST_BRACKET;
                } else {
                    return this;
                }
            }
        },
        FIRST_BRACKET {
            @Override
            NetconfMessageState evaluateChar(char c) {
                if (c == ']') {
                    return SECOND_BRACKET;
                } else {
                    return NO_MATCHING_PATTERN;
                }
            }
        },
        SECOND_BRACKET {
            @Override
            NetconfMessageState evaluateChar(char c) {
                if (c == '>') {
                    return FIRST_BIGGER;
                } else {
                    return NO_MATCHING_PATTERN;
                }
            }
        },
        FIRST_BIGGER {
            @Override
            NetconfMessageState evaluateChar(char c) {
                if (c == ']') {
                    return THIRD_BRACKET;
                } else {
                    return NO_MATCHING_PATTERN;
                }
            }
        },
        THIRD_BRACKET {
            @Override
            NetconfMessageState evaluateChar(char c) {
                if (c == ']') {
                    return ENDING_BIGGER;
                } else {
                    return NO_MATCHING_PATTERN;
                }
            }
        },
        ENDING_BIGGER {
            @Override
            NetconfMessageState evaluateChar(char c) {
                if (c == '>') {
                    return END_PATTERN;
                } else {
                    return NO_MATCHING_PATTERN;
                }
            }
        },
        END_PATTERN {
            @Override
            NetconfMessageState evaluateChar(char c) {
                return NO_MATCHING_PATTERN;
            }
        };

        abstract NetconfMessageState evaluateChar(char c);
    }

    public void run() {
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));
            try {
                boolean socketClosed = false;
                StringBuilder deviceReplyBuilder = new StringBuilder();
                while (!socketClosed) {
                    int cInt = bufferReader.read();
                    if (cInt == -1) {
                        log.debug("Netconf device {}  sent error char in session," +
                                          " will need to be reopend", netconfDeviceInfo);
                        NetconfDeviceOutputEvent event = new NetconfDeviceOutputEvent(
                                NetconfDeviceOutputEvent.Type.DEVICE_UNREGISTERED,
                                null, null, Optional.of(-1), netconfDeviceInfo);
                        netconfDeviceEventListeners.forEach(
                                listener -> listener.event(event));
                        socketClosed = true;
                    }
                    char c = (char) cInt;
                    state = state.evaluateChar(c);
                    deviceReplyBuilder.append(c);
                    if (state == NetconfMessageState.END_PATTERN) {
                        String deviceReply = deviceReplyBuilder.toString();
                        if (deviceReply.equals(END_PATTERN)) {
                            socketClosed = true;
                            NetconfDeviceOutputEvent event = new NetconfDeviceOutputEvent(
                                    NetconfDeviceOutputEvent.Type.DEVICE_UNREGISTERED,
                                    null, null, Optional.of(-1), netconfDeviceInfo);
                            netconfDeviceEventListeners.forEach(
                                    listener -> listener.event(event));
                            this.interrupt();
                        } else {
                            deviceReply = deviceReply.replace(END_PATTERN, "");
                            if (deviceReply.contains(RPC_REPLY) ||
                                    deviceReply.contains(RPC_ERROR) ||
                                    deviceReply.contains(HELLO)) {
                                NetconfDeviceOutputEvent event = new NetconfDeviceOutputEvent(
                                        NetconfDeviceOutputEvent.Type.DEVICE_REPLY,
                                        null, deviceReply, getMsgId(deviceReply), netconfDeviceInfo);
                                sessionDelegate.notify(event);
                                netconfDeviceEventListeners.forEach(
                                        listener -> listener.event(event));
                            } else if (deviceReply.contains(NOTIFICATION_LABEL)) {
                                final String finalDeviceReply = deviceReply;
                                netconfDeviceEventListeners.forEach(
                                        listener -> listener.event(new NetconfDeviceOutputEvent(
                                                NetconfDeviceOutputEvent.Type.DEVICE_NOTIFICATION,
                                                null, finalDeviceReply, getMsgId(finalDeviceReply),
                                                netconfDeviceInfo)));
                            } else {
                                log.info("Error on replay from device {} ", deviceReply);
                            }
                            deviceReplyBuilder.setLength(0);
                        }
                    }
                }
            } catch (IOException e) {
                log.warn("Error in reading from the session for device {} ", netconfDeviceInfo, e);
                throw new RuntimeException(new NetconfException("Error in reading from the session for device {}" +
                                                                        netconfDeviceInfo, e));
                //TODO should we send a socket closed message to listeners ?
            }
    }

    private static Optional<Integer> getMsgId(String reply) {
        if (reply.contains(MESSAGE_ID)) {
            String[] outer = reply.split(MESSAGE_ID);
            Preconditions.checkArgument(outer.length != 1,
                                        "Error in retrieving the message id");
            String messageID = outer[1].substring(0, 3).replace("\"", "");
            Preconditions.checkNotNull(Integer.parseInt(messageID),
                                       "Error in retrieving the message id");
            return Optional.of(Integer.parseInt(messageID));
        } else if (reply.contains(HELLO)) {
            return Optional.of(0);
        }
        return Optional.empty();
    }

    public void addDeviceEventListener(NetconfDeviceOutputEventListener listener) {
        if (!netconfDeviceEventListeners.contains(listener)) {
            netconfDeviceEventListeners.add(listener);
        }
    }

    public void removeDeviceEventListener(NetconfDeviceOutputEventListener listener) {
        netconfDeviceEventListeners.remove(listener);
    }
}

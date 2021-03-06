/*
 * Copyright 2016 Open Networking Laboratory
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
package org.onosproject.ospf.protocol.ospfpacket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.onlab.packet.Ip4Address;
import org.onosproject.ospf.exceptions.OspfParseException;
import org.onosproject.ospf.protocol.util.OspfPacketType;

/**
 * Representation of an OSPF message.
 */
public interface OspfMessage {

    /**
     * Returns the type of OSPF message.
     *
     * @return OSPF message type
     */
    public OspfPacketType ospfMessageType();

    /**
     * Reads from ChannelBuffer and initializes the type of LSA.
     *
     * @param channelBuffer channel buffer instance
     * @throws OspfParseException might throws exception while parsing buffer
     */
    void readFrom(ChannelBuffer channelBuffer) throws OspfParseException;

    /**
     * Returns OSPFMessage as byte array.
     *
     * @return OSPF message as bytes
     */
    byte[] asBytes();

    /**
     * Sets the source IP address.
     *
     * @param sourceIp IP address
     */
    public void setSourceIp(Ip4Address sourceIp);

    /**
     * Gets the destination IP address.
     *
     * @return destination IP address
     */
    public Ip4Address destinationIp();

    /**
     * Sets destination IP.
     *
     * @param destinationIp destination IP address
     */
    public void setDestinationIp(Ip4Address destinationIp);
}
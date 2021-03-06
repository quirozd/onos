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
package org.onosproject.bgpio.types;

import org.junit.Test;
import com.google.common.testing.EqualsTester;

/**
 * Test for action redirect flow specification component.
 */
public class BgpFsActionReDirectTest {

    private final BgpFsActionReDirect tlv1 = new BgpFsActionReDirect(new byte[100]);
    private final BgpFsActionReDirect sameAsTlv1 = new BgpFsActionReDirect(new byte[100]);
    private final BgpFsActionReDirect tlv2 = new BgpFsActionReDirect(new byte[200]);

    @Test
    public void testEquality() {
        new EqualsTester()
        .addEqualityGroup(tlv1, sameAsTlv1)
        .addEqualityGroup(tlv2)
        .testEquals();
    }
}

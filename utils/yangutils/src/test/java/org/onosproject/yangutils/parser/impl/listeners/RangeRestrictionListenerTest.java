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

package org.onosproject.yangutils.parser.impl.listeners;


import java.io.IOException;
import java.util.ListIterator;

import org.junit.Test;
import org.onosproject.yangutils.datamodel.YangNode;
import org.onosproject.yangutils.datamodel.YangLeaf;
import org.onosproject.yangutils.datamodel.YangLeafList;
import org.onosproject.yangutils.datamodel.YangRangeRestriction;
import org.onosproject.yangutils.datamodel.YangRangeInterval;
import org.onosproject.yangutils.datamodel.YangModule;
import org.onosproject.yangutils.datamodel.YangNodeType;
import org.onosproject.yangutils.datamodel.YangDataTypes;
import org.onosproject.yangutils.parser.exceptions.ParserException;
import org.onosproject.yangutils.parser.impl.YangUtilsParserManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test cases for range restriction listener.
 */
public class RangeRestrictionListenerTest {

    private final YangUtilsParserManager manager = new YangUtilsParserManager();

    /**
     * Checks valid range statement as sub-statement of leaf statement.
     */
    @Test
    public void processValidRangeStatement() throws IOException, ParserException {

        YangNode node = manager.getDataModel("src/test/resources/ValidRangeStatement.yang");

        assertThat((node instanceof YangModule), is(true));
        assertThat(node.getNodeType(), is(YangNodeType.MODULE_NODE));
        YangModule yangNode = (YangModule) node;
        assertThat(yangNode.getName(), is("Test"));

        ListIterator<YangLeaf> leafIterator = yangNode.getListOfLeaf().listIterator();
        YangLeaf leafInfo = leafIterator.next();

        assertThat(leafInfo.getLeafName(), is("invalid-interval"));
        assertThat(leafInfo.getDataType().getDataTypeName(), is("int32"));
        assertThat(leafInfo.getDataType().getDataType(), is(YangDataTypes.INT32));
        YangRangeRestriction rangeRestriction = (YangRangeRestriction) leafInfo
                .getDataType().getDataTypeExtendedInfo();

        ListIterator<YangRangeInterval> rangeListIterator = rangeRestriction.getAscendingRangeIntervals()
                                                    .listIterator();
        YangRangeInterval rangeInterval = rangeListIterator.next();
        assertThat(rangeInterval.getStartValue(), is(10));
        assertThat(rangeInterval.getEndValue(), is(20));
    }

    /**
     * Checks valid range statement as sub-statement of leaf-list.
     */
    @Test
    public void processRangeStatementInsideLeafList() throws IOException, ParserException {

        YangNode node = manager.getDataModel("src/test/resources/RangeStatementInsideLeafList.yang");

        assertThat((node instanceof YangModule), is(true));
        assertThat(node.getNodeType(), is(YangNodeType.MODULE_NODE));
        YangModule yangNode = (YangModule) node;
        assertThat(yangNode.getName(), is("Test"));

        ListIterator<YangLeafList> leafListIterator = yangNode.getListOfLeafList().listIterator();
        YangLeafList leafListInfo = leafListIterator.next();

        assertThat(leafListInfo.getLeafName(), is("invalid-interval"));
        assertThat(leafListInfo.getDataType().getDataTypeName(), is("int32"));
        assertThat(leafListInfo.getDataType().getDataType(), is(YangDataTypes.INT32));
        YangRangeRestriction rangeRestriction = (YangRangeRestriction) leafListInfo
                .getDataType().getDataTypeExtendedInfo();

        ListIterator<YangRangeInterval> rangeListIterator = rangeRestriction.getAscendingRangeIntervals()
                .listIterator();
        YangRangeInterval rangeInterval = rangeListIterator.next();

        assertThat(rangeInterval.getStartValue(), is(10));
        assertThat(rangeInterval.getEndValue(), is(20));
    }
}
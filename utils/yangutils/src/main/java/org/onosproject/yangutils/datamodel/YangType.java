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

package org.onosproject.yangutils.datamodel;

import org.onosproject.yangutils.datamodel.exceptions.DataModelException;
import org.onosproject.yangutils.parser.Parsable;
import org.onosproject.yangutils.utils.YangConstructType;

/*
 * Reference:RFC 6020.
 * The "type" statement takes as an argument a string that is the name
 *  of a YANG built-in type or a derived type, followed by an optional
 *  block of sub-statements that are used to put further restrictions
 *  on the type.
 *
 *  The restrictions that can be applied depend on the type being restricted.
 *  The type's sub-statements
 *
 * +------------------+---------+-------------+------------------------------------+
 * | substatement     | section | cardinality | mapped data type                   |
 * +------------------+---------+-------------+------------------------------------+
 * | bit              | 9.7.4   | 0..n        | - YangBit used in YangBits         |
 * | enum             | 9.6.4   | 0..n        | - YangEnum used in YangEnumeration |
 * | length           | 9.4.4   | 0..1        | - used for string                  |
 * | path             | 9.9.2   | 0..1        | - TODO leaf-ref                    |
 * | pattern          | 9.4.6   | 0..n        | - used for string                  |
 * | range            | 9.2.4   | 0..1        | - used for integer data type       |
 * | require-instance | 9.13.2  | 0..1        | - TODO instance-identifier         |
 * | type             | 7.4     | 0..n        | - TODO union                       |
 * +------------------+---------+-------------+------------------------------------+
 */

/**
 * Maintains the data type information.
 *
 * @param <T> YANG data type info
 */
public class YangType<T> implements Parsable, Resolvable {

    /**
     * YANG node identifier.
     */
    private YangNodeIdentifier nodeIdentifier;

    /**
     * Java package in which the Java type is defined.
     */
    private String javaPackage;

    /**
     * YANG data type.
     */
    private YangDataTypes dataType;

    /**
     * Additional information about data type, example restriction info, named
     * values, etc. The extra information is based on the data type. Based on
     * the data type, the extended info can vary.
     */
    private T dataTypeExtendedInfo;

    /**
     * Effective built-in type, requried in case type of typedef is again a
     * derived type. This information is to be added during linking.
     */
    private YangDataTypes effectiveBuiltInType;

    /**
     * Effective pattern restriction, requried in case type of typedef is again
     * a derived type. This information is to be added during linking.
     */
    private YangPatternRestriction effectivePatternRestriction;

    /**
     * Status of resolution. If completely resolved enum value is "RESOLVED",
     * if not enum value is "UNRESOLVED", in case reference of grouping/typedef
     * is added to uses/type but it's not resolved value of enum should be
     * "PARTIALLY_RESOLVED".
     */
    private ResolvableStatus resolvableStatus;

    /**
     * Default constructor.
     */
    public YangType() {

        nodeIdentifier = new YangNodeIdentifier();
        resolvableStatus = ResolvableStatus.UNRESOLVED;
    }

    /**
     * Returns prefix associated with data type name.
     *
     * @return prefix associated with data type name
     */
    public String getPrefix() {
        return nodeIdentifier.getPrefix();
    }

    /**
     * Set prefix associated with data type name.
     *
     * @param prefix prefix associated with data type name
     */
    public void setPrefix(String prefix) {
        nodeIdentifier.setPrefix(prefix);
    }

    /**
     * Get the name of data type.
     *
     * @return the name of data type
     */
    public String getDataTypeName() {
        return nodeIdentifier.getName();
    }

    /**
     * Set the name of the data type.
     *
     * @param typeName the name to set
     */
    public void setDataTypeName(String typeName) {
        nodeIdentifier.setName(typeName);
    }

    /**
     * Get the Java package where the type is defined.
     *
     * @return Java package where the type is defined
     */
    public String getJavaPackage() {
        return javaPackage;
    }

    /**
     * Set Java package where the type is defined.
     *
     * @param javaPackage Java package where the type is defined
     */
    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    /**
     * Get the type of data.
     *
     * @return the data type
     */
    public YangDataTypes getDataType() {
        return dataType;
    }

    /**
     * Set the type of data.
     *
     * @param dataType data type
     */
    public void setDataType(YangDataTypes dataType) {
        this.dataType = dataType;
    }

    /**
     * Get the data type meta data.
     *
     * @return the data type meta data
     */
    public T getDataTypeExtendedInfo() {
        return dataTypeExtendedInfo;
    }

    /**
     * Set the data type meta data.
     *
     * @param dataTypeInfo the meta data to set
     */
    public void setDataTypeExtendedInfo(T dataTypeInfo) {
        this.dataTypeExtendedInfo = dataTypeInfo;
    }

    /**
     * Returns node identifier.
     *
     * @return node identifier
     */
    public YangNodeIdentifier getNodeIdentifier() {
        return nodeIdentifier;
    }

    /**
     * Set node identifier.
     *
     * @param nodeIdentifier the node identifier
     */
    public void setNodeIdentifier(YangNodeIdentifier nodeIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
    }

    /**
     * Return effective built-in type.
     *
     * @return effective built-in type
     */
    public YangDataTypes getEffectiveBuiltInType() {
        return effectiveBuiltInType;
    }

    /**
     * Set effective built-in type.
     *
     * @param effectiveBuiltInType effective built-in type
     */
    public void setEffectiveBuiltInType(YangDataTypes effectiveBuiltInType) {
        this.effectiveBuiltInType = effectiveBuiltInType;
    }

    /**
     * Returns effective pattern restriction.
     *
     * @return effective pattern restriction
     */
    public YangPatternRestriction getEffectivePatternRestriction() {
        return effectivePatternRestriction;
    }

    /**
     * Set effective pattern restriction.
     *
     * @param effectivePatternRestriction effective pattern restriction
     */
    public void setEffectivePatternRestriction(YangPatternRestriction effectivePatternRestriction) {
        this.effectivePatternRestriction = effectivePatternRestriction;
    }

    /**
     * Returns the type of the parsed data.
     *
     * @return returns TYPE_DATA
     */
    @Override
    public YangConstructType getYangConstructType() {
        return YangConstructType.TYPE_DATA;
    }

    /**
     * Validate the data on entering the corresponding parse tree node.
     *
     * @throws DataModelException a violation of data model rules
     */
    @Override
    public void validateDataOnEntry() throws DataModelException {
        // TODO auto-generated method stub, to be implemented by parser

    }

    /**
     * Validate the data on exiting the corresponding parse tree node.
     *
     * @throws DataModelException a violation of data model rules
     */
    @Override
    public void validateDataOnExit() throws DataModelException {
        // TODO auto-generated method stub, to be implemented by parser

    }

    @Override
    public ResolvableStatus getResolvableStatus() {
        return resolvableStatus;
    }

    @Override
    public void setResolvableStatus(ResolvableStatus resolvableStatus) {
        this.resolvableStatus = resolvableStatus;
    }

    @Override
    public void resolve() {
        //TODO: implement the method.
    }
}

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

package org.onosproject.yangutils.translator.tojava.javamodel;

import java.io.IOException;
import org.onosproject.yangutils.datamodel.YangRpc;
import org.onosproject.yangutils.translator.tojava.JavaCodeGenerator;

/**
 * Rpc information extended to support java code generation.
 */
public class YangJavaRpc extends YangRpc implements JavaCodeGenerator {

    /**
     * Creates an instance of java Rpc.
     */
    public YangJavaRpc() {
    }

    /**
     * Prepare the information for java code generation corresponding to YANG
     * rpc info.
     *
     * @param codeGenDir code generation directory
     * @throws IOException IO operation fail
     */
    @Override
    public void generateCodeEntry(String codeGenDir) throws IOException {
          // TODO
    }

    /**
     * Create a java file using the YANG rpc info.
     *
     * @throws IOException IO operation fail
     */
    @Override
    public void generateCodeExit() throws IOException {
          // TODO
    }
}

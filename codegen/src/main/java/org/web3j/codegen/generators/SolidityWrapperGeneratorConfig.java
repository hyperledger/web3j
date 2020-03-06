/*
 * Copyright 2020 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.codegen.generators;

import org.web3j.codegen.GenerationReporter;

public class SolidityWrapperGeneratorConfig {
    public final boolean generateSendTxForCalls;
    public final boolean useNativeJavaTypes;
    public final boolean useJavaPrimitiveTypes;
    public final int addressLength;
    public final GenerationReporter reporter;

    public SolidityWrapperGeneratorConfig(
            boolean generateSendTxForCalls,
            boolean useNativeJavaTypes,
            boolean useJavaPrimitiveTypes,
            int addressLength,
            GenerationReporter reporter) {
        this.generateSendTxForCalls = generateSendTxForCalls;
        this.useNativeJavaTypes = useNativeJavaTypes;
        this.useJavaPrimitiveTypes = useJavaPrimitiveTypes;
        this.addressLength = addressLength;
        this.reporter = reporter;
    }
}

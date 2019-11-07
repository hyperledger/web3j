/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.codegen.unit.gen.templates;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import org.web3j.tuples.Tuple;

import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

public abstract class Template {
    List<Class> deployArguments;
    Class contractName;
    Type returnType;
    List<ParameterSpec> methodParameters;

    Template() {}

    protected String customParameters() {
        List<String> generated = new ArrayList<>();
        for (Class type : deployArguments) {
            if (type.equals(String.class)) {
                generated.add("$S");
            } else if (type.equals(BigInteger.class)) {
                generated.add("$T.ONE");
            } else if (type.equals(List.class)) {
                generated.add("new $T<>()");
            } else if (type.equals(Tuple.class)) {
                generated.add("new $T<>()");
            } else if (type.equals(byte[].class)) {
                generated.add("new $T{}");
            } else {
                generated.add("$L");
            }
        }
        return String.join(",", generated);
    }

    private Object getDefaultArgumentValues(Class classToCheck) {
        if (classToCheck.equals(String.class)) {
            return "REPLACE_ME";
        } else if (classToCheck.equals(BigInteger.class)) {
            return BigInteger.class;
        } else if (classToCheck.equals(List.class)) {
            return ArrayList.class;
        } else if (classToCheck.equals(Tuple.class)) {
            return Tuple.class;
        } else if (classToCheck.equals(byte[].class)) {
            return byte[].class;
        } else {
            return toCamelCase(classToCheck);
        }
    }

    protected Object[] dynamicArguments() {
        return deployArguments.stream().map(this::getDefaultArgumentValues).toArray();
    }

    public abstract MethodSpec generate();

    abstract Object[] arguments();
}

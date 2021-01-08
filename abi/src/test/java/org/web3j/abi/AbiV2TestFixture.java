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
package org.web3j.abi;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.StaticArray1;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.StaticArray3;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;

public class AbiV2TestFixture {

    public static final String FUNC_GETBAR = "getBar";

    public static final String FUNC_GETFOO = "getFoo";

    public static final String FUNC_GETFOOBAR = "getFooBar";

    public static final String FUNC_GETFOOBARBAR = "getFooBarBar";

    public static final String FUNC_GETFOOFOOBARBAR = "getFooFooBarBar";

    public static final String FUNC_GETNARBARBARFUZZFOONARFUZZNUUFOOFUZZFUNCTION =
            "getNarBarBarFuzzFooNarFuzzNuuFooFuzz";

    public static final String FUNC_GETFOOUINT = "getFooUint";

    public static final String FUNC_GETFOOSTATICARRAY1 = "getFooStaticArray1";

    public static final String FUNC_GETFOOSTATICARRAY2 = "getFooStaticArray2";

    public static final String FUNC_GETFOOSTATICARRAY3 = "getFooStaticArray3";

    public static final String FUNC_GETFOODYNAMICARRAY = "getFooDynamicArray";

    public static final String FUNC_GETNARBARFOONARFOODYNAMICARRAY = "getNarBarFooNarFooArrays";

    public static final String FUNC_IDNARBARFOONARFOODYNAMICARRAY = "idNarBarFooNarFooArrays";

    public static final String FUNC_GETBARDYNAMICARRAY = "getBarDynamicArray";

    public static final String FUNC_GETBARSTATICARRAY = "getBarStaticArray";

    public static final String FUNC_SETBARSTATICARRAY = "setBarStaticArray";

    public static final String FUNC_SETBARDYNAMICARRAY = "setBarDynamicArray";

    public static final String FUNC_GETNARDYNAMICARRAY = "getNarDynamicArray";

    public static final String FUNC_GETNARSTATICARRAY = "getNarStaticArray";

    public static final String FUNC_SETFOODYNAMICARRAY = "setFooDynamicArray";

    public static final String FUNC_GETFOOMULTIPLESTATICARRAY = "getFooMultipleStaticArray";

    public static final String FUNC_GETFOOMULTIPLEDYNAMICARRAY = "getFooMultipleDynamicArray";

    public static final String FUNC_IDNARBARFOONARFOOARRAYS = "idNarBarFooNarFooArrays";

    public static final String FUNC_IDBARNARFOONARFOOARRAYS = "idBarNarFooNarFooArrays";

    public static final String FUNC_GETFOOMULTIPLEDYNAMICSTATICARRAY =
            "getFooMultipleDynamicStaticArray";

    public static final String FUNC_GETFUZZ = "getFuzz";

    public static final String FUNC_GETFUZZFUZZ = "getFuzzFuzz";

    public static final String FUNC_GETNAZ = "getNaz";

    public static final String FUNC_GETNAR = "getNar";

    public static final String FUNC_SETBAR = "setBar";

    public static final String FUNC_SETBAZ = "setBaz";

    public static final String FUNC_SETBOZ = "setBoz";

    public static final String FUNC_SETFOO = "setFoo";

    public static final String FUNC_SETFUZZ = "setFuzz";

    public static final String FUNC_SETNAZ = "setNaz";

    public static final String FUNC_SETNUU = "setNuu";

    public static final String FUNC_SETWIZ = "setWiz";

    public static final String FUNC_addDynamicBytesArray = "addDynamicBytesArray";

    public static class Foo extends DynamicStruct {
        public String id;

        public String name;

        public Foo(String id, String name) {
            super(
                    new org.web3j.abi.datatypes.Utf8String(id),
                    new org.web3j.abi.datatypes.Utf8String(name));
            this.id = id;
            this.name = name;
        }

        public Foo(Utf8String id, Utf8String name) {
            super(id, name);
            this.id = id.getValue();
            this.name = name.getValue();
        }
    }

    public static final org.web3j.abi.datatypes.Function setFooFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETFOO,
                    Arrays.<Type>asList(new Foo("id", "name")),
                    Collections.emptyList());

    public static final org.web3j.abi.datatypes.Function getFooFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOO, Arrays.<Type>asList(), Arrays.asList(new TypeReference<Foo>() {}));

    public static final org.web3j.abi.datatypes.Function getFooUintFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOUINT,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<Foo>() {}, new TypeReference<Uint256>() {}));

    public static final org.web3j.abi.datatypes.Function getFooStaticArray1Function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOSTATICARRAY1,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<StaticArray1<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function getFooStaticArray2Function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOSTATICARRAY2,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<StaticArray2<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function getFooStaticArray3Function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOSTATICARRAY3,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<StaticArray3<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function getFooDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOODYNAMICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<DynamicArray<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function getNarBarFooNarFooDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETNARBARFOONARFOODYNAMICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(
                            new TypeReference<StaticArray3<Nar>>() {},
                            new TypeReference<StaticArray3<Bar>>() {},
                            new TypeReference<DynamicArray<Foo>>() {},
                            new TypeReference<DynamicArray<Nar>>() {},
                            new TypeReference<StaticArray3<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function idNarBarFooNarFooDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_IDNARBARFOONARFOODYNAMICARRAY,
                    Arrays.asList(
                            new StaticArray3<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", ""))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo")))),
                            new StaticArray3<>(
                                    AbiV2TestFixture.Bar.class,
                                    new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                    new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name")),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", "")))),
                            new StaticArray3<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name"),
                                    new AbiV2TestFixture.Foo("id", "name"),
                                    new AbiV2TestFixture.Foo("id", "name"))),
                    Arrays.asList(
                            new TypeReference<StaticArray3<Nar>>() {},
                            new TypeReference<StaticArray3<Bar>>() {},
                            new TypeReference<DynamicArray<Foo>>() {},
                            new TypeReference<DynamicArray<Nar>>() {},
                            new TypeReference<StaticArray3<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function getBarDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETBARDYNAMICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<DynamicArray<Bar>>() {}));

    public static final org.web3j.abi.datatypes.Function getBarStaticArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETBARSTATICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<StaticArray3<Bar>>() {}));

    @SuppressWarnings("unchecked")
    public static final org.web3j.abi.datatypes.Function setBarStaticArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETBARSTATICARRAY,
                    Arrays.<Type>asList(
                            new StaticArray3(
                                    AbiV2TestFixture.Bar.class,
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(0), BigInteger.valueOf(0)),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(0), BigInteger.valueOf(0)))),
                    Arrays.asList());

    @SuppressWarnings("unchecked")
    public static final org.web3j.abi.datatypes.Function setBarDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETBARDYNAMICARRAY,
                    Arrays.<Type>asList(
                            new DynamicArray(
                                    AbiV2TestFixture.Bar.class,
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(0), BigInteger.valueOf(0)),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(0), BigInteger.valueOf(0)))),
                    Arrays.asList());

    public static final org.web3j.abi.datatypes.Function getNarDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETNARDYNAMICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<DynamicArray<Nar>>() {}));

    public static final org.web3j.abi.datatypes.Function getNarStaticArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETNARSTATICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<StaticArray3<Nar>>() {}));

    public static final org.web3j.abi.datatypes.Function getFooMultipleStaticArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOMULTIPLESTATICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(
                            new TypeReference<StaticArray3<Foo>>() {},
                            new TypeReference<StaticArray2<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function getFooMultipleDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOMULTIPLEDYNAMICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.asList(
                            new TypeReference<DynamicArray<Foo>>() {},
                            new TypeReference<DynamicArray<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function idNarBarFooNarFooArraysFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_IDNARBARFOONARFOOARRAYS,
                    Arrays.<Type>asList(
                            new DynamicArray<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", "")))),
                            new StaticArray3<>(
                                    AbiV2TestFixture.Bar.class,
                                    new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                    new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name")),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", "")))),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name"))),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<DynamicArray<Nar>>() {},
                            new TypeReference<StaticArray3<Bar>>() {},
                            new TypeReference<DynamicArray<Foo>>() {},
                            new TypeReference<DynamicArray<Nar>>() {},
                            new TypeReference<DynamicArray<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function idNarBarFooNarFooArraysFunction2 =
            new org.web3j.abi.datatypes.Function(
                    FUNC_IDNARBARFOONARFOOARRAYS,
                    Arrays.<Type>asList(
                            new StaticArray3<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", ""))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo")))),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Bar.class,
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(12), BigInteger.valueOf(33)),
                                    new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name")),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", "")))),
                            new StaticArray3<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name"),
                                    new AbiV2TestFixture.Foo("id", "name"),
                                    new AbiV2TestFixture.Foo("id", "name"))),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<StaticArray3<Nar>>() {},
                            new TypeReference<DynamicArray<Bar>>() {},
                            new TypeReference<DynamicArray<Foo>>() {},
                            new TypeReference<DynamicArray<Nar>>() {},
                            new TypeReference<StaticArray3<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function idBarNarFooNarFooArraysFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_IDBARNARFOONARFOOARRAYS,
                    Arrays.<Type>asList(
                            new StaticArray3<>(
                                    AbiV2TestFixture.Bar.class,
                                    new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO),
                                    new AbiV2TestFixture.Bar(
                                            BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                    new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                            new StaticArray3<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", ""))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo")))),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name")),
                            new DynamicArray<>(
                                    AbiV2TestFixture.Nar.class,
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                    new AbiV2TestFixture.Nar(
                                            new AbiV2TestFixture.Nuu(
                                                    new AbiV2TestFixture.Foo("", "")))),
                            new StaticArray3<>(
                                    AbiV2TestFixture.Foo.class,
                                    new AbiV2TestFixture.Foo("id", "name"),
                                    new AbiV2TestFixture.Foo("id", "name"),
                                    new AbiV2TestFixture.Foo("id", "name"))),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<StaticArray3<Bar>>() {},
                            new TypeReference<StaticArray3<Nar>>() {},
                            new TypeReference<DynamicArray<Foo>>() {},
                            new TypeReference<DynamicArray<Nar>>() {},
                            new TypeReference<StaticArray3<Foo>>() {}));

    public static final org.web3j.abi.datatypes.Function getFooMultipleDynamicStaticArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOMULTIPLEDYNAMICSTATICARRAY,
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<StaticArray3<Foo>>() {},
                            new TypeReference<DynamicArray<Foo>>() {}));

    public static class Bar extends StaticStruct {
        public BigInteger id;

        public BigInteger data;

        public Bar(BigInteger id, BigInteger data) {
            super(
                    new org.web3j.abi.datatypes.generated.Uint256(id),
                    new org.web3j.abi.datatypes.generated.Uint256(data));
            this.id = id;
            this.data = data;
        }

        public Bar(Uint256 id, Uint256 data) {
            super(id, data);
            this.id = id.getValue();
            this.data = data.getValue();
        }
    }

    public static final org.web3j.abi.datatypes.Function setBarFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETBAR,
                    Arrays.<Type>asList(new Bar(BigInteger.ONE, BigInteger.TEN)),
                    Collections.<TypeReference<?>>emptyList());

    public static final org.web3j.abi.datatypes.Function getBarFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETBAR,
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Bar>() {}));

    public static final org.web3j.abi.datatypes.Function getFooBarFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOBAR,
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Foo>() {}, new TypeReference<Bar>() {}));

    public static final org.web3j.abi.datatypes.Function getFooBarBarFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOBARBAR,
                    Arrays.<Type>asList(),
                    Arrays.asList(
                            new TypeReference<Foo>() {},
                            new TypeReference<Bar>() {},
                            new TypeReference<Bar>() {}));

    public static final org.web3j.abi.datatypes.Function getFooFooBarBarFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFOOFOOBARBAR,
                    Arrays.<Type>asList(),
                    Arrays.asList(
                            new TypeReference<Foo>() {},
                            new TypeReference<Foo>() {},
                            new TypeReference<Bar>() {},
                            new TypeReference<Bar>() {}));

    public static final org.web3j.abi.datatypes.Function
            getNarBarBarFuzzFooNarFuzzNuuFooFuzzFunction =
                    new org.web3j.abi.datatypes.Function(
                            FUNC_GETNARBARBARFUZZFOONARFUZZNUUFOOFUZZFUNCTION,
                            Arrays.<Type>asList(),
                            Arrays.asList(
                                    new TypeReference<Nar>() {},
                                    new TypeReference<Bar>() {},
                                    new TypeReference<Bar>() {},
                                    new TypeReference<Fuzz>() {},
                                    new TypeReference<Foo>() {},
                                    new TypeReference<Nar>() {},
                                    new TypeReference<Fuzz>() {},
                                    new TypeReference<Nuu>() {},
                                    new TypeReference<Foo>() {},
                                    new TypeReference<Fuzz>() {}));

    public static class Baz extends DynamicStruct {
        public String id;

        public BigInteger data;

        public Baz(String id, BigInteger data) {
            super(
                    new org.web3j.abi.datatypes.Utf8String(id),
                    new org.web3j.abi.datatypes.generated.Uint256(data));
            this.id = id;
            this.data = data;
        }

        public Baz(Utf8String id, Uint256 data) {
            super(id, data);
            this.id = id.getValue();
            this.data = data.getValue();
        }
    }

    public static final org.web3j.abi.datatypes.Function setBazFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETBAZ,
                    Arrays.<Type>asList(new Baz("id", BigInteger.ONE)),
                    Collections.<TypeReference<?>>emptyList());

    public static class Boz extends DynamicStruct {
        public BigInteger data;

        public String id;

        public Boz(BigInteger data, String id) {
            super(
                    new org.web3j.abi.datatypes.generated.Uint256(data),
                    new org.web3j.abi.datatypes.Utf8String(id));
            this.data = data;
            this.id = id;
        }

        public Boz(Uint256 data, Utf8String id) {
            super(data, id);
            this.data = data.getValue();
            this.id = id.getValue();
        }
    }

    public static final org.web3j.abi.datatypes.Function setBozFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETBOZ,
                    Arrays.<Type>asList(new Boz(BigInteger.ONE, "id")),
                    Collections.<TypeReference<?>>emptyList());

    public static final org.web3j.abi.datatypes.Function getBozFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETBOZ,
                    Collections.<Type>emptyList(),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Boz>() {}));

    public static class Fuzz extends StaticStruct {
        public Bar bar;

        public BigInteger data;

        public Fuzz(Bar bar, BigInteger data) {
            super(bar, new org.web3j.abi.datatypes.generated.Uint256(data));
            this.bar = bar;
            this.data = data;
        }

        public Fuzz(Bar bar, Uint256 data) {
            super(bar, data);
            this.bar = bar;
            this.data = data.getValue();
        }
    }

    public static final org.web3j.abi.datatypes.Function setFooDynamicArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETFOODYNAMICARRAY,
                    Collections.singletonList(
                            new DynamicArray<>(
                                    Foo.class,
                                    new Foo("", ""),
                                    new Foo("id", "name"),
                                    new Foo("", ""))),
                    Collections.emptyList());

    public static final org.web3j.abi.datatypes.Function setDoubleFooStaticArrayFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETFOODYNAMICARRAY,
                    Arrays.<Type>asList(new Foo("", ""), new Foo("id", "name"), new Foo("", "")),
                    Collections.emptyList());

    public static final org.web3j.abi.datatypes.Function setFuzzFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETFUZZ,
                    Arrays.<Type>asList(
                            new Fuzz(new Bar(BigInteger.ONE, BigInteger.TEN), BigInteger.ONE)),
                    Collections.emptyList());

    public static final org.web3j.abi.datatypes.Function getFuzzFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFUZZ,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<Fuzz>() {}));

    public static final org.web3j.abi.datatypes.Function getFuzzFuzzFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETFUZZFUZZ,
                    Arrays.<Type>asList(),
                    Arrays.asList(new TypeReference<Fuzz>() {}, new TypeReference<Fuzz>() {}));

    public static class Nuu extends DynamicStruct {
        public Foo foo;

        public Nuu(Foo foo) {
            super(foo);
            this.foo = foo;
        }
    }

    public static final org.web3j.abi.datatypes.Function setNuuFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETNUU,
                    Arrays.<Type>asList(new Nuu(new Foo("id", "name"))),
                    Collections.<TypeReference<?>>emptyList());

    public static final org.web3j.abi.datatypes.Function getNuuFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETNUU,
                    Collections.<Type>emptyList(),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Nuu>() {}));

    public static class Nar extends DynamicStruct {
        public Nuu nuu;

        public Nar(Nuu nuu) {
            super(nuu);
            this.nuu = nuu;
        }
    }

    public static class Naz extends DynamicStruct {
        public Nar nar;

        public BigInteger data;

        public Naz(Nar nar, BigInteger data) {
            super(nar, new org.web3j.abi.datatypes.generated.Uint256(data));
            this.nar = nar;
            this.data = data;
        }

        public Naz(Nar nar, Uint256 data) {
            super(nar, data);
            this.nar = nar;
            this.data = data.getValue();
        }
    }

    public static class Nazz extends DynamicStruct {
        public Naz naz;

        public BigInteger data;

        public Nazz(Naz naz, BigInteger data) {
            super(naz, new org.web3j.abi.datatypes.generated.Uint256(data));
            this.naz = naz;
            this.data = data;
        }

        public Nazz(Naz naz, Uint256 data) {
            super(naz, data);
            this.naz = naz;
            this.data = data.getValue();
        }
    }

    public static final org.web3j.abi.datatypes.Function setNazFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETNAZ,
                    Arrays.<Type>asList(
                            new Naz(new Nar(new Nuu(new Foo("id", "name"))), BigInteger.ONE)),
                    Collections.emptyList());

    public static final org.web3j.abi.datatypes.Function getNazFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETNAZ, Arrays.<Type>asList(), Arrays.asList(new TypeReference<Naz>() {}));

    public static final org.web3j.abi.datatypes.Function getNarFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETNAR, Arrays.<Type>asList(), Arrays.asList(new TypeReference<Nar>() {}));

    public static class Wiz extends DynamicStruct {
        public Foo foo;

        public String data;

        public Wiz(Foo foo, String data) {
            super(foo, new org.web3j.abi.datatypes.Utf8String(data));
            this.foo = foo;
            this.data = data;
        }

        public Wiz(Foo foo, Utf8String data) {
            super(foo, data);
            this.foo = foo;
            this.data = data.getValue();
        }
    }

    public static final org.web3j.abi.datatypes.Function setWizFunction =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETWIZ,
                    Arrays.<Type>asList(new Wiz(new Foo("id", "name"), "data")),
                    Collections.emptyList());

    public static class BytesStruct extends DynamicStruct {
        public byte[] pubkey;

        public BigInteger something;

        public byte[] metadata;

        public BytesStruct(byte[] pubkey, BigInteger something, byte[] metadata) {
            super(
                    new org.web3j.abi.datatypes.DynamicBytes(pubkey),
                    new org.web3j.abi.datatypes.generated.Uint32(something),
                    new org.web3j.abi.datatypes.DynamicBytes(metadata));
            this.pubkey = pubkey;
            this.something = something;
            this.metadata = metadata;
        }

        public BytesStruct(DynamicBytes pubkey, Uint32 something, DynamicBytes metadata) {
            super(pubkey, something, metadata);
            this.pubkey = pubkey.getValue();
            this.something = something.getValue();
            this.metadata = metadata.getValue();
        }
    }

    public static final Function addDynamicBytesArrayFunction =
            new Function(
                    FUNC_addDynamicBytesArray,
                    Arrays.<Type>asList(
                            new BytesStruct(
                                    "dynamic".getBytes(), BigInteger.ZERO, "Bytes".getBytes())),
                    Collections.<TypeReference<?>>emptyList());
}

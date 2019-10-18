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
package org.web3j.protocol.parity.methods.response;

import java.math.BigInteger;
import java.util.List;

/**
 * VMTrace used in following methods.
 *
 * <ol>
 *   <li>trace_call
 *   <li>trace_rawTransaction
 *   <li>trace_replayTransaction
 * </ol>
 */
public class VMTrace {

    private String code;
    private List<VMOperation> ops;

    public static class VMOperation {

        private VMTrace sub;
        private BigInteger cost;
        private Ex ex;
        private BigInteger pc;

        public static class Ex {

            private Mem mem;
            private List<String> push;
            private Store store;
            private BigInteger used;

            public static class Mem {

                private String data;
                private BigInteger off;

                public Mem() {}

                public Mem(String data, BigInteger off) {
                    this.data = data;
                    this.off = off;
                }

                public String getData() {
                    return data;
                }

                public void setData(String data) {
                    this.data = data;
                }

                public BigInteger getOff() {
                    return off;
                }

                public void setOff(BigInteger off) {
                    this.off = off;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) {
                        return true;
                    }
                    if (o == null || !(o instanceof Mem)) {
                        return false;
                    }

                    Mem mem = (Mem) o;

                    if (getData() != null
                            ? !getData().equals(mem.getData())
                            : mem.getData() != null) {
                        return false;
                    }
                    return getOff() != null ? getOff().equals(mem.getOff()) : mem.getOff() == null;
                }

                @Override
                public int hashCode() {
                    int result = getData() != null ? getData().hashCode() : 0;
                    result = 31 * result + (getOff() != null ? getOff().hashCode() : 0);
                    return result;
                }

                @Override
                public String toString() {
                    return "Mem{" + "data='" + getData() + '\'' + ", off=" + getOff() + '}';
                }
            }

            public static class Store {

                String key;
                String val;

                public Store() {}

                public Store(String key, String val) {
                    this.key = key;
                    this.val = val;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getVal() {
                    return val;
                }

                public void setVal(String val) {
                    this.val = val;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) {
                        return true;
                    }
                    if (o == null || !(o instanceof Store)) {
                        return false;
                    }

                    Store store = (Store) o;

                    if (getKey() != null
                            ? !getKey().equals(store.getKey())
                            : store.getKey() != null) {
                        return false;
                    }
                    return getVal() != null
                            ? getVal().equals(store.getVal())
                            : store.getVal() == null;
                }

                @Override
                public int hashCode() {
                    int result = getKey() != null ? getKey().hashCode() : 0;
                    result = 31 * result + (getVal() != null ? getVal().hashCode() : 0);
                    return result;
                }

                @Override
                public String toString() {
                    return "Store{" + "key='" + getKey() + '\'' + ", val='" + getVal() + '\'' + '}';
                }
            }

            public Ex() {}

            public Ex(Mem mem, List<String> push, Store store, BigInteger used) {
                this.mem = mem;
                this.push = push;
                this.store = store;
                this.used = used;
            }

            public Mem getMem() {
                return mem;
            }

            public void setMem(Mem mem) {
                this.mem = mem;
            }

            public List<String> getPush() {
                return push;
            }

            public void setPush(List<String> push) {
                this.push = push;
            }

            public Store getStore() {
                return store;
            }

            public void setStore(Store store) {
                this.store = store;
            }

            public BigInteger getUsed() {
                return used;
            }

            public void setUsed(BigInteger used) {
                this.used = used;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || !(o instanceof Ex)) {
                    return false;
                }

                Ex ex = (Ex) o;

                if (getMem() != null ? !getMem().equals(ex.getMem()) : ex.getMem() != null) {
                    return false;
                }
                if (getPush() != null ? !getPush().equals(ex.getPush()) : ex.getPush() != null) {
                    return false;
                }
                if (getStore() != null
                        ? !getStore().equals(ex.getStore())
                        : ex.getStore() != null) {
                    return false;
                }
                return getUsed() != null ? getUsed().equals(ex.getUsed()) : ex.getUsed() == null;
            }

            @Override
            public int hashCode() {
                int result = getMem() != null ? getMem().hashCode() : 0;
                result = 31 * result + (getPush() != null ? getPush().hashCode() : 0);
                result = 31 * result + (getStore() != null ? getStore().hashCode() : 0);
                result = 31 * result + (getUsed() != null ? getUsed().hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                return "Ex{"
                        + "mem="
                        + getMem()
                        + ", push="
                        + getPush()
                        + ", store="
                        + getStore()
                        + ", used="
                        + getUsed()
                        + '}';
            }
        }

        public VMOperation() {}

        public VMOperation(VMTrace sub, BigInteger cost, Ex ex, BigInteger pc) {
            this.sub = sub;
            this.cost = cost;
            this.ex = ex;
            this.pc = pc;
        }

        public VMTrace getSub() {
            return sub;
        }

        public void setSub(VMTrace sub) {
            this.sub = sub;
        }

        public BigInteger getCost() {
            return cost;
        }

        public void setCost(BigInteger cost) {
            this.cost = cost;
        }

        public Ex getEx() {
            return ex;
        }

        public void setEx(Ex ex) {
            this.ex = ex;
        }

        public BigInteger getPc() {
            return pc;
        }

        public void setPc(BigInteger pc) {
            this.pc = pc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof VMOperation)) {
                return false;
            }

            VMOperation that = (VMOperation) o;

            if (getSub() != null ? !getSub().equals(that.getSub()) : that.getSub() != null) {
                return false;
            }
            if (getCost() != null ? !getCost().equals(that.getCost()) : that.getCost() != null) {
                return false;
            }
            if (getEx() != null ? !getEx().equals(that.getEx()) : that.getEx() != null) {
                return false;
            }
            return getPc() != null ? getPc().equals(that.getPc()) : that.getPc() == null;
        }

        @Override
        public int hashCode() {
            int result = getSub() != null ? getSub().hashCode() : 0;
            result = 31 * result + (getCost() != null ? getCost().hashCode() : 0);
            result = 31 * result + (getEx() != null ? getEx().hashCode() : 0);
            result = 31 * result + (getPc() != null ? getPc().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "VMOperation{"
                    + "sub="
                    + getSub()
                    + ", cost="
                    + getCost()
                    + ", ex="
                    + getEx()
                    + ", pc="
                    + getPc()
                    + '}';
        }
    }

    public VMTrace() {}

    public VMTrace(String code, List<VMOperation> ops) {
        this.code = code;
        this.ops = ops;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<VMOperation> getOps() {
        return ops;
    }

    public void setOps(List<VMOperation> ops) {
        this.ops = ops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof VMTrace)) {
            return false;
        }

        VMTrace vmTrace = (VMTrace) o;

        if (getCode() != null ? !getCode().equals(vmTrace.getCode()) : vmTrace.getCode() != null) {
            return false;
        }
        return getOps() != null ? getOps().equals(vmTrace.getOps()) : vmTrace.getOps() == null;
    }

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (getOps() != null ? getOps().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VMTrace{" + "code='" + getCode() + '\'' + ", ops=" + getOps() + '}';
    }
}

package org.web3j.protocol.core.methods.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * AbiDefinition wrapper
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbiDefinition {
    private boolean constant;
    private List<NamedType> inputs;
    private String name;
    private List<NamedType> outputs;
    private String type;
    private boolean payable;

    public AbiDefinition() {
    }

    public AbiDefinition(boolean constant, List<NamedType> inputs, String name,
                         List<NamedType> outputs, String type, boolean payable) {
        this.constant = constant;
        this.inputs = inputs;
        this.name = name;
        this.outputs = outputs;
        this.type = type;
        this.payable = false;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public List<NamedType> getInputs() {
        return inputs;
    }

    public void setInputs(List<NamedType> inputs) {
        this.inputs = inputs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NamedType> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<NamedType> outputs) {
        this.outputs = outputs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbiDefinition)) return false;

        AbiDefinition that = (AbiDefinition) o;

        if (constant != that.constant) return false;
        if (payable != that.payable) return false;
        if (inputs != null ? !inputs.equals(that.inputs) : that.inputs != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (outputs != null ? !outputs.equals(that.outputs) : that.outputs != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;

    }

    @Override
    public int hashCode() {
        int result = (constant ? 1 : 0);
        result = 31 * result + (inputs != null ? inputs.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (outputs != null ? outputs.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (payable ? 1 : 0);
        return result;
    }

    public static class NamedType {
        private String name;
        private String type;
        private boolean indexed;

        public NamedType() {
        }

        public NamedType(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isIndexed() {
            return indexed;
        }

        public void setIndexed(boolean indexed) {
            this.indexed = indexed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NamedType)) return false;

            NamedType namedType = (NamedType) o;

            if (indexed != namedType.indexed) return false;
            if (name != null ? !name.equals(namedType.name) : namedType.name != null) return false;
            return type != null ? type.equals(namedType.type) : namedType.type == null;

        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + (indexed ? 1 : 0);
            return result;
        }
    }
}

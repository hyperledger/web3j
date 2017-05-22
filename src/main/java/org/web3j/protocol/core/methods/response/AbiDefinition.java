package org.web3j.protocol.core.methods.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * AbiDefinition wrapper.
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbiDefinition)) {
            return false;
        }

        AbiDefinition that = (AbiDefinition) o;

        if (isConstant() != that.isConstant()) {
            return false;
        }
        if (isPayable() != that.isPayable()) {
            return false;
        }
        if (getInputs() != null
                ? !getInputs().equals(that.getInputs()) : that.getInputs() != null) {
            return false;
        }
        if (getName() != null
                ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getOutputs() != null
                ? !getOutputs().equals(that.getOutputs()) : that.getOutputs() != null) {
            return false;
        }
        return getType() != null
                ? getType().equals(that.getType()) : that.getType() == null;
    }

    @Override
    public int hashCode() {
        int result = (isConstant() ? 1 : 0);
        result = 31 * result + (getInputs() != null ? getInputs().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getOutputs() != null ? getOutputs().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (isPayable() ? 1 : 0);
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
            if (this == o) {
                return true;
            }
            if (!(o instanceof NamedType)) {
                return false;
            }

            NamedType namedType = (NamedType) o;

            if (isIndexed() != namedType.isIndexed()) {
                return false;
            }
            if (getName() != null
                    ? !getName().equals(namedType.getName()) : namedType.getName() != null) {
                return false;
            }
            return getType() != null
                    ? getType().equals(namedType.getType()) : namedType.getType() == null;
        }

        @Override
        public int hashCode() {
            int result = getName() != null ? getName().hashCode() : 0;
            result = 31 * result + (getType() != null ? getType().hashCode() : 0);
            result = 31 * result + (isIndexed() ? 1 : 0);
            return result;
        }
    }
}

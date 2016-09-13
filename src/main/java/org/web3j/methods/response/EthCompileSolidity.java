package org.web3j.methods.response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_compileSolidity
 */
public class EthCompileSolidity extends Response<EthCompileSolidity.CompiledSolidity> {

    public CompiledSolidity getCompiledSolidity() {
        return getResult();
    }

    public static class CompiledSolidity {
        private String code;
        private SolidityInfo info;

        public CompiledSolidity() {
        }

        public CompiledSolidity(String code, SolidityInfo info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public SolidityInfo getInfo() {
            return info;
        }

        public void setInfo(SolidityInfo info) {
            this.info = info;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CompiledSolidity that = (CompiledSolidity) o;

            if (code != null ? !code.equals(that.code) : that.code != null) return false;
            return info != null ? info.equals(that.info) : that.info == null;

        }

        @Override
        public int hashCode() {
            int result = code != null ? code.hashCode() : 0;
            result = 31 * result + (info != null ? info.hashCode() : 0);
            return result;
        }
    }

    public static class SolidityInfo {
        private String source;
        private String language;
        private String languageVersion;
        private String compilerVersion;
        private List<AbiDefinition> abiDefinition;
        private Documentation userDoc;
        private Documentation developerDoc;

        public SolidityInfo() {
        }

        public SolidityInfo(String source, String language, String languageVersion,
                            String compilerVersion, List<AbiDefinition> abiDefinition,
                            Documentation userDoc, Documentation developerDoc) {
            this.source = source;
            this.language = language;
            this.languageVersion = languageVersion;
            this.compilerVersion = compilerVersion;
            this.abiDefinition = abiDefinition;
            this.userDoc = userDoc;
            this.developerDoc = developerDoc;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLanguageVersion() {
            return languageVersion;
        }

        public void setLanguageVersion(String languageVersion) {
            this.languageVersion = languageVersion;
        }

        public String getCompilerVersion() {
            return compilerVersion;
        }

        public void setCompilerVersion(String compilerVersion) {
            this.compilerVersion = compilerVersion;
        }

        public List<AbiDefinition> getAbiDefinition() {
            return abiDefinition;
        }

        public void setAbiDefinition(List<AbiDefinition> abiDefinition) {
            this.abiDefinition = abiDefinition;
        }

        public Documentation getUserDoc() {
            return userDoc;
        }

        public void setUserDoc(Documentation userDoc) {
            this.userDoc = userDoc;
        }

        public Documentation getDeveloperDoc() {
            return developerDoc;
        }

        public void setDeveloperDoc(Documentation developerDoc) {
            this.developerDoc = developerDoc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SolidityInfo that = (SolidityInfo) o;

            if (source != null ? !source.equals(that.source) : that.source != null) return false;
            if (language != null ? !language.equals(that.language) : that.language != null)
                return false;
            if (languageVersion != null ? !languageVersion.equals(that.languageVersion) : that.languageVersion != null)
                return false;
            if (compilerVersion != null ? !compilerVersion.equals(that.compilerVersion) : that.compilerVersion != null)
                return false;
            if (abiDefinition != null ? !abiDefinition.equals(that.abiDefinition) : that.abiDefinition != null)
                return false;
            if (userDoc != null ? !userDoc.equals(that.userDoc) : that.userDoc != null)
                return false;
            return developerDoc != null ? developerDoc.equals(that.developerDoc) : that.developerDoc == null;

        }

        @Override
        public int hashCode() {
            int result = source != null ? source.hashCode() : 0;
            result = 31 * result + (language != null ? language.hashCode() : 0);
            result = 31 * result + (languageVersion != null ? languageVersion.hashCode() : 0);
            result = 31 * result + (compilerVersion != null ? compilerVersion.hashCode() : 0);
            result = 31 * result + (abiDefinition != null ? abiDefinition.hashCode() : 0);
            result = 31 * result + (userDoc != null ? userDoc.hashCode() : 0);
            result = 31 * result + (developerDoc != null ? developerDoc.hashCode() : 0);
            return result;
        }
    }

    public static class AbiDefinition {
        private boolean constant;
        private List<NamedType> inputs;
        private String name;
        private List<NamedType> outputs;
        private String type;

        public AbiDefinition() {
        }

        public AbiDefinition(boolean constant, List<NamedType> inputs, String name,
                             List<NamedType> outputs, String type) {
            this.constant = constant;
            this.inputs = inputs;
            this.name = name;
            this.outputs = outputs;
            this.type = type;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AbiDefinition that = (AbiDefinition) o;

            if (constant != that.constant) return false;
            if (inputs != null ? !inputs.equals(that.inputs) : that.inputs != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (outputs != null ? !outputs.equals(that.outputs) : that.outputs != null)
                return false;
            return type != null ? type.equals(that.type) : that.type == null;

        }

        @Override
        public int hashCode() {
            int result = (constant ? 1 : 0);
            result = 31 * result + (inputs != null ? inputs.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (outputs != null ? outputs.hashCode() : 0);
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }

        public static class NamedType {
            private String name;
            private String type;

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

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                NamedType namedType = (NamedType) o;

                if (name != null ? !name.equals(namedType.name) : namedType.name != null)
                    return false;
                return type != null ? type.equals(namedType.type) : namedType.type == null;

            }

            @Override
            public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + (type != null ? type.hashCode() : 0);
                return result;
            }
        }
    }

    public static class Documentation {
        // No documentation available
        // See https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compilesolidity
        private Map<String, String> methods;  // unknown object type

        public Documentation() {
            this.methods = Collections.emptyMap();
        }

        public Map<String, String> getMethods() {
            return methods;
        }

        public void setMethods(Map<String, String> methods) {
            this.methods = methods;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Documentation that = (Documentation) o;

            return methods != null ? methods.equals(that.methods) : that.methods == null;

        }

        @Override
        public int hashCode() {
            return methods != null ? methods.hashCode() : 0;
        }
    }
}

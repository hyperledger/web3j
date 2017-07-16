package org.web3j.protocol.core.methods.response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.web3j.protocol.core.Response;

/**
 * eth_compileSolidity.
 */
public class EthCompileSolidity extends Response<Map<String, EthCompileSolidity.Code>> {

    public Map<String, EthCompileSolidity.Code> getCompiledSolidity() {
        return getResult();
    }

    public static class Code {
        private String code;
        private SolidityInfo info;

        public Code() {
        }

        public Code(String code) {
            this.code = code;
        }

        public Code(String code, SolidityInfo info) {
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
            if (this == o) {
                return true;
            }
            if (!(o instanceof Code)) {
                return false;
            }

            Code code1 = (Code) o;

            if (getCode() != null ? !getCode().equals(code1.getCode()) : code1.getCode() != null) {
                return false;
            }
            return getInfo() != null ? getInfo().equals(code1.getInfo()) : code1.getInfo() == null;
        }

        @Override
        public int hashCode() {
            int result = getCode() != null ? getCode().hashCode() : 0;
            result = 31 * result + (getInfo() != null ? getInfo().hashCode() : 0);
            return result;
        }
    }

    public static class SolidityInfo {
        private String source;
        private String language;
        private String languageVersion;
        private String compilerVersion;
        private String compilerOptions;
        private List<AbiDefinition> abiDefinition;
        private Documentation userDoc;
        private Documentation developerDoc;

        public SolidityInfo() {
        }

        public SolidityInfo(String source, String language, String languageVersion,
                            String compilerVersion, String compilerOptions,
                            List<AbiDefinition> abiDefinition, Documentation userDoc,
                            Documentation developerDoc) {
            this.source = source;
            this.language = language;
            this.languageVersion = languageVersion;
            this.compilerVersion = compilerVersion;
            this.compilerOptions = compilerOptions;
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

        public String getCompilerOptions() {
            return compilerOptions;
        }

        public void setCompilerOptions(String compilerOptions) {
            this.compilerOptions = compilerOptions;
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
            if (this == o) {
                return true;
            }
            if (!(o instanceof SolidityInfo)) {
                return false;
            }

            SolidityInfo that = (SolidityInfo) o;

            if (getSource() != null
                    ? !getSource().equals(that.getSource()) : that.getSource() != null) {
                return false;
            }
            if (getLanguage() != null
                    ? !getLanguage().equals(that.getLanguage()) : that.getLanguage() != null) {
                return false;
            }
            if (getLanguageVersion() != null
                    ? !getLanguageVersion().equals(that.getLanguageVersion())
                    : that.getLanguageVersion() != null) {
                return false;
            }
            if (getCompilerVersion() != null
                    ? !getCompilerVersion().equals(that.getCompilerVersion())
                    : that.getCompilerVersion() != null) {
                return false;
            }
            if (getCompilerOptions() != null
                    ? !getCompilerOptions().equals(that.getCompilerOptions())
                    : that.getCompilerOptions() != null) {
                return false;
            }
            if (getAbiDefinition() != null
                    ? !getAbiDefinition().equals(that.getAbiDefinition())
                    : that.getAbiDefinition() != null) {
                return false;
            }
            if (getUserDoc() != null
                    ? !getUserDoc().equals(that.getUserDoc()) : that.getUserDoc() != null) {
                return false;
            }
            return getDeveloperDoc() != null
                    ? getDeveloperDoc().equals(that.getDeveloperDoc())
                    : that.getDeveloperDoc() == null;
        }

        @Override
        public int hashCode() {
            int result = getSource() != null ? getSource().hashCode() : 0;
            result = 31 * result + (getLanguage() != null ? getLanguage().hashCode() : 0);
            result = 31 * result
                    + (getLanguageVersion() != null ? getLanguageVersion().hashCode() : 0);
            result = 31 * result
                    + (getCompilerVersion() != null ? getCompilerVersion().hashCode() : 0);
            result = 31 * result
                    + (getCompilerOptions() != null ? getCompilerOptions().hashCode() : 0);
            result = 31 * result + (getAbiDefinition() != null ? getAbiDefinition().hashCode() : 0);
            result = 31 * result + (getUserDoc() != null ? getUserDoc().hashCode() : 0);
            result = 31 * result + (getDeveloperDoc() != null ? getDeveloperDoc().hashCode() : 0);
            return result;
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
            if (this == o) {
                return true;
            }
            if (!(o instanceof Documentation)) {
                return false;
            }

            Documentation that = (Documentation) o;

            return getMethods() != null
                    ? getMethods().equals(that.getMethods()) : that.getMethods() == null;
        }

        @Override
        public int hashCode() {
            return getMethods() != null ? getMethods().hashCode() : 0;
        }
    }
}

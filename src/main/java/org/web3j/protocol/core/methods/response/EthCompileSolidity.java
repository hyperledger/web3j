package org.web3j.protocol.core.methods.response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.web3j.protocol.core.Response;

/**
 * eth_compileSolidity
 */
public class EthCompileSolidity extends Response<EthCompileSolidity.CompiledSolidity> {

    public CompiledSolidity getCompiledSolidity() {
        return getResult();
    }

    public static class CompiledSolidity {
        private Code test;

        public CompiledSolidity() { }

        public CompiledSolidity(Code test) {
            this.test = test;
        }

        public Code getTest() {
            return test;
        }

        public void setTest(Code test) {
            this.test = test;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CompiledSolidity that = (CompiledSolidity) o;

            return test != null ? test.equals(that.test) : that.test == null;

        }

        @Override
        public int hashCode() {
            return test != null ? test.hashCode() : 0;
        }
    }

    public static class Code {
        private String code;
        private SolidityInfo info;

        public Code() {
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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Code that = (Code) o;

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
            if (compilerOptions != null ? !compilerOptions.equals(that.compilerOptions) : that.compilerOptions != null)
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
            result = 31 * result + (compilerOptions != null ? compilerOptions.hashCode() : 0);
            result = 31 * result + (abiDefinition != null ? abiDefinition.hashCode() : 0);
            result = 31 * result + (userDoc != null ? userDoc.hashCode() : 0);
            result = 31 * result + (developerDoc != null ? developerDoc.hashCode() : 0);
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

/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.griffon.compiler.support;

import griffon.plugins.ldap.GriffonLdapSchema;
import griffon.plugins.ldap.GriffonLdapSchemaClass;
import org.codehaus.griffon.runtime.ldap.AbstractGriffonLdapSchema;
import org.codehaus.griffon.compiler.GriffonCompilerContext;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;

/**
 * Handles generation of code for LDAP Schema classes.<p/>
 *
 * @author Andres Almiray 
 */
@GroovyASTTransformation(phase=CompilePhase.CANONICALIZATION)
public class GriffonLdapSchemaASTTransformation extends GriffonArtifactASTTransformation {
    private static final String ARTIFACT_PATH = "ldap";
    private static final ClassNode GRIFFON_LDAP_SCHEMA_CLASS = makeClassSafe(GriffonLdapSchema.class);
    private static final ClassNode ABSTRACT_GRIFFON_LDAP_SCHEMA_CLASS = makeClassSafe(AbstractGriffonLdapSchema.class);

    public static boolean isLdapSchemaArtifact(ClassNode classNode, SourceUnit source) {
        if (classNode == null || source == null) return false;
        return ARTIFACT_PATH.equals(GriffonCompilerContext.getArtifactPath(source)) && classNode.getName().endsWith(GriffonLdapSchemaClass.TRAILING);
    }

    protected String getArtifactType() {
        return GriffonLdapSchemaClass.TYPE;
    }

    protected ClassNode getSuperClassNode(ClassNode classNode) {
        return ABSTRACT_GRIFFON_LDAP_SCHEMA_CLASS;
    }

    protected ClassNode getInterfaceNode() {
        return GRIFFON_LDAP_SCHEMA_CLASS;
    }

    protected boolean matches(ClassNode classNode, SourceUnit source) {
        return isLdapSchemaArtifact(classNode, source);
    }

    protected ASTInjector[] getASTInjectors() {
        return new ASTInjector[]{
            new GriffonArtifactASTInjector()
        };
    }
}
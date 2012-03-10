/*
 * Copyright 2009-2012 the original author or authors.
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

import org.codehaus.griffon.ast.GriffonASTUtils;
import org.codehaus.griffon.compiler.GriffonCompilerContext;
import org.codehaus.griffon.compiler.SourceUnitCollector;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles generation of code for LDAP Schema classes.<p/>
 *
 * @author Andres Almiray 
 */
@GroovyASTTransformation(phase=CompilePhase.CANONICALIZATION)
public class GriffonLdapSchemaASTTransformation extends GriffonArtifactASTTransformation {
    private static final Logger LOG = LoggerFactory.getLogger(GriffonLdapSchemaASTTransformation.class);
    private static final String ARTIFACT_PATH = "ldap";
    private static final ClassNode GRIFFON_LDAP_SCHEMA_CLASS = ClassHelper.makeWithoutCaching(GriffonLdapSchema.class);
    private static final ClassNode ABSTRACT_GRIFFON_LDAP_SCHEMA_CLASS = ClassHelper.makeWithoutCaching(AbstractGriffonLdapSchema.class);

    public static boolean isLdapSchemaArtifact(ClassNode classNode, SourceUnit source) {
        if (classNode == null || source == null) return false;
        return ARTIFACT_PATH.equals(GriffonCompilerContext.getArtifactPath(source)) && classNode.getName().endsWith(GriffonLdapSchemaClass.TRAILING);
    }

    protected void transform(ClassNode classNode, SourceUnit source, String artifactPath) {
        if (!isLdapSchemaArtifact(classNode, source)) return;
        doTransform(classNode);
    }

    private void doTransform(ClassNode classNode) {
        ClassNode superClass = classNode.getSuperClass();
        if (ClassHelper.OBJECT_TYPE.equals(superClass)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting " + ABSTRACT_GRIFFON_LDAP_SCHEMA_CLASS.getName() + " as the superclass of " + classNode.getName());
            }
            classNode.setSuperClass(ABSTRACT_GRIFFON_LDAP_SCHEMA_CLASS);
        } else if (!classNode.implementsInterface(GRIFFON_LDAP_SCHEMA_CLASS)) {
            inject(classNode, superClass);
        }
    }

    private void inject(ClassNode classNode, ClassNode superClass) {
        SourceUnit superSource = SourceUnitCollector.getInstance().getSourceUnit(superClass);
        if (isLdapSchemaArtifact(superClass, superSource)) return;

        if (superSource == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Injecting " + GRIFFON_LDAP_SCHEMA_CLASS.getName() + " behavior to " + classNode.getName());
            }
            // 1. add interface
            GriffonASTUtils.injectInterface(classNode, GRIFFON_LDAP_SCHEMA_CLASS);
            // 2. add methods
            ASTInjector injector = new GriffonArtifactASTInjector();
            injector.inject(classNode, GriffonLdapSchemaClass.TYPE);
        } else {
            doTransform(superClass);
        }
    }
}
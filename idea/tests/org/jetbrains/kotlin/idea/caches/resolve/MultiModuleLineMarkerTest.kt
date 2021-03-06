/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.caches.resolve

import com.intellij.openapi.roots.CompilerModuleExtension
import com.intellij.openapi.roots.ModuleRootModificationUtil
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.config.TargetPlatformKind
import org.jetbrains.kotlin.idea.stubs.createFacet
import org.jetbrains.kotlin.idea.test.PluginTestCaseBase
import org.jetbrains.kotlin.test.TestJdkKind

class MultiModuleLineMarkerTest : AbstractMultiModuleHighlightingTest() {

    override fun getTestDataPath() = PluginTestCaseBase.getTestDataPathBase() + "/multiModuleLineMarker/"

    override val shouldCheckLineMarkers = true

    override val shouldCheckResult = false

    override fun doTestLineMarkers() = true

    fun testFromActualAnnotation() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromActualPrimaryConstructor() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromActualSealedClass() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromActualSecondaryConstructor() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromActualTypeAlias() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromClassToAlias() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromClassToJavaAliasInTest() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromCommonToJvmHeader() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromCommonToJvmImpl() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromExpectedAnnotation() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromExpectedPrimaryConstructor() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromExpectedSealedClass() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromExpectedSecondaryConstructor() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testFromExpectedTypeAlias() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testHierarchyWithExpectClassCommonSide() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testHierarchyWithExpectClassCommonSideNonJavaIds() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testHierarchyWithExpectClassPlatformSide() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testKotlinTestAnnotations() {
        doMultiPlatformTest(TargetPlatformKind.JavaScript,
                            configureModule = { module, _ ->
                                ModuleRootModificationUtil.updateModel(module) {
                                    with(it.getModuleExtension(CompilerModuleExtension::class.java)!!) {
                                        inheritCompilerOutputPath(false)
                                        setCompilerOutputPathForTests("js_out")
                                    }
                                }
                            })
    }

    fun testTopLevelFunWithKotlinTest() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }

    fun testSuspendImplInPlatformModules() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6], TargetPlatformKind.JavaScript)
    }

    fun testTransitive() {
        val commonModule = module("common", TestJdkKind.MOCK_JDK)
        commonModule.createFacet(TargetPlatformKind.Common, false)
        val jvmPlatform = TargetPlatformKind.Jvm[JvmTarget.JVM_1_6]

        val baseModule = module("jvm_base", TestJdkKind.MOCK_JDK)
        baseModule.createFacet(jvmPlatform, implementedModuleName = "common")
        baseModule.enableMultiPlatform()
        baseModule.addDependency(commonModule)

        val userModule = module("jvm_user", TestJdkKind.MOCK_JDK)
        userModule.createFacet(jvmPlatform)
        userModule.enableMultiPlatform()
        userModule.addDependency(commonModule)
        userModule.addDependency(baseModule)

        checkHighlightingInAllFiles()
    }

    fun testTransitiveCommon() {
        val commonBaseModule = module("common_base", TestJdkKind.MOCK_JDK)
        commonBaseModule.createFacet(TargetPlatformKind.Common, false)

        val commonUserModule = module("common_user", TestJdkKind.MOCK_JDK)
        commonUserModule.createFacet(TargetPlatformKind.Common, false)
        commonUserModule.enableMultiPlatform()
        commonUserModule.addDependency(commonBaseModule)

        val jvmPlatform = TargetPlatformKind.Jvm[JvmTarget.JVM_1_6]
        val jvmModule = module("jvm", TestJdkKind.MOCK_JDK)
        jvmModule.createFacet(jvmPlatform, implementedModuleName = "common_user")
        jvmModule.enableMultiPlatform()
        jvmModule.addDependency(commonBaseModule)
        jvmModule.addDependency(commonUserModule)

        checkHighlightingInAllFiles()
    }

    fun testWithOverloads() {
        doMultiPlatformTest(TargetPlatformKind.Jvm[JvmTarget.JVM_1_6])
    }
}
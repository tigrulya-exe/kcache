package ru.nsu.kcache.creator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import ru.nsu.kcache.KCacheableMetadata
import ru.nsu.manasyan.kcache.core.handler.GeneratedHandlerMetadataContainer
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadataContainer
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadata as GeneratedMetadata

class HandlerMetadataContainerCreator {
    companion object {
        private const val metadataMapPropertyName = "metadataMap"
    }

    val handlerMetadataClassName = GeneratedHandlerMetadataContainer::class.simpleName!!

    private val metadataMapTypeName: ParameterizedTypeName = Map::class
        .asClassName()
        .parameterizedBy(
            String::class.asClassName(),
            GeneratedMetadata::class.asClassName()
        )

    private fun createProperty() = PropertySpec.builder(
        metadataMapPropertyName,
        metadataMapTypeName,
        KModifier.PRIVATE
    ).build()

    private fun createInitializerBlock(metadata: KCacheableMetadata): CodeBlock {
        val concurrentHashMapClassName = ClassName(
            "java.util.concurrent",
            "ConcurrentHashMap"
        )

        val requestHandlerMetadataClassName = ClassName(
            GeneratedMetadata::class.java.packageName,
            GeneratedMetadata::class.simpleName!!
        )

        return CodeBlock.builder()
            .addStatement("$metadataMapPropertyName = %T()", concurrentHashMapClassName)
            .apply {
                metadata.forEach { entry ->
                    entry.value.let { metadataVal ->
                        addStatement(
                            """
                    $metadataMapPropertyName["${entry.key}"] = %T(
                        listOf(
                            ${
                                metadataVal.tableStates
                                    .joinToString(
                                        separator = ",\n",
                                        prefix = "\"",
                                        postfix = "\""
                                    )
                            }
                        ),
                        ${metadataVal.onCacheHitResultBuilder}::class,
                        ${metadataVal.onCacheMissResultBuilder}::class
                    )
                """.trimIndent(),
                            requestHandlerMetadataClassName
                        )
                    }
                }
            }
            .build()
    }

    private fun createGetMetadataFunction() = FunSpec.builder("getMetadata")
        .addModifiers(KModifier.OVERRIDE)
        .addParameter(
            ParameterSpec.builder(
                "handlerName",
                String::class
            ).build()
        )
        .addStatement("return metadataMap[handlerName]")
        .returns(GeneratedMetadata::class.asTypeName().copy(nullable = true))
        .build()

    private fun createGetAllMetadataFunction() = FunSpec.builder("getAllMetadata")
        .addModifiers(KModifier.OVERRIDE)
        .addStatement("return HashMap(metadataMap)")
        .returns(metadataMapTypeName)
        .build()

    private fun createTypeSpec(metadata: KCacheableMetadata) = TypeSpec.classBuilder(handlerMetadataClassName)
        .addSuperinterface(RequestHandlerMetadataContainer::class)
        .addProperty(
            createProperty()
        )
        .addInitializerBlock(
            createInitializerBlock(metadata)
        )
        .addFunctions(
            listOf(
                createGetMetadataFunction(),
                createGetAllMetadataFunction()
            )
        ).build()

    fun create(metadata: KCacheableMetadata): FileSpec {
        return FileSpec.builder(
            GeneratedHandlerMetadataContainer::class.java.packageName,
            handlerMetadataClassName
        ).addType(
            createTypeSpec(metadata)
        ).build()
    }
}
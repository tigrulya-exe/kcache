package ru.nsu.kcache

import com.google.auto.service.AutoService
import ru.nsu.kcache.creator.HandlerMetadataContainerCreator
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import java.nio.file.Paths
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic


typealias KCacheableMetadata = MutableMap<String, RequestHandlerMetadata>

@AutoService(Processor::class)
class KCacheableProcessor : AbstractProcessor() {

    companion object {
        private const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private lateinit var messager: Messager

    private lateinit var filer: Filer

    private lateinit var elementUtils: Elements

    private lateinit var typeUtils: Types

    private val kCacheableMetadata: KCacheableMetadata = mutableMapOf()

    private val metadataCreator = HandlerMetadataContainerCreator()

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        filer = processingEnv.filer
        elementUtils = processingEnv.elementUtils
        typeUtils = processingEnv.typeUtils
    }

    private fun getResultBuilderFactoryClassName(annotation: KCacheable) =
        getAnnotationValueClassName(annotation) { resultBuilderFactory }

    // dirty hack from stackoverflow ¯\_(ツ)_/¯
    private fun getAnnotationValueClassName(
        annotation: KCacheable,
        action: KCacheable.() -> Unit
    ): String? {
        try {
            annotation.action()
        } catch (mte: MirroredTypeException) {
            return (processingEnv.typeUtils.asElement(
                mte.typeMirror
            ) as TypeElement).qualifiedName.toString()
        }
        return null
    }

    private fun createMetadataContainerClass() {
        val kaptDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        metadataCreator
            .create(kCacheableMetadata)
            .writeTo(
                Paths.get(
                    kaptDir, metadataCreator.handlerMetadataClassName
                )
            )
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        runCatching {
            roundEnv.getElementsAnnotatedWith(KCacheable::class.java)
                ?.filterIsInstance<ExecutableElement>()
                ?.forEach { element ->
                    element.getAnnotation(KCacheable::class.java)?.let {
                        val enclosingName = elementUtils.getBinaryName(
                            // we know that element's kind is method
                            element.enclosingElement as TypeElement
                        )

                        val parameters = element.parameters.joinToString(
                            prefix = "(",
                            separator = ",",
                            postfix = ")"
                        ) {
                            when (val type = it.asType()) {
                                is DeclaredType -> typeUtils.asElement(type)
                                else -> type
                            }.toString()
                        }

                        kCacheableMetadata["$enclosingName.${element.simpleName}$parameters"] =
                            RequestHandlerMetadata(
                                tableStates = it.tables.asList(),
                                resultBuilderFactory = getResultBuilderFactoryClassName(it)!!,
                                key = it.key
                            )
                    }
                }

            if (!roundEnv.processingOver()) {
                return false
            }

            createMetadataContainerClass()
            return true
        }.onFailure { exception ->
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "[KCacheProcessor] Error during processing: ${exception.localizedMessage}"
            )
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                exception.stackTrace.joinToString("\n")
            )
        }
        return false
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(KCacheable::class.java.canonicalName)
    }
}
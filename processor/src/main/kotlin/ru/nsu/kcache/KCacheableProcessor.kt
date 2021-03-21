package ru.nsu.kcache

import com.google.auto.service.AutoService
import ru.nsu.kcache.creator.HandlerMetadataContainerCreator
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import java.nio.file.Path
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.util.Elements
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

    private val kCacheableMetadata: KCacheableMetadata = mutableMapOf()

    private val metadataCreator = HandlerMetadataContainerCreator()

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        filer = processingEnv.filer
        elementUtils = processingEnv.elementUtils
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
                Path.of(
                    kaptDir, metadataCreator.handlerMetadataClassName
                )
            )
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        runCatching {
            roundEnv.getElementsAnnotatedWith(KCacheable::class.java)
                ?.filter { it.kind == ElementKind.METHOD }
                ?.forEach { element ->
                    val enclosingName = elementUtils.getBinaryName(
                        // we know that element's kind is method
                        element.enclosingElement as TypeElement
                    )

                    element.getAnnotation(KCacheable::class.java)?.let {
                        kCacheableMetadata["$enclosingName.$element"] = RequestHandlerMetadata(
                            tableStates = it.tables.asList(),
                            resultBuilderFactory = getResultBuilderFactoryClassName(it)!!
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
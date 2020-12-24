package ru.nsu.kcache

import com.google.auto.service.AutoService
import com.google.gson.Gson
import ru.nsu.manasyan.kcache.core.KCacheable
import ru.nsu.manasyan.kcache.core.RequestStatesMapper
import ru.nsu.manasyan.kcache.defaults.RamRequestStatesMapper
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.FileObject
import javax.tools.StandardLocation

@AutoService(Processor::class)
class KCacheableProcessor : AbstractProcessor() {

    companion object {
        private const val MAPPINGS_FILE_PREFIX = ""
    }

    private lateinit var messager: Messager

    private lateinit var filer: Filer

    private lateinit var elementUtils: Elements

    private val mapper = RamRequestStatesMapper()

    private val gson = Gson()

    private lateinit var mappingsFile: FileObject

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        filer = processingEnv.filer
        elementUtils = processingEnv.elementUtils

        mappingsFile = filer.createResource(
            StandardLocation.CLASS_OUTPUT,
            MAPPINGS_FILE_PREFIX,
            RequestStatesMapper.MAPPINGS_FILE_PATH
        )
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(KCacheable::class.java)
            ?.filter { it.kind == ElementKind.METHOD }
            ?.forEach { element ->
                val enclosingName = elementUtils.getBinaryName(
                    // we know that element's kind is method
                    element.enclosingElement as TypeElement
                )
                element.getAnnotation(KCacheable::class.java)?.let {
                    mapper.setRequestStates(
                        "$enclosingName.${element.simpleName}",
                        it.tables.asList()
                    )
                }
            }

        if (!roundEnv.processingOver()) {
            return false
        }

        mappingsFile.openWriter().use {
            it.write(gson.toJson(mapper))
        }

        return true
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(KCacheable::class.java.canonicalName)
    }
}
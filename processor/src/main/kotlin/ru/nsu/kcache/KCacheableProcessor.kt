package ru.nsu.kcache

import com.google.auto.service.AutoService
import ru.nsu.manasyan.kcache.core.KCacheable
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class KCacheableProcessor : AbstractProcessor() {

    private lateinit var messager: Messager

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
//        types = processingEnv.typeUtils
//        filer = processingEnv.filer
//        elementUtils = processingEnv.elementUtils
        messager = processingEnv.messager
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        roundEnv?.getElementsAnnotatedWith(KCacheable::class.java)?.forEach {
            messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    it.simpleName
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
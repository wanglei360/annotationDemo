package com.apt_kotlin

import com.lib.KBindView
import com.lib.imp.BindingSuffix
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

/**
 * 该工具需要使用kapt引入，否则创建的类会在build.generated.ap_generated_sources路径中，
 * 而这个路径的kotlin文件不会被打包到apk中，所以会导致类找不到异常
 */
class ProcessorKotlin : AbstractProcessor() {
    private var filer: Filer? = null
    private var messager: Messager? = null
    private var elementUtils: Elements? = null

    //每个存在注解的类整理出来，key:package_classname value:被注解的类型元素
    private val annotationClassMap: HashMap<String, MutableList<Element>> = HashMap()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        filer = processingEnv.filer;
        elementUtils = processingEnv.elementUtils;
        messager = processingEnv.messager;
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (!roundEnv.processingOver()) {
            demo()
            buildAnnotatedElement(roundEnv, KBindView::class.java)
        } else {
            System.out.println("")
            annotationClassMap.forEach { map ->
                //调用处的路径    com.demo
                val packageName = map.key.split("_")[0]

                //调用处名字    KotlinAnnotationActivity
                val typeName = map.key.split("_")[1]

                //KotlinAnnotationActivity_BindingKssss
                val classNameSuffix = "$typeName${BindingSuffix.GENERATED_CLASS_SUFFIX_KOTLIN}"

                //activity的名字
                val className = ClassName.bestGuess("$packageName.$typeName")

                //创建kt文件
                filer?.apply {
                    FileSpec.builder(packageName, classNameSuffix)
                        .addType(getTypeSpec(classNameSuffix, className, map))
                        .build().writeTo(this)
                }
            }
        }
        return true
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(KBindView::class.java.canonicalName)
    }

    /*
    创建以下样式的类
    public class KotlinAnnotationActivity_BindingKssss(activity: KotlinAnnotationActivity) {
        init { bind(activity) }
        public fun bind(activity: KotlinAnnotationActivity): Unit {
            activity.tv = activity.findViewById<android.widget.TextView>(2131231131)
        }
    }
     */
    private fun getTypeSpec(
        className: String,
        classBinding: ClassName,
        map: Map.Entry<String, MutableList<Element>>
    ): TypeSpec {
        return TypeSpec.classBuilder(className).apply { //创建类
            //添加构造函数
            primaryConstructor(
                FunSpec.constructorBuilder().apply {
                    addParameter("activity", classBinding)//构造函数添加参数
                    addStatement("bind(activity)")//构造函数调用bind方法
                }.build()
            )

            //创建bind方法
            addFunction(
                FunSpec.builder("bind").apply {
                    addParameter("activity", classBinding)//添加参数
                    map.value.forEach {//添加方法体
                        addStatement(
                            "activity.${it.simpleName} = activity.findViewById<${it.asType()}>(${
                                it.getAnnotation(
                                    KBindView::class.java
                                ).value
                            })"
                        )//添加方法体
                    }
                }.build()
            )
        }.build()
    }

    private fun buildAnnotatedElement(roundEnv: RoundEnvironment, clazz: Class<out Annotation?>) {
        for (element in roundEnv.getElementsAnnotatedWith(clazz)) {
            val className = getFullClassName(element)
            var cacheElements: MutableList<Element>? = annotationClassMap[className]
            if (cacheElements == null) {
                cacheElements = ArrayList()
                annotationClassMap[className] = cacheElements
            }
            cacheElements.add(element)
        }
    }

    private fun getFullClassName(element: Element): String {
        val typeElement = element.enclosingElement as TypeElement
        val packageName = elementUtils!!.getPackageOf(typeElement).qualifiedName.toString()
        return packageName + "_" + typeElement.simpleName.toString()
    }

    private fun demo() {
        val companion = TypeSpec.companionObjectBuilder()
            .addProperty(
                PropertySpec.builder("buzz", String::class)
                    // 初始化值
                    .initializer("%S", "buz111z")
                    // 修饰符
                    .addModifiers(KModifier.CONST)
                    // 注释
                    .addKdoc("println(%S)", "Beep!")
                    .build()
            )
            .addFunction(
                FunSpec.builder("beep")
                    .addStatement("println(%S)", "Beep!")
                    .build()
            )
            .build()

        val helloWorld = TypeSpec.classBuilder("HelloWorld").addType(companion).build()
        // 写出 kt文件名
        val file = FileSpec.builder("", "HelloWorld").addType(helloWorld).build()
        // 生成目录；用于写入
        val dir = File("code")
        dir.mkdirs()
        file.writeTo(dir)
    }
}
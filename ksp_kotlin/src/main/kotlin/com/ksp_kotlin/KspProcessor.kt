package com.ksp_kotlin

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.lib.imp.KspBindView
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

/**
 * 创建者：leiwu
 * <p> 时间：2022/4/6 10:25
 * <p> 类描述：只能生成代码，没有找到像kapt那种获取注解信息的方法
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
class KspProcessor(env: SymbolProcessorEnvironment) : SymbolProcessor {

    private var routerBindType: KSType? = null
    private var isload = false
    private val codeGenerator: CodeGenerator by lazy { env.codeGenerator }
    private val logger: KSPLogger by lazy { env.logger }
    private val packageInfo: HashMap<String, String> by lazy { HashMap() }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isload) {
            return emptyList()
        }

        val symbols = resolver.getSymbolsWithAnnotation(KspBindView::class.java.name)

        routerBindType = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(KspBindView::class.java.name)
        )?.asType(emptyList()) ?: kotlin.run {
            logger.error("JsonClass type not found on the classpath.")
            return emptyList()
        }
        symbols.asSequence().forEach {
            if (!add(it)) {
                //前面的那些各种检查之类的要有，否则报错，各种奇怪的错
                resolver.initPackageInfo()
            }
        }
        isload = true
        return emptyList()
    }

    private fun add(type: KSAnnotated): Boolean {
        logger.check(type is KSClassDeclaration && type.origin == Origin.KOTLIN, type) {
            "@JsonClass can't be applied to $type: must be a Kotlin class"
        }
        return type !is KSClassDeclaration
    }

    fun KSPLogger.check(condition: Boolean, element: KSNode?, message: () -> String) {
        if (!condition) {
            error(message(), element)
        }
    }

    /**
     * 收集注解调用处的类名和路径
     */
    private fun Resolver.initPackageInfo() {
        getAllFiles().forEach {
            // /Users/leiwu/Downloads/test/annotationDemos/netDemos/kspDemo/app/src/main/java/com/kspdemo/NewMainActivity.kt
            val filePath = it.filePath
            // NewMainActivity.kt
            val className = it.fileName.split(".")[0]
            // com.kspdemo
            val classPath = it.packageName.asString()
            packageInfo[className] = classPath
        }

        val packageName = "com.demo"
        val className = "ClassNameSuffixyyyyyy"
        val parameter = ClassName.bestGuess("$packageName.KspKotlinAnnotationActivity")

        // todo 79行报FileAlreadyExistsException，所以导致程序无法正常运行，
        // todo 虽然报错，但是下方代码可以正常生成代码，文件位置app/build/generated/ksp/debug/kotlin/com/demo
        val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, packageName, className)
        val spec = FileSpec.builder(packageName, className)
            .addType(getTypeSpec(className, parameter))
            .build()
        file.use {
            it.write(spec.toString().toByteArray())
        }
    }


    private fun getTypeSpec(className: String, classBinding: ClassName): TypeSpec {
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
//                    map.value.forEach {//添加方法体
//                        addStatement(
//                            "activity.${it.simpleName} = activity.findViewById<${it.asType()}>(${
//                                it.getAnnotation(
//                                    KspBindView::class.java
//                                ).value
//                            })"
//                        )//添加方法体
//                    }
                }.build()
            )
        }.build()
    }
}
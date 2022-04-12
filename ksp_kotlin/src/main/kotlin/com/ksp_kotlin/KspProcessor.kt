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

    private lateinit var logger: KSPLogger
    private lateinit var codeGenerator: CodeGenerator
    private lateinit var routerBindType: KSType
    private var isload = false


    private val packageInfo: HashMap<String, String> by lazy { HashMap() }


    init {
        this.codeGenerator = env.codeGenerator
        this.logger = env.logger
    }


    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isload) {
            return emptyList()
        }

        //这一行是写死的生成代码，没有动态数据，没找到办法获取
        resolver.initPackageInfo()

        val symbols1 = resolver.getSymbolsWithAnnotation(KspBindView::class.java.name)

        val routerBindType = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(KspBindView::class.java.name)
        )?.asType(emptyList()) ?: kotlin.run {
            logger.error("JsonClass type not found on the classpath.")
            return emptyList()
        }

        symbols1.forEach {
            it.annotations.forEach {
                val shortName = it.shortName
                shortName.getShortName()//注解的名字

                it.arguments.forEach {
                    it.value//注解的值
                }
            }
        }

        return symbols1.toList()
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

        val packageName = "com.kspdemo"
        val className = "ClassNameSuffixyyyyyy"
        val parameter = ClassName.bestGuess("$packageName.NewMainActivity")

        val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, packageName, className)
//        val spec = FileSpec.builder(packageName, className)
//            .addType(getTypeSpec(className, parameter))
//            .build()
//        file.use {
//            val content = spec.toString().toByteArray()
//            it.write(content)
//        }
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
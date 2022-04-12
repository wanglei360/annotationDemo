package com.apt;

import com.lib.BindView;
import com.lib.imp.BindingSuffix;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * 创建者：leiwu
 * <p> 时间：2022/3/2 10:01
 * <p> 类描述：
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
public class ProcessorJava extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;
    //每个存在注解的类整理出来，key:package_classname value:被注解的类型元素
    private Map<String, List<Element>> annotationClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            buildAnnotatedElement(roundEnv, BindView.class);
        } else {
            for (Map.Entry<String, List<Element>> entry : annotationClassMap.entrySet()) {
                String packageName = entry.getKey().split("_")[0];//调用处的路径
                String typeName = entry.getKey().split("_")[1];//调用处名字
                ClassName className = ClassName.get(packageName, typeName);//根据路径和名字获取类名
                ClassName classNameSuffix = ClassName.get(packageName,//获取带生产类的名字
                        typeName + BindingSuffix.GENERATED_CLASS_SUFFIX);
                TypeSpec.Builder classBuilder = createClass(classNameSuffix);//创建类

                createConstructor(classBuilder, className);//添加构造函数

                MethodSpec.Builder bindViewsMethodBuilder =//添加bindViews方法
                        createBindViewsMethod(className);
                createMethodBody(entry, bindViewsMethodBuilder);//增加方法体
                classBuilder.addMethod(bindViewsMethodBuilder.build());//将构建出来的方法添加到类里面

                try {//将类写入文件中
                    JavaFile.builder(packageName, classBuilder.build())
                            .build().writeTo(filer);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
                }
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(BindView.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void buildAnnotatedElement(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        for (Element element : roundEnv.getElementsAnnotatedWith(clazz)) {
            String className = getFullClassName(element);
            List<Element> cacheElements = annotationClassMap.get(className);
            if (cacheElements == null) {
                cacheElements = new ArrayList<>();
                annotationClassMap.put(className, cacheElements);
            }
            cacheElements.add(element);
        }
    }

    private String getFullClassName(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        return packageName + "_" + typeElement.getSimpleName().toString();
    }

    /* 创建要生成的类，如下所示
    public class MainActivity$Binding {}
    */
    private TypeSpec.Builder createClass(ClassName className) {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);
    }

    /* 创建构造函数构造函数
     public MainActivity$Binding(MainActivity activity) {
        bindViews(activity);
     } */
    private void createConstructor(TypeSpec.Builder classBuilder, ClassName className) {
        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(className, "activity")
                .addStatement("$N($N)", "bindViews", "activity")
                .build());
    }

    /* 创建方法bindViews(MainActivity activity)
    private void bindViews(MainActivity activity) {}
    */
    private MethodSpec.Builder createBindViewsMethod(ClassName className) {
        return MethodSpec.methodBuilder("bindViews")
                .addModifiers(Modifier.PRIVATE)
                .returns(void.class)
                .addParameter(className, "activity");
    }

    /* 增加方法体
    activity.tvHello = (TextView)activity.findViewById(2131165326);
    */
    private void createMethodBody(Map.Entry<String, List<Element>> entry, MethodSpec.Builder bindViewsMethodBuilder) {
        for (VariableElement variableElement : ElementFilter.fieldsIn(entry.getValue())) {
            BindView bindView = variableElement.getAnnotation(BindView.class);
            if (bindView != null) {
                bindViewsMethodBuilder.addStatement("$N.$N = ($T)$N.findViewById($L)",
                        "activity",
                        variableElement.getSimpleName(),
                        variableElement,
                        "activity",
                        bindView.value());
            }
        }
    }
}

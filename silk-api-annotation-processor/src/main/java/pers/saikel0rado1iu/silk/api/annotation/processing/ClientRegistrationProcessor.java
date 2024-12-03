/*
 * This file is part of Silk API.
 * Copyright (C) 2023 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.api.annotation.processing;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import pers.saikel0rado1iu.silk.api.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;

import javax.annotation.Nullable;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pers.saikel0rado1iu.silk.api.annotation.processing.ProcessorUtil.getTypeElement;

/**
 * <h2>客户端注册处理器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("pers.saikel0rado1iu.silk.api.annotation.ClientRegistration")
public final class ClientRegistrationProcessor extends AbstractProcessor {
    static Optional<TypeSpec.Builder> generateMethod(@Nullable TypeSpec.Builder builder,
                                                     Element element,
                                                     ProcessingEnvironment processingEnv,
                                                     ClientRegistration clientRegistration) {
        final TypeElement registrar = getTypeElement(processingEnv, clientRegistration::registrar);
        final TypeElement type = getTypeElement(processingEnv, clientRegistration::type);

        if (registrar == null) {
            String msg = String.format("未找到注册器：%s", clientRegistration.registrar());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
            return Optional.empty();
        }
        if (type == null) {
            String msg = String.format("未找到注册类型：%s", clientRegistration.type());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
            return Optional.empty();
        }
        builder = builder != null ? builder : ProcessorUtil.createTypeBuilder(type, element);
        // 注册方法
        // 提取构造方法
        Optional<ExecutableElement> constructorOpt = registrar
                .getEnclosedElements()
                .stream()
                .filter(e -> e.getKind() == ElementKind.CONSTRUCTOR)
                .map(e -> (ExecutableElement) e)
                .max(Comparator.comparingInt(executable -> executable.getParameters().size()));
        // 获取类的类型信息
        TypeMirror typeMirror = type.asType();
        TypeName typeName = TypeName.get(typeMirror);
        // 处理泛型类型
        if (typeMirror instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (!typeArguments.isEmpty()) {
                TypeName[] names = typeArguments
                        .stream()
                        .map(typeArg -> WildcardTypeName.subtypeOf(Object.class))
                        .toArray(TypeName[]::new);
                typeName = ParameterizedTypeName
                        .get(ClassName.get((TypeElement) declaredType.asElement()), names);
            }
        }
        if (constructorOpt.isPresent()) {
            Elements utils = processingEnv.getElementUtils();
            ClassName annoName = ClassName.get(utils.getTypeElement("net.fabricmc.api.Environment"));
            // 创建注解
            AnnotationSpec annotationSpec = AnnotationSpec
                    .builder(annoName)
                    .addMember("value",
                            "$T.$L",
                            utils.getTypeElement("net.fabricmc.api.EnvType"),
                            utils.getTypeElement("net.fabricmc.api.EnvType")
                                 .getEnclosedElements()
                                 .getFirst())
                    .build();
            String javadoc = """
                    客户端注册方法<p>
                    提供 {@link Runnable} 进行注册，您应该以如下方式进行客户端注册：
                    <p>
                    <pre>{@code
                    abstract class ClientFoo implements ClientItemRegistry {
                        static {
                                ItemRegistry.registrar(() -> ...).register(EXAMPLE_ITEM);
                        }
                    
                        private ClientFoo() {
                        }
                    }
                    }</pre>
                    
                    @param registerMethod   注册方法，在此方法内为所有需要客户端注册的对象进行注册
                    @return                 客户端注册器
                    """;
            MethodSpec.Builder methodBuilder = MethodSpec
                    .methodBuilder("registrar")
                    .addJavadoc(javadoc)
                    .addAnnotation(annotationSpec)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addTypeVariable(TypeVariableName.get("T", typeName))
                    .returns(TypeName.get(registrar.asType()));
            StringBuilder pars = new StringBuilder();
            for (VariableElement param : constructorOpt.get().getParameters()) {
                TypeMirror paramType = param.asType();
                String paramName = param.getSimpleName().toString();
                if (pars.isEmpty()) {
                    pars.append(paramName);
                } else {
                    pars.append(", ").append(paramName);
                }
                ParameterSpec parameterSpec = ParameterSpec
                        .builder(TypeName.get(paramType), paramName)
                        .build();
                methodBuilder.addParameter(parameterSpec);
            }
            // 添加客户端注册方法
            builder.addMethod(methodBuilder
                    .addStatement("return new $T(" + pars + ")", registrar)
                    .build());
            return Optional.of(builder);
        } else {
            String msg = String.format("未找到构造方法：%s", clientRegistration.registrar());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
            return Optional.empty();
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Elements elementUtils = processingEnv.getElementUtils();

        for (Element element : roundEnv.getElementsAnnotatedWith(ClientRegistration.class)) {
            if (!ProcessorUtil.checkAnnotation(ClientRegistration.class, roundEnv,
                    processingEnv, (TypeElement) element)) {
                return true;
            }
            ClientRegistration clientRegistration = element.getAnnotation(ClientRegistration.class);
            boolean registrarEqualsClass = "java.lang.Class".equals(getTypeElement(
                    processingEnv, clientRegistration::registrar)
                    .getQualifiedName()
                    .toString());
            boolean typeEqualsClass = "java.lang.Class".equals(getTypeElement(
                    processingEnv, clientRegistration::type)
                    .getQualifiedName()
                    .toString());
            if (registrarEqualsClass && typeEqualsClass) {
                continue;
            }
            // 写入文件
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            Optional<TypeSpec.Builder> optionalBuilder = ClientRegistrationProcessor
                    .generateMethod(null, element, processingEnv, clientRegistration);
            if (optionalBuilder.isEmpty()) {
                return false;
            }
            TypeSpec.Builder builder = optionalBuilder.get();
            ServerRegistration serverRegistration = element.getAnnotation(ServerRegistration.class);
            if (serverRegistration != null) {
                ServerRegistrationProcessor
                        .generateMethod(builder, element, processingEnv, serverRegistration);
            }
            // 创建一个文件
            try {
                TypeSpec typeSpec = builder.build();
                FileObject existingFile = processingEnv.getFiler().getResource(
                        StandardLocation.SOURCE_OUTPUT, packageName, typeSpec.name + ".java");
                if (existingFile != null && existingFile.getLastModified() > 0) {
                    return true;
                }
                try {
                    JavaFile.builder(packageName, typeSpec)
                            .build()
                            .writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    String msg = String.format("无法生成源码：%s", e.getMessage());
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
                    return false;
                }
            } catch (IOException e) {
                String msg = String.format("%s 生成文件被占用，可能是代码正在生成中：%s", element, e.getMessage());
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg);
            }
        }
        return true;
    }
}
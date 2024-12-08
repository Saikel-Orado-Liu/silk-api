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
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static pers.saikel0rado1iu.silk.api.annotation.processing.ProcessorUtil.getTypeElement;

/**
 * <h2>服务端注册处理器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu">
 *         <img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4">
 *         </a>
 * @since 1.0.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("pers.saikel0rado1iu.silk.api.annotation.ServerRegistration")
public final class ServerRegistrationProcessor extends AbstractProcessor {
    static Optional<TypeSpec.Builder> generateMethod(@Nullable TypeSpec.Builder builder,
                                                     Element element,
                                                     ProcessingEnvironment processingEnv,
                                                     ServerRegistration serverRegistration) {
        final TypeElement registrar = getTypeElement(processingEnv, serverRegistration::registrar);
        final TypeElement type = getTypeElement(processingEnv, serverRegistration::type);
        final TypeElement generics = getTypeElement(processingEnv, serverRegistration::generics);

        if (registrar == null) {
            String msg = String.format("未找到注册器：%s", serverRegistration.registrar());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
            return Optional.empty();
        }
        if (type == null) {
            String msg = String.format("未找到注册类型：%s", serverRegistration.type());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
            return Optional.empty();
        }
        if (generics == null) {
            String msg = String.format("未找到注册泛型：%s", serverRegistration.type());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
            return Optional.empty();
        }
        builder = builder != null
                ? builder
                : ProcessorUtil.createTypeBuilder(type, element);
        // 获取类的类型信息
        TypeMirror genericsMirror = generics.asType();
        TypeName genericsName = TypeName.get(genericsMirror);
        // 处理泛型类型
        if (genericsMirror instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (!typeArguments.isEmpty()) {
                TypeName[] names = typeArguments
                        .stream()
                        .map(typeArg -> WildcardTypeName.subtypeOf(Object.class))
                        .toArray(TypeName[]::new);
                genericsName = ParameterizedTypeName.get(ClassName.get((TypeElement) declaredType.asElement()), names);
            }
        }
        // 注册方法
        String javadoc =
                """
                服务端注册方法
                <p>
                此方法返回一个服务端注册器，注册器注册返回注册对象
                
                @param $N   注册对象的提供器
                @return     服务端注册器
                """;
        String argName = type.getSimpleName().toString().toLowerCase();
        ParameterizedTypeName paramTypeName = processingEnv
                .getTypeUtils().isSameType(type.asType(), generics.asType())
                ? ParameterizedTypeName.get(ClassName.get(Supplier.class), TypeVariableName.get("T"))
                : ParameterizedTypeName.get(ClassName.get(Supplier.class),
                ParameterizedTypeName.get(ClassName.get(type), TypeVariableName.get("T")));
        MethodSpec registrarMethod = MethodSpec
                .methodBuilder("registrar")
                .addJavadoc(javadoc, argName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(TypeVariableName.get("T", genericsName))
                .addParameter(paramTypeName, argName)
                .addStatement("return new $T($N)", registrar, argName)
                .returns(TypeName.get(registrar.asType()))
                .build();
        // 添加服务端注册方法
        builder.addMethod(registrarMethod);
        return Optional.of(builder);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Elements elementUtils = processingEnv.getElementUtils();

        for (Element element : roundEnv.getElementsAnnotatedWith(ServerRegistration.class)) {
            if (!ProcessorUtil.checkAnnotation(ServerRegistration.class, roundEnv,
                    processingEnv, (TypeElement) element)) {
                return true;
            }
            ServerRegistration serverRegistration = element.getAnnotation(ServerRegistration.class);
            boolean registrarEqualsClass = "java.lang.Class".equals(getTypeElement(processingEnv,
                    serverRegistration::registrar)
                    .getQualifiedName()
                    .toString());
            boolean typeEqualsClass = "java.lang.Class".equals(getTypeElement(processingEnv,
                    serverRegistration::type)
                    .getQualifiedName()
                    .toString());
            if (registrarEqualsClass && typeEqualsClass) {
                continue;
            }
            // 写入文件
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            Optional<TypeSpec.Builder> optionalBuilder = ServerRegistrationProcessor
                    .generateMethod(null, element, processingEnv, serverRegistration);
            if (optionalBuilder.isEmpty()) {
                return false;
            }
            TypeSpec.Builder builder = optionalBuilder.get();
            ClientRegistration clientRegistration = element.getAnnotation(ClientRegistration.class);
            if (clientRegistration != null) {
                ClientRegistrationProcessor.generateMethod(builder, element, processingEnv, clientRegistration);
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
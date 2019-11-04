package nl.avans.testhelper.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class Parser {
    public PClass getClass(String className) throws IOException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(Paths.get("src/"+className+".java"));
        Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName(className);
        return new PClass(className, classA.get());
    }
}

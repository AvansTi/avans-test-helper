package nl.avans.testhelper.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;
import java.util.stream.Collectors;

public class PClass {

    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final String name;

    public PClass(String className, ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.name = className;
    }

    public PMethod getMethod(String method)
    {
        List<MethodDeclaration> methods = classOrInterfaceDeclaration.getMethods();
        return new PMethod(this, methods.stream().filter(m -> m.getNameAsString().equals(method)).collect(Collectors.toList()));
    }

    public PConstructor getConstructor()
    {
        List<ConstructorDeclaration> constructors = classOrInterfaceDeclaration.getConstructors();
        return new PConstructor(this, constructors);
    }
}

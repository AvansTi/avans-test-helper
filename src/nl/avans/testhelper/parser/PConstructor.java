package nl.avans.testhelper.parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class PConstructor {
    private final List<ConstructorDeclaration> constructors;
    private PClass pClass;

    PConstructor(PClass pclass, List<ConstructorDeclaration> methods) {
        this.constructors = methods;
        this.pClass = pclass;
    }

    public PConstructor noParams()
    {
        return new PConstructor(pClass, constructors.stream()
                .filter(p -> p.getParameters().size() == 0)
                .collect(Collectors.toList()));
    }
    public PConstructor parameters(String... params) {
        return new PConstructor(pClass, constructors.stream()
                .filter(p -> {
                    if(p.getParameters().size() != params.length)
                        return false;
                    for(int i = 0; i < p.getParameters().size(); i++)
                        if(!p.getParameters().get(i).getType().toString().equals(params[i]))
                            return false;
                    return true;
                }).collect(Collectors.toList()));
    }

    private void checkComplete() {
        Assert.assertTrue("Error in test, multiple methods found", this.constructors.size() == 1);
    }

    public String getCode()
    {
        checkComplete();
        return constructors.get(0).toString();
    }

    public void assertContains(Statement statement) {
        checkComplete();
        boolean contains = assertContainsRecursive(statement, constructors.get(0).getChildNodes());
        if(!contains)
            Assert.fail("Your method does not contain a "+statement.toString()+" statement. The code to your method is \n" + this.getCode());
    }

    private boolean assertContainsRecursive(Statement statement, List<Node> childNodes) {
        for(Node n : childNodes) {

            if(n instanceof BlockStmt)
            {
                BlockStmt blockStmt = (BlockStmt)n;
                if(assertContainsRecursive(statement, blockStmt.getChildNodes()))
                    return true;
            }
            if(statement == Statement.ForEach && n instanceof ForEachStmt)
                return true;
            if(statement == Statement.For && n instanceof ForStmt)
                return true;
            if(statement == Statement.While && n instanceof WhileStmt)
                return true;
            if(statement == Statement.Constructor && n instanceof ExplicitConstructorInvocationStmt)
                return true;
        }
        return false;
    }

}

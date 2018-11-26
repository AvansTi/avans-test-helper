package nl.avans.testhelper;

import fi.helsinki.cs.tmc.edutestutils.Reflex;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class TestHelperJavaFX {

    /**
     * Test if the JavaFX application is correctly implemented
     * @param object
     * @param classType
     * @param stage
     */
    public static void testJavaFXStart(Object object, Class classType, Stage stage) {

        TestHelper.testInterface(classType.getName(), Application.class);

        try {
            Reflex.reflect(classType).method("start").returningVoid().taking(Stage.class).invokeOn(object, stage);
        } catch (Throwable ex) {
            fail("Cannot use method start. Error: " + ex.getMessage());
        }
    }

    /**
     * Find all user components of a specific type
     * @param root Parent element
     * @param type Type of element to search for
     * @param <T> Type of element to search for
     * @return List of all matched types found under the parent
     */
    public static <T extends Node> List<T> getAllNodesOfType(Parent root, Class type) {
        List<T> nodes = new ArrayList<T>();
        addAllDescendentsNodes(root, nodes, type);
        return nodes;
    }

    /**
     * Internal method to do recurisive search of UI compoenents
     * @param parent Parent element
     * @param nodes List of nodes from type T
     * @param type Type of element to search for
     * @param <T> Type of element to search for
     */
    private static <T extends Node> void addAllDescendentsNodes(Parent parent, List<T> nodes, Class type) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node.getClass().isAssignableFrom(type))
                nodes.add((T)node);
            if (node instanceof Parent)
                addAllDescendentsNodes((Parent)node, nodes, type);
        }
    }



}

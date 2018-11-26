package nl.avans.testhelper;

import fi.helsinki.cs.tmc.edutestutils.Reflex;
import javafx.application.Application;
import javafx.stage.Stage;

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
            fail("Cannot use methode start. Error:" + ex.getMessage());
        }
    }



}

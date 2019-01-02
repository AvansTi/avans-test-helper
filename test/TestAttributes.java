import fi.helsinki.cs.tmc.edutestutils.Reflex;
import nl.avans.testhelper.TestHelper;
import org.junit.Test;

public class TestAttributes
{
	@Test
	public void testAttributes() throws Throwable {
		Reflex.ClassRef<Object> clz = Reflex.reflect("TestClassExtends");

		Object o = clz.constructor().taking(String.class, String.class).invoke("Hello", "World");

		TestHelper.testAttributeValue("TestClassExtends", "value", String.class, o, "Hello");
		TestHelper.testAttributeValue("TestClassExtends", "otherValue", String.class, o, "World");


	}


}




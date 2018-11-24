package nl.avans.testhelper;

import com.sun.deploy.util.ReflectionUtil;
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestHelper {

	public static void testClassExists(String className) {
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);

		assertNotNull("Class " + className +" not found", clazz);
		assertTrue("Class " + className + " should be public", clazz.isPublic());
	}

	public static void testAttribute(String className, String attributeName, Class type)
	{
		testClassExists(className);

		Reflex.ClassRef<Object> clazz;
		clazz = Reflex.reflect(className);

		boolean found = false;
		Field[] fields = ReflectionUtils.findClass(className).getDeclaredFields();
		for(Field field : fields)
		{
			if(field.getName().equals(attributeName))
			{
				int modifiers = field.getModifiers();
				assertTrue("Attribute " + className + "." + attributeName + " should be private or protected", Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers) );
				assertFalse("Attribute " + className + "." + attributeName + " should not be static", Modifier.isStatic(modifiers));
				assertFalse("Attribute " + className + "." + attributeName + " should not be final", Modifier.isFinal(modifiers));
				assertTrue("Attribute " + className + "." + attributeName + " is the wrong type. It is " + field.getType().toString() + ", but should be " + type.toString(), field.getType().equals(type));
				found = true;
			}
		}
		assertTrue("Class " + className + " does not have an attribute called " + attributeName, found);
	}

	public static void testAttributeCount(String className, int count)
	{
		Field[] fields = ReflectionUtils.findClass(className).getDeclaredFields();
		assertTrue("Class " + className + " should only have " + count + " attributes, but " + fields.length + " were found", fields.length == count);
	}

	public static void testMethodCount(String className, int count)
	{
		Method[] methods = ReflectionUtils.findClass(className).getDeclaredMethods();
		assertTrue("Class " + className + " should only have " + count + " methods, but " + methods.length + " were found", methods.length == count);
	}

	public static String[] callStaticVoidMethod(String className, String methodName)
	{
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);

		MockStdio io = new MockStdio();
		io.enable();
		try {
			clazz.staticMethod(methodName).returningVoid().takingNoParams().invoke();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			assertTrue("Error running method", false);
		}

		String[] out = io.getSysOut().split("\n");
		io.disable();
		return out;
	}
	public static String[] callVoidMethod(String className, String methodName, Object object)
	{
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);

		MockStdio io = new MockStdio();
		io.enable();
		try {
			clazz.method(methodName).returningVoid().takingNoParams().invokeOn(object);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			assertTrue("Error running method", false);
		}

		String[] out = io.getSysOut().split("\n");
		io.disable();
		return out;
	}

	public static String[] callMain(String className)
	{
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);

		MockStdio io = new MockStdio();
		io.enable();
		try {
			clazz.staticMethod("main").returningVoid().taking(String[].class).invoke(null);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			assertTrue("Error running method", false);
		}

		String[] out = io.getSysOut().split("\n");
		io.disable();
		return out;
	}

	public static void testContains(String[] lines, String text)
	{
		boolean contains = false;
		for(String line : lines)
			if(line.contains(text))
				contains = true;
		assertTrue("Output should contain " + text + ", but this text was not found. Your output was:\n" + String.join("\n", lines), contains);
	}

	public static ContainOrderTester testContains(String[] lines)
	{
		return new ContainOrderTester(lines);
	}


}

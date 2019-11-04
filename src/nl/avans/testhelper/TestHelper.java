package nl.avans.testhelper;

import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import nl.avans.testhelper.parser.Parser;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

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
		return callVoidMethod(className, methodName, object, "");
	}
	public static String[] callVoidMethod(String className, String methodName, Object object, String input)
	{
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);

		MockStdio io = new MockStdio();
		io.enable();
		io.setSysIn(input);
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

	/**
	 * Test if the class inherits a specific interface
	 * @param className subclass name
	 * @param implInterface Interface class
	 */
	public static void testInterface(String className, Class implInterface) {
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);

		assertTrue("Class " + className + " should implement interface " + implInterface.getName(), clazz.inherits(implInterface));

	}

	public static ContainOrderTester testContains(String[] lines)
	{
		return new ContainOrderTester(lines);
	}


	public static void testIsAbstract(String className)
	{
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);
		assert(clazz != null);
		assertTrue("Class " + className + " should be abstract", Modifier.isAbstract(clazz.cls().getModifiers()));
	}

	public static void testIsNotAbstract(String className)
	{
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);
		assert(clazz != null);
		assertFalse("Class " + className + " should be abstract", Modifier.isAbstract(clazz.cls().getModifiers()));
	}

	public static void testExtends(String className, String extendsClass)
	{
		Reflex.ClassRef<Object> clazz = Reflex.reflect(className);
		assert(clazz != null);

		Reflex.ClassRef<Object> extendsClazz = Reflex.reflect(extendsClass);
		assert(extendsClazz != null);

		assertEquals("Class " + className + " should extend " + extendsClass, clazz.cls().getSuperclass(), extendsClazz.cls());
	}




	public static void testGetter(String className, String attributeName, Class type)
	{
//		testClassExists(className);
		Reflex.ClassRef<Object> clazz;
		clazz = Reflex.reflect(className);
		String getterName = "get" + capitalize(attributeName);
		Reflex.MethodRef0 getter = clazz.method(getterName).returning(type).takingNoParams();
		assertNotNull("Getter " + className + "." + getterName + " not found", getter);
	}

	public static void testSetter(String className, String attributeName, Class type)
	{
//		testClassExists(className);
		Reflex.ClassRef<Object> clazz;
		clazz = Reflex.reflect(className);
		String setterName = "set" + capitalize(attributeName);
		Reflex.MethodRef1 setter = clazz.method(setterName).returningVoid().taking(type);
		assertNotNull("Setter " + className + "." + setterName + " not found", setter);
	}


	public static void testGetterSetter(String className, String attributeName, Class type)
	{
		testGetter(className, attributeName, type);
		testSetter(className, attributeName, type);
	}


	public static <T> void testGetterSetter(String className, String attributeName, Class type, Object object, T toSet)
	{
		testGetter(className, attributeName, type);
		testSetter(className, attributeName, type);

		Reflex.ClassRef<Object> clazz;
		clazz = Reflex.reflect(className);
		String getterName = "get" + capitalize(attributeName);
		Reflex.MethodRef0 getter = clazz.method(getterName).returning(type).takingNoParams();
		String setterName = "set" + capitalize(attributeName);
		Reflex.MethodRef1 setter = clazz.method(setterName).returningVoid().taking(type);

		Field field = getAttribute(className, attributeName, type);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			setter.invokeOn(object, toSet);

			T gotten = (T) getter.invokeOn(object);
			assertEquals("Returned value of setter is not equal to set value", toSet, gotten);

			T fieldValue = (T) field.get(object);
			assertEquals("Set value is not the right value", toSet, fieldValue);

		} catch (Throwable throwable) {
			fail("Error calling getter or setter: " + throwable);
		}

		field.setAccessible(accessible);

	}

	public static<T> void testAttributeValue(String className, String attributeName, Class type, Object object, T value) throws IllegalAccessException {
		Field field = getAttribute(className, attributeName, type);
		boolean accessable = field.isAccessible();
		field.setAccessible(true);
		assertEquals("Attribute value for " + className + "." + attributeName + " is not set properly", field.get(object), value);
		field.setAccessible(accessable);
	}

	public static void testAttributeExists(String className, String attributeName)
	{
		Class<?> clazz = ReflectionUtils.findClass(className);
		Field[] fields = clazz.getDeclaredFields();
		boolean found = false;
		for(Field field : fields)
			if(field.getName().equals(attributeName))
				found = true;
		Assert.assertTrue("Class " + className + " does not have an attribute called " + attributeName, found);
	}


	public static Field getAttribute(String className, String attributeName, Class type)
	{
		return getAttribute(ReflectionUtils.findClass(className), attributeName, type);
	}

	public static Field getAttribute(Class<?> clazz, String attributeName, Class type)
	{
		Field[] fields = clazz.getDeclaredFields();
		String className = clazz.getName();
		for(Field field : fields)
		{
			if(field.getName().equals(attributeName))
			{
				int modifiers = field.getModifiers();
				assertTrue("Attribute " + className + "." + attributeName + " should be private or protected", Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers) );
				assertFalse("Attribute " + className + "." + attributeName + " should not be static", Modifier.isStatic(modifiers));
				assertFalse("Attribute " + className + "." + attributeName + " should not be final", Modifier.isFinal(modifiers));
				if(field.getType().equals(type))
					return field;
			}
		}

		Class<?> superClass = clazz.getSuperclass();
		if(superClass != null)
			return getAttribute(superClass, attributeName, type);
		return null;
	}



	private static String capitalize(String string) {
		if(string.length() <= 1)
			return string.toUpperCase();
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	public static Parser parser()
	{
		return new Parser();
	}


}

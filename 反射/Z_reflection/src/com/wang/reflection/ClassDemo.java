package com.wang.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wang.annotation.Comment;

/**
 * @author wxe
 * @since 1.0.0
 */
public class ClassDemo {
	
	public static void main(String[] args) {
		Class<?> classes = null;
		try {
			classes = Class.forName("com.wang.day.UserDemo");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		getClassString(classes);
		
		getClassName(classes);
		
		getHumpClassName(classes);
		
		getPublicField(classes);
		
		getAllField(classes);
		
		getConstructor(classes);
		
		getEnumFiled(classes);
	}
	/**
	 * 获取类名字符串，包含包名
	 * @param classes
	 */
	public static void getClassString(Class<?> classes){
		String classString = classes.getName();
		System.out.println(classString);//---com.wang.day.UserDemo
	}
	/**
	 * 简单的类名，不包含包名
	 * @param classes
	 * @return
	 */
	public static String getClassName(Class<?> classes){
		String className = classes.getSimpleName();
		System.out.println(className);//---UserDemo
		return className;
	}
	/**
	 * 获取驼峰式类名
	 * @param classes
	 */
	public static void getHumpClassName(Class<?> classes){
		String className = classes.getSimpleName();
		char[] chars = className.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		String humpName = new String(chars);
		System.out.println(humpName);//userDemo
	}
	/**
	 * 获取public修饰的属性，包括父类
	 * @param classes
	 */
	public static void getPublicField(Class<?> classes){
		Field[] fileds = classes.getFields();
		for (Field field : fileds) {
			String fieldName = field.getName();
			System.out.println(fieldName);//属性全部为private ，无输出
		}
	}
	/**
	 * 获取所有修饰符的属性
	 * @param classes
	 * @return
	 */
	public static Field[] getAllField(Class<?> classes){
		Field[] fileds = classes.getDeclaredFields();//指定类中声明为公有的(public)的所有变量集合,包括父类的
		for (Field field : fileds) {
			String fieldName = field.getName();
			System.out.println(fieldName);//age ,name
		}
		return fileds;
	}
	/**
	 * 获取构造器
	 * @param classes
	 */
	@SuppressWarnings("rawtypes")
	public static void getConstructor(Class<?> classes){
		Constructor[] constructors = classes.getConstructors();//包含每一个声明为公有的（Public）构造方法。
		for (Constructor constructor : constructors) {
			Class[] parameterTypes = constructor.getParameterTypes();//构造方法参数类型
			System.out.println(parameterTypes[0]);//class java.lang.String
		}
	}
	/**
	 * 获取方法
	 * @param clazz
	 * @return
	 */
	public static Method[] getMethods(Class<?> clazz){
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			System.out.println(method.getName());
		}
		return methods;
	}
	/**
	 * 获取枚举类型
	 * @param classes
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getEnumFiled(Class<?> classes) {
		Field[] fields = getAllField(classes);
		for (Field field : fields) {
			if (field.getAnnotation(Comment.class) != null && field.getAnnotation(Comment.class).enumFlag() == true) {
				//存在注解,获取注解对应的Class
				Class<Enum> enumClass = (Class<Enum>) field.getType();
				String enumName = enumClass.getSimpleName();
				
				System.out.println("enumName:"+enumName);
				
				Map<String, String> enumMap = parseEumnToMap(enumClass);
				for (String key : enumMap.keySet()) {
					System.out.println("key:"+key+"   value:"+enumMap.get(key));
				}
			}
		}
	}
	/**
	 * 将反射解析为map集合
	 * @param classes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> Map<String, String> parseEumnToMap(Class<?> clazz){
		final Map<String, String> map = new LinkedHashMap<String, String>();
		if (clazz.isEnum()) {
			E[] enumKey = (E[]) clazz.getEnumConstants();
			for (E e : enumKey) {
				Method[] getters = getMethods(clazz);
				try {
					for (Method method : getters) {
						if (method.getName().startsWith("get")) {
							method.setAccessible(true);
							Object value = method.invoke(e);
							map.put(e.name(), value.toString());
						}
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
		}
		
		return map;
	}

}

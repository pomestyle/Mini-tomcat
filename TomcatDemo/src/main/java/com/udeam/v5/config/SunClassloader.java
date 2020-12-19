package com.udeam.v5.config;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

@SuppressWarnings("all")
public class SunClassloader extends ClassLoader {

	
	@Override
	public Class<?> findClass(String classPath) throws ClassNotFoundException {

 
		try (InputStream in = new FileInputStream(classPath)) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int i = 0;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			byte[] byteArray = out.toByteArray();
			return defineClass(byteArray, 0, byteArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return null;
 
	}




	public static void main(String[] args) throws Exception {
		SunClassloader sunClazz = new SunClassloader();
		Class<?> clazz = sunClazz.findClass("MyServlet1");
		System.out.println(clazz.newInstance());
	}
}
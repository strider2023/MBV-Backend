package com.mbv.framework.util;

import java.io.Reader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class JSONUtil {

	private static Gson gson = new GsonBuilder().setExclusionStrategies(new JSONIgnoreStrategy()).setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

	private static Gson printGson = new GsonBuilder().setExclusionStrategies(new JSONIgnoreStrategy()).setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().create();

	public static String getJsonString(Object object) {
		return gson.toJson(object);
	}

	public static String getPrintableJsonString(Object object) {
		return printGson.toJson(object);
	}

	public static <T> T getObject(String jsonString, Class<T> clazz) {
		return jsonString != null ? gson.fromJson(jsonString, clazz) : null;
	}
	
	public static <T> T getObject(JsonElement json, Class<T> clazz) {
		return json != null ? gson.fromJson(json, clazz) : null;
	}

	public static <T> T getObject(Reader reader, Class<T> clazz) {
		return reader != null ? gson.fromJson(reader, clazz) : null;
	}

	public static <T> T getObject(String jsonString, Type type) {
		return gson.fromJson(jsonString, type);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	public static @interface JSONIgnore {
	}

	public static class JSONIgnoreStrategy implements ExclusionStrategy {

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(JSONIgnore.class) != null;
		}
	}
	
}

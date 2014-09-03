package eu.telecomnancy.factapgx.vendor.dependencyinjection;

import java.util.HashMap;
import java.util.Map;

public class Container {
	
	private static Map<String, Object> services;
	
	public static void set(String name, Object service) {
		if (null == services) {
			services = new HashMap<String, Object>();
		}
		services.put(name, service);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T get(String name, Class<T> c) {
		if (has(name)) {
			return (T) services.get(name);
		}
		return null;
	}
	
	public static boolean has(String name) {
		return services != null && services.containsKey(name);
	}
}

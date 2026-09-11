package dev.quercusdata.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import java.util.logging.Logger;

/**
 * Retrieves info from config.properties.
 *
 */
public class ApplicationGetPropertyValues {

	static Logger logger = Logger.getLogger(ApplicationGetPropertyValues.class.getName());
	private InputStream input;
	private Properties prop;

	public ApplicationGetPropertyValues(String propFileName) {
		prop = new Properties();
		try {
			input = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (input != null) {
				prop.load(input);
			} else {
				logger.severe("property file '" + propFileName + "' not found");
				throw new FileNotFoundException("property file '" + propFileName + "' not found");
			}
		} catch (Exception e) {
			logger.severe("ApplicationGetPropertyValues() '" + e.getMessage());
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.severe("ApplicationGetPropertyValues() '" + e.getMessage());
			}
		}
	}
	/*
	 * Given a property key gets its value.
	 */
	public String getPropValue( String key) throws IOException {

		String value = "";
		// get the property value and print it out
		value = prop.getProperty( key);
		return value;
	}

	/**
	 * Given a property key gets an array containing all values separated by a given string.
	 *
	 * @param key
	 * @param separator
	 * @return
	 * @throws IOException
	 */
	public List<String> getPropValueArray( String key, String separator) throws IOException {

		String allValues = getPropValue(key);
		List<String> values = valuesToArray(allValues, separator);
		return values;
	}

	/**
	 * Converts a String into a List of Strings.
	 *
	 * @param allValues
	 * @param separator
	 * @return
	 */
	public static List<String> valuesToArray(String allValues, String separator) {
		List<String> values = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(allValues, separator);
		while(tokenizer.hasMoreTokens()) {
			values.add(tokenizer.nextToken());
		}
		return values;
	}

	public List<String> getPropValueExtensionsArray(String key, String separator) throws IOException {
		String allValues = getPropValue(key);
		return valuesToExtensionsArray(allValues, separator);
	}

	public static List<String> valuesToExtensionsArray(String allValues, String separator) {
		List<String> values = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(allValues, separator);
		while (tokenizer.hasMoreTokens()) {
			String normalized = normalizeExtension(tokenizer.nextToken());
			if (!normalized.isEmpty()) {
				values.add(normalized);
			}
		}
		return values;
	}

	private static String normalizeExtension(String extension) {
		String normalized = extension == null ? "" : extension.trim().toLowerCase();
		while (normalized.startsWith(".")) {
			normalized = normalized.substring(1);
		}
		return normalized;
	}

}


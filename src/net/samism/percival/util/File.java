package net.samism.percival.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/21/14
 * Time: 10:44 PM
 * <p/>
 * Loads a (text) file with UTF-8 in a Jar-friendly way - namely using Class.getResourceAsStream(), etc
 */
public final class File {
	private static final Logger log = LoggerFactory.getLogger(File.class);

	public static String loadText(Class<?> c, String relativePath) {
		try (InputStream is = c.getResourceAsStream(relativePath);
			 InputStreamReader ir = new InputStreamReader(is, "UTF-8");
			 BufferedReader br = new BufferedReader(ir)) {

			StringBuilder sb = new StringBuilder();

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				sb.append(line);
			}

			return sb.toString();
		} catch (IOException e) {
			log.error("Error loading file: " + relativePath);
			return null;
		}
	}
}
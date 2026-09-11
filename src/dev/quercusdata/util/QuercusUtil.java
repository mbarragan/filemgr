package dev.quercusdata.util;

import java.io.IOException;
import java.util.List;

import dev.quercusdata.indexer.Parameters;

public class QuercusUtil {

	public static final String SPLIT_CHAR = "\\";

	public static Parameters readProperties() throws IOException {
		ApplicationGetPropertyValues properties = new ApplicationGetPropertyValues("fileindexer.properties");
		Parameters params = new Parameters();
		params.setUnidadOrigen(properties.getPropValue("unidadOrigen") + SPLIT_CHAR);
		params.setExcluyeDirectorios(properties.getPropValueArray("excluyeDirectorios", ";"));
		params.setFicheroSalida(properties.getPropValue("ficheroSalida"));
		params.setExtensionesFicheros(properties.getPropValueExtensionsArray("extensionesFicheros", ";"));
		return params;
	}

	public static String listToString(List<String> list) {
		String converted = "";
		if(list == null || list.isEmpty()) {
			return converted;
		}
		for(String item:list) {
			converted += item + "; ";
		}
		return converted.substring(0, converted.length() - 2);
	}
}


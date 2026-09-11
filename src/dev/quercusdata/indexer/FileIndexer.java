package dev.quercusdata.indexer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import dev.quercusdata.util.QuercusUtil;

/**
 * Busca entre todos los ficheros MP3 aquellos con una determinada duracion.
 *
 * @author Manu
 *
 */
public class FileIndexer {

	static Logger logger = Logger.getLogger(FileIndexer.class.getName());

	private StringBuilder salida = new StringBuilder();
	private List<QuercusFile> archivos = new ArrayList<>();

	private Parameters params;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileInputStream fi = new FileInputStream("src/quercuslogger.properties");
			System.out.println(new String( fi.readAllBytes()));
			LogManager.getLogManager().readConfiguration(fi);

		} catch (SecurityException | IOException e1) {
			e1.printStackTrace();
		} finally {
			new FileIndexer();
		}
	}

	FileIndexer() {
		try {
			params = QuercusUtil.readProperties();
			execute();
		} catch (IOException e1) {
			logger.severe(e1.getMessage());
		}
	}

	public FileIndexer(Parameters param) {
		param.setUnidadOrigen(param.getUnidadOrigen() + QuercusUtil.SPLIT_CHAR);
		this.params = param;

		try {
			execute();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	private void execute() throws IOException {
		recorre(params.getUnidadOrigen());
		archivos = ordena(archivos);
		volcarSalida();
		logger.info("***************************************************************************\n"
				+ archivos.toString() + "\n.FINITO!");
	}

	private List<QuercusFile> ordena(List<QuercusFile> archivos) {
		Collections.sort(archivos);
		// archivos.sort( comparator);
		salida.append(archivos.toString());
		return archivos;
	}

	private void volcarSalida() {
		File logFile = new File(params.getUnidadOrigen() + params.getFicheroSalida());

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(logFile));
			String res = salida.substring(1, salida.length() - 1).toString();
			res = res.replace(", ", "");
			writer.write(res);

			writer.close();

		} catch (IOException e) {
			logger.severe("volcarSalida() " + e.getMessage());
		}
	}

	private void recorre(String directorio) throws IOException {
		File f = new File(directorio);
		String[] todos = f.list();
		if (todos == null) {
			return;
		}

		for (String fichero : todos) {
			File subFile = new File(directorio + fichero);

			if (subFile.isDirectory()) {
				if (params.getExcluyeDirectorios().contains(directorio + fichero)) {
					continue;
				}
				recorre(directorio + fichero + QuercusUtil.SPLIT_CHAR);
			} else {
				int extensionIndex = fichero.lastIndexOf(".");
				if (extensionIndex < 0 || extensionIndex == fichero.length() - 1) {
					continue;
				}
				String extension = fichero.substring(extensionIndex + 1).trim().toLowerCase();
				if (params.getExtensionesFicheros().contains(extension)) {
					QuercusFile archivo = new QuercusFile();
					archivo.setDirectorio(directorio);
					archivo.setTitulo(fichero);
					archivo.setExtension(extension);
					archivo.setTamanoBytes(subFile.length());
					archivo.setFechaModificacionMillis(subFile.lastModified());

					archivos.add(archivo);
				}
			}

		}
	}

	/**
	 * @return the param
	 */
	public Parameters getParam() {
		return params;
	}

	/**
	 * @param param the parameters to set
	 */
	public void setParam(Parameters param) {
		this.params = param;
	}
}


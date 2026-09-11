package dev.quercusdata.indexer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class QuercusFile implements Comparable<QuercusFile>{
	private static final String[] SIZE_UNITS = { "B", "KB", "MB", "GB", "TB" };
	private static final DateTimeFormatter MODIFIED_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private String titulo;
	private String directorio;
	private String extension = "";
	private String autor = "";
	private long tamanoBytes;
	private long fechaModificacionMillis;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDirectorio() {
		return directorio;
	}

	public void setDirectorio(String directorio) {
		this.directorio = directorio;
	}

	 /**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the autor
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * @param autor the autor to set
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}

	public long getTamanoBytes() {
		return tamanoBytes;
	}

	public void setTamanoBytes(long tamanoBytes) {
		this.tamanoBytes = tamanoBytes;
	}

	public long getFechaModificacionMillis() {
		return fechaModificacionMillis;
	}

	public void setFechaModificacionMillis(long fechaModificacionMillis) {
		this.fechaModificacionMillis = fechaModificacionMillis;
	}

	@Override
	  public String toString()
	  {
		 if( getTitulo().startsWith( ",")) {
			 setTitulo( getTitulo().substring( 1));
		 }
		 return getTitulo().trim() + "|" + getDirectorio().trim() + "|" + getExtension().trim() + "|" + getAutor().trim()
		 		+ "|" + formatTamanoLegible(getTamanoBytes()) + "|" + formatFechaModificacion(getFechaModificacionMillis()) + "\n";
	  }

	private String formatTamanoLegible(long bytes) {
		double size = bytes;
		int unitIndex = 0;
		while (size >= 1024 && unitIndex < SIZE_UNITS.length - 1) {
			size = size / 1024.0;
			unitIndex++;
		}
		DecimalFormat formatter = new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.US));
		return formatter.format(size) + " " + SIZE_UNITS[unitIndex];
	}

	private String formatFechaModificacion(long millis) {
		return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(MODIFIED_AT_FORMATTER);
	}

	@Override
	public int compareTo(QuercusFile o) {
		return titulo.compareToIgnoreCase( o.getTitulo());
	}

}


package dev.quercusdata.indexer;

import java.util.List;

public class Parameters {

	//Aqu� la lista de directorios a exclu�r. Se rellena dentro del constructor.
		private List<String> excluyeDirectorios;
		private String unidadOrigen;
		private String ficheroSalida;
		private List<String> extensionesFicheros;
		/**
		 * @return the excluyeDirectorios
		 */
		public List<String> getExcluyeDirectorios() {
			return excluyeDirectorios;
		}
		/**
		 * @param excluyeDirectorios the excluyeDirectorios to set
		 */
		public void setExcluyeDirectorios(List<String> excluyeDirectorios) {
			this.excluyeDirectorios = excluyeDirectorios;
		}
		/**
		 * @return the unidadOrigen
		 */
		public String getUnidadOrigen() {
			return unidadOrigen;
		}
		/**
		 * @param unidadOrigen the unidadOrigen to set
		 */
		public void setUnidadOrigen(String unidadOrigen) {
			this.unidadOrigen = unidadOrigen;
		}
		/**
		 * @return the ficheroSalida
		 */
		public String getFicheroSalida() {
			return ficheroSalida;
		}
		/**
		 * @param ficheroSalida the ficheroSalida to set
		 */
		public void setFicheroSalida(String ficheroSalida) {
			this.ficheroSalida = ficheroSalida;
		}
		/**
		 * @return the extensionesFicheros
		 */
		public List<String> getExtensionesFicheros() {
			return extensionesFicheros;
		}
		/**
		 * @param extensionesFicheros the extensionesFicheros to set
		 */
		public void setExtensionesFicheros(List<String> extensionesFicheros) {
			this.extensionesFicheros = extensionesFicheros;
		}


}


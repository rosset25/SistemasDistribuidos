package tablero;

public class Barco {	
	/**
	 * Clase para guardar la informacion de un barco en una partida de 'Hundir la flota'
	 */

	private int filaInicial,	// coordenadas iniciales del barco
	            columnaInicial; 
	private char orientacion;	// 'H': horizontal; 'V':vertical
	private int	tamanyo,		// numero de casillas que ocupa
	            tocadas;		// numero de casillas tocadas

	/**
	 * Constructor por defecto. No hace nada
	 */
	public Barco() { 
		super();
	}

	/**
	 * Constructor con argumentos
	 * @param f				fila del barco
	 * @param c				columna del barco
	 * @param orientacion	orientacion (vertical u horizontal)
	 * @param tamanyo		tamanyo del barco
	 */
	public Barco(int f, int c, char orientacion, int tamanyo) {
		super();
		this.filaInicial = f;
		this.columnaInicial = c;
		this.orientacion = orientacion;
		this.tamanyo = tamanyo;
		this.tocadas = 0;
	}


	/**
	 * Toca una nueva casilla de un barco e indica si se ha hundido
	 * @return	valor logico indicando si se ha hundido
	 */
	public boolean tocaBarco() {
		this.tocadas++;
		return (this.tocadas == this.tamanyo);
	}

	@Override
	public String toString() {
		return filaInicial + "#" + columnaInicial + "#" + orientacion + "#" + tamanyo;
	}

	/******************************************************************************************/
	/*****************************     GETTERS y SETTERS    ***********************************/
	/******************************************************************************************/


	/**
	 * @return the filaInicial
	 */
	public int getFilaInicial() {
		return filaInicial;
	}
	/**
	 * @param f the filaInicial to set
	 */

	/**
	 * @return the columnaInicial
	 */
	public int getColumnaInicial() {
		return columnaInicial;
	}

	/**
	 * @return the orientacion
	 */
	public char getOrientacion() {
		return orientacion;
	}

	/**
	 * @return the tamanyo
	 */
	public int getTamanyo() {
		return tamanyo;
	}

	/**
	 * @return the tocadas
	 */
	public int getTocadas() {
		return tocadas;
	}



} // end class Barco

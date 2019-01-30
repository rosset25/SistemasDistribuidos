package tablero;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Juego {

	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz gráfica (GUI)
	 */

	/** Parametros por defecto de una partida */
	public static final int NUMFILAS=8, NUMCOLUMNAS=8, NUMBARCOS=6;

	private GuiTablero guiTablero = null;			// El juego se encarga de crear y modificar la interfaz gráfica
	private Partida partida = null;                 // Objeto con los datos de la partida en juego
	
	/** Atributos de la partida guardados en el juego para simplificar su implementación */
	private int quedan = NUMBARCOS, disparos = 0;

	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 * @param args
	 */
	public static void main(String[] args) {
		Juego juego = new Juego();
		juego.ejecuta();
	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz grafica: tablero
	 */
	private void ejecuta() {
		// Instancia la primera partida
		partida = new Partida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta

	/******************************************************************************************/
	/*********************  CLASE INTERNA GuiTablero   ****************************************/
	/******************************************************************************************/
	private class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null;        // Tablero de juego
		private JLabel estado = null;       // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas de la partida

		/**
         * Constructor de una tablero dadas sus dimensiones
         */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas = numFilas;
			this.numColumnas = numColumnas;
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		public void dibujaTablero() {
			anyadeMenu();
			anyadeGrid(numFilas, numColumnas);		
			anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);		
			frame.setSize(300, 300);
			frame.setVisible(true);	
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {
            
			JMenuBar menuBar = new JMenuBar(); //barra donde irá el mené
			JMenu menu = new JMenu("Opciones");	
			
			//creamos un escuchador para el menú
			MenuListener ml = new MenuListener();
			
			//las diferentes opciones del menú
			JMenuItem mostrarSolucion = new JMenuItem("Mostrar solución");
			mostrarSolucion.addActionListener(ml); //escuchador
			
			JMenuItem nuevaPartida = new JMenuItem("Nueva partida");
			nuevaPartida.addActionListener(ml); //escuchador
			
			JMenuItem salir = new JMenuItem("Salir");
			salir.addActionListener(ml); //escuchador
			
			//se añaden las opciones al menú
			menu.add(mostrarSolucion);
			menu.add(nuevaPartida);
			menu.add(salir);
			
			//se añade el menú a la barra
			menuBar.add(menu);
			
			//la barra se añade a la ventana
			frame.add(menuBar, BorderLayout.NORTH);
			
		} // end anyadeMenu

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas.
		 * Cada casilla sera un boton con su correspondiente escuchador
		 * @param nf	numero de filas
		 * @param nc	numero de columnas
		 */
		private void anyadeGrid(int nf, int nc) {
            
			GridLayout tablero = new GridLayout(nf+1,nc+2);
			/* Se le suma una fila de más para colocar los números, y dos
			 * columnas para colocar las letras en ambos lados
			 */
			
			buttons = new JButton[nf][nc]; //se inicializa la matriz con las filas y columnas deseadas
			JPanel panel = new JPanel(); //panel que contiene los botones
			
			panel.setLayout(tablero); //se añade el grid al panel
			
			//los números que marcan las columnas
			JLabel columnas = new JLabel(" "); 
			panel.add(columnas);	
			
			for(int i=1; i <= numColumnas; i++) {
				columnas = new JLabel(""+i, SwingConstants.CENTER); 
				panel.add(columnas);
			}
			//SwingConstants.CENTER -> centra la String que haya en la etiqueta
			
			columnas = new JLabel("  ");
			panel.add(columnas);
			
			
			//se crea el escuchador de los botones
			ButtonListener bl = new ButtonListener();
			
			//char letra = 'A';
			int valorLetra = (int) 'A'; //el valor se usará para sacar el resto de letras
			
			//Se crean los botones del tablero
			for(int i=0; i < nf; i++) {
				JLabel filas = new JLabel(Character.toString((char)valorLetra), SwingConstants.CENTER);
				panel.add(filas);  //se pasa el valor de la letra a char
				
				for(int j=0; j < nc; j++) {
					//JButton parteTablero = new JButton();
					buttons[i][j] = new JButton(); //se guardan los botenes en la matriz
					buttons[i][j].addActionListener(bl); //escuchador
					//se guarda, como propiedades del botón, su posición
					buttons[i][j].putClientProperty("fil", i);
					buttons[i][j].putClientProperty("col", j);
					
					panel.add(buttons[i][j]);
				}	
				filas = new JLabel(Character.toString((char)valorLetra), SwingConstants.CENTER);
				
				valorLetra++; //se usará para ir recorriendo las letras en orden alfabético
				panel.add(filas); //se añade otra letra después de cada fila de botones
			}
			 
			frame.add(panel); //se añade el panel a la ventana
			
			
		} // end anyadeGrid


		/**
		 * Anyade el panel de estado al tablero
		 * @param cadena	cadena inicial del panel de estado
		 */
		private void anyadePanelEstado(String cadena) {	
			
			JPanel panelEstado = new JPanel();
			estado = new JLabel(cadena);
			panelEstado.add(estado);
			
			// El panel de estado queda en la posición SOUTH del frame
			frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
		} // end anyadePanel Estado

		/**
		 * Cambia la cadena mostrada en el panel de estado
		 * @param cadenaEstado	nuevo estado
		 */
		public void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado

		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 */
		public void muestraSolucion() {
            
			//pinta todas las celdas azules
			for (int i=0; i < buttons.length; i++) {
				for(int j=0; j < buttons[0].length; j++) {
					//buttons[i][j].setBackground(Color.CYAN);
					pintaBoton(buttons[i][j],Color.CYAN);
				}
				quedan = 0; //para que el escuchador no haga nada cuando se presionen los botones
			}
			
			String[] soluciones = partida.getSolucion(); //se obtiene la info de los barcos
			
			//se pintan los barcos
			for(int i=0; i < soluciones.length; i++) {
				pintaBarcoHundido(soluciones[i]);
			}
			
			
		} // end muestraSolucion


		/**
		 * Pinta un barco como hundido en el tablero
		 * @param cadenaBarco	cadena con los datos del barco codifificados como
		 *                      "filaInicial#columnaInicial#orientacion#tamanyo"
		 */
		public void pintaBarcoHundido(String cadenaBarco) {
			
            String[] infoBarco = cadenaBarco.split("#"); 
            
            //filIni, colIni, orientacion, tamanyo
            int fil = Integer.parseInt(infoBarco[0]);
            int col = Integer.parseInt(infoBarco[1]);
            String horientacion = infoBarco[2];
            int tamanyo = Integer.parseInt(infoBarco[3]);
            
            //se recorre el barco
            if (horientacion.equals("V")) { //si es vertical, pasamos a la siguiente fila
            	
	            for(int i=0; i < tamanyo; i++) {
	            	pintaBoton(buttons[fil][col],Color.RED);
	            	fil++;
	            }
            }else {
            	for(int i=0; i < tamanyo; i++) {
	            	pintaBoton(buttons[fil][col],Color.RED);
	            	col++; //si es horizontal pintamos la siguiente columna
            	}
	        }
            		
            
            
		} // end pintaBarcoHundido

		/**
		 * Pinta un botón de un color dado
		 * @param b			boton a pintar
		 * @param color		color a usar
		 */
		public void pintaBoton(JButton b, Color color) {
			b.setBackground(color);
			// El siguiente código solo es necesario en Mac OS X
			b.setOpaque(true);
			b.setBorderPainted(false);
		} // end pintaBoton

		/**
		 * Limpia las casillas del tablero pintándolas del gris por defecto
		 */
		public void limpiaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					buttons[i][j].setBackground(null);
					buttons[i][j].setOpaque(true);
					buttons[i][j].setBorderPainted(true);
				}
			}
		} // end limpiaTablero

		/**
		 * 	Destruye y libera la memoria de todos los componentes del frame
		 */
		public void liberaRecursos() {
			frame.dispose();
		} // end liberaRecursos

		private JButton[][] getBotones(){
			return buttons;
		}
		
		

	} // end class GuiTablero

	/******************************************************************************************/
	/*********************  CLASE INTERNA MenuListener ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 * 
	 */
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
           
			// se podría haber hecho un case... pero meh
			if( e.getActionCommand().equals("Nueva partida")) {
				//Se limpia el tablero y se inicializa una nueva partida
				guiTablero.limpiaTablero();
				
				partida = new Partida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
				quedan = NUMBARCOS; //se reinician la cantidad de barcos
				disparos = 0; //se reinician los disparos
				
				//se cambia la info del panel de estado
				guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
				
			}else {
				if(e.getActionCommand().equals("Mostrar solución")) {
					guiTablero.muestraSolucion();
				
				}else { //Salir
					guiTablero.liberaRecursos();
					System.exit(0);
				}
			}
		
		} // end actionPerformed

	} // end class MenuListener


	

	/******************************************************************************************/
	/*********************  CLASE INTERNA ButtonListener **************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero
	 * Para poder identificar el boton que ha generado el evento se pueden usar las propiedades
	 * de los componentes, apoyandose en los metodos putClientProperty y getClientProperty
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
           
			if (quedan > 0) { //si quedan barcos por hundir
				disparos++;
				
				//Se saca la posición del botón pulsado (un poco bruto todo)
				JButton boton = (JButton) e.getSource();
				int fila = (int)boton.getClientProperty("fil");
				int columna = (int)boton.getClientProperty("col");
				
				int solucion = partida.pruebaCasilla(fila,columna);
				
				if (solucion < 0) { /* o bien es mar o bien el botón ya había sido pulsado antes
									 * o bien el barco está tocado
									 */
					switch (solucion) {
					case -1:  //agua
						guiTablero.pintaBoton(boton, Color.CYAN); break; //pintar mar
					case -2: //tocado
						guiTablero.pintaBoton(boton, Color.ORANGE); break; //pintar barco tocado
					}		//no hace falta pintar un barco ya hundido
					
				}else { //se ha hundido un barco
					guiTablero.pintaBarcoHundido(partida.getBarco(solucion));
					quedan--; //cuando el contador llegue a 0 se acaba la partida
				}
				
				if (quedan <= 0) { /* es la última vez que se podrá pulsar un botón porque 
									* se ha hundido el último barco, y se cambia el panel
									* por: GAME OVER
									*/
					guiTablero.cambiaEstado("GAME OVER en " + disparos + " disparos");
				}else {
					guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
				}
				
			} //en caso de no quedar barcos, no se hace nada
		
			
        } // end actionPerformed

	} // end class ButtonListener



} // end class Juego

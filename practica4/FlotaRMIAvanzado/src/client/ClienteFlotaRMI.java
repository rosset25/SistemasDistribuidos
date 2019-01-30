package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.*;

import java.util.*;

import common.IntCallbackCliente;
import common.IntServidorJuegoRMI;
import common.IntServidorPartidasRMI;


public class ClienteFlotaRMI {

	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz gráfica (GUI)
	 */

	/** Parametros por defecto de una partida */
	public static final int NUMFILAS = 8, NUMCOLUMNAS = 8, NUMBARCOS = 6;

	private GuiTablero guiTablero = null; // El juego se encarga de crear y modificar la interfaz gráfica

	private IntServidorPartidasRMI partida = null; // Objeto con los datos de la partida en juego
	int portNum = 1099;
	private IntServidorJuegoRMI juegoObj = null; // Referencia al objeto del servidor
	
	private String apodo = null;  /* Apodo del jugador, se usará para darlo de baja y de alta en el servidor
						 		   * NOTA: el nombre del jugador se deberá dar al inicio del juego
						 		   */

	/**
	 * Atributos de la partida guardados en el juego para simplificar su
	 * implementación
	 */
	private int quedan = NUMBARCOS, disparos = 0;

	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ClienteFlotaRMI juego = new ClienteFlotaRMI();
		juego.ejecuta();

	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz
	 * grafica: tablero
	 */
	private void ejecuta() { 
		try {
			
            System.setProperty("java.security.policy", "src/client/java.policy");
            System.setSecurityManager(new SecurityManager());
            
			// misma dirección que el servidor
			String registryURL = "rmi://localhost:" + portNum + "/flota";
			/*
			 * El cliente obtiene del registro un stub (juegoObj) del objeto remoto (la
			 * partida) Este stub se guarda en una referencia a la interfaz
			 * IntServidorJuegoRMI (registryURL)
			 */
			
			//se pide por pantalla el nombre del jugador
			Scanner teclado = new Scanner(System.in);
			System.out.print("Apodo: ");
			apodo = teclado.nextLine();
			System.out.println();
			
			
			//se instancia el la comunicación con el servidor
			juegoObj = (IntServidorJuegoRMI) Naming.lookup(registryURL);

			partida = juegoObj.nuevoServidorPartidas();
			partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Instancia la primera partida
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta

	/******************************************************************************************/
	/*********************
	 * CLASE INTERNA GuiTablero
	 ****************************************/
	/******************************************************************************************/
	private class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null; // Tablero de juego
		private JLabel estado = null; // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas de la partida
		private JMenuBar menuBar = new JMenuBar(); // Barra donde irán los menús

		/**
		 * Constructor de una tablero dadas sus dimensiones
		 */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas = numFilas;
			this.numColumnas = numColumnas;
			frame = new JFrame(apodo);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		public void dibujaTablero() {
			anyadeMenu();
			anyadeMenu2();
			anyadeGrid(numFilas, numColumnas);
			anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
			frame.setSize(300, 300);
			frame.setVisible(true);
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {

			//JMenuBar menuBar = new JMenuBar(); // barra donde irá el mené
			JMenu menu = new JMenu("Opciones");

			// creamos un escuchador para el menú
			MenuListener ml = new MenuListener();

			// las diferentes opciones del menú
			JMenuItem mostrarSolucion = new JMenuItem("Mostrar solución");
			mostrarSolucion.addActionListener(ml); // escuchador

			JMenuItem nuevaPartida = new JMenuItem("Nueva partida");
			nuevaPartida.addActionListener(ml); // escuchador

			JMenuItem salir = new JMenuItem("Salir");
			salir.addActionListener(ml); // escuchador

			// se añaden las opciones al menú
			menu.add(mostrarSolucion);
			menu.add(nuevaPartida);
			menu.add(salir);

			// se añade el menú a la barra
			menuBar.add(menu);

			// la barra se añade a la ventana
			frame.add(menuBar, BorderLayout.NORTH);
			//frame.add(menuBar);

		} // end anyadeMenu
		
		

		/**
		 * Anyade el menu de multijugador y le asocia un escuchador
		 */
		private void anyadeMenu2() {

			//JMenuBar menuBar = new JMenuBar(); // Barra donde irá el menú multijugador
			JMenu menu2 = new JMenu("Multijugador");

			// creamos un escuchador para el menú de multijugador
			MenuListener2 m2 = new MenuListener2();

			// las diferentes opciones del menú
			JMenuItem proponPartida = new JMenuItem("Propón partida");
			proponPartida.addActionListener(m2); // escuchador

			JMenuItem borraPartida = new JMenuItem("Borrar partida");
			borraPartida.addActionListener(m2); // escuchador
			
			JMenuItem listaPartidas = new JMenuItem("Lista de partidas");
			listaPartidas.addActionListener(m2); // escuchador
			
			JMenuItem aceptaPartida = new JMenuItem("Aceptar partida");
			aceptaPartida.addActionListener(m2); // escuchador

			// se añaden las opciones al menú
			menu2.add(proponPartida);
			menu2.add(borraPartida);
			menu2.add(listaPartidas);
			menu2.add(aceptaPartida);

			// se añade el menú a la barra
			menuBar.add(menu2);
			/* la barra no hace falta volverla a añadir al frame, 
			 * ya se ha añadido en el método anterior de anyadeMenu()
			 */

		} // end anyadeMenu2

		
		

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas. Cada casilla sera
		 * un boton con su correspondiente escuchador
		 * 
		 * @param nf numero de filas
		 * @param nc numero de columnas
		 */
		private void anyadeGrid(int nf, int nc) {

			GridLayout tablero = new GridLayout(nf + 1, nc + 2);
			/*
			 * Se le suma una fila de más para colocar los números, y dos columnas para
			 * colocar las letras en ambos lados
			 */

			buttons = new JButton[nf][nc]; // se inicializa la matriz con las filas y columnas deseadas
			JPanel panel = new JPanel(); // panel que contiene los botones

			panel.setLayout(tablero); // se añade el grid al panel

			// los números que marcan las columnas
			JLabel columnas = new JLabel(" ");
			panel.add(columnas);

			for (int i = 1; i <= numColumnas; i++) {
				columnas = new JLabel("" + i, SwingConstants.CENTER);
				panel.add(columnas);
			}
			// SwingConstants.CENTER -> centra la String que haya en la etiqueta

			columnas = new JLabel("  ");
			panel.add(columnas);

			// se crea el escuchador de los botones
			ButtonListener bl = new ButtonListener();

			// char letra = 'A';
			int valorLetra = (int) 'A'; // el valor se usará para sacar el resto de letras

			// Se crean los botones del tablero
			for (int i = 0; i < nf; i++) {
				JLabel filas = new JLabel(Character.toString((char) valorLetra), SwingConstants.CENTER);
				panel.add(filas); // se pasa el valor de la letra a char

				for (int j = 0; j < nc; j++) {
					// JButton parteTablero = new JButton();
					buttons[i][j] = new JButton(); // se guardan los botenes en la matriz
					buttons[i][j].addActionListener(bl); // escuchador
					// se guarda, como propiedades del botón, su posición
					buttons[i][j].putClientProperty("fil", i);
					buttons[i][j].putClientProperty("col", j);

					panel.add(buttons[i][j]);
				}
				filas = new JLabel(Character.toString((char) valorLetra), SwingConstants.CENTER);

				valorLetra++; // se usará para ir recorriendo las letras en orden alfabético
				panel.add(filas); // se añade otra letra después de cada fila de botones
			}

			frame.add(panel); // se añade el panel a la ventana

		} // end anyadeGrid

		/**
		 * Anyade el panel de estado al tablero
		 * 
		 * @param cadena cadena inicial del panel de estado
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
		 * 
		 * @param cadenaEstado nuevo estado
		 */
		public void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado

		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 */
		public void muestraSolucion() {

			// pinta todas las celdas azules
			for (int i = 0; i < buttons.length; i++) {
				for (int j = 0; j < buttons[0].length; j++) {
					// buttons[i][j].setBackground(Color.CYAN);
					pintaBoton(buttons[i][j], Color.CYAN);
				}
				quedan = 0; // para que el escuchador no haga nada cuando se presionen los botones
			}

			String[] soluciones;
			try {
				soluciones = partida.getSolucion();
				// se pintan los barcos
				for (int i = 0; i < soluciones.length; i++) {
					pintaBarcoHundido(soluciones[i]);
				}
				// se obtiene la info de los barcos
			} catch (RemoteException e) {
				// 
				e.printStackTrace();
			}

		} // end muestraSolucion

		/**
		 * Pinta un barco como hundido en el tablero
		 * 
		 * @param cadenaBarco cadena con los datos del barco codifificados como
		 *                    "filaInicial#columnaInicial#orientacion#tamanyo"
		 */
		public void pintaBarcoHundido(String cadenaBarco) {

			String[] infoBarco = cadenaBarco.split("#");

			// filIni, colIni, orientacion, tamanyo
			int fil = Integer.parseInt(infoBarco[0]);
			int col = Integer.parseInt(infoBarco[1]);
			String horientacion = infoBarco[2];
			int tamanyo = Integer.parseInt(infoBarco[3]);

			// se recorre el barco
			if (horientacion.equals("V")) { // si es vertical, pasamos a la siguiente fila

				for (int i = 0; i < tamanyo; i++) {
					pintaBoton(buttons[fil][col], Color.RED);
					fil++;
				}
			} else {
				for (int i = 0; i < tamanyo; i++) {
					pintaBoton(buttons[fil][col], Color.RED);
					col++; // si es horizontal pintamos la siguiente columna
				}
			}

		} // end pintaBarcoHundido

		/**
		 * Pinta un botón de un color dado
		 * 
		 * @param b     boton a pintar
		 * @param color color a usar
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
		 * Destruye y libera la memoria de todos los componentes del frame
		 */
		public void liberaRecursos() {
			frame.dispose();
		} // end liberaRecursos

		private JButton[][] getBotones() {
			return buttons;
		}

	} // end class GuiTablero

	/******************************************************************************************/
	/*********************
	 * CLASE INTERNA MenuListener
	 ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 * 
	 */
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// se podría haber hecho un case... pero meh
			if (e.getActionCommand().equals("Nueva partida")) {
				// Se limpia el tablero y se inicializa una nueva partida
				guiTablero.limpiaTablero();

				try {
					partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				quedan = NUMBARCOS; // se reinician la cantidad de barcos
				disparos = 0; // se reinician los disparos

				// se cambia la info del panel de estado
				guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);

			} else {
				if (e.getActionCommand().equals("Mostrar solución")) {
					guiTablero.muestraSolucion();

				} else { // Salir
					guiTablero.liberaRecursos();
					System.exit(0);
				}
			}

		} // end actionPerformed

	} // end class MenuListener

	/******************************************************************************************/
	/*********************
	 * CLASE INTERNA MenuListener2
	 ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu Multijugador
	 */
	private class MenuListener2 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand().equals("Propón partida")) {
				/*
				 * con la interfaz IntServidorJuego, se llama al método:
				 * juegoObj.proponPartida(nombreJugador, callbackClObject)
				 * este método dará de alta al cliente para que otro jugador
				 * pueda aceptar la partida propuesta
				 * NOTA: el nombre del cliente ya se ha pedido por consola al inicio del juego
				 */
				
				try { 
					
					//se crea el callback del cliente
					IntCallbackCliente jugador = new ImplCallbackCliente();
				
					if(juegoObj.proponPartida(apodo, jugador)) {
						System.out.println("Partida propuesta");
					}else {
						System.out.println("No se ha podido proponer la partida");
					}
					
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
			} else {
				if (e.getActionCommand().equals("Borrar partida")) {
					/*
					 * llamar al método juegoObj,borraPartida(nombreJugador)
					 * da de baja al cliente, es decir, ya no estará en la
					 * lista de clientes que esperan a una partida
					 * Se ejecuta cuando el cliente cancela la partida que había propuesto
					 * También se podría ajecutar el método cuando el cliente encuentre a un rival
					 */
					try {
						if (juegoObj.borraPartida(apodo)) {
							System.out.println("Partida borrada");
						}else {
							System.out.println("No existe la partida");
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}

				} else {
					if  (e.getActionCommand().equals("Lista de partidas")) {
						/*
						 * se printeará por pantalla la lista de los jugadores apuntados
						 * método: juegoObj.listaPartidas()
						 */
						String[] jugadores = new String[0];
						
						try {
							jugadores = juegoObj.listaPartidas();	
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						
						System.out.println("Jugadores que han propuesto una partida:");
						for(int i=0; i < jugadores.length; i++) {
							System.out.println(jugadores[i]);
						}
						
					} else {		//Aceptar partida
						/*
						 * se tiene que dar de baja al jugador que esté en la lista esperando
						 * y comunicarlo con el jugador que lo ha llamado
						 * método: juegoObj.aceptaPartida(nombreTuyo, nombreRival)
						 */
						System.out.print("Nombre del jugador rival: ");
						Scanner teclado = new Scanner(System.in);
						String rival = teclado.nextLine();
						System.out.println();
						
						try {
							if(juegoObj.aceptaPartida(apodo, rival)) {
								System.out.println("La partida ha sido aceptada");
							}else {
								System.out.println("No se ha podido aceptar la partida");
							}
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						
						
					}
				}
			}

		} // end actionPerformed

	} // end class MenuListener

	/******************************************************************************************/
	/*********************
	 * CLASE INTERNA ButtonListener
	 **************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero Para poder
	 * identificar el boton que ha generado el evento se pueden usar las propiedades
	 * de los componentes, apoyandose en los metodos putClientProperty y
	 * getClientProperty
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (quedan > 0) { // si quedan barcos por hundir
				disparos++;

				// Se saca la posición del botón pulsado (un poco bruto todo)
				JButton boton = (JButton) e.getSource();
				int fila = (int) boton.getClientProperty("fil");
				int columna = (int) boton.getClientProperty("col");

				int solucion;
				try {
					solucion = partida.pruebaCasilla(fila, columna);

					if (solucion < 0) { /*
										 * o bien es mar o bien el botón ya había sido pulsado antes o bien el barco
										 * está tocado
										 */
						switch (solucion) {
						case -1: // agua
							guiTablero.pintaBoton(boton, Color.CYAN);
							break; // pintar mar
						case -2: // tocado
							guiTablero.pintaBoton(boton, Color.ORANGE);
							break; // pintar barco tocado
						} // no hace falta pintar un barco ya hundido

					} else { // se ha hundido un barco
						guiTablero.pintaBarcoHundido(partida.getBarco(solucion));
						quedan--; // cuando el contador llegue a 0 se acaba la partida
					}

					if (quedan <= 0) { /*
										 * es la última vez que se podrá pulsar un botón porque se ha hundido el último
										 * barco, y se cambia el panel por: GAME OVER
										 */
						guiTablero.cambiaEstado("GAME OVER en " + disparos + " disparos");
					} else {
						guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
					}

				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

			} // en caso de no quedar barcos, no se hace nada

		} // end actionPerformed

	} // end class ButtonListener

} // end class Juego

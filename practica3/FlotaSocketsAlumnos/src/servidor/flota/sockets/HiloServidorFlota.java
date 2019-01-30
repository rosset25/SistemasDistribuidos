package servidor.flota.sockets;

import java.io.IOException;
import partida.flota.sockets.*;
import comun.flota.sockets.MyStreamSocket;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del juego
 * Hundir la flota. El metodo run contiene la logica para gestionar una sesion
 * con un cliente.
 */

// Revisar el apartado 5.5. del libro de Liu

class HiloServidorFlota implements Runnable {
	MyStreamSocket myDataSocket;
	private Partida partida = null;

	/**
	 * Construye el objeto a ejecutar por la hebra para servir a un cliente
	 * 
	 * @param myDataSocket socket stream para comunicarse con el cliente
	 */
	HiloServidorFlota(MyStreamSocket myDataSocket) {
		//socket del servidor
		this.myDataSocket = myDataSocket;
	}

	/*  Gestiona una sesion con un cliente	*/
	public void run() {

		boolean done = false; //sólo será true cuando se quiera cortar la conexión
		int operacion = 0;
		try {
			while (!done) {
				String[] argumentos = myDataSocket.receiveMessage().split("#");
				operacion = Integer.parseInt(argumentos[0]);
				// Recibe una peticion del cliente
				// Extrae la operación y los argumentos

				switch (operacion) {
				case 0: 	// Fin de conexión con el cliente
					System.out.println("Fin de la sesión.");
					try {
						myDataSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					done = true;
					break;

				case 1: { 	// Crea nueva partida
	
					int nFilas = Integer.parseInt(argumentos[1]);
					int nColumnas = Integer.parseInt(argumentos[2]);
					int nBarcos = Integer.parseInt(argumentos[3]);
					partida = new Partida(nFilas, nColumnas, nBarcos);
					
					System.out.println("Nueva Partida creada");
					break;
				}

				case 2: { 	// Prueba una casilla y devuelve el resultado al cliente
					
					int fila = Integer.parseInt(argumentos[1]);
					int columna = Integer.parseInt(argumentos[2]);
					String res = Integer.toString(partida.pruebaCasilla(fila, columna));
					
					myDataSocket.sendMessage(res);
					break;
				}

				case 3: { 	// Obtiene los datos de un barco y se los devuelve al cliente
					
					int idBarco = Integer.parseInt(argumentos[1]);
					String res = partida.getBarco(idBarco);
					//le envía al cliente los datos del barco (una cadena)
					myDataSocket.sendMessage(res);
					break;
				}

				case 4: { 	// Devuelve al cliente la solucion en forma de vector de cadenas
					// Primero envia el numero de barcos
					// Despues envia una cadena por cada barco

					String[] vectorSoluciones = partida.getSolucion();
					int numBarcos = vectorSoluciones.length;
					//envía al cliente la cantidad de barcos 
					myDataSocket.sendMessage(numBarcos + "");
					/*uno por uno irá enviando la info. de los barcos contenidos 
					 * en el vector devuelto como resultado de getSolucion()
					 */
					for (int i = 0; i < numBarcos; i++) {
						myDataSocket.sendMessage(vectorSoluciones[i]);
					}
					break;
				}
				} // fin switch
			} // fin while
		} // fin try
		catch (Exception ex) {
			System.out.println("Exception caught in thread: " + ex);
		} // fin catch
	} // fin run

} // fin class

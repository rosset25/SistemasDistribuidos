package servidor.flota.sockets;

import java.net.ServerSocket;

import comun.flota.sockets.MyStreamSocket;

/**
 * Este modulo contiene la logica de aplicacion del servidor del juego Hundir la
 * flota Utiliza sockets en modo stream para llevar a cabo la comunicacion entre
 * procesos. Puede servir a varios clientes de modo concurrente lanzando una
 * hebra para atender a cada uno de ellos. Se le puede indicar el puerto del
 * servidor en linea de ordenes.
 */

public class ServidorFlotaSockets {

	public static void main(String[] args) {

		// Acepta conexiones vía socket de distintos clientes.
		// Por cada conexión establecida lanza una hebra de la clase HiloServidorFlota.
		// Revisad el apartado 5.5 del libro de Liu
		
		int puertoServidor = 1099; // Puerto por defecto

		try {
			//No hace falta, aunque se le podría haber pasado como parámetro el puerto,
			/* if (args.length == 1)
				puertoServidor = Integer.parseInt(args[0]);
			*/

			ServerSocket miSocketConexion = new ServerSocket(puertoServidor);
			System.out.println("Servidor Echo listo");
			while (true) {
				System.out.println("Espera una conexión.");
				//se crea el socket y el hilo sel servidor
				MyStreamSocket miSocketDatos = new MyStreamSocket(miSocketConexion.accept());
				Thread elHilo = new Thread(new HiloServidorFlota(miSocketDatos));
				elHilo.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	} // fin main
} // fin class

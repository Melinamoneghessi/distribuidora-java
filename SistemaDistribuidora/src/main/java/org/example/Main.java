package org.example;

import com.distribuidora.data.HibernateUtil;
import com.distribuidora.service.DistribuidoraService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Crea un objeto para leer lo que escribe el usuario
        Scanner scanner = new Scanner(System.in);
        String nombre =scanner.nextLine();

        DistribuidoraService service= DistribuidoraService.getInstancia();

        int opcion;
        do {
            System.out.println("=== Distribuidora de Alimentos ===");
            System.out.println("1. Registrar proveedor");
            System.out.println("2. Registrar producto");
            System.out.println("3. Buscar productos");
            System.out.println("4. Ver resumen por proveedor");
            System.out.println("0. Salir");

            System.out.println("Seleccione una opcion:");
            //lee lo que el usuario escribe y convierte el texto a número
            opcion=Integer.parseInt(scanner.nextLine());

            //según lo que eligio:
            switch (opcion){
                case 1:
                    System.out.println("Razon social: ");
                    String razonSocial = scanner.nextLine();

                    System.out.print("CUIT: ");
                    String cuit = scanner.nextLine();

                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    service.registrarProveedor(razonSocial, cuit, telefono, email);
                    break;
                case 2:
                    System.out.print("Nombre del producto: ");
                    String nombreProd = scanner.nextLine();

                    System.out.print("Categoría: ");
                    String categoria = scanner.nextLine();

                    System.out.print("Precio unitario: ");
                    double precioUnitario = Double.parseDouble(scanner.nextLine());

                    System.out.print("Stock: ");
                    int stock = Integer.parseInt(scanner.nextLine());

                    System.out.print("ID del proveedor: ");
                    Long idProveedor = Long.parseLong(scanner.nextLine());

                    service.registrarProducto(nombreProd, categoria, precioUnitario, stock, idProveedor);
                    break;
                case 3:
                    System.out.print("Categoría (enter para omitir): ");
                    String categoriaFiltro = scanner.nextLine();
                    if (categoriaFiltro.isBlank()) categoriaFiltro = null;

                    System.out.print("Precio máximo (enter para omitir): ");
                    String precioTexto = scanner.nextLine();
                    Double precioMax = null;
                    if (!precioTexto.isBlank()) precioMax = Double.parseDouble(precioTexto);

                    System.out.print("Stock mínimo (enter para omitir): ");
                    String stockTexto = scanner.nextLine();
                    Integer stockMin = null;
                    if (!stockTexto.isBlank()) stockMin = Integer.parseInt(stockTexto);

                    service.buscarProductos(categoriaFiltro, precioMax, stockMin);
                    break;
                case 4:
                    service.mostrarResumenPorProveedor();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
            System.out.println();
        }while (opcion!=0);
            scanner.close();
            HibernateUtil.shutdown();

    }
}
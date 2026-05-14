package com.distribuidora.service;

import com.distribuidora.data.DistribuidoraRepository;
import com.distribuidora.model.Producto;
import com.distribuidora.model.Proveedor;

import java.util.List;

public class DistribuidoraService {

    private static DistribuidoraService instancia;
    private DistribuidoraRepository repository;

    private DistribuidoraService() {
        this.repository = new DistribuidoraRepository();
    }

    public static DistribuidoraService getInstancia() {
        if (instancia == null) {
            instancia = new DistribuidoraService();
        }
        return instancia;
    }

    public void registrarProveedor(String razonSocial, String cuit, String telefono, String email) {
        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial(razonSocial);
        proveedor.setCuit(cuit);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);

        repository.guardarProveedor(proveedor);
        System.out.println("Proveedor registrado correctamente");
    }

    public void registrarProducto(String nombre, String categoria, Double precioUnitario, int stock, Long idProveedor) {
        Proveedor proveedor = repository.buscarProveedorPorId(idProveedor);
        if (proveedor == null) {
            System.out.println("Error: no existe un proveedor con Id" + idProveedor);
            return;
        }
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecioUnitario(precioUnitario);
        producto.setStock(stock);
        producto.setProveedor(proveedor);

        repository.guardarProducto(producto);
        System.out.println("Producto registrado correctamente");
    }

    public void buscarProductos(String categoria, Double precioMax, Integer stockMin) {
        List<Producto> productos = repository.buscarProductos(categoria, precioMax, stockMin);

        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos con esos filtros");
            return;
        }

        System.out.println("==PRODUCTOS ENCONTRADOS==");
        for (Producto producto : productos) {
            System.out.println("ID: " + producto.getId());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Categoría: " + producto.getCategoria());
            System.out.println("Precio unitario: $" + producto.getPrecioUnitario());
            System.out.println("Stock: " + producto.getStock());
            System.out.println("Proveedor: " + producto.getProveedor().getRazonSocial());
            System.out.println("-----------------------------");
        }
    }

    public void mostrarResumenPorProveedor() {
        List<Object[]> resumen = repository.resumenPorProveedor();

        if (resumen.isEmpty()) {
            System.out.println("No hay productos cargados para mostrar resumen.");
            return;
        }

        System.out.println("==RESUMEN POR PROVEEDOR==");
        for (Object[] fila : resumen) {
            Proveedor proveedor = (Proveedor) fila[0];
            Long cantidadProductos = (Long) fila[1];
            Long stockTotal = (Long) fila[2];
            Double valorTotal = (Double) fila[3];

            System.out.println("Proveedor: " + proveedor.getRazonSocial());
            System.out.println("Cantidad de productos: " + cantidadProductos);
            System.out.println("Stock total: " + stockTotal);
            System.out.println("Valor total inventario: $" + valorTotal);
            System.out.println("-----------------------------");
        }
    }
}
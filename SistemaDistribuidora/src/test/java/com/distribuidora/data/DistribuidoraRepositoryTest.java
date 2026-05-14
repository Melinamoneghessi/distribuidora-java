package com.distribuidora.data;

import com.distribuidora.model.Producto;
import com.distribuidora.model.Proveedor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DistribuidoraRepositoryTest {

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private DistribuidoraRepository repository;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure().buildSessionFactory();

        session = sessionFactory.openSession();

        transaction = session.beginTransaction();

        repository = new DistribuidoraRepository(session);
    }

    @After
    public void tearDown() {

        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }

        if (session != null) {
            session.close();
        }

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void guardarProveedor_buscarProveedorPorId_retornaProveedorCorrecto() {

        Proveedor proveedor = new Proveedor();

        proveedor.setRazonSocial("Distribuidora Norte");
        proveedor.setCuit("20-11111111-1");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("norte@test.com");

        repository.guardarProveedor(proveedor);

        assertNotNull(proveedor.getId());

        Proveedor encontrado = repository.buscarProveedorPorId(proveedor.getId());

        assertNotNull(encontrado);
        assertEquals("Distribuidora Norte", encontrado.getRazonSocial());
        assertEquals("20-11111111-1", encontrado.getCuit());
    }

    @Test
    public void buscarProveedorPorId_idInexistente_retornaNull() {

        Proveedor encontrado = repository.buscarProveedorPorId(9999L);

        assertNull(encontrado);
    }

    @Test
    public void guardarProducto_buscarProductosSinFiltros_retornaProductoCorrecto() {

        Proveedor proveedor = new Proveedor();

        proveedor.setRazonSocial("Distribuidora Sur");
        proveedor.setCuit("20-22222222-2");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("sur@test.com");

        repository.guardarProveedor(proveedor);

        Producto producto = new Producto();

        producto.setNombre("Arroz");
        producto.setCategoria("Alimentos");
        producto.setPrecioUnitario(1500.0);
        producto.setStock(20);
        producto.setProveedor(proveedor);

        repository.guardarProducto(producto);

        assertNotNull(producto.getId());

        List<Producto> productos = repository.buscarProductos(null, null, null);

        assertEquals(1, productos.size());
        assertEquals("Arroz", productos.get(0).getNombre());
        assertEquals("Alimentos", productos.get(0).getCategoria());
    }

    @Test
    public void buscarProductos_porCategoria_retornaSoloEsaCategoria() {

        Proveedor proveedor = new Proveedor();

        proveedor.setRazonSocial("Proveedor Test");
        proveedor.setCuit("20-33333333-3");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("test@test.com");

        repository.guardarProveedor(proveedor);

        Producto p1 = new Producto();

        p1.setNombre("Arroz");
        p1.setCategoria("Alimentos");
        p1.setPrecioUnitario(1500.0);
        p1.setStock(20);
        p1.setProveedor(proveedor);

        repository.guardarProducto(p1);

        Producto p2 = new Producto();

        p2.setNombre("Lavandina");
        p2.setCategoria("Limpieza");
        p2.setPrecioUnitario(900.0);
        p2.setStock(15);
        p2.setProveedor(proveedor);

        repository.guardarProducto(p2);

        List<Producto> resultado = repository.buscarProductos("Alimentos", null, null);

        assertEquals(1, resultado.size());
        assertEquals("Arroz", resultado.get(0).getNombre());
    }

    @Test
    public void buscarProductos_porPrecioMaximo_retornaProductosConPrecioMenorOIgual() {

        Proveedor proveedor = new Proveedor();

        proveedor.setRazonSocial("Proveedor Test");
        proveedor.setCuit("20-44444444-4");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("test@test.com");

        repository.guardarProveedor(proveedor);

        Producto p1 = new Producto();

        p1.setNombre("Barato");
        p1.setCategoria("General");
        p1.setPrecioUnitario(500.0);
        p1.setStock(10);
        p1.setProveedor(proveedor);

        repository.guardarProducto(p1);

        Producto p2 = new Producto();

        p2.setNombre("Caro");
        p2.setCategoria("General");
        p2.setPrecioUnitario(3000.0);
        p2.setStock(10);
        p2.setProveedor(proveedor);

        repository.guardarProducto(p2);

        List<Producto> resultado = repository.buscarProductos(null, 1000.0, null);

        assertEquals(1, resultado.size());
        assertEquals("Barato", resultado.get(0).getNombre());
    }

    @Test
    public void buscarProductos_porStockMinimo_retornaProductosConStockMayorOIgual() {

        Proveedor proveedor = new Proveedor();

        proveedor.setRazonSocial("Proveedor Test");
        proveedor.setCuit("20-55555555-5");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("test@test.com");

        repository.guardarProveedor(proveedor);

        Producto p1 = new Producto();

        p1.setNombre("Stock bajo");
        p1.setCategoria("General");
        p1.setPrecioUnitario(1000.0);
        p1.setStock(3);
        p1.setProveedor(proveedor);

        repository.guardarProducto(p1);

        Producto p2 = new Producto();

        p2.setNombre("Stock alto");
        p2.setCategoria("General");
        p2.setPrecioUnitario(1000.0);
        p2.setStock(20);
        p2.setProveedor(proveedor);

        repository.guardarProducto(p2);

        List<Producto> resultado = repository.buscarProductos(null, null, 10);

        assertEquals(1, resultado.size());
        assertEquals("Stock alto", resultado.get(0).getNombre());
    }

    @Test
    public void buscarProductos_conTodosLosFiltros_retornaProductoCorrecto() {

        Proveedor proveedor = new Proveedor();

        proveedor.setRazonSocial("Proveedor Test");
        proveedor.setCuit("20-66666666-6");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("test@test.com");

        repository.guardarProveedor(proveedor);

        Producto p1 = new Producto();

        p1.setNombre("Arroz");
        p1.setCategoria("Alimentos");
        p1.setPrecioUnitario(1000.0);
        p1.setStock(20);
        p1.setProveedor(proveedor);

        repository.guardarProducto(p1);

        Producto p2 = new Producto();

        p2.setNombre("Yerba");
        p2.setCategoria("Alimentos");
        p2.setPrecioUnitario(3000.0);
        p2.setStock(20);
        p2.setProveedor(proveedor);

        repository.guardarProducto(p2);

        List<Producto> resultado = repository.buscarProductos("Alimentos", 1500.0, 10);

        assertEquals(1, resultado.size());
        assertEquals("Arroz", resultado.get(0).getNombre());
    }

    @Test
    public void resumenPorProveedor_retornaCantidadStockTotalYValorTotal() {

        Proveedor proveedor = new Proveedor();

        proveedor.setRazonSocial("Proveedor Resumen");
        proveedor.setCuit("20-77777777-7");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("resumen@test.com");

        repository.guardarProveedor(proveedor);

        Producto p1 = new Producto();

        p1.setNombre("Producto 1");
        p1.setCategoria("General");
        p1.setPrecioUnitario(100.0);
        p1.setStock(10);
        p1.setProveedor(proveedor);

        repository.guardarProducto(p1);

        Producto p2 = new Producto();

        p2.setNombre("Producto 2");
        p2.setCategoria("General");
        p2.setPrecioUnitario(200.0);
        p2.setStock(5);
        p2.setProveedor(proveedor);

        repository.guardarProducto(p2);

        List<Object[]> resumen = repository.resumenPorProveedor();

        assertEquals(1, resumen.size());

        Object[] fila = resumen.get(0);

        assertEquals(proveedor.getId(), ((Proveedor) fila[0]).getId());
        assertEquals(2L, fila[1]);
        assertEquals(15L, fila[2]);
        assertEquals(2000.0, fila[3]);
    }
}
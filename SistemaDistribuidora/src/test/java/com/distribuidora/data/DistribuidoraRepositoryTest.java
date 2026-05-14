package com.distribuidora.data;

import com.distribuidora.model.Proveedor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistribuidoraRepositoryTest {
    private DistribuidoraRepository repository;

    @BeforeEach
    void Setup(){
        repository=DistribuidoraRepository.getInstancia();
        limpiarTablas();
    }

    @AfterEach
    void tearDown(){
        limpiarTablas();
    }

    // Elimina todas las filas respetando el orden de claves foráneas:
    private void limpiarTablas(){
        try(var session=HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();

            session.createNativeMutationQuery("DELETE FROM producto").executeUpdate();
            session.createNativeMutationQuery("DELETE FROM proveedor").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Test
    void guardarProveedor_buscarProveedorPorId_RetronaProveedorCoreecto(){
        Proveedor proveedor =new Proveedor();
        proveedor.setRazonSocial("Distribuidora Norte");
        proveedor.setCuit("20-11111111-1");
        proveedor.setTelefono("3492123456");
        proveedor.setEmail("norte@test.com");

        repository.guardarProveedor(proveedor);

        Proveedor encontrado=repository.buscarProveedorPorId(proveedor.getId());

        assertNotNull(encontrado);
        assertEquals("Distribuidora Norte", encontrado.getRazonSocial());
        assertEquals("20-11111111-1", encontrado.getCuit());
    }
}

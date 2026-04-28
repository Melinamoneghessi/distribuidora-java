package com.distribuidora.data;

import com.distribuidora.model.Proveedor;
import com.distribuidora.model.Producto;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DistribuidoraRepository {
    private static DistribuidoraRepository instancia;

    private DistribuidoraRepository() {
    }

    public static DistribuidoraRepository getInstancia() {
        if (instancia == null) {
            instancia = new DistribuidoraRepository();
        }
        return instancia;
    }

    public void guardarProveedor(Proveedor proveedor) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(proveedor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void guardarProducto(Producto producto) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.persist(producto);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Proveedor buscarProveedorPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Proveedor.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Producto> buscarProductos(String categoria, Double precioMax, Integer stockMin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Producto p WHERE 1=1";

            if (categoria != null && !categoria.isEmpty()) {
                hql += "AND p.categoria=:categoria";
            }
            if (precioMax != null) {
                hql += " AND p.precioUnitario <= :precioMax";
            }
            if (stockMin != null) {
                hql += " AND p.stock >= :stockMin";
            }
            var query = session.createQuery(hql, Producto.class);

            if (categoria != null && !categoria.isEmpty()) {
                query.setParameter("categoria", categoria);
            }

            if (precioMax != null) {
                query.setParameter("precioMax", precioMax);
            }

            if (stockMin != null) {
                query.setParameter("stockMin", stockMin);
            }

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Object[]> resumenPorProveedor() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = """
                        SELECT p.proveedor, 
                               COUNT(p), 
                               SUM(p.stock), 
                               SUM(p.stock * p.precioUnitario)
                        FROM Producto p
                        GROUP BY p.proveedor
                    """;

            return session.createQuery(hql, Object[].class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

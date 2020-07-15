/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.controllers;

import com.Marlon.backend.persistence.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Marlon.backend.persistence.entities.Citas;
import com.Marlon.backend.persistence.entities.Recetas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ALEX
 */
public class RecetasJpaController implements Serializable {

    public RecetasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recetas recetas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Citas idCita = recetas.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                recetas.setIdCita(idCita);
            }
            em.persist(recetas);
            if (idCita != null) {
                idCita.getRecetasCollection().add(recetas);
                idCita = em.merge(idCita);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recetas recetas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recetas persistentRecetas = em.find(Recetas.class, recetas.getIdRecetas());
            Citas idCitaOld = persistentRecetas.getIdCita();
            Citas idCitaNew = recetas.getIdCita();
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                recetas.setIdCita(idCitaNew);
            }
            recetas = em.merge(recetas);
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.getRecetasCollection().remove(recetas);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.getRecetasCollection().add(recetas);
                idCitaNew = em.merge(idCitaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recetas.getIdRecetas();
                if (findRecetas(id) == null) {
                    throw new NonexistentEntityException("The recetas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recetas recetas;
            try {
                recetas = em.getReference(Recetas.class, id);
                recetas.getIdRecetas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recetas with id " + id + " no longer exists.", enfe);
            }
            Citas idCita = recetas.getIdCita();
            if (idCita != null) {
                idCita.getRecetasCollection().remove(recetas);
                idCita = em.merge(idCita);
            }
            em.remove(recetas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recetas> findRecetasEntities() {
        return findRecetasEntities(true, -1, -1);
    }

    public List<Recetas> findRecetasEntities(int maxResults, int firstResult) {
        return findRecetasEntities(false, maxResults, firstResult);
    }

    private List<Recetas> findRecetasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recetas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Recetas findRecetas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recetas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recetas> rt = cq.from(Recetas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

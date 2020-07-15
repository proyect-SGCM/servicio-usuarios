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
import com.Marlon.backend.persistence.entities.Examenes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ALEX
 */
public class ExamenesJpaController implements Serializable {

    public ExamenesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Examenes examenes) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Citas idCita = examenes.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                examenes.setIdCita(idCita);
            }
            em.persist(examenes);
            if (idCita != null) {
                idCita.getExamenesCollection().add(examenes);
                idCita = em.merge(idCita);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Examenes examenes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Examenes persistentExamenes = em.find(Examenes.class, examenes.getIdExamen());
            Citas idCitaOld = persistentExamenes.getIdCita();
            Citas idCitaNew = examenes.getIdCita();
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                examenes.setIdCita(idCitaNew);
            }
            examenes = em.merge(examenes);
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.getExamenesCollection().remove(examenes);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.getExamenesCollection().add(examenes);
                idCitaNew = em.merge(idCitaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examenes.getIdExamen();
                if (findExamenes(id) == null) {
                    throw new NonexistentEntityException("The examenes with id " + id + " no longer exists.");
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
            Examenes examenes;
            try {
                examenes = em.getReference(Examenes.class, id);
                examenes.getIdExamen();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examenes with id " + id + " no longer exists.", enfe);
            }
            Citas idCita = examenes.getIdCita();
            if (idCita != null) {
                idCita.getExamenesCollection().remove(examenes);
                idCita = em.merge(idCita);
            }
            em.remove(examenes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Examenes> findExamenesEntities() {
        return findExamenesEntities(true, -1, -1);
    }

    public List<Examenes> findExamenesEntities(int maxResults, int firstResult) {
        return findExamenesEntities(false, maxResults, firstResult);
    }

    private List<Examenes> findExamenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Examenes.class));
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

    public Examenes findExamenes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Examenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Examenes> rt = cq.from(Examenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

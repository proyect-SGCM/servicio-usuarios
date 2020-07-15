/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.controllers;

import com.Marlon.backend.persistence.controllers.exceptions.NonexistentEntityException;
import com.Marlon.backend.persistence.entities.Personasdb;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ALEX
 */
public class PersonasdbJpaController1 implements Serializable {

    public PersonasdbJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personasdb personasdb) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(personasdb);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personasdb personasdb) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            personasdb = em.merge(personasdb);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = personasdb.getId();
                if (findPersonasdb(id) == null) {
                    throw new NonexistentEntityException("The personasdb with id " + id + " no longer exists.");
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
            Personasdb personasdb;
            try {
                personasdb = em.getReference(Personasdb.class, id);
                personasdb.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personasdb with id " + id + " no longer exists.", enfe);
            }
            em.remove(personasdb);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personasdb> findPersonasdbEntities() {
        return findPersonasdbEntities(true, -1, -1);
    }

    public List<Personasdb> findPersonasdbEntities(int maxResults, int firstResult) {
        return findPersonasdbEntities(false, maxResults, firstResult);
    }

    private List<Personasdb> findPersonasdbEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personasdb.class));
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

    public Personasdb findPersonasdb(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personasdb.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonasdbCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personasdb> rt = cq.from(Personasdb.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

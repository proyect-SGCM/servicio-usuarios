/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.controllers;

import com.Marlon.backend.persistence.controllers.exceptions.NonexistentEntityException;
import com.Marlon.backend.persistence.entities.Especialidad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Marlon.backend.persistence.entities.Medicos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ALEX
 */
public class EspecialidadJpaController implements Serializable {

    public EspecialidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Especialidad especialidad) {
        if (especialidad.getMedicosCollection() == null) {
            especialidad.setMedicosCollection(new ArrayList<Medicos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Medicos> attachedMedicosCollection = new ArrayList<Medicos>();
            for (Medicos medicosCollectionMedicosToAttach : especialidad.getMedicosCollection()) {
                medicosCollectionMedicosToAttach = em.getReference(medicosCollectionMedicosToAttach.getClass(), medicosCollectionMedicosToAttach.getIdMedico());
                attachedMedicosCollection.add(medicosCollectionMedicosToAttach);
            }
            especialidad.setMedicosCollection(attachedMedicosCollection);
            em.persist(especialidad);
            for (Medicos medicosCollectionMedicos : especialidad.getMedicosCollection()) {
                medicosCollectionMedicos.getEspecialidadCollection().add(especialidad);
                medicosCollectionMedicos = em.merge(medicosCollectionMedicos);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Especialidad especialidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Especialidad persistentEspecialidad = em.find(Especialidad.class, especialidad.getIdEspecialidad());
            Collection<Medicos> medicosCollectionOld = persistentEspecialidad.getMedicosCollection();
            Collection<Medicos> medicosCollectionNew = especialidad.getMedicosCollection();
            Collection<Medicos> attachedMedicosCollectionNew = new ArrayList<Medicos>();
            for (Medicos medicosCollectionNewMedicosToAttach : medicosCollectionNew) {
                medicosCollectionNewMedicosToAttach = em.getReference(medicosCollectionNewMedicosToAttach.getClass(), medicosCollectionNewMedicosToAttach.getIdMedico());
                attachedMedicosCollectionNew.add(medicosCollectionNewMedicosToAttach);
            }
            medicosCollectionNew = attachedMedicosCollectionNew;
            especialidad.setMedicosCollection(medicosCollectionNew);
            especialidad = em.merge(especialidad);
            for (Medicos medicosCollectionOldMedicos : medicosCollectionOld) {
                if (!medicosCollectionNew.contains(medicosCollectionOldMedicos)) {
                    medicosCollectionOldMedicos.getEspecialidadCollection().remove(especialidad);
                    medicosCollectionOldMedicos = em.merge(medicosCollectionOldMedicos);
                }
            }
            for (Medicos medicosCollectionNewMedicos : medicosCollectionNew) {
                if (!medicosCollectionOld.contains(medicosCollectionNewMedicos)) {
                    medicosCollectionNewMedicos.getEspecialidadCollection().add(especialidad);
                    medicosCollectionNewMedicos = em.merge(medicosCollectionNewMedicos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = especialidad.getIdEspecialidad();
                if (findEspecialidad(id) == null) {
                    throw new NonexistentEntityException("The especialidad with id " + id + " no longer exists.");
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
            Especialidad especialidad;
            try {
                especialidad = em.getReference(Especialidad.class, id);
                especialidad.getIdEspecialidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The especialidad with id " + id + " no longer exists.", enfe);
            }
            Collection<Medicos> medicosCollection = especialidad.getMedicosCollection();
            for (Medicos medicosCollectionMedicos : medicosCollection) {
                medicosCollectionMedicos.getEspecialidadCollection().remove(especialidad);
                medicosCollectionMedicos = em.merge(medicosCollectionMedicos);
            }
            em.remove(especialidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Especialidad> findEspecialidadEntities() {
        return findEspecialidadEntities(true, -1, -1);
    }

    public List<Especialidad> findEspecialidadEntities(int maxResults, int firstResult) {
        return findEspecialidadEntities(false, maxResults, firstResult);
    }

    private List<Especialidad> findEspecialidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Especialidad.class));
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

    public Especialidad findEspecialidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Especialidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getEspecialidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Especialidad> rt = cq.from(Especialidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

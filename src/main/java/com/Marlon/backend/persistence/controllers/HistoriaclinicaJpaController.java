/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.controllers;

import com.Marlon.backend.persistence.controllers.exceptions.IllegalOrphanException;
import com.Marlon.backend.persistence.controllers.exceptions.NonexistentEntityException;
import com.Marlon.backend.persistence.entities.Historiaclinica;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Marlon.backend.persistence.entities.Pacientes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ALEX
 */
public class HistoriaclinicaJpaController implements Serializable {

    public HistoriaclinicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historiaclinica historiaclinica) {
        if (historiaclinica.getPacientesCollection() == null) {
            historiaclinica.setPacientesCollection(new ArrayList<Pacientes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pacientes> attachedPacientesCollection = new ArrayList<Pacientes>();
            for (Pacientes pacientesCollectionPacientesToAttach : historiaclinica.getPacientesCollection()) {
                pacientesCollectionPacientesToAttach = em.getReference(pacientesCollectionPacientesToAttach.getClass(), pacientesCollectionPacientesToAttach.getIdPacientes());
                attachedPacientesCollection.add(pacientesCollectionPacientesToAttach);
            }
            historiaclinica.setPacientesCollection(attachedPacientesCollection);
            em.persist(historiaclinica);
            for (Pacientes pacientesCollectionPacientes : historiaclinica.getPacientesCollection()) {
                Historiaclinica oldIdhistoriaClinicaOfPacientesCollectionPacientes = pacientesCollectionPacientes.getIdhistoriaClinica();
                pacientesCollectionPacientes.setIdhistoriaClinica(historiaclinica);
                pacientesCollectionPacientes = em.merge(pacientesCollectionPacientes);
                if (oldIdhistoriaClinicaOfPacientesCollectionPacientes != null) {
                    oldIdhistoriaClinicaOfPacientesCollectionPacientes.getPacientesCollection().remove(pacientesCollectionPacientes);
                    oldIdhistoriaClinicaOfPacientesCollectionPacientes = em.merge(oldIdhistoriaClinicaOfPacientesCollectionPacientes);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historiaclinica historiaclinica) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historiaclinica persistentHistoriaclinica = em.find(Historiaclinica.class, historiaclinica.getIdhistoriaClinica());
            Collection<Pacientes> pacientesCollectionOld = persistentHistoriaclinica.getPacientesCollection();
            Collection<Pacientes> pacientesCollectionNew = historiaclinica.getPacientesCollection();
            List<String> illegalOrphanMessages = null;
            for (Pacientes pacientesCollectionOldPacientes : pacientesCollectionOld) {
                if (!pacientesCollectionNew.contains(pacientesCollectionOldPacientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pacientes " + pacientesCollectionOldPacientes + " since its idhistoriaClinica field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pacientes> attachedPacientesCollectionNew = new ArrayList<Pacientes>();
            for (Pacientes pacientesCollectionNewPacientesToAttach : pacientesCollectionNew) {
                pacientesCollectionNewPacientesToAttach = em.getReference(pacientesCollectionNewPacientesToAttach.getClass(), pacientesCollectionNewPacientesToAttach.getIdPacientes());
                attachedPacientesCollectionNew.add(pacientesCollectionNewPacientesToAttach);
            }
            pacientesCollectionNew = attachedPacientesCollectionNew;
            historiaclinica.setPacientesCollection(pacientesCollectionNew);
            historiaclinica = em.merge(historiaclinica);
            for (Pacientes pacientesCollectionNewPacientes : pacientesCollectionNew) {
                if (!pacientesCollectionOld.contains(pacientesCollectionNewPacientes)) {
                    Historiaclinica oldIdhistoriaClinicaOfPacientesCollectionNewPacientes = pacientesCollectionNewPacientes.getIdhistoriaClinica();
                    pacientesCollectionNewPacientes.setIdhistoriaClinica(historiaclinica);
                    pacientesCollectionNewPacientes = em.merge(pacientesCollectionNewPacientes);
                    if (oldIdhistoriaClinicaOfPacientesCollectionNewPacientes != null && !oldIdhistoriaClinicaOfPacientesCollectionNewPacientes.equals(historiaclinica)) {
                        oldIdhistoriaClinicaOfPacientesCollectionNewPacientes.getPacientesCollection().remove(pacientesCollectionNewPacientes);
                        oldIdhistoriaClinicaOfPacientesCollectionNewPacientes = em.merge(oldIdhistoriaClinicaOfPacientesCollectionNewPacientes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historiaclinica.getIdhistoriaClinica();
                if (findHistoriaclinica(id) == null) {
                    throw new NonexistentEntityException("The historiaclinica with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historiaclinica historiaclinica;
            try {
                historiaclinica = em.getReference(Historiaclinica.class, id);
                historiaclinica.getIdhistoriaClinica();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historiaclinica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pacientes> pacientesCollectionOrphanCheck = historiaclinica.getPacientesCollection();
            for (Pacientes pacientesCollectionOrphanCheckPacientes : pacientesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Historiaclinica (" + historiaclinica + ") cannot be destroyed since the Pacientes " + pacientesCollectionOrphanCheckPacientes + " in its pacientesCollection field has a non-nullable idhistoriaClinica field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(historiaclinica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Historiaclinica> findHistoriaclinicaEntities() {
        return findHistoriaclinicaEntities(true, -1, -1);
    }

    public List<Historiaclinica> findHistoriaclinicaEntities(int maxResults, int firstResult) {
        return findHistoriaclinicaEntities(false, maxResults, firstResult);
    }

    private List<Historiaclinica> findHistoriaclinicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historiaclinica.class));
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

    public Historiaclinica findHistoriaclinica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historiaclinica.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoriaclinicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historiaclinica> rt = cq.from(Historiaclinica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

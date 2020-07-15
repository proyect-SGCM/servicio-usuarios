/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.controllers;

import com.Marlon.backend.persistence.controllers.exceptions.IllegalOrphanException;
import com.Marlon.backend.persistence.controllers.exceptions.NonexistentEntityException;
import com.Marlon.backend.persistence.entities.Citas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Marlon.backend.persistence.entities.Medicos;
import com.Marlon.backend.persistence.entities.Pacientes;
import com.Marlon.backend.persistence.entities.Recetas;
import java.util.ArrayList;
import java.util.Collection;
import com.Marlon.backend.persistence.entities.Examenes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ALEX
 */
public class CitasJpaController implements Serializable {

    public CitasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Citas citas) throws IllegalOrphanException {
        if (citas.getRecetasCollection() == null) {
            citas.setRecetasCollection(new ArrayList<Recetas>());
        }
        if (citas.getExamenesCollection() == null) {
            citas.setExamenesCollection(new ArrayList<Examenes>());
        }
        List<String> illegalOrphanMessages = null;
        Medicos idMedicoOrphanCheck = citas.getIdMedico();
        if (idMedicoOrphanCheck != null) {
            Citas oldCitasOfIdMedico = idMedicoOrphanCheck.getCitas();
            if (oldCitasOfIdMedico != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Medicos " + idMedicoOrphanCheck + " already has an item of type Citas whose idMedico column cannot be null. Please make another selection for the idMedico field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicos idMedico = citas.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                citas.setIdMedico(idMedico);
            }
            Pacientes idPaciente = citas.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPacientes());
                citas.setIdPaciente(idPaciente);
            }
            Collection<Recetas> attachedRecetasCollection = new ArrayList<Recetas>();
            for (Recetas recetasCollectionRecetasToAttach : citas.getRecetasCollection()) {
                recetasCollectionRecetasToAttach = em.getReference(recetasCollectionRecetasToAttach.getClass(), recetasCollectionRecetasToAttach.getIdRecetas());
                attachedRecetasCollection.add(recetasCollectionRecetasToAttach);
            }
            citas.setRecetasCollection(attachedRecetasCollection);
            Collection<Examenes> attachedExamenesCollection = new ArrayList<Examenes>();
            for (Examenes examenesCollectionExamenesToAttach : citas.getExamenesCollection()) {
                examenesCollectionExamenesToAttach = em.getReference(examenesCollectionExamenesToAttach.getClass(), examenesCollectionExamenesToAttach.getIdExamen());
                attachedExamenesCollection.add(examenesCollectionExamenesToAttach);
            }
            citas.setExamenesCollection(attachedExamenesCollection);
            em.persist(citas);
            if (idMedico != null) {
                idMedico.setCitas(citas);
                idMedico = em.merge(idMedico);
            }
            if (idPaciente != null) {
                idPaciente.getCitasCollection().add(citas);
                idPaciente = em.merge(idPaciente);
            }
            for (Recetas recetasCollectionRecetas : citas.getRecetasCollection()) {
                Citas oldIdCitaOfRecetasCollectionRecetas = recetasCollectionRecetas.getIdCita();
                recetasCollectionRecetas.setIdCita(citas);
                recetasCollectionRecetas = em.merge(recetasCollectionRecetas);
                if (oldIdCitaOfRecetasCollectionRecetas != null) {
                    oldIdCitaOfRecetasCollectionRecetas.getRecetasCollection().remove(recetasCollectionRecetas);
                    oldIdCitaOfRecetasCollectionRecetas = em.merge(oldIdCitaOfRecetasCollectionRecetas);
                }
            }
            for (Examenes examenesCollectionExamenes : citas.getExamenesCollection()) {
                Citas oldIdCitaOfExamenesCollectionExamenes = examenesCollectionExamenes.getIdCita();
                examenesCollectionExamenes.setIdCita(citas);
                examenesCollectionExamenes = em.merge(examenesCollectionExamenes);
                if (oldIdCitaOfExamenesCollectionExamenes != null) {
                    oldIdCitaOfExamenesCollectionExamenes.getExamenesCollection().remove(examenesCollectionExamenes);
                    oldIdCitaOfExamenesCollectionExamenes = em.merge(oldIdCitaOfExamenesCollectionExamenes);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Citas citas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Citas persistentCitas = em.find(Citas.class, citas.getIdCita());
            Medicos idMedicoOld = persistentCitas.getIdMedico();
            Medicos idMedicoNew = citas.getIdMedico();
            Pacientes idPacienteOld = persistentCitas.getIdPaciente();
            Pacientes idPacienteNew = citas.getIdPaciente();
            Collection<Recetas> recetasCollectionOld = persistentCitas.getRecetasCollection();
            Collection<Recetas> recetasCollectionNew = citas.getRecetasCollection();
            Collection<Examenes> examenesCollectionOld = persistentCitas.getExamenesCollection();
            Collection<Examenes> examenesCollectionNew = citas.getExamenesCollection();
            List<String> illegalOrphanMessages = null;
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                Citas oldCitasOfIdMedico = idMedicoNew.getCitas();
                if (oldCitasOfIdMedico != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Medicos " + idMedicoNew + " already has an item of type Citas whose idMedico column cannot be null. Please make another selection for the idMedico field.");
                }
            }
            for (Recetas recetasCollectionOldRecetas : recetasCollectionOld) {
                if (!recetasCollectionNew.contains(recetasCollectionOldRecetas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recetas " + recetasCollectionOldRecetas + " since its idCita field is not nullable.");
                }
            }
            for (Examenes examenesCollectionOldExamenes : examenesCollectionOld) {
                if (!examenesCollectionNew.contains(examenesCollectionOldExamenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examenes " + examenesCollectionOldExamenes + " since its idCita field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                citas.setIdMedico(idMedicoNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPacientes());
                citas.setIdPaciente(idPacienteNew);
            }
            Collection<Recetas> attachedRecetasCollectionNew = new ArrayList<Recetas>();
            for (Recetas recetasCollectionNewRecetasToAttach : recetasCollectionNew) {
                recetasCollectionNewRecetasToAttach = em.getReference(recetasCollectionNewRecetasToAttach.getClass(), recetasCollectionNewRecetasToAttach.getIdRecetas());
                attachedRecetasCollectionNew.add(recetasCollectionNewRecetasToAttach);
            }
            recetasCollectionNew = attachedRecetasCollectionNew;
            citas.setRecetasCollection(recetasCollectionNew);
            Collection<Examenes> attachedExamenesCollectionNew = new ArrayList<Examenes>();
            for (Examenes examenesCollectionNewExamenesToAttach : examenesCollectionNew) {
                examenesCollectionNewExamenesToAttach = em.getReference(examenesCollectionNewExamenesToAttach.getClass(), examenesCollectionNewExamenesToAttach.getIdExamen());
                attachedExamenesCollectionNew.add(examenesCollectionNewExamenesToAttach);
            }
            examenesCollectionNew = attachedExamenesCollectionNew;
            citas.setExamenesCollection(examenesCollectionNew);
            citas = em.merge(citas);
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.setCitas(null);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.setCitas(citas);
                idMedicoNew = em.merge(idMedicoNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getCitasCollection().remove(citas);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getCitasCollection().add(citas);
                idPacienteNew = em.merge(idPacienteNew);
            }
            for (Recetas recetasCollectionNewRecetas : recetasCollectionNew) {
                if (!recetasCollectionOld.contains(recetasCollectionNewRecetas)) {
                    Citas oldIdCitaOfRecetasCollectionNewRecetas = recetasCollectionNewRecetas.getIdCita();
                    recetasCollectionNewRecetas.setIdCita(citas);
                    recetasCollectionNewRecetas = em.merge(recetasCollectionNewRecetas);
                    if (oldIdCitaOfRecetasCollectionNewRecetas != null && !oldIdCitaOfRecetasCollectionNewRecetas.equals(citas)) {
                        oldIdCitaOfRecetasCollectionNewRecetas.getRecetasCollection().remove(recetasCollectionNewRecetas);
                        oldIdCitaOfRecetasCollectionNewRecetas = em.merge(oldIdCitaOfRecetasCollectionNewRecetas);
                    }
                }
            }
            for (Examenes examenesCollectionNewExamenes : examenesCollectionNew) {
                if (!examenesCollectionOld.contains(examenesCollectionNewExamenes)) {
                    Citas oldIdCitaOfExamenesCollectionNewExamenes = examenesCollectionNewExamenes.getIdCita();
                    examenesCollectionNewExamenes.setIdCita(citas);
                    examenesCollectionNewExamenes = em.merge(examenesCollectionNewExamenes);
                    if (oldIdCitaOfExamenesCollectionNewExamenes != null && !oldIdCitaOfExamenesCollectionNewExamenes.equals(citas)) {
                        oldIdCitaOfExamenesCollectionNewExamenes.getExamenesCollection().remove(examenesCollectionNewExamenes);
                        oldIdCitaOfExamenesCollectionNewExamenes = em.merge(oldIdCitaOfExamenesCollectionNewExamenes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = citas.getIdCita();
                if (findCitas(id) == null) {
                    throw new NonexistentEntityException("The citas with id " + id + " no longer exists.");
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
            Citas citas;
            try {
                citas = em.getReference(Citas.class, id);
                citas.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Recetas> recetasCollectionOrphanCheck = citas.getRecetasCollection();
            for (Recetas recetasCollectionOrphanCheckRecetas : recetasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Citas (" + citas + ") cannot be destroyed since the Recetas " + recetasCollectionOrphanCheckRecetas + " in its recetasCollection field has a non-nullable idCita field.");
            }
            Collection<Examenes> examenesCollectionOrphanCheck = citas.getExamenesCollection();
            for (Examenes examenesCollectionOrphanCheckExamenes : examenesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Citas (" + citas + ") cannot be destroyed since the Examenes " + examenesCollectionOrphanCheckExamenes + " in its examenesCollection field has a non-nullable idCita field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medicos idMedico = citas.getIdMedico();
            if (idMedico != null) {
                idMedico.setCitas(null);
                idMedico = em.merge(idMedico);
            }
            Pacientes idPaciente = citas.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getCitasCollection().remove(citas);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(citas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Citas> findCitasEntities() {
        return findCitasEntities(true, -1, -1);
    }

    public List<Citas> findCitasEntities(int maxResults, int firstResult) {
        return findCitasEntities(false, maxResults, firstResult);
    }

    private List<Citas> findCitasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Citas.class));
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

    public Citas findCitas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Citas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Citas> rt = cq.from(Citas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

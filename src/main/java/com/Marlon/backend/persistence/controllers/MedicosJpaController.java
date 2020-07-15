/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.controllers;

import com.Marlon.backend.persistence.controllers.exceptions.IllegalOrphanException;
import com.Marlon.backend.persistence.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.Marlon.backend.persistence.entities.Citas;
import com.Marlon.backend.persistence.entities.Usuarios;
import com.Marlon.backend.persistence.entities.Especialidad;
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
public class MedicosJpaController implements Serializable {

    public MedicosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicos medicos) {
        if (medicos.getEspecialidadCollection() == null) {
            medicos.setEspecialidadCollection(new ArrayList<Especialidad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Citas citas = medicos.getCitas();
            if (citas != null) {
                citas = em.getReference(citas.getClass(), citas.getIdCita());
                medicos.setCitas(citas);
            }
            Usuarios usuariosIdUsuarios = medicos.getUsuariosIdUsuarios();
            if (usuariosIdUsuarios != null) {
                usuariosIdUsuarios = em.getReference(usuariosIdUsuarios.getClass(), usuariosIdUsuarios.getIdUsuarios());
                medicos.setUsuariosIdUsuarios(usuariosIdUsuarios);
            }
            Collection<Especialidad> attachedEspecialidadCollection = new ArrayList<Especialidad>();
            for (Especialidad especialidadCollectionEspecialidadToAttach : medicos.getEspecialidadCollection()) {
                especialidadCollectionEspecialidadToAttach = em.getReference(especialidadCollectionEspecialidadToAttach.getClass(), especialidadCollectionEspecialidadToAttach.getIdEspecialidad());
                attachedEspecialidadCollection.add(especialidadCollectionEspecialidadToAttach);
            }
            medicos.setEspecialidadCollection(attachedEspecialidadCollection);
            em.persist(medicos);
            if (citas != null) {
                Medicos oldIdMedicoOfCitas = citas.getIdMedico();
                if (oldIdMedicoOfCitas != null) {
                    oldIdMedicoOfCitas.setCitas(null);
                    oldIdMedicoOfCitas = em.merge(oldIdMedicoOfCitas);
                }
                citas.setIdMedico(medicos);
                citas = em.merge(citas);
            }
            if (usuariosIdUsuarios != null) {
                usuariosIdUsuarios.getMedicosCollection().add(medicos);
                usuariosIdUsuarios = em.merge(usuariosIdUsuarios);
            }
            for (Especialidad especialidadCollectionEspecialidad : medicos.getEspecialidadCollection()) {
                especialidadCollectionEspecialidad.getMedicosCollection().add(medicos);
                especialidadCollectionEspecialidad = em.merge(especialidadCollectionEspecialidad);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicos medicos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicos persistentMedicos = em.find(Medicos.class, medicos.getIdMedico());
            Citas citasOld = persistentMedicos.getCitas();
            Citas citasNew = medicos.getCitas();
            Usuarios usuariosIdUsuariosOld = persistentMedicos.getUsuariosIdUsuarios();
            Usuarios usuariosIdUsuariosNew = medicos.getUsuariosIdUsuarios();
            Collection<Especialidad> especialidadCollectionOld = persistentMedicos.getEspecialidadCollection();
            Collection<Especialidad> especialidadCollectionNew = medicos.getEspecialidadCollection();
            List<String> illegalOrphanMessages = null;
            if (citasOld != null && !citasOld.equals(citasNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Citas " + citasOld + " since its idMedico field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (citasNew != null) {
                citasNew = em.getReference(citasNew.getClass(), citasNew.getIdCita());
                medicos.setCitas(citasNew);
            }
            if (usuariosIdUsuariosNew != null) {
                usuariosIdUsuariosNew = em.getReference(usuariosIdUsuariosNew.getClass(), usuariosIdUsuariosNew.getIdUsuarios());
                medicos.setUsuariosIdUsuarios(usuariosIdUsuariosNew);
            }
            Collection<Especialidad> attachedEspecialidadCollectionNew = new ArrayList<Especialidad>();
            for (Especialidad especialidadCollectionNewEspecialidadToAttach : especialidadCollectionNew) {
                especialidadCollectionNewEspecialidadToAttach = em.getReference(especialidadCollectionNewEspecialidadToAttach.getClass(), especialidadCollectionNewEspecialidadToAttach.getIdEspecialidad());
                attachedEspecialidadCollectionNew.add(especialidadCollectionNewEspecialidadToAttach);
            }
            especialidadCollectionNew = attachedEspecialidadCollectionNew;
            medicos.setEspecialidadCollection(especialidadCollectionNew);
            medicos = em.merge(medicos);
            if (citasNew != null && !citasNew.equals(citasOld)) {
                Medicos oldIdMedicoOfCitas = citasNew.getIdMedico();
                if (oldIdMedicoOfCitas != null) {
                    oldIdMedicoOfCitas.setCitas(null);
                    oldIdMedicoOfCitas = em.merge(oldIdMedicoOfCitas);
                }
                citasNew.setIdMedico(medicos);
                citasNew = em.merge(citasNew);
            }
            if (usuariosIdUsuariosOld != null && !usuariosIdUsuariosOld.equals(usuariosIdUsuariosNew)) {
                usuariosIdUsuariosOld.getMedicosCollection().remove(medicos);
                usuariosIdUsuariosOld = em.merge(usuariosIdUsuariosOld);
            }
            if (usuariosIdUsuariosNew != null && !usuariosIdUsuariosNew.equals(usuariosIdUsuariosOld)) {
                usuariosIdUsuariosNew.getMedicosCollection().add(medicos);
                usuariosIdUsuariosNew = em.merge(usuariosIdUsuariosNew);
            }
            for (Especialidad especialidadCollectionOldEspecialidad : especialidadCollectionOld) {
                if (!especialidadCollectionNew.contains(especialidadCollectionOldEspecialidad)) {
                    especialidadCollectionOldEspecialidad.getMedicosCollection().remove(medicos);
                    especialidadCollectionOldEspecialidad = em.merge(especialidadCollectionOldEspecialidad);
                }
            }
            for (Especialidad especialidadCollectionNewEspecialidad : especialidadCollectionNew) {
                if (!especialidadCollectionOld.contains(especialidadCollectionNewEspecialidad)) {
                    especialidadCollectionNewEspecialidad.getMedicosCollection().add(medicos);
                    especialidadCollectionNewEspecialidad = em.merge(especialidadCollectionNewEspecialidad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medicos.getIdMedico();
                if (findMedicos(id) == null) {
                    throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.");
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
            Medicos medicos;
            try {
                medicos = em.getReference(Medicos.class, id);
                medicos.getIdMedico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Citas citasOrphanCheck = medicos.getCitas();
            if (citasOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicos (" + medicos + ") cannot be destroyed since the Citas " + citasOrphanCheck + " in its citas field has a non-nullable idMedico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios usuariosIdUsuarios = medicos.getUsuariosIdUsuarios();
            if (usuariosIdUsuarios != null) {
                usuariosIdUsuarios.getMedicosCollection().remove(medicos);
                usuariosIdUsuarios = em.merge(usuariosIdUsuarios);
            }
            Collection<Especialidad> especialidadCollection = medicos.getEspecialidadCollection();
            for (Especialidad especialidadCollectionEspecialidad : especialidadCollection) {
                especialidadCollectionEspecialidad.getMedicosCollection().remove(medicos);
                especialidadCollectionEspecialidad = em.merge(especialidadCollectionEspecialidad);
            }
            em.remove(medicos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medicos> findMedicosEntities() {
        return findMedicosEntities(true, -1, -1);
    }

    public List<Medicos> findMedicosEntities(int maxResults, int firstResult) {
        return findMedicosEntities(false, maxResults, firstResult);
    }

    private List<Medicos> findMedicosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicos.class));
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

    public Medicos findMedicos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicos.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicos> rt = cq.from(Medicos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

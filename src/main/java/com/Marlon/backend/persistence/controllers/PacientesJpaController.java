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
import com.Marlon.backend.persistence.entities.Historiaclinica;
import com.Marlon.backend.persistence.entities.Usuarios;
import com.Marlon.backend.persistence.entities.Citas;
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
public class PacientesJpaController implements Serializable {

    public PacientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pacientes pacientes) {
        if (pacientes.getCitasCollection() == null) {
            pacientes.setCitasCollection(new ArrayList<Citas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historiaclinica idhistoriaClinica = pacientes.getIdhistoriaClinica();
            if (idhistoriaClinica != null) {
                idhistoriaClinica = em.getReference(idhistoriaClinica.getClass(), idhistoriaClinica.getIdhistoriaClinica());
                pacientes.setIdhistoriaClinica(idhistoriaClinica);
            }
            Usuarios idUsuarios = pacientes.getIdUsuarios();
            if (idUsuarios != null) {
                idUsuarios = em.getReference(idUsuarios.getClass(), idUsuarios.getIdUsuarios());
                pacientes.setIdUsuarios(idUsuarios);
            }
            Collection<Citas> attachedCitasCollection = new ArrayList<Citas>();
            for (Citas citasCollectionCitasToAttach : pacientes.getCitasCollection()) {
                citasCollectionCitasToAttach = em.getReference(citasCollectionCitasToAttach.getClass(), citasCollectionCitasToAttach.getIdCita());
                attachedCitasCollection.add(citasCollectionCitasToAttach);
            }
            pacientes.setCitasCollection(attachedCitasCollection);
            em.persist(pacientes);
            if (idhistoriaClinica != null) {
                idhistoriaClinica.getPacientesCollection().add(pacientes);
                idhistoriaClinica = em.merge(idhistoriaClinica);
            }
            if (idUsuarios != null) {
                idUsuarios.getPacientesCollection().add(pacientes);
                idUsuarios = em.merge(idUsuarios);
            }
            for (Citas citasCollectionCitas : pacientes.getCitasCollection()) {
                Pacientes oldIdPacienteOfCitasCollectionCitas = citasCollectionCitas.getIdPaciente();
                citasCollectionCitas.setIdPaciente(pacientes);
                citasCollectionCitas = em.merge(citasCollectionCitas);
                if (oldIdPacienteOfCitasCollectionCitas != null) {
                    oldIdPacienteOfCitasCollectionCitas.getCitasCollection().remove(citasCollectionCitas);
                    oldIdPacienteOfCitasCollectionCitas = em.merge(oldIdPacienteOfCitasCollectionCitas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pacientes pacientes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes persistentPacientes = em.find(Pacientes.class, pacientes.getIdPacientes());
            Historiaclinica idhistoriaClinicaOld = persistentPacientes.getIdhistoriaClinica();
            Historiaclinica idhistoriaClinicaNew = pacientes.getIdhistoriaClinica();
            Usuarios idUsuariosOld = persistentPacientes.getIdUsuarios();
            Usuarios idUsuariosNew = pacientes.getIdUsuarios();
            Collection<Citas> citasCollectionOld = persistentPacientes.getCitasCollection();
            Collection<Citas> citasCollectionNew = pacientes.getCitasCollection();
            List<String> illegalOrphanMessages = null;
            for (Citas citasCollectionOldCitas : citasCollectionOld) {
                if (!citasCollectionNew.contains(citasCollectionOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasCollectionOldCitas + " since its idPaciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idhistoriaClinicaNew != null) {
                idhistoriaClinicaNew = em.getReference(idhistoriaClinicaNew.getClass(), idhistoriaClinicaNew.getIdhistoriaClinica());
                pacientes.setIdhistoriaClinica(idhistoriaClinicaNew);
            }
            if (idUsuariosNew != null) {
                idUsuariosNew = em.getReference(idUsuariosNew.getClass(), idUsuariosNew.getIdUsuarios());
                pacientes.setIdUsuarios(idUsuariosNew);
            }
            Collection<Citas> attachedCitasCollectionNew = new ArrayList<Citas>();
            for (Citas citasCollectionNewCitasToAttach : citasCollectionNew) {
                citasCollectionNewCitasToAttach = em.getReference(citasCollectionNewCitasToAttach.getClass(), citasCollectionNewCitasToAttach.getIdCita());
                attachedCitasCollectionNew.add(citasCollectionNewCitasToAttach);
            }
            citasCollectionNew = attachedCitasCollectionNew;
            pacientes.setCitasCollection(citasCollectionNew);
            pacientes = em.merge(pacientes);
            if (idhistoriaClinicaOld != null && !idhistoriaClinicaOld.equals(idhistoriaClinicaNew)) {
                idhistoriaClinicaOld.getPacientesCollection().remove(pacientes);
                idhistoriaClinicaOld = em.merge(idhistoriaClinicaOld);
            }
            if (idhistoriaClinicaNew != null && !idhistoriaClinicaNew.equals(idhistoriaClinicaOld)) {
                idhistoriaClinicaNew.getPacientesCollection().add(pacientes);
                idhistoriaClinicaNew = em.merge(idhistoriaClinicaNew);
            }
            if (idUsuariosOld != null && !idUsuariosOld.equals(idUsuariosNew)) {
                idUsuariosOld.getPacientesCollection().remove(pacientes);
                idUsuariosOld = em.merge(idUsuariosOld);
            }
            if (idUsuariosNew != null && !idUsuariosNew.equals(idUsuariosOld)) {
                idUsuariosNew.getPacientesCollection().add(pacientes);
                idUsuariosNew = em.merge(idUsuariosNew);
            }
            for (Citas citasCollectionNewCitas : citasCollectionNew) {
                if (!citasCollectionOld.contains(citasCollectionNewCitas)) {
                    Pacientes oldIdPacienteOfCitasCollectionNewCitas = citasCollectionNewCitas.getIdPaciente();
                    citasCollectionNewCitas.setIdPaciente(pacientes);
                    citasCollectionNewCitas = em.merge(citasCollectionNewCitas);
                    if (oldIdPacienteOfCitasCollectionNewCitas != null && !oldIdPacienteOfCitasCollectionNewCitas.equals(pacientes)) {
                        oldIdPacienteOfCitasCollectionNewCitas.getCitasCollection().remove(citasCollectionNewCitas);
                        oldIdPacienteOfCitasCollectionNewCitas = em.merge(oldIdPacienteOfCitasCollectionNewCitas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pacientes.getIdPacientes();
                if (findPacientes(id) == null) {
                    throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.");
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
            Pacientes pacientes;
            try {
                pacientes = em.getReference(Pacientes.class, id);
                pacientes.getIdPacientes();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Citas> citasCollectionOrphanCheck = pacientes.getCitasCollection();
            for (Citas citasCollectionOrphanCheckCitas : citasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pacientes (" + pacientes + ") cannot be destroyed since the Citas " + citasCollectionOrphanCheckCitas + " in its citasCollection field has a non-nullable idPaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Historiaclinica idhistoriaClinica = pacientes.getIdhistoriaClinica();
            if (idhistoriaClinica != null) {
                idhistoriaClinica.getPacientesCollection().remove(pacientes);
                idhistoriaClinica = em.merge(idhistoriaClinica);
            }
            Usuarios idUsuarios = pacientes.getIdUsuarios();
            if (idUsuarios != null) {
                idUsuarios.getPacientesCollection().remove(pacientes);
                idUsuarios = em.merge(idUsuarios);
            }
            em.remove(pacientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pacientes> findPacientesEntities() {
        return findPacientesEntities(true, -1, -1);
    }

    public List<Pacientes> findPacientesEntities(int maxResults, int firstResult) {
        return findPacientesEntities(false, maxResults, firstResult);
    }

    private List<Pacientes> findPacientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pacientes.class));
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

    public Pacientes findPacientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pacientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pacientes> rt = cq.from(Pacientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

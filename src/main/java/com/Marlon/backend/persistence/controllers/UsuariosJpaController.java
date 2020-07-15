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
import com.Marlon.backend.persistence.entities.Roles;
import java.util.ArrayList;
import java.util.Collection;
import com.Marlon.backend.persistence.entities.Medicos;
import com.Marlon.backend.persistence.entities.Pacientes;
import com.Marlon.backend.persistence.entities.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ALEX
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getRolesCollection() == null) {
            usuarios.setRolesCollection(new ArrayList<Roles>());
        }
        if (usuarios.getMedicosCollection() == null) {
            usuarios.setMedicosCollection(new ArrayList<Medicos>());
        }
        if (usuarios.getPacientesCollection() == null) {
            usuarios.setPacientesCollection(new ArrayList<Pacientes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Roles> attachedRolesCollection = new ArrayList<Roles>();
            for (Roles rolesCollectionRolesToAttach : usuarios.getRolesCollection()) {
                rolesCollectionRolesToAttach = em.getReference(rolesCollectionRolesToAttach.getClass(), rolesCollectionRolesToAttach.getIdRol());
                attachedRolesCollection.add(rolesCollectionRolesToAttach);
            }
            usuarios.setRolesCollection(attachedRolesCollection);
            Collection<Medicos> attachedMedicosCollection = new ArrayList<Medicos>();
            for (Medicos medicosCollectionMedicosToAttach : usuarios.getMedicosCollection()) {
                medicosCollectionMedicosToAttach = em.getReference(medicosCollectionMedicosToAttach.getClass(), medicosCollectionMedicosToAttach.getIdMedico());
                attachedMedicosCollection.add(medicosCollectionMedicosToAttach);
            }
            usuarios.setMedicosCollection(attachedMedicosCollection);
            Collection<Pacientes> attachedPacientesCollection = new ArrayList<Pacientes>();
            for (Pacientes pacientesCollectionPacientesToAttach : usuarios.getPacientesCollection()) {
                pacientesCollectionPacientesToAttach = em.getReference(pacientesCollectionPacientesToAttach.getClass(), pacientesCollectionPacientesToAttach.getIdPacientes());
                attachedPacientesCollection.add(pacientesCollectionPacientesToAttach);
            }
            usuarios.setPacientesCollection(attachedPacientesCollection);
            em.persist(usuarios);
            for (Roles rolesCollectionRoles : usuarios.getRolesCollection()) {
                rolesCollectionRoles.getUsuariosCollection().add(usuarios);
                rolesCollectionRoles = em.merge(rolesCollectionRoles);
            }
            for (Medicos medicosCollectionMedicos : usuarios.getMedicosCollection()) {
                Usuarios oldUsuariosIdUsuariosOfMedicosCollectionMedicos = medicosCollectionMedicos.getUsuariosIdUsuarios();
                medicosCollectionMedicos.setUsuariosIdUsuarios(usuarios);
                medicosCollectionMedicos = em.merge(medicosCollectionMedicos);
                if (oldUsuariosIdUsuariosOfMedicosCollectionMedicos != null) {
                    oldUsuariosIdUsuariosOfMedicosCollectionMedicos.getMedicosCollection().remove(medicosCollectionMedicos);
                    oldUsuariosIdUsuariosOfMedicosCollectionMedicos = em.merge(oldUsuariosIdUsuariosOfMedicosCollectionMedicos);
                }
            }
            for (Pacientes pacientesCollectionPacientes : usuarios.getPacientesCollection()) {
                Usuarios oldIdUsuariosOfPacientesCollectionPacientes = pacientesCollectionPacientes.getIdUsuarios();
                pacientesCollectionPacientes.setIdUsuarios(usuarios);
                pacientesCollectionPacientes = em.merge(pacientesCollectionPacientes);
                if (oldIdUsuariosOfPacientesCollectionPacientes != null) {
                    oldIdUsuariosOfPacientesCollectionPacientes.getPacientesCollection().remove(pacientesCollectionPacientes);
                    oldIdUsuariosOfPacientesCollectionPacientes = em.merge(oldIdUsuariosOfPacientesCollectionPacientes);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuarios());
            Collection<Roles> rolesCollectionOld = persistentUsuarios.getRolesCollection();
            Collection<Roles> rolesCollectionNew = usuarios.getRolesCollection();
            Collection<Medicos> medicosCollectionOld = persistentUsuarios.getMedicosCollection();
            Collection<Medicos> medicosCollectionNew = usuarios.getMedicosCollection();
            Collection<Pacientes> pacientesCollectionOld = persistentUsuarios.getPacientesCollection();
            Collection<Pacientes> pacientesCollectionNew = usuarios.getPacientesCollection();
            List<String> illegalOrphanMessages = null;
            for (Medicos medicosCollectionOldMedicos : medicosCollectionOld) {
                if (!medicosCollectionNew.contains(medicosCollectionOldMedicos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Medicos " + medicosCollectionOldMedicos + " since its usuariosIdUsuarios field is not nullable.");
                }
            }
            for (Pacientes pacientesCollectionOldPacientes : pacientesCollectionOld) {
                if (!pacientesCollectionNew.contains(pacientesCollectionOldPacientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pacientes " + pacientesCollectionOldPacientes + " since its idUsuarios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Roles> attachedRolesCollectionNew = new ArrayList<Roles>();
            for (Roles rolesCollectionNewRolesToAttach : rolesCollectionNew) {
                rolesCollectionNewRolesToAttach = em.getReference(rolesCollectionNewRolesToAttach.getClass(), rolesCollectionNewRolesToAttach.getIdRol());
                attachedRolesCollectionNew.add(rolesCollectionNewRolesToAttach);
            }
            rolesCollectionNew = attachedRolesCollectionNew;
            usuarios.setRolesCollection(rolesCollectionNew);
            Collection<Medicos> attachedMedicosCollectionNew = new ArrayList<Medicos>();
            for (Medicos medicosCollectionNewMedicosToAttach : medicosCollectionNew) {
                medicosCollectionNewMedicosToAttach = em.getReference(medicosCollectionNewMedicosToAttach.getClass(), medicosCollectionNewMedicosToAttach.getIdMedico());
                attachedMedicosCollectionNew.add(medicosCollectionNewMedicosToAttach);
            }
            medicosCollectionNew = attachedMedicosCollectionNew;
            usuarios.setMedicosCollection(medicosCollectionNew);
            Collection<Pacientes> attachedPacientesCollectionNew = new ArrayList<Pacientes>();
            for (Pacientes pacientesCollectionNewPacientesToAttach : pacientesCollectionNew) {
                pacientesCollectionNewPacientesToAttach = em.getReference(pacientesCollectionNewPacientesToAttach.getClass(), pacientesCollectionNewPacientesToAttach.getIdPacientes());
                attachedPacientesCollectionNew.add(pacientesCollectionNewPacientesToAttach);
            }
            pacientesCollectionNew = attachedPacientesCollectionNew;
            usuarios.setPacientesCollection(pacientesCollectionNew);
            usuarios = em.merge(usuarios);
            for (Roles rolesCollectionOldRoles : rolesCollectionOld) {
                if (!rolesCollectionNew.contains(rolesCollectionOldRoles)) {
                    rolesCollectionOldRoles.getUsuariosCollection().remove(usuarios);
                    rolesCollectionOldRoles = em.merge(rolesCollectionOldRoles);
                }
            }
            for (Roles rolesCollectionNewRoles : rolesCollectionNew) {
                if (!rolesCollectionOld.contains(rolesCollectionNewRoles)) {
                    rolesCollectionNewRoles.getUsuariosCollection().add(usuarios);
                    rolesCollectionNewRoles = em.merge(rolesCollectionNewRoles);
                }
            }
            for (Medicos medicosCollectionNewMedicos : medicosCollectionNew) {
                if (!medicosCollectionOld.contains(medicosCollectionNewMedicos)) {
                    Usuarios oldUsuariosIdUsuariosOfMedicosCollectionNewMedicos = medicosCollectionNewMedicos.getUsuariosIdUsuarios();
                    medicosCollectionNewMedicos.setUsuariosIdUsuarios(usuarios);
                    medicosCollectionNewMedicos = em.merge(medicosCollectionNewMedicos);
                    if (oldUsuariosIdUsuariosOfMedicosCollectionNewMedicos != null && !oldUsuariosIdUsuariosOfMedicosCollectionNewMedicos.equals(usuarios)) {
                        oldUsuariosIdUsuariosOfMedicosCollectionNewMedicos.getMedicosCollection().remove(medicosCollectionNewMedicos);
                        oldUsuariosIdUsuariosOfMedicosCollectionNewMedicos = em.merge(oldUsuariosIdUsuariosOfMedicosCollectionNewMedicos);
                    }
                }
            }
            for (Pacientes pacientesCollectionNewPacientes : pacientesCollectionNew) {
                if (!pacientesCollectionOld.contains(pacientesCollectionNewPacientes)) {
                    Usuarios oldIdUsuariosOfPacientesCollectionNewPacientes = pacientesCollectionNewPacientes.getIdUsuarios();
                    pacientesCollectionNewPacientes.setIdUsuarios(usuarios);
                    pacientesCollectionNewPacientes = em.merge(pacientesCollectionNewPacientes);
                    if (oldIdUsuariosOfPacientesCollectionNewPacientes != null && !oldIdUsuariosOfPacientesCollectionNewPacientes.equals(usuarios)) {
                        oldIdUsuariosOfPacientesCollectionNewPacientes.getPacientesCollection().remove(pacientesCollectionNewPacientes);
                        oldIdUsuariosOfPacientesCollectionNewPacientes = em.merge(oldIdUsuariosOfPacientesCollectionNewPacientes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getIdUsuarios();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuarios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Medicos> medicosCollectionOrphanCheck = usuarios.getMedicosCollection();
            for (Medicos medicosCollectionOrphanCheckMedicos : medicosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Medicos " + medicosCollectionOrphanCheckMedicos + " in its medicosCollection field has a non-nullable usuariosIdUsuarios field.");
            }
            Collection<Pacientes> pacientesCollectionOrphanCheck = usuarios.getPacientesCollection();
            for (Pacientes pacientesCollectionOrphanCheckPacientes : pacientesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Pacientes " + pacientesCollectionOrphanCheckPacientes + " in its pacientesCollection field has a non-nullable idUsuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Roles> rolesCollection = usuarios.getRolesCollection();
            for (Roles rolesCollectionRoles : rolesCollection) {
                rolesCollectionRoles.getUsuariosCollection().remove(usuarios);
                rolesCollectionRoles = em.merge(rolesCollectionRoles);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

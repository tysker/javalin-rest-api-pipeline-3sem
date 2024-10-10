package dk.lyngby.security;

import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.security.model.Role;
import dk.lyngby.security.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.Set;

public class AuthDao {

    private final EntityManagerFactory emf;

    public AuthDao(EntityManagerFactory _emf) {
        this.emf = _emf;
    }

    public User registerUser(String username, String password, Set<String> roleList) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            User user = new User(username, password);
            Set<Role> roles = user.getRoleList();

            for (String roleName : roleList) {
                Role role = em.find(Role.class, Role.RoleName.valueOf(roleName));
                if (role == null) {
                    role = new Role(Role.RoleName.valueOf(roleName));
                    em.persist(role);
                }
                roles.add(new Role(Role.RoleName.valueOf(roleName)));
            }

            user.setRoleList(roles);

            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    public void checkUser(String username) {

        try (var em = emf.createEntityManager()) {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (user != null) throw new ApiException(400, "User already exists");
        } catch (NoResultException e) {
            // Do nothing
        }
    }

    public void checkRoles(Set<String> roleList) {
        Role.RoleName[] roleNames = Role.RoleName.values();

        for (String roleName : roleList) {
            boolean roleExists = false;
            for (Role.RoleName role : roleNames) {
                if (roleName.equals(role.toString())) {
                    roleExists = true;
                    break;
                }
            }
            if (!roleExists) throw new ApiException(400, "Role does not exist");
        }
    }

    public User verifyUser(String username, String password) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (user == null || !user.verifyPassword(password)) {
                throw new AuthorizationException(401, "Invalid user name or password");
            }
            em.getTransaction().commit();
            return user;
        }
    }

    public User getUser(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            em.getTransaction().commit();
            return user;
        } catch (NoResultException e) {
            throw new ApiException(401, "Invalid user");
        }
    }

}

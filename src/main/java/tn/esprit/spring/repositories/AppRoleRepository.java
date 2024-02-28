package tn.esprit.spring.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tn.esprit.spring.entities.AppRole;

@RepositoryRestResource
public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    public AppRole findByRoleName(String roleName);
}

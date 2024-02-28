package tn.esprit.spring.services;


import tn.esprit.spring.entities.AppUser;

import java.util.List;


public interface ImpAppUserService {
    public AppUser addAppUser(AppUser c, Long m, Long u);

    public List<AppUser> getListAppUser();

    public AppUser getAppUser(Long id);

    public List<AppUser> searchAppUser(String nc);

    public void updateAppUser(Long id);

    public void deleteAppUser(Long id);
}

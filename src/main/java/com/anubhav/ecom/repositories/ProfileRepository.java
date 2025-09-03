package com.anubhav.ecom.repositories;


import com.anubhav.ecom.models.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
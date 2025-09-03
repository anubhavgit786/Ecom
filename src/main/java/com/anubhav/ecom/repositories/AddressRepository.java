package com.anubhav.ecom.repositories;


import com.anubhav.ecom.models.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
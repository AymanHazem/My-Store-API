package com.ayman.my_store_api.entities.repositories;

import com.ayman.my_store_api.entities.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long>
{

}

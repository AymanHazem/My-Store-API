package com.ayman.my_store_api.entities.repositories;

import com.ayman.my_store_api.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}

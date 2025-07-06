package com.ayman.my_store_api.repositories;

import com.ayman.my_store_api.entities.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
}

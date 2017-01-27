package com.gsc.repository;

import com.gsc.entity.GscUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<GscUser, Long> {
    GscUser findByUserName(String userName);
}

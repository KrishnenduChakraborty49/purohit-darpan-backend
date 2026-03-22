package com.purohitdarpan.repository;

import com.purohitdarpan.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByPujaId(Long pujaId);
    List<Resource> findByResourceType(Resource.ResourceType resourceType);
}

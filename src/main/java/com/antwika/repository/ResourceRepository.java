package com.antwika.repository;

import com.antwika.api.generated.entities.Resource;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {}

package com.spotit.api.configuration.repository;

import com.spotit.api.configuration.entity.GlobalConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GlobalConfigurationRepository extends JpaRepository<GlobalConfiguration, UUID> {
    Optional<GlobalConfiguration> findByName(String name);

    List<GlobalConfiguration> findAllByOrderByNameAsc();

    List<GlobalConfiguration> findByNameStartingWithOrderByNameAsc(String namePrefix);

    List<GlobalConfiguration> findByGroupNameOrderByNameAsc(String groupName);

    @Query("select distinct g.groupName from GlobalConfiguration g order by g.groupName")
    List<String> findDistinctGroupNames();
}

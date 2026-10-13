package com.freightfox.transporterassignment.repository;

import com.freightfox.transporterassignment.entity.Transporter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporterRepository extends JpaRepository<Transporter, Long> {

    @Override
    @EntityGraph(attributePaths = {"laneQuotes", "laneQuotes.lane"})
    List<Transporter> findAll();
}

package com.freightfox.transporterassignment.repository;

import com.freightfox.transporterassignment.entity.LaneQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LaneQuoteRepository extends JpaRepository<LaneQuote, Long> {

    @Modifying
    @Query("DELETE FROM LaneQuote lq WHERE lq.transporter.id = :transporterId")
    void deleteByTransporterId(Long transporterId);
}

package com.mapoh.ppg.dao;

import com.mapoh.ppg.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mabohv
 * @date 2025/3/2 11:05
 */

@Repository
public interface RefundDao extends JpaRepository<Refund, Long> {


    @SuppressWarnings("all")
    /**
     * 分页查询优化
     * select * from refund where status = false order by refund_id limit page, size  1.92
     * select * from refund where status = false and refund_id  >  0.02 max id query  0.02
     * select * from refunds where status = false and id between 4000000 and 4000010; between ... and...
     * select * from refunds INNER JOIN(select id from refunds limit 4000000,10) as a using(id) latency query
     * @param offset
     * @param size
     * @param status
     */
    @Query(value = "SELECT r.* FROM refunds r " +
            "INNER JOIN (SELECT id FROM refunds WHERE status = :status LIMIT :offset, :size) a " +
            "ON r.id = a.id",
            nativeQuery = true)
    List<Refund> getRefundListByStatus(@Param("offset") int offset,
                                       @Param("size") int size,
                                       @Param("status") String status);


}

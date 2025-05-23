package com.mapoh.ppg.feign;

import com.mapoh.ppg.feign.hystrix.ContractFeignHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/19 21:38
 */

@FeignClient(value = "eureka-client-contract-distribution",
fallback = ContractFeignHystrix.class)
public interface ContractServiceFeign {

    @GetMapping("ppg-contract/distribution/amount/{contractId}")
    public CommonResponse<BigDecimal> getAmount(@PathVariable("contractId") Long contractId);

    @PostMapping("ppg-contract/distribution/validContract/{contractId}")
    public CommonResponse<Boolean> validContract(@PathVariable("contractId") Long contractId);

    @GetMapping("ppg-contract/distribution/unitamount/{contractId}")
    public CommonResponse<BigDecimal> getUnitAmount(@PathVariable("contractId") Long contractId);

    @GetMapping("ppg-contract/distribution/getContractId/{contractId}")
    CommonResponse<Long> getMerchantId(@PathVariable("contractId") Long contractId);

    @GetMapping("/ppg-contract/distribution/getContractVo/{contractId}")
    public CommonResponse<ContractVo> getContractVo(@PathVariable("contractId") Long contractId);

    @PostMapping("/ppg-contract/distribution/refund/updateStatus/{contractId}")
    public CommonResponse<Boolean> updateRefundStatus(@PathVariable("contractId") Long contractId);
}

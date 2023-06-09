package com.claivenant.PaymentService.Service;

import com.claivenant.PaymentService.Entity.TransactionDetails;
import com.claivenant.PaymentService.Model.PaymentMode;
import com.claivenant.PaymentService.Model.PaymentRequest;
import com.claivenant.PaymentService.Model.PaymentResponse;
import com.claivenant.PaymentService.Repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
        log.info("Getting payment for details for orderId: {}");
        TransactionDetails transactionDetails
                =transactionDetailsRepository.findByOrderId(Long.valueOf(orderId));

        PaymentResponse paymentResponse
                =PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentDate(transactionDetails.getPaymentDate())
                .orderId(transactionDetails.getOrderId())
                .build();

        return paymentResponse;
    }

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording Payment Details :{},paymentRequest");
        TransactionDetails transactionDetails
                =TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();
        transactionDetailsRepository.save(transactionDetails);
        log.info("Transaction Completed with Id: {}",transactionDetails.getId());
        return transactionDetails.getId();
    }
}

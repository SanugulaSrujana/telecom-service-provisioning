package com.telecom.telecom_service_provisioning.service.implementations;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telecom.telecom_service_provisioning.exception_handling.customExceptions.ResourceNotFoundException;
import com.telecom.telecom_service_provisioning.model.TvServiceAvailed;
import com.telecom.telecom_service_provisioning.model.compositekey_models.TvServicesAvailedId;
import com.telecom.telecom_service_provisioning.repository.TvServiceAvailedRepository;

@Service
public class AvailedTvServiceManager {


    @Autowired
    private AuthenticationServiceImpl authService;

    @Autowired
    private TvServiceAvailedRepository availedTvServiceRepo;

    public java.util.List<TvServiceAvailed> getActiveSubscribedServices(Integer userId) {
        return availedTvServiceRepo.findByUserIdAndActiveTrue(userId);
    }
    
    public void deactivateService(Integer serviceId, LocalDate startDate) throws Exception {
        Integer userId = authService.getCurrentUserDetails().getUserId();
        TvServicesAvailedId tvServicesAvailedId = new TvServicesAvailedId();
        tvServicesAvailedId.setUserId(userId);
        tvServicesAvailedId.setStartDate(startDate);
        tvServicesAvailedId.setServiceId(serviceId);
        TvServiceAvailed availedTvService = availedTvServiceRepo
                .findById(tvServicesAvailedId)
                .orElseThrow(() -> new ResourceNotFoundException("AvailedTVService with id " + tvServicesAvailedId + " doesn't exists"));
        availedTvService.setActive(false);
        availedTvServiceRepo.save(availedTvService);
    }
}

package com.ippl.difability.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.request.HumanResourceProfileRequest;
import com.ippl.difability.dto.response.user.HumanResourceDetailResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.CompanyRepository;
import com.ippl.difability.repository.HumanResourceRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HumanResourceService {
    private final HumanResourceRepository humanResourceRepository;
    private final CompanyRepository companyRepository;
    private final LogService logService;

    public HumanResourceDetailResponse getHrProfile(Long id){
        HumanResource humanResource = humanResourceRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        return new HumanResourceDetailResponse(
            humanResource.getFullName(),
            humanResource.getContact(),
            humanResource.getPpImagePath()
        );
    }

    public HumanResourceDetailResponse getMyProfile(String username){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        return new HumanResourceDetailResponse(
            humanResource.getFullName(),
            humanResource.getContact(),
            humanResource.getPpImagePath()
        );
    }

    public void updateHumanResourceProfile(String username, HumanResourceProfileRequest request){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        validateHumanResourceInput(humanResource, request);
        humanResourceRepository.save(humanResource);

        logService.log(
            username,
            humanResource.getRole().name(),
            "UPDATE_PROFILE"
        );
    }

    public void deleteHumanResource(String username, Long hrId){
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        HumanResource humanResource = humanResourceRepository.findById(hrId)
            .orElseThrow(UserNotFoundException::new);
        
        if(!company.getId().equals(humanResource.getCompany().getId())){
            throw new ForbiddenException();
        }

        logService.log(
            username,
            company.getRole().name(),
            "DELETE_HR"
        );

        humanResourceRepository.delete(humanResource);
    }

    private void validateHumanResourceInput(HumanResource humanResource, HumanResourceProfileRequest request){
        if(request.fullName() != null) humanResource.setFullName(request.fullName());
        if(request.contact() != null) humanResource.setContact(request.contact());
        if(request.ppImagePath() != null) humanResource.setPpImagePath(request.ppImagePath());
    }
}

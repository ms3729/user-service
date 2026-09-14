package com.rata.userService.services.impl;

import com.rata.userService.dto.responseFiltering.PageResponse;
import com.rata.userService.enums.RelationType;
import com.rata.userService.errorHandling.ResourceNotFoundException;
import com.rata.userService.mappers.EmployeeMapper;
import com.rata.userService.models.User;
import com.rata.userService.models.docs.EmployeeGrid;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.*;
import com.rata.userService.repositories.mongodb.employee.EmployeeGridRepository;
import com.rata.userService.repositories.mysql.PartyRepository;
import com.rata.userService.services.interfaces.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final PartyRepository partyRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeGridRepository employeeGridRepository;
    private final PartyService partyService;
    private final PersonService personService;
    private final PersonTranslationService partyTranslationService;
    private final PartyIdentifierService partyIdentifierService;
    private final PartyContactService partyContactService;
    private final MembershipService membershipService;
    private final UserService userService;


    // ==================== Create ====================

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        // ۱. ساخت Party
        Party party = partyService.save(request);

        // ۲. ساخت PersonProfile
        personService.save(party, request);

        // ۳. ذخیره ترجمه‌ها
        if (request.translations() != null) {
            partyTranslationService.save(party, request);
        }

        // ۴. ذخیره شناسه‌ها
        if (request.identifiers() != null) {
            partyIdentifierService.save(party, request);
        }

        // ۵. ذخیره تماس‌ها
        if (request.contacts() != null) {
            partyContactService.save(party, request);
        }

        // ۶. ذخیره عضویت‌ها در شرکت‌ها
        if (request.organizations() != null) {
            membershipService.save(party, request);
        }

        User user = userService.createUserForParty(party);
        // ۷. برگرداندن نتیجه
        Party savedParty = partyRepository.findByIdWithDetails(party.getId())
                .orElseThrow(() -> new ResourceNotFoundException("کارمند یافت نشد"));

        return employeeMapper.toResponse(savedParty).withUser(user);
    }

    // ==================== Update ====================

    @Override
    public EmployeeResponse updateEmployee(long partyId, UpdateEmployeeRequest request) {
        Party party = getEmployeeParty(partyId);

        // به‌روزرسانی فیلدهای پایه
        party = partyService.update(party, request);

        // به‌روزرسانی پروفایل شخص
        if (party.getPerson() != null) {
            personService.update(party, request);
        }

        // به‌روزرسانی ترجمه‌ها
        if (request.translations() != null) {
            partyTranslationService.save(party, request);
        }

        Party updatedParty = partyRepository.findByIdWithDetails(partyId)
                .orElseThrow(() -> new ResourceNotFoundException("کارمند یافت نشد"));

        return employeeMapper.toResponse(updatedParty);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<EmployeeGrid> grid(EmployeeSearchCriteria filter, Pageable pageable) {
        Page<EmployeeGrid> grid = employeeGridRepository.searchEmployees(filter, pageable);
        return toPageResponse(grid);
    }

    public PageResponse<EmployeeGrid> toPageResponse(Page<EmployeeGrid> page) {
        PageResponse<EmployeeGrid> pageResponse = new PageResponse<>();
        pageResponse.setContent(page.getContent());
        pageResponse.setPage(page.getNumber());
        pageResponse.setSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalSize(page.getTotalElements());
        return pageResponse;
    }
    // ==================== Get ====================

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployee(Long partyId) {
        Party party = getEmployeeParty(partyId);
        return employeeMapper.toResponse(party);
    }

    // ==================== Delete ====================

    @Override
    public void deleteEmployee(long partyId) {
        Party party = getEmployeeParty(partyId);

        // حذف وابستگی‌ها
        partyTranslationService.deleteByPartyId(partyId);

        partyContactService.deleteByPartyId(partyId);

        membershipService.deleteEmployeeByPartyId(partyId);

        personService.deleteByPartyId(partyId);

        partyRepository.delete(party);
    }

    // ==================== Status ====================

    @Override
    public EmployeeResponse updateStatus(long partyId, boolean status) {
        Party party = getEmployeeParty(partyId);
        party.setEnabled(status);
        partyRepository.save(party);

        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    // ==================== Company Membership ====================

    @Override
    public EmployeeResponse assignToOrganization(long partyId, AssignOrganizationRequest request) {
        Party party = getEmployeeParty(partyId);
        membershipService.save(party, request);
        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    @Override
    public EmployeeResponse removeFromOrganization(long partyId, long organizationId) {
        getEmployeeParty(partyId);
        membershipService.removeFromOrganization(partyId, organizationId);

        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    // ==================== Translations ====================

    @Override
    public EmployeeResponse upsertTranslation(Long partyId, UpsertTranslationRequest request) {
        Party party = getEmployeeParty(partyId);
        partyTranslationService.save(party, request);
        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    @Override
    public EmployeeResponse deleteTranslation(long partyId, String languageCode) {
        getEmployeeParty(partyId);
        partyTranslationService.deleteByPartyIdAndLang(partyId, languageCode);

        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    // ==================== Contacts ====================

    @Override
    public EmployeeResponse addContact(long partyId, AddContactRequest request) {
        Party party = getEmployeeParty(partyId);
        partyContactService.save(party, request);

        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    @Override
    public EmployeeResponse removeContact(long partyId, long contactId) {
        getEmployeeParty(partyId);
        partyContactService.delete(contactId);

        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    // ==================== Identifiers ====================

    @Override
    public EmployeeResponse addIdentifier(long partyId, AddIdentifierRequest request) {
        Party party = getEmployeeParty(partyId);
        partyIdentifierService.save(party, request);

        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    @Override
    public EmployeeResponse removeIdentifier(long partyId, long identifierId) {
        getEmployeeParty(partyId);
        partyIdentifierService.delete(identifierId);

        return employeeMapper.toResponse(
                partyRepository.findByIdWithDetails(partyId).orElseThrow());
    }

    // ==================== Private Helpers ====================

    private Party getEmployeeParty(long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "کارمند با شناسه " + partyId + " یافت نشد"));
        membershipService.isActiveEmployee(partyId, RelationType.EMPLOYEE);
        return party;
    }

}
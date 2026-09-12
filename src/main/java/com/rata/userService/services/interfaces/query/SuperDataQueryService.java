package com.rata.userService.services.interfaces.query;

import com.rata.userService.records.superData.BankRecord;
import com.rata.userService.records.superData.ZoneRecord;

public interface SuperDataQueryService {

    BankRecord findBankById(int id);

    BankRecord findBankByCode(String code);

    ZoneRecord findCityById(int id);

    ZoneRecord findStateById(int id);

    ZoneRecord findCountryById(int id);
}

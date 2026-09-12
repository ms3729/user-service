package com.rata.userService.records;

import java.util.List;

public record UserMenuRecord(long id, String name, String icon, String url,String app,String component,
                             String translationKey, String permission,
                             List<UserMenuRecord> childes) {
}

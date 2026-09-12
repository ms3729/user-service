package com.rata.userService.controllers;

import com.rata.userService.dto.VerificationDTO;
import com.rata.userService.records.ResponseResult;
import com.rata.userService.services.interfaces.command.VerificationCommandService;
import com.rata.userService.services.interfaces.query.VerificationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/verification")
@Tag(name = "Verification APIs")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationQueryService verificationQueryService;
    private final VerificationCommandService verificationCommandService;

    @GetMapping("/identifier-code/{identifierCode}/sms-code/{smsCode}/data/{data}")
    public ResponseEntity<?> checkCode(@PathVariable String identifierCode,
                                       @PathVariable String smsCode,
                                       @PathVariable String data,
                                       @RequestParam(name = "carry_cargo_count", required = false) Integer carryCargoCount) {
        if (carryCargoCount != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bundle_no", data);
            jsonObject.put("carry_cargo_count", carryCargoCount);
            data = jsonObject.toString();
        }
        return new ResponseEntity<>(new ResponseResult("", verificationQueryService.checkVerificationCode(identifierCode, smsCode, data)), HttpStatus.OK);
    }

    @Operation(summary = "send verificationCode")
    @PostMapping(value = "/send")
    public ResponseEntity<?> sendVerificationCode(@RequestBody VerificationDTO dto) {
        String verificationCode = verificationQueryService.checkValidityResendSms(dto.getData());
        if (verificationCode != null) {
            return new ResponseEntity<>(new ResponseResult("error.resend_verification_send_timeout", verificationCode), HttpStatus.TOO_MANY_REQUESTS);
        }
        return new ResponseEntity<>(new ResponseResult("", verificationCommandService.save(dto)), HttpStatus.CREATED);
    }

}

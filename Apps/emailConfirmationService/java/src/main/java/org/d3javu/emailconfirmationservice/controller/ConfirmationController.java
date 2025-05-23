package org.d3javu.emailconfirmationservice.controller;

import lombok.RequiredArgsConstructor;
import org.d3javu.emailconfirmationservice.service.EmailConfirmationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/confirmation")
@RequiredArgsConstructor
public class ConfirmationController {

    private final EmailConfirmationService emailConfirmationService;

    @GetMapping
    public String confirmEmail(@RequestParam String token){
        if (this.emailConfirmationService.confirmEmailByToken(token)) return "SuccessTemplate";
        return "ErrorTemplate";
    }

}

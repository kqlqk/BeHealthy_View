package me.kqlqk.behealthy.view.controller;

import me.kqlqk.behealthy.view.dto.auth.LoginDTO;
import me.kqlqk.behealthy.view.dto.auth.RegistrationDTO;
import me.kqlqk.behealthy.view.dto.auth.UserAuthDTO;
import me.kqlqk.behealthy.view.feign_client.AuthenticationClient;
import me.kqlqk.behealthy.view.feign_client.GatewayClient;
import me.kqlqk.behealthy.view.model.AuthInfo;
import me.kqlqk.behealthy.view.service.AuthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class GuestController {
    private final GatewayClient gatewayClient;
    private final AuthenticationClient authenticationClient;
    private final AuthInfoService authInfoService;

    @Autowired
    public GuestController(GatewayClient gatewayClient, AuthenticationClient authenticationClient, AuthInfoService authInfoService) {
        this.gatewayClient = gatewayClient;
        this.authenticationClient = authenticationClient;
        this.authInfoService = authInfoService;
    }

    @GetMapping
    public String getMainPage(HttpServletRequest request) {
        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());

        if (authInfo != null) {
            return "redirect:/me/" + authInfo.getUserId();
        }

        return "guest/MainPage";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());

        return "guest/LoginPage";
    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute("loginDTO") @Valid LoginDTO loginDTO,
                        BindingResult bindingResult,
                        Model model,
                        HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "guest/LoginPage";
        }

        ResponseEntity<?> responseOfGateway;
        try {
            responseOfGateway = gatewayClient.logIn(loginDTO);
        } catch (RuntimeException e) {
            model.addAttribute("exception", e.getMessage());
            return "guest/LoginPage";
        }

        HttpHeaders headers = responseOfGateway.getHeaders();
        String accessToken;
        String refreshToken;
        try {
            accessToken = headers.get("Authorization_access").get(0);
            refreshToken = headers.get("Authorization_refresh").get(0);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return "redirect:/login";
        }

        UserAuthDTO userAuthDTO = authenticationClient.getUserByEmail(loginDTO.getEmail());

        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());
        if (authInfo != null) {
            authInfo.setUserId(userAuthDTO.getId());
            authInfo.setAccessToken(accessToken);
            authInfo.setRefreshToken(refreshToken);
        } else {
            authInfo = new AuthInfo(userAuthDTO.getId(), request.getRemoteAddr(), accessToken, refreshToken);
        }

        authInfoService.saveOrUpdate(authInfo);

        return "redirect:/me/" + userAuthDTO.getId();
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());

        return "guest/RegistrationPage";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
                               BindingResult bindingResult,
                               Model model,
                               HttpServletRequest request) {
        if (bindingResult.hasErrors() || !registrationDTO.getConfirmPassword().equals(registrationDTO.getPassword())) {
            return "guest/RegistrationPage";
        }

        ResponseEntity<?> responseOfGateway;
        try {
            responseOfGateway = gatewayClient.registration(registrationDTO);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User with email = " + registrationDTO.getEmail() + " already exists")) {
                model.addAttribute("emailException", "Email already taken");
            }
            return "guest/RegistrationPage";
        }

        HttpHeaders headers = responseOfGateway.getHeaders();
        String accessToken;
        String refreshToken;
        try {
            accessToken = headers.get("Authorization_access").get(0);
            refreshToken = headers.get("Authorization_refresh").get(0);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return "redirect:/registration";
        }

        UserAuthDTO userAuthDTO = authenticationClient.getUserByEmail(registrationDTO.getEmail());

        AuthInfo authInfo = authInfoService.getByRemoteAddr(request.getRemoteAddr());
        if (authInfo != null) {
            authInfo.setUserId(userAuthDTO.getId());
            authInfo.setAccessToken(accessToken);
            authInfo.setRefreshToken(refreshToken);
        } else {
            authInfo = new AuthInfo(userAuthDTO.getId(), request.getRemoteAddr(), accessToken, refreshToken);
        }

        authInfoService.saveOrUpdate(authInfo);

        return "redirect:/me/" + userAuthDTO.getId();
    }

}

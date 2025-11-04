package br.com.fiap.api.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {
    @GetMapping("/login")
    public String carregapagina(){
        return "autenticacao/login";

    }
}

package com.esprit.summer.Controller;

import com.esprit.summer.Entities.Client;
import com.esprit.summer.Services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/Client")
public class ClientController {
    @Autowired
    ClientService clientService;

    @PostMapping("/ADDClient")
    public void addClient(@RequestBody Client client) {
        clientService.addAclient(client);
    }


}

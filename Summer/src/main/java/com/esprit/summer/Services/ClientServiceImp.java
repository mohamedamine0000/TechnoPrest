package com.esprit.summer.Services;

import com.esprit.summer.Entities.Client;
import com.esprit.summer.Entities.Cuisinier;
import com.esprit.summer.Repositories.ClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ClientServiceImp implements ClientService {
    @Autowired
    ClientRepo clientRepo;
    @Override
    public List<Client> getClients(){
    return clientRepo.findAll();
    }

    @Override
    public Client getClient(int id) {
        return clientRepo.findById(id).get();
    }



    @Override
    public List<Client> getClientsByName(String nom) {
        return clientRepo.findAllByNom(nom);
    }

    @Override
    public List<Client> getClientsByLastName(String prenom) {
        return clientRepo.findAllByPrenom(prenom);
    }

    @Override
    public void addAclient(Client client) {
        clientRepo.save(client);
    }

    @Override
    public Client modifyClient(Client client) {
        return clientRepo.save(client);
    }

    @Override
    public void deleteClient(int id) {
        clientRepo.deleteById(id);
    }

    ;


}

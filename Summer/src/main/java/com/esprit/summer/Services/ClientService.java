package com.esprit.summer.Services;

import com.esprit.summer.Entities.Client;
import com.esprit.summer.Entities.Cuisinier;

import java.util.List;

public interface ClientService {
    public List<Client> getClients();
    public Client getClient(int id);
    public List<Client> getClientsByName(String nom);
    public List<Client> getClientsByLastName(String prenom);
    public void addAclient(Client client);
    public Client modifyClient(Client client);
    public void deleteClient(int id);

}

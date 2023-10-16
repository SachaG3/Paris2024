package fr.normanbet.paris.p2024.services;

import fr.normanbet.paris.p2024.repositories.OlympiadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OlympiadService {
    @Autowired
    private OlympiadRepository olympiadRepository;
}


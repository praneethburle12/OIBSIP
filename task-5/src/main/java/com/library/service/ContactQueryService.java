package com.library.service;

import com.library.model.ContactQuery;
import com.library.model.User;
import com.library.repository.ContactQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactQueryService {

    @Autowired
    private ContactQueryRepository contactQueryRepository;

    public ContactQuery saveQuery(ContactQuery query) {
        return contactQueryRepository.save(query);
    }

    public List<ContactQuery> getAllQueries() {
        return contactQueryRepository.findAll();
    }

    public List<ContactQuery> getQueriesByStatus(String status) {
        return contactQueryRepository.findByStatus(status);
    }

    public List<ContactQuery> getQueriesByUser(User user) {
        return contactQueryRepository.findByUser(user);
    }

    public Optional<ContactQuery> getQueryById(Long id) {
        return contactQueryRepository.findById(id);
    }

    public void updateStatus(Long id, String status) {
        contactQueryRepository.findById(id).ifPresent(query -> {
            query.setStatus(status);
            contactQueryRepository.save(query);
        });
    }

    public void deleteQuery(Long id) {
        contactQueryRepository.deleteById(id);
    }
}

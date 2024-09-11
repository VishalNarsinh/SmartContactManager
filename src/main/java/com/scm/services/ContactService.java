package com.scm.services;

import com.scm.entities.Contact;
import com.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    public Contact saveContact(Contact contact, User user, MultipartFile file);

    public Contact updateContact(Contact contact, User user, MultipartFile file);

    public void deleteContact(int contactId);

    public Contact getContactByContactId(int contactId);

    public Page<Contact> getContactsByUser(User user,int pageNumber,int pageSize,String sortBy,String sortOrder );

    public Page<Contact> search(User user,String field, String value,int pageNumber,int pageSize,String sortBy,String sortOrder);
}

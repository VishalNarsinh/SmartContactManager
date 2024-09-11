package com.scm.services.impl;

import com.cloudinary.Cloudinary;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.exceptions.ResourceNotFoundException;
import com.scm.helper.AppConstants;
import com.scm.repositories.ContactRepository;
import com.scm.services.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ContactServiceImpl implements ContactService {


    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);
    private final ImageServiceImpl imageServiceImpl;
    private final ContactRepository contactRepository;

//    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, ImageServiceImpl imageServiceImpl) {
        this.contactRepository = contactRepository;
        this.imageServiceImpl = imageServiceImpl;
    }


    @Override
    public Contact saveContact(Contact contact, User user, MultipartFile file) {
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        if (file != null && !file.isEmpty()) {
            String filename = UUID.randomUUID().toString();
            String imageUrl = imageServiceImpl.uploadImage1(file, AppConstants.CONTACT_IMAGE_FOLDER, filename);
            contact.setPicture(imageUrl);
            contact.setImagePublicId(AppConstants.CONTACT_IMAGE_FOLDER + "/" + filename);

        } else {
            String imageUrl = imageServiceImpl.getUrlFromPublicId(AppConstants.DEFAULT_CONTACT_IMAGE_ID);
            contact.setPicture(imageUrl);
            contact.setImagePublicId(AppConstants.DEFAULT_CONTACT_IMAGE_ID);
        }
        contact.setUser(user);
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact, User user, MultipartFile file) {
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Contact oldContact = contactRepository.findById(contact.getContactId()).orElseThrow(()->new ResourceNotFoundException("Contact not found with contactId" + contact.getContactId()));
        if (file != null && !file.isEmpty()) {
            imageServiceImpl.deleteImage(oldContact.getImagePublicId());
            String filename = UUID.randomUUID().toString();
            String imageUrl = imageServiceImpl.uploadImage(file, AppConstants.CONTACT_IMAGE_FOLDER, filename);
            contact.setPicture(imageUrl);
            contact.setImagePublicId(AppConstants.CONTACT_IMAGE_FOLDER + "/" + filename);
        }
        else{
            contact.setPicture(oldContact.getPicture());
            contact.setImagePublicId(oldContact.getImagePublicId());
        }
        contact.setUser(user);
        return contactRepository.save(contact);
    }

    @Override
    public void deleteContact(int contactId) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new ResourceNotFoundException("Contact not found with contact id " + contactId));
        contactRepository.delete(contact);
        imageServiceImpl.deleteImage(contact.getImagePublicId());
    }


    @Override
    public Contact getContactByContactId(int contactId) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new ResourceNotFoundException("Contact not found with contact id " + contactId));
        if(!contact.getImagePublicId().equals(AppConstants.DEFAULT_CONTACT_IMAGE_ID)) {
            contact.setPicture(imageServiceImpl.getUrlFromPublicIdWithoutFolder(contact.getImagePublicId()));
        }
        return contact;
    }


    @Override
    public Page<Contact> getContactsByUser(User user,int pageNumber,int pageSize,String sortBy,String sortOrder) {
//        return contactRepository.findAllByUser_UserId(user.getUserId());
        Sort sort = sortOrder.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,sort);
        return contactRepository.findByUser(user,pageRequest);
    }

    @Override
    public Page<Contact> search(User user, String field, String value,int pageNumber,int pageSize,String sortBy,String sortOrder) {
        Page<Contact> contactPage = new PageImpl<Contact>(List.of());
//        by default using whatever field selected for sorting
        Sort sort = sortOrder.equalsIgnoreCase("DESC") ? Sort.by(field).descending() : Sort.by(field).ascending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        contactPage = switch (field) {
            case "name" -> contactRepository.findByUserAndNameContainingIgnoreCase(user, value, pageRequest);
            case "email" -> contactRepository.findByUserAndEmailContainingIgnoreCase(user, value, pageRequest);
            case "phoneNumber" ->
                    contactRepository.findByUserAndPhoneNumberContainingIgnoreCase(user, value, pageRequest);
            default -> contactPage;
        };
        return contactPage;
    }


    public List<Contact> getContactsByName(String name) {
        return contactRepository.findAllByNameContaining(name);
    }
}

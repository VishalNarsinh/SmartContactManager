package com.scm.repositories;

import com.scm.entities.Contact;
import com.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ContactRepository extends JpaRepository<Contact,Integer> {
    List<Contact> findAllByUser_UserId(int userId);

    Page<Contact> findByUser(User user, Pageable pageable);

    List<Contact> findAllByNameContaining(String name);

    @Query(value = "select * from contact where user_id=?1 and ?2=?3",nativeQuery = true)
    List<Contact> customSearch(String userId,String field,String value);

    Page<Contact> findByUserAndNameContainingIgnoreCase(User user, String name,Pageable pageable);

    Page<Contact> findByUserAndEmailContainingIgnoreCase(User user, String email,Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContainingIgnoreCase(User user, String phoneNumber,Pageable pageable);
}

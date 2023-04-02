/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ogive.oheo.persistence.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ogive.oheo.persistence.model.Book;

/**
 *
 * @author Vishnu Awasthi Vishnuawasthi121@gmail.com
 *
 */
public interface BookRepository extends CrudRepository<Book, Long> {

	List<Book> findByTitle(String title);

	//Optional<Book> findOne(long id);
}

package com.dxc.libraryapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dxc.libraryapi.entity.Books;

@Repository
public interface BookRepository extends JpaRepository<Books, Integer> {

	Books findByTitle(String title);

	List<Books> findAllByPublishDate(LocalDate publishDate);

}
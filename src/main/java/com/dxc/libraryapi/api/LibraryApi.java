package com.dxc.libraryapi.api;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dxc.libraryapi.entity.Books;
import com.dxc.libraryapi.exception.BookException;
import com.dxc.libraryapi.service.BooksService;

@RestController
@RequestMapping("/books")
public class LibraryApi {

	@Autowired
	private BooksService booksService;

	@GetMapping
	public ResponseEntity<List<Books>> getAllBooks() {
		return new ResponseEntity<List<Books>>(booksService.getAllBooks(), HttpStatus.OK);
	}

	@GetMapping("/{bookId:[0-9]{1,5}}")
	public ResponseEntity<Books> getBookById(@PathVariable("bookId") int bcode) {
		ResponseEntity<Books> response = null;

		Books book = booksService.getById(bcode);

		if (book == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(book, HttpStatus.OK);
		}

		return response;
	}

	@GetMapping("/{title:[A-Za-z]{5,45}}")
	public ResponseEntity<Books> getItemByTitle(@PathVariable("title") String title) {
		ResponseEntity<Books> response = null;

		Books book = booksService.findByTitle(title);

		if (book == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(book, HttpStatus.OK);
		}

		return response;
	}

	@GetMapping("/{pDate:[0-9]{4}-[0-9]{2}-[0-9]{2}}")
	public ResponseEntity<List<Books>> getBookByPublishDate(
			@PathVariable("pDate") @DateTimeFormat(iso = ISO.DATE) LocalDate publishDate) {
		ResponseEntity<List<Books>> response = null;

		List<Books> books = booksService.findAllByPublishDate(publishDate);

		if (books == null || books.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(books, HttpStatus.OK);
		}

		return response;
	}

	/*
	 * @GetMapping("/priceBetween/{lbound}/and/{ubound}") public
	 * ResponseEntity<List<Item>> getItemByPriceRange(@PathVariable("lbound") double
	 * lBound,
	 * 
	 * @PathVariable("ubound") double uBound) { ResponseEntity<List<Item>> response
	 * = null;
	 * 
	 * List<Item> items = itemService.findInPriceRange(lBound, uBound);
	 * 
	 * if (items == null || items.size() == 0) { response = new
	 * ResponseEntity<>(HttpStatus.NOT_FOUND); } else { response = new
	 * ResponseEntity<>(items, HttpStatus.OK); }
	 * 
	 * return response; }
	 */

	@PostMapping
	public ResponseEntity<Books> createItem(@Valid @RequestBody Books book, BindingResult result) throws BookException {
		ResponseEntity<Books> response = null;

		if (result.hasErrors()) {
			StringBuilder errMsg = new StringBuilder();
			for (FieldError err : result.getFieldErrors()) {
				errMsg.append(err.getDefaultMessage() + ",");
			}
			throw new BookException(errMsg.toString());
		} else {
			booksService.add(book);
			response = new ResponseEntity<>(book, HttpStatus.OK);
		}

		return response;
	}

	@PutMapping
	public ResponseEntity<Books> updateItem(@Valid @RequestBody Books book, BindingResult result) throws BookException {
		ResponseEntity<Books> response = null;

		if (result.hasErrors()) {
			StringBuilder errMsg = new StringBuilder();
			for (FieldError err : result.getFieldErrors()) {
				errMsg.append(err.getDefaultMessage() + ",");
			}
			throw new BookException(errMsg.toString());
		} else {
			booksService.update(book);
			response = new ResponseEntity<>(book, HttpStatus.OK);
		}

		return response;
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBooksById(@PathVariable("bookId") int bcode) throws BookException {
		booksService.deleteById(bcode);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

package com.vallacartelera.app.controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vallacartelera.app.models.Genre;
import com.vallacartelera.app.models.Movie;
import com.vallacartelera.app.services.IGenreService;
import com.vallacartelera.app.services.IMovieService;

@Controller
public class GenreController {
	@Autowired
	private IMovieService movieService;
	
	@Autowired
	private IGenreService genreService;

	protected final Log logger = LogFactory.getLog(getClass());

	@GetMapping(path = "/genres/{id}")
	public String list(@PathVariable(name = "id") Long genreId, Model model) {
		
		Genre genre = genreService.findById(genreId);
		
		List<Movie> movies = movieService.findByGenreId(genreId);
		
		model.addAttribute("genre", genre);
		model.addAttribute("movies", movies);
		model.addAttribute("title", genre.getName());
		return "genre/filmsByGenre";
	}
}

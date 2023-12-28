package com.vallacartelera.app.controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vallacartelera.app.models.Cinema;
import com.vallacartelera.app.models.Movie;
import com.vallacartelera.app.services.ICinemaService;
import com.vallacartelera.app.services.IMovieService;

@Controller
public class MovieController {
	@Autowired
	private IMovieService movieService;
	
	@Autowired
	private ICinemaService cinemaService;

	protected final Log logger = LogFactory.getLog(getClass());

	@GetMapping(path = "/movies/{id}")
	public String list(@PathVariable(name = "id") Long movieId, Model model) {
		
		Movie movie = movieService.findById(movieId);
		
		List<Cinema> cinemas = cinemaService.findAllByMovieId(movieId);
		
		model.addAttribute("cinemas", cinemas);
		model.addAttribute("movie", movie);
		model.addAttribute("title", movie.getTitle());
		return "movie/movieData";
	}
}

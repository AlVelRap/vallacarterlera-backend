package com.vallacartelera.app.controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vallacartelera.app.models.Actor;
import com.vallacartelera.app.models.Movie;
import com.vallacartelera.app.services.IActorService;
import com.vallacartelera.app.services.IMovieService;

@Controller
public class ActorController {
	@Autowired
	private IMovieService movieService;
	
	@Autowired
	private IActorService actorService;

	protected final Log logger = LogFactory.getLog(getClass());

	@GetMapping(path = "/actors/{id}")
	public String list(@PathVariable(name = "id") Long actorId, Model model) {
		
		Actor actor = actorService.findById(actorId);
		
		List<Movie> movies = movieService.findByActorId(actorId);
		
		model.addAttribute("actor", actor);
		model.addAttribute("movies", movies);
		model.addAttribute("title", actor.getName());
		return "genre/filmsByGenre";
	}
}

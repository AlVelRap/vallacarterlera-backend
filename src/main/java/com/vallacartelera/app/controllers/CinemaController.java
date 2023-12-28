package com.vallacartelera.app.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vallacartelera.app.models.Cinema;
import com.vallacartelera.app.models.Session;
import com.vallacartelera.app.services.ICinemaService;
import com.vallacartelera.app.services.ISessionService;

@Controller
//@RequestMapping("/cinemas")
public class CinemaController {

	@Autowired
	private ICinemaService cinemaService;

	@Autowired
	private ISessionService sessionService;

	protected final Log logger = LogFactory.getLog(getClass());

	@GetMapping(path = "/cinemas/list")
	public String list(Model model) {
		List<Cinema> cinemas = cinemaService.findAll();
		model.addAttribute("cinemas", cinemas);
		model.addAttribute("title", "Cinema's List");
		return "cinemaList";
	}

	@GetMapping(path = "/cinemas/{id}/sessions")
	public String listSessionsForCinema(@PathVariable(name = "id") Long cinemaId, Map<String, Object> model) {
		Cinema cinema = cinemaService.findById(cinemaId);
		
		model.put("cinema", cinema);
		model.put("title", "Vallacartelera: " + cinema.getName());
		
		List<List<Session>> movies = new ArrayList<>();
		Set<Long> listMoviesId = new HashSet<Long>();
		
		for (Session sess : cinema.getSessions()) {
			listMoviesId.add(sess.getMovie().getId());
		}
		
		for (Long movieId : listMoviesId) {
			movies.add(sessionService.findSessionsForMovieAndForCinema(cinemaId, movieId));
		}
		
		model.put("movies", movies);
		
		return "cinemaSessions";
	}

}

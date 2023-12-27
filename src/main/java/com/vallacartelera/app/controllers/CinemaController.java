package com.vallacartelera.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vallacartelera.app.models.Cinema;
import com.vallacartelera.app.models.Session;
import com.vallacartelera.app.services.ICinemaService;

@Controller
@RequestMapping("/cinemas")
public class CinemaController {

	@Autowired
	private ICinemaService cinemaService;
	
	protected final Log logger = LogFactory.getLog(getClass());

	@GetMapping(path = "/list")
	public String list(Model model) {
		List<Cinema> cinemas = cinemaService.findAll();
		model.addAttribute("cinemas", cinemas);
		model.addAttribute("title", "Cinema's List");
		return "cinemaList";
	}

}

package com.ascend.acm.hackaton.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ascend.acm.hackaton.model.Nearby;

@Controller
public class NearbyController {

	@Autowired
	ServletContext context;

	@SuppressWarnings("unchecked")
	@MessageMapping("/nearby")
	@SendTo("/topic/nearby")
	public List<Nearby> nearby(Nearby nearby) throws Exception {
		List<Nearby> nearbies = (List<Nearby>) context.getAttribute("nearbies");
		nearbies = (null == nearbies) ? new ArrayList<Nearby>() : nearbies;
		if (!nearby.getDisplayName().equals("") && !nearby.getMobileNo().equals("")) {
			nearbies.add(nearby);
		}
		// Get distinct only
		List<Nearby> distinctElements = nearbies.stream().filter(distinctByKey(p -> p.getMobileNo()))
				.collect(Collectors.toList());

		context.setAttribute("nearbies", nearbies);

		return distinctElements;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}

package io.seyon.apigateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

	@Autowired
	CacheManager cm;
	
	
	private static final Logger log = LoggerFactory.getLogger(GeneralController.class);

	@GetMapping("/clearCache")
	public ResponseEntity<String> clearCache(){
		cm.getCacheNames().forEach(chnm->{
			log.info("Clearing {}",chnm);
			Cache ch=cm.getCache(chnm);
			ch.clear();
			log.info("Cleared {}",chnm);
		});
		return new ResponseEntity<String>("cache cleared", HttpStatus.OK);
	}
	
}

package com.ogive.oheo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.dto.MemoryStats;

@RestController
public class AppHealthCheckController {

	@GetMapping(value = "/memory")
	public ResponseEntity<Object> getMemoryStatistics() {
		MemoryStats stats = new MemoryStats();
		stats.setTotalMemory(Runtime.getRuntime().totalMemory()/(1024*1024*1024));
		stats.setMaxMemory(Runtime.getRuntime().maxMemory()/(1024*1024*1024));
		stats.setFreeMemory(Runtime.getRuntime().freeMemory()/(1024*1024*1024));
		return new ResponseEntity<Object>(stats, HttpStatus.OK);
	}
}

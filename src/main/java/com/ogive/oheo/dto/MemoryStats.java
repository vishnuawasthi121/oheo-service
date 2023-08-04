package com.ogive.oheo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemoryStats {

	private long totalMemory;
	private long maxMemory;
	private long freeMemory;
	
}

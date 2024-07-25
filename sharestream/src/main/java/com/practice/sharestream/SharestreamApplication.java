package com.practice.sharestream;

import com.practice.sharestream.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SharestreamApplication implements CommandLineRunner {

	@Autowired
	private FileService fileService;

	public static void main(String[] args) {
		SpringApplication.run(SharestreamApplication.class, args);
	}

	//Initialize Storage on Startup
	@Override
	public void run(String... args) throws Exception {
		fileService.init();
	}
}


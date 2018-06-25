package com.danielcotter.swingit;

import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.danielcotter.swingit.controller.MainController;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Main {

	public static void main(String args[]) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		ConfigurableApplicationContext context = SpringApplication.run(Main.class);
		((MainController) context.getBean("mainController")).init();
	}
}
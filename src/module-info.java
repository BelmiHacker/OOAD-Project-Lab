module OOADLabProject {
	requires transitive java.sql;
	requires java.desktop;
	requires javafx.controls;
	requires javafx.graphics;

	exports model;
	exports database;
	exports main;
	exports controller;
	exports view;
}
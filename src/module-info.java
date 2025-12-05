module OOADLabProject {
	requires transitive java.sql;
	requires transitive java.desktop;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
	requires transitive javafx.fxml;

	exports model;
	exports database;
	exports main;
	exports controller;
	exports view;
}
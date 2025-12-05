module OOADLabProject {
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.controls;

	
	opens main to javafx.graphics;
	exports main;
}
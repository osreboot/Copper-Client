package com.osreboot.copper.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;

import com.osreboot.ridhvl2.menu.HvlFont;
import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;

public class Main extends HvlTemplateI{

	public static void main(String[] args){
		new Main();
	}
	
	public Main(){
		super(new HvlDisplayWindowed(144, 1280, 720, "Project Copper | Client", true));
	}

	@Override
	public void initialize(){
		hvlLoad("INOF.hvlft");
		hvlFont(0).set(HvlFont.TAG_X_SPACING, 32f);
		
//		Resources.load();
		
//		Menu.initialize();
	}

	@Override
	public void update(float delta){
//		Menu.update(delta);
	}

}

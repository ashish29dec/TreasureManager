/**
 * 
 */
package com.foodtreasure.account.treasuremanager;

import android.app.Application;
import android.content.Context;

/**
 * @author agupta
 *
 */
public class TreasureApplication extends Application {

	public static Context context;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
	}
}

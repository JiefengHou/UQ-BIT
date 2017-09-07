package 
{
	import flash.display.*;
	import flash.events.*;
	import flash.text.*;
	import flash.ui.Mouse;
	import flash.utils.Timer;
	
	public class theNewObject extends MovieClip
	{
		var place:String;
		
		public function theNewObject()
		{
		}
		
		//This function is that create a new red heart at the specified position
		public function setPlace(obj:Object)
		{
			place = getPlace();
			if(place == "window1")
			{
				obj.x=210;
				obj.y=170;
			}
			
			else if(place == "window2")
			{
				obj.x=510;
				obj.y=170;			
			}
			
			else if(place == "window3")
			{
				obj.x=810;
				obj.y=170;			
			}
			
			else if(place == "window4")
			{
				obj.x=210;
				obj.y=425;				
			}
			
			else if(place == "window5")
			{
				obj.x=810;
				obj.y=425;			
			}
			
			else
			{
				obj.x=510;
				obj.y=495;				
			}
		}
		
		//This function is to decide to object will appear in which windows and door 
		public function getPlace()
		{
			if (Math.random() < .16) 
			{
				return "window1";		
			}
			
			else if (Math.random() >= .16 && Math.random() < .32)
			{
				return "window2";
			}
			
			else if (Math.random() >= .32 && Math.random() < .48)
			{
				return "window3";
			}
			
			else if (Math.random() >= .48 && Math.random() < .64)
			{
				return "window4";
			}
			
			else if (Math.random() >= .64 && Math.random() < .80)
			{
				return "window5";
			}

			else 
			{
				return "door";	
			}			
		}
		

	}
	
}
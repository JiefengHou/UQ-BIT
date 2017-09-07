package 
{
	import flash.display.*;
	import flash.events.*;
	import flash.text.*;
	import flash.ui.Mouse;
	import flash.utils.Timer;
	import fl.text.ruler.RulerMarker;


	public class main extends MovieClip
	{
		
		var theAim;
		var theEnemy;
		var thePeople;
		var theHeart;
		var theHit;
		var theBack;
		var theLevel;
		var theRule;
		var theGun;
		var theControl;
		var theGameOver;
		var theGunFire;
		var timer:Timer;
		var time:Number;
		var clickState:Boolean;
		var newTimer:Timer;
		var objectDisappearTimer:Timer;
		var place:String;
		var score:Number;
		var object:Number;
		var level:Number;
		var heartNumber:Number;
		var removeTimer:Timer;
		var removeGameInfor:Timer;
		var hitTimer:Timer;
		var gameState:Boolean = true;
		var nextLevel:Boolean = false;
		var theNewEnemy:Number;
		var theNewPeople:Number;
		var theDisapearTime:Number;
		var startButton:start = new start();
		var againButton:Again = new Again();


		public function main()
		{
			startGame();
		}

		//This function is that the start page of game
		public function startGame()
		{
			level = 1;
			this.theBack = new back();
			this.addChild(theBack);
			this.addChild(startButton);
			startButton.x = stage.stageWidth / 2;
			startButton.y = stage.stageHeight / 1.1;
			startButton.addEventListener(MouseEvent.CLICK,clickStart);
		}

		//This function is that show the rule of this game and click screen to continue
		public function clickStart(e:MouseEvent):void
		{
			this.removeChild(theBack);
			this.removeChild(startButton);
			this.theRule = new rule();
			this.addChild(theRule);
			theRule.addEventListener(MouseEvent.CLICK,showController);
		}

		//This function is that show how to play this game and click screen to continue
		public function showController(e:MouseEvent):void
		{
			this.removeChild(theRule);
			this.theControl = new control();
			this.addChild(theControl);
			theControl.addEventListener(MouseEvent.CLICK,showLevel);
		}

		//This function is that show the level of game 
		public function showLevel(e:MouseEvent):void
		{
			this.removeChild(theControl);
			gameState = true;
			levelDisplay();
		}


		//This function is that show the level of game and click to start to play game
		public function levelDisplay()
		{
			if (level==1)
			{
				this.theLevel = new level1();
			}
			else if (level ==2)
			{
				this.theLevel = new level2();
			}
			else if (level ==3)
			{
				this.theLevel = new level3();
			}
			else if (level ==4)
			{
				this.theLevel = new level4();
			}
			else if (level ==5)
			{
				this.theLevel = new level5();
			}

			this.addChild(theLevel);
			level_text.text = "Level " + String(level);
			theLevel.addEventListener(MouseEvent.CLICK,gameStart);
		}

		//This function is that start to play this game
		public function gameStart(e:MouseEvent):void
		{
			this.removeChild(theLevel);
			init();
		}

		//the initial function, this function will set some attributes, like time, object dispearing time, heart number and score
		public function init()
		{			
			if (level==1)
			{
				time = 20;
				heartNumber = 3;
				score = 0;
				theDisapearTime = 3000;

			}
			else
			{
				if (level==2)
				{
					time = 25;
					theDisapearTime = 2500;
				}
				else if (level==3)
				{
					time = 30;
					theDisapearTime = 2000;
				}
				else if (level==4)
				{
					time = 35;
					theDisapearTime = 1750;
				}
				else if (level==5)
				{
					time = 40;
					theDisapearTime = 1500;
				}

				score = int(Score_text.text);
				heartNumber = int(Heart_text.text);
			}
			
			this.setGun();
			this.setupAim();
			this.myTimer();
			Heart_text.text = String(heartNumber);
			Score_text.text = String(score);
			Timer_text.text = String(time);
			this.setNew();
		}



		//This function is that new object will appear after 1s. or if your red heart is 0, you will lose. or if you passed all levels, you
		//win this game. or if you finish this level, you will enter next level.
		public function setNew()
		{
			if (gameState)
			{
				newTimer = new Timer(1000,1);
				newTimer.addEventListener(TimerEvent.TIMER,newObject);
				newTimer.start();

			}
			else
			{
				stage.removeEventListener(MouseEvent.CLICK, shotSound);
				stage.removeEventListener(MouseEvent.MOUSE_DOWN, setGunFire);
				stage.removeEventListener(MouseEvent.MOUSE_UP, removeGunFire);				
				gameInfor.text = "";
				timer.stop();
				this.removeChild(theAim);
				this.removeChild(theGun);
				Mouse.show();

				if (nextLevel==false)
				{
					gameOver();
				}
				else
				{
					if (level>5)
					{
						gameOver();
					}
					else
					{
						gameState = true;
						levelDisplay();
					}
				}
			}
		}

		//This function is decide which object will appear using Math.random()
		public function newObject(e:TimerEvent):void
		{
			stage.addEventListener(MouseEvent.CLICK, shotSound);
			stage.addEventListener(MouseEvent.MOUSE_DOWN, setGunFire);
			stage.addEventListener(MouseEvent.MOUSE_UP, removeGunFire );
			
			if (Math.random() < 0.70)
			{
				newEnemy();
				object = 0;
			}
			else if (Math.random()>=.070 && Math.random()< 0.88)
			{
				newPeople();
				object = 1;
			}
			else
			{
				newHeart();
				object = 2;
			}

		}

		//This function is to create a new red head
		public function newHeart()
		{
			this.theHeart = new heart();
			var theNewObj:theNewObject = new theNewObject();
			theNewObj.setPlace(theHeart);
			this.addChild(theHeart);
			theHeart.addEventListener("objectDisappear",theObjectDisappearTime);
			theHeart.addEventListener(MouseEvent.CLICK,shoot);
			theHeart.buttonMode = true;
			theHeart.dispatchEvent(new Event("objectDisappear"));
		}

		//This function is to create a new people
		public function newPeople()
		{
			this.thePeople = new people();
			var theNewObj:theNewObject = new theNewObject();
			theNewObj.setPlace(thePeople);
			this.addChild(thePeople);
			thePeople.addEventListener("objectDisappear",theObjectDisappearTime);
			thePeople.addEventListener(MouseEvent.CLICK,shoot);
			thePeople.buttonMode = true;
			thePeople.dispatchEvent(new Event("objectDisappear"));

		}

		//This function is to create a new enemy
		public function newEnemy()
		{
			this.theEnemy = new enemy();
			var theNewObj:theNewObject = new theNewObject();
			theNewObj.setPlace(theEnemy);
			this.addChild(theEnemy);
			theEnemy.addEventListener("objectDisappear",theObjectDisappearTime);
			theEnemy.addEventListener(MouseEvent.CLICK,shoot);
			theEnemy.buttonMode = true;
			theEnemy.dispatchEvent(new Event("objectDisappear"));
		}


		//This function is that object will disappear after 1s
		public function theObjectDisappearTime(e:Event):void
		{
			objectDisappearTimer = new Timer(theDisapearTime,1);//object disapear time
			objectDisappearTimer.addEventListener(TimerEvent.TIMER,objectDisappear);
			objectDisappearTimer.start();
		}


		//This function is that make object disappear(enemy,people and red heart);
		public function objectDisappear(e:Event):void
		{

			objectDisappearTimer.removeEventListener(TimerEvent.TIMER,objectDisappear);
			if (object == 0)
			{
				theEnemy.removeEventListener("objectDisappear",theObjectDisappearTime);
				theEnemy.removeEventListener(MouseEvent.CLICK,shoot);
				theEnemy.buttonMode = false;
				this.theHit = new hit();
				this.addChild(theHit);
				gameInfor.text = "-1 Heard";
				RemoveText();
				hitTimer = new Timer(500);//hit u time
				hitTimer.addEventListener(TimerEvent.TIMER,hitYou);
				hitTimer.start();
			}
			else if (object == 1)
			{
				thePeople.removeEventListener("objectDisappear",theObjectDisappearTime);
				thePeople.removeEventListener(MouseEvent.CLICK,shoot);
				thePeople.buttonMode = false;
				this.removeChild(thePeople);
				setNew();
			}
			else if (object==2)
			{
				theHeart.removeEventListener("objectDisappear",theObjectDisappearTime);
				theHeart.removeEventListener(MouseEvent.CLICK,shoot);
				theHeart.buttonMode = false;
				this.removeChild(theHeart);
				setNew();
			}
		}

		//This function is that the enemy will hit you before he disappear, if your red heart is 0, you will game over
		public function hitYou(e:Event):void
		{
			hitTimer.removeEventListener(TimerEvent.TIMER,hitYou);
			Heart_text.text=String(int(Heart_text.text)-1);
			this.removeChild(theHit);
			this.removeChild(theEnemy);
			if (int(Heart_text.text)==0)
			{
				timer.stop();
				gameState = false;
				gotoAndStop(2);
				nextLevel = false;
			}
			setNew();
		}

		//This function is that if you shoot the object, the score(red heart) may change 
		public function shoot(e:Event):void
		{
			objectDisappearTimer.removeEventListener(TimerEvent.TIMER,objectDisappear);
			if (object == 0)
			{
				Score_text.text=String(int(Score_text.text)+10);
				gameInfor.text = "+10 points";
				RemoveText();
				
				theEnemy.rotation += -90;
				theEnemy.removeEventListener("objectDisappear",theObjectDisappearTime);
				theEnemy.removeEventListener(MouseEvent.CLICK,shoot);
				theEnemy.buttonMode = false;
				RemoveTimer();
			}
			else if (object == 1)
			{
				if (int(Score_text.text)>=20)
				{
					Score_text.text=String(int(Score_text.text)-20);
					gameInfor.text = "-20 points";
					RemoveText();
				}
				else
				{
					Score_text.text = "0";
					gameInfor.text = "Your score is less than 20";
					RemoveText();
					
				}

				thePeople.rotation +=  -90;
				thePeople.removeEventListener("objectDisappear",theObjectDisappearTime);
				thePeople.removeEventListener(MouseEvent.CLICK,shoot);
				thePeople.buttonMode = false;
				RemoveTimer();

			}
			else
			{
				if (int(Heart_text.text)<3)
				{
					Heart_text.text=String(int(Heart_text.text)+1);
					gameInfor.text = "+1 Heard";
					RemoveText();
				}
				else
				{
					gameInfor.text = "Your heart is full";
					RemoveText();
				}
				
				theHeart.rotation +=  -90;
				theHeart.removeEventListener("objectDisappear",theObjectDisappearTime);
				theHeart.removeEventListener(MouseEvent.CLICK,shoot);
				theHeart.buttonMode = false;
				RemoveTimer();
			}
			setNew();
		}
		
		//This function is that remove the game text after 0.8s
		public function RemoveText()
		{
			removeGameInfor = new Timer(800);
			removeGameInfor.addEventListener(TimerEvent.TIMER,removeText);
			removeGameInfor.start();
		}
		
		//This function is that remove the text and timer listener
		public function removeText(e:Event):void
		{
			removeGameInfor.removeEventListener(TimerEvent.TIMER,removeText);
			gameInfor.text="";
		}


		//This function is a timer, after .3s, the object will disapper after you shot them
		public function RemoveTimer()
		{
			removeTimer = new Timer(300);
			removeTimer.addEventListener(TimerEvent.TIMER,removeObject);
			removeTimer.start();
		}

		//This fcuntion is that remove the objec and timer listener 
		public function removeObject(e:Event):void
		{

			removeTimer.removeEventListener(TimerEvent.TIMER,removeObject);

			if (object==0)
			{
				this.removeChild(theEnemy);
			}
			else if (object==1)
			{
				this.removeChild(thePeople);
			}
			else if (object==2)
			{
				this.removeChild(theHeart);
			}

		}

		//This function create an aim and hide the mouse
		public function setupAim()
		{

			this.theAim = new aim();
			theAim.x = stage.stageWidth / 2;
			theAim.y = stage.stageHeight / 2;
			this.addChild(theAim);
			Mouse.hide();
			stage.addEventListener(MouseEvent.MOUSE_MOVE,aimMove);


		}

		//This function is that you can control the aim by mouse
		public function aimMove(e:MouseEvent):void
		{
			theAim.x = this.mouseX;
			theAim.y = this.mouseY;

		}
		
		//This function is that set up the gun and add a listener to mouse move
		public function setGun()
		{
			this.theGun = new gun();
			theGun.x = 510;
			theGun.y = 715;	
			this.addChild(theGun);
			stage.addEventListener(MouseEvent.MOUSE_MOVE,gunMove);
		}
		
		//This function is that the gun will move x with mouse move
		public function gunMove(e:MouseEvent):void
		{
			
			theGun.x = this.mouseX;
			theGun.y = 715;	
		}

		//This function is that show shot sound when you click the mouse
		public function shotSound(e:MouseEvent):void
		{
			var sound:gunSound = new gunSound();
			sound.play();
		}
		
		//This function is that show the gun fire when you mouse down
		public function setGunFire(e:MouseEvent):void
		{
			theGunFire = new gunFire();
			theGunFire.x = this.mouseX-5;
			theGunFire.y = 660;
			this.addChild(theGunFire);
		}

		//This function is that hide the gun fire when you mouse up
		public function removeGunFire(e:MouseEvent):void
		{
			this.removeChild(theGunFire);
		}

		//This function is a timer, it will count to 0
		public function myTimer():void
		{

			timer = new Timer(1000);
			timer.addEventListener(TimerEvent.TIMER,countDown);
			timer.start();
		}

		//This is a count down function, if timer count to 0, player can enter next level
		public function countDown(event:TimerEvent):void
		{
			time--;
			Timer_text.text = String(time);
			if (time==0)
			{
				timer.stop();
				gameState = false;
				gotoAndStop(2);
				level++;
				nextLevel = true;
			}
		}

		//This function is that if you lose all red hearts and passed all levels, you will be game over.
		//This function will show the text "You Win" or "Game Over"， the score and again button
		public function gameOver()
		{
			this.theGameOver = new gameover();
			this.addChild(theGameOver);
			this.addChild(againButton);
			final_text.text = "Your Score: " + String(int(Score_text.text));
			this.addChild(final_text);
			if (level >5)
			{
				gameover_text.text = "You Win";
			}
			else
			{
				gameover_text.text = "Game Over";
			}

			this.addChild(gameover_text);
			againButton.x = stage.stageWidth / 2;
			againButton.y = stage.stageHeight / 1.3;
			againButton.addEventListener(MouseEvent.CLICK,clickAgain);
		}

		//This function is that if you are game over, you can click again button to play again
		public function clickAgain(e:MouseEvent):void
		{
			this.removeChild(theGameOver);
			this.removeChild(againButton);
			this.removeChild(final_text);
			this.removeChild(gameover_text);
			startGame();
		}
	}
}
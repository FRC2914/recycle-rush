#include "FastLED.h"

#define NUM_LEDS 80
#define MAX_BRIGHTNESS 50
//pins:
#define DATA_PIN 11
#define CLOCK_PIN 13

//modes:
#define FREEDOM 110
#define LIBERTY 120
#define REDBEAM 130
#define CANDY 140
#define RAINBOW 150
#define PATRIOT 160
#define DEPLETE 170
#define GREEN  180

//animations
#define CYLONUP    1
#define CYLONDOWN  2
#define FLASHRED   3
#define FLASHGREEN 4

//macros
#define LENGTH(x)  (sizeof(x) / sizeof(x[0]))

CRGB leds[NUM_LEDS];
//CRGB stateLeds[NUM_LEDS];
int mode = PATRIOT;
int animation = 0;
int wait = 10;
int lastMode = 0;
int counter = 0;
int darkness = 0;
void setup()
{
  FastLED.addLeds<WS2801, DATA_PIN, CLOCK_PIN, RGB>(leds, NUM_LEDS);
  Serial.begin(9600);
}

void loop(){
  if (Serial.available()){
    byte b = Serial.read();
    Serial.println(b);
    if ( b > 100 && b < 200){
      mode = b;
    }
    else if ( b > 0 && b < 100){
      animation = b;
      //for (int i = 0; i < NUM_LEDS; i++){
        //stateLeds[i] = leds[i];
      //}
      switch(animation){
        case CYLONUP://starts at ends
          cylonUp(CRGB::Red, 20);
        break;
        case CYLONDOWN://starts in centre
          cylonDown(CRGB::Red, 20);
        break;
        case FLASHRED: //all to red for 100 ms
          setAll(CRGB::Red);
          FastLED.show();
          delay(1000);
        break;
        case FLASHGREEN: //all to green for 100 ms
          setAll(CRGB::Green);
          FastLED.show();
          delay(1000);
        break;
       }
       counter = 0;
       //for (int i = 0; i < NUM_LEDS; i++){
        //leds[i] = stateLeds[i];
      //}
     }
  }
    if(lastMode != mode)
    {
      counter = 0;
      lastMode = mode;
      Serial.println("mode changed");
    }
    
     switch(mode){
      case REDBEAM: // Red beam quickly goes back and forth
        cylon(CRGB::Red,wait);
      break;
      case CANDY: //dispays random set of changing light colors
        randomColor();
      break;
      case RAINBOW: //displays transitioning primary colors
        rainbow(0);
      break;
      case FREEDOM: //displays blue with alternating red and white
        flag();
      break;
      case LIBERTY: //moving red white and blue, nothing happens
        banner(counter);
      break;
      case PATRIOT:// moving red white and blue
      patriot();
      break;
      case DEPLETE: //starts full, depletes from center to ends
        depleting(CRGB::Red,CRGB::Black,15000*2);
      break;
      case GREEN: //starts full, depletes from center to ends
        setAll(CRGB::Green);
         FastLED.show();
      break;
  }
  counter++;

}

void banner(int index){
  FastLED.setBrightness(255);
  int blocksize = 3;
  for(int led = 0; led <= NUM_LEDS; led+=blocksize){//pretend there are 81 LEDs but don't write the last one.  Helps with block size 3
    //define color for block
    CRGB color;
    switch((led*2/3)%3){
      case 0:
        color = CRGB(255,0,0);
        break;
      case 1:
        color = CRGB(255,255,255);
        break;
       case 2:
        color = CRGB(0,0,255);
        break;
    }
    //set block
    for(int i = 0; i < blocksize; i++){
      int ledIndexToIlluminate = ((led+i+index)%81);
      if(ledIndexToIlluminate<80)//ignore the final LED
        leds[ledIndexToIlluminate] = color;
    }
  }
  delay(250);
  FastLED.show();
}

void flag(){
  FastLED.setBrightness(MAX_BRIGHTNESS);
  for(int led = 0; led < 20; led++){
    leds[led] = CRGB(0,0,255);
  }
  for(int led = 20; led < NUM_LEDS; led+=4){
    leds[led] = CRGB(255,0,0);
    leds[led+1] = CRGB(255,0,0);
    leds[led+2] = CRGB(255,255,255);
    leds[led+3] = CRGB(255,255,255);
  }
  FastLED.show();
}

void randomColor()
{
  FastLED.setBrightness(MAX_BRIGHTNESS);
  for(int led = 0; led < NUM_LEDS; led++){
    leds[led] = CHSV(random(0,255),200,255);
  }
  FastLED.show();
}

void cylon(CRGB color, int wait){
  FastLED.setBrightness(MAX_BRIGHTNESS);
  int led = 0;
  if (counter % 2 == 0){
    for(; led < NUM_LEDS; led++){
      leds[led] = color;
      FastLED.show();
      leds[led] = CRGB::Black;
      delay(wait);
    }
  }
  else{
    for(;led >= 0; led--){
      leds[led] = color;
      FastLED.show();
      leds[led] = CRGB::Black;
      delay(wait);
    }
  }
  
}

void cylonUp(CRGB color, int wait){
  FastLED.setBrightness(MAX_BRIGHTNESS);
  int led = 0;
  setAll(CRGB::Black);
   for(; led < NUM_LEDS/2; led++){
     leds[led] = color;
     leds[abs(led-80)] = color;
     FastLED.show();
     leds[led] = CRGB::Black;
     leds[abs(led-80)] = CRGB::Black;
     delay(wait);
  }
  Serial.println("exit");
}
void cylonDown(CRGB color, int wait){
  FastLED.setBrightness(MAX_BRIGHTNESS);
   setAll(CRGB::Black);
  int led = NUM_LEDS/2;
   for(; led > 0; led--){
     leds[led] = color;
     leds[abs(led-80)] = color;
     FastLED.show();
     leds[led] = CRGB::Black;
     leds[abs(led-80)] = CRGB::Black;
     delay(wait);
  
  }
  Serial.println("exit");
}

void rainbow(int offset)
{
  FastLED.setBrightness(MAX_BRIGHTNESS);
  for(int led = 0; led < NUM_LEDS; led++){
    leds[led] = CHSV((255/NUM_LEDS)*((led+offset)%NUM_LEDS), 200, 255);
  }
  FastLED.show();
}

void patriot(){
  FastLED.setBrightness(MAX_BRIGHTNESS);
  CRGB color;
  counter = counter%(NUM_LEDS*63);
  switch((counter/NUM_LEDS)%3){
    case 0:
      color = CRGB(255,0,0);
    break;
    case 1:
      color = CRGB(255,255,255);
    break;
    case 2:
      color = CRGB(0,0,255);
    break;
  }
  if((counter/80)%2 == 0){
    leds[counter%NUM_LEDS] = color;
  }else{
    leds[NUM_LEDS - (counter)%80 - 1] = color;
  }
  FastLED.show();
  delay(wait);                                      
}

void setAll(CRGB color){
  for(int i = 0; i < NUM_LEDS; i++){
    leds[i] = color;
  }
}

void depleting(CRGB full, CRGB empty, int time){
  int idelay = time/NUM_LEDS;
  int centerN = NUM_LEDS/2 - 1;
  int centerP = NUM_LEDS/2;
  if(counter%centerP == 0){
     setAll(full);
   }

  FastLED.show();
  

    leds[centerP + counter%centerP] = empty;
    leds[centerN - counter%centerP] = empty;
    
    FastLED.show();
    
    delay(idelay);
  
}


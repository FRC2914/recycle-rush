#include "FastLED.h"

#define NUM_LEDS 80
#define MAX_BRIGHTNESS 255
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
#define FADE 2

CRGB leds[NUM_LEDS];
int mode = PATRIOT;
int wait = 10;
int lastMode = 0;
int counter = 0;
int darkness = 0;
void setup()
{
  FastLED.addLeds<WS2801, DATA_PIN, CLOCK_PIN, RGB>(leds, NUM_LEDS);
  Serial.begin(9600);
}

void loop()
{
//  if (Serial.available()){
//    byte b = Serial.read();
//    if ( b > 100 && b < 200){
//      b = mode;
//      for( int i = 0; i<=255; i++){
//        darkness = darkness + FADE;
//        banner(counter++);
//       }
//    }
// }
//    if(mode != lastMode){
//      counter = 0;
//      lastMode = mode;
//    }
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
    case LIBERTY: // moving red white and blue
      banner(counter++);
    break;
    case PATRIOT:
      patriot();
    break;
  }
  counter++;

}

void banner(int index){
  FastLED.setBrightness(darkness);
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

void cylon(CRGB color, int wait)
{
  FastLED.setBrightness(MAX_BRIGHTNESS);
  int led = 0;
  for(; led < NUM_LEDS; led++){
    leds[led] = color;
    FastLED.show();
    leds[led] = CRGB::Black;
    delay(wait);
  }
  for(;led >= 0; led--){
    leds[led] = color;
    FastLED.show();
    leds[led] = CRGB::Black;
    delay(wait);
  }
  
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
    Serial.println(NUM_LEDS - (counter)%80);
  }
  FastLED.show();
  delay(wait);                                      
}

void setAll(CRGB color){
  for(int i = 0; i < NUM_LEDS; i++){
    leds[i] = color;
  }
}

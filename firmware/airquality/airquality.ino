
#include <RFduinoBLE.h>

#define CHILD_ID_AIQ 0
#define AIQ_SENSOR_ANALOG_PIN 0

#define MQ135_DEFAULTPPM 399 //default ppm of CO2 for calibration
#define MQ135_DEFAULTRO 68550 //default Ro for MQ135_DEFAULTPPM ppm of CO2
#define MQ135_SCALINGFACTOR 116.6020682 //CO2 gas value
#define MQ135_EXPONENT -2.769034857 //CO2 gas value
#define MQ135_MAXRSRO 2.428 //for CO2
#define MQ135_MINRSRO 0.358 //for CO2

unsigned long SLEEP_TIME = 3000; // Sleep time between reads (in seconds)
//VARIABLES
//float Ro = 10000.0;    // this has to be tuned 10K Ohm
float mq135_ro = 10000.0;    // this has to be tuned 10K Ohm
int val = 0;           // variable to store the value coming from the sensor
float valAIQ =0.0;
float lastAIQ =0.0;
//
int sensor = 2;

void setup() {
  //Pin mode to input for reading sensor MQ 135 data
  pinMode(sensor, INPUT_PULLUP);
  // initialize serial:
  Serial.begin(9600);
  RFduinoBLE.advertisementData = "FreshFreedom";
  Serial.println("**Starting FreshFreedom**");
  // start the BLE stack
  RFduinoBLE.begin();
}

/*
 * get the calibrated ro based upon read resistance, and a know ppm
 */
long mq135_getro(long resvalue, double ppm) {
return (long)(resvalue * exp( log(MQ135_SCALINGFACTOR/ppm) / MQ135_EXPONENT ));
}

/*
 * get the ppm concentration
 */
double mq135_getppm(long resvalue, long ro) {
double ret = 0;
double validinterval = 0;
validinterval = resvalue/(double)ro;
if(validinterval<MQ135_MAXRSRO && validinterval>MQ135_MINRSRO) {
ret = (double)MQ135_SCALINGFACTOR * pow( ((double)resvalue/ro), MQ135_EXPONENT);
}
return ret;
}
void loop() {
  
  uint16_t sensorValue = analogRead(sensor);
  Serial.print("sensorValue:");
  Serial.println(sensorValue);
  uint16_t val =  ((float)47000*(1023-sensorValue)/sensorValue); 
  Serial.print("value:");
  Serial.println(val);
  mq135_ro = mq135_getro(val, MQ135_DEFAULTPPM);
  Serial.print("getro:");
  Serial.println(mq135_ro);
  //convert to ppm (using default ro)
  valAIQ = mq135_getppm(val, MQ135_DEFAULTRO);
  // send the sample to the Android
  Serial.print("getppm:");
  Serial.println(valAIQ);
  if (valAIQ != lastAIQ) {
      lastAIQ = ceil(valAIQ);
      if(lastAIQ!=0.0){
        RFduinoBLE.sendInt(lastAIQ);
      }
      Serial.print("valAIQ:");
      Serial.println(lastAIQ);
  } 

  delay(SLEEP_TIME);
}


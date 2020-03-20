Android Coding Challenge
Our application collects real time location data, and processes that data.  We typically collect GPS location data at a rate of 1 sample per second, while in a moving vehicle.  The only way to test our application on a real Android device, aside from getting in a moving vehicle, or using our GPS test chamber, is to use what is called a mock location provider.  

https://developer.android.com/reference/android/location/LocationManager#addTestProvider(java.lang.String,%20boolean,%20boolean,%20boolean,%20boolean,%20boolean,%20boolean,%20boolean,%20int,%20int)

Take a look at the LocationManager methods involving test providers and use them to implement an Android app which simulates a trip in a moving vehicle.  You can use the android emulator included with Android Studio, which has google maps installed.  

I have included a video of an implementation of this app.  This is a very poor implementation with a bad user experience.  You should strive to use a clean material design.  Hopefully your implementation is an improvement! 

I have also included a set of location data.  This is at a frequency of one location sample per second.  The format is lattitude, longitue, heading, speed, and accuracy.  Use this to generate your mock locations.  This route is right by our office in Baton Rouge.  Check out all the great places to eat lunch!

Good Luck! 

Exercise 3: Move the plane
==========================

Connect the control wheel of the plane to the orientation sensor of the device.

A decorator for the Android orientation sensor (be.cegeka.android.rx.infrastructure.RotationSensorImpl) is already given.
Please have a look at this class to see a way to make an Android Sensor available as a Stream from events. The sensor will start emitting items when an observer subscribes.
When the last observer unsubscribes the sensor will stop.
Have a look at Subjects to implement the sensor decorator (https://github.com/ReactiveX/RxJava/wiki/Subject).

The tasks you need to complete in this exercise are:

    * Have a look at the RotationSensorImpl
    * Convert the stream of degrees to a stream of directions the plane needs to move (be.cegeka.android.rx.domain.ControlWheel)
    * Given a start position of the plane, calculate the correct position of plane and see the plane moves when you rotate your device
    * Make the build green
    
HINT: use the scan function to calculate the correct position of the plane:
https://github.com/ReactiveX/RxJava/wiki/Transforming-Observables#scan
    
    


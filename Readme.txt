AsteroidShooting
================

This application should be compatible with all devices running android 2.3.3 and higher.
This application is licensed under GPLv3.

Thanks to Jasper den Ouden for the Asteroid image.
Thanks to Dravenx for the Space ship image.
Thanks to Marvin for the shooting sound.
The font used in game is GNU Free Mono licensed under GPLv3 available at http://gnu.org/licenses/gpl.html

Author: Joshua Bowren

Building
========
Run:

android update project -p . -t n

Where n is the target android version you want to use, use android list targets to view all targets. Use 21 for android 4.4

Then run:

ant debug

or

ant release

This builds an android package. Debug makes the installation easier as you do not need to sign the application.

To install to your device run

ant installd

or

ant installr

d for debug and r for release.

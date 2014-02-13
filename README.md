SeeUV
=====

A simple textured 3d model viewer. Click and drag to rotate around the model, and use the scroll wheel to zoom. If the provided texture file is modified, SeeUV will automatically detect this and reload the texture.

To compile, use the provided ant script. A platform independent JAR will be deposited in the `dist` directory along with a series of platform specific binaries for LWJGL support.

SeeUV can be run from the command line like so:

    java -jar SeeUV.jar <model.obj> <texture.png>

This tool is still under development, so expect some bugs and instability.

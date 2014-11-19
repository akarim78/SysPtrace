SysPtrace
=========

Our approach:
--------------------
We create a ptrace program which tracks the system calls using a while loop and repeatedly calls the "ptrace" system calls using appropriate parameters. We get the system call number by reading the values from the register and get the corresponding names. We count how many times each system call is called and dump the count and the system call name into a file which has the "package name" of the app being traced as its filename. We store this file on the sdcard of the device. To compile the ptrace.c file, we cross compile it using the NDK so that the binary runs on the ARM device.

The java code obtains root access and calls the ptrace binary that we just created to trace an application. If we want to stop tracing, we signal the ptrace c code to break out of the while loop. Once we break out of the while loop the file is created by the ptrace utility. We read this file in the java code and display the results in the UI.

Usgae of ptrace utility:
------------------------
The ptrace utitlity can be run in the adb shell using the command:
ptrace -p <processId> -f <packagename>
- processid is the pid of the application to be traced and
- packagename is the name of the application package.

Device Constraints:
-------------------
Device should have root access.
A ptrace binary has to be copied to the device's /system/bin/ folder.
The utility works on ARM phones only.

Instructions:
-------------
To compile ptrace program for ARM architecture, you'll need to cross-compile it. Follow instructions below for mac OS:
1. Download NDK from Android developer site
2. Set a path variable for the NDK in :
	export ANDROID_NDK="/Users/jeffwilliams/ndk/android-ndk-r10c"

3. Set environment variables for cross compiling (for ARM):
export NDK_TOOLCHAIN=$ANDROID_NDK/toolchains/arm-linux-androideabi-4.6/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-
export NDK_SYSROOT=$ANDROID_NDK/platforms/android-16/arch-arm

4. Compile:
make CC=${NDK_TOOLCHAIN}gcc CFLAGS=--sysroot=${NDK_SYSROOT} ptrace

5. Connect the device to the dev machine

6. Copy the ptrace binary using adb shell to /system/bin directory of the device

7. If /system/bin isread-only remount it as RW and then copy

8. Run the SysPtrace application in the device.

References:
-----------
http://www.linuxjournal.com/node/6100/
http://syscalls.kernelgrok.com/
http://yaapb.wordpress.com/2012/09/22/build-a-custom-android-emulator-image/
http://www.wikihow.com/Root-the-Samsung-Galaxy-Nexus-(I9250)
http://man7.org/linux/man-pages/man2/ptrace.2.html
http://www.secretmango.com/jimb/Whitepapers/ptrace/ptrace.html
https://blog.nelhage.com/2010/08/write-yourself-an-strace-in-70-lines-of-code/


/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#include <string.h>
#include <jni.h>
#include <sys/ptrace.h>
#include <sys/wait.h>
#include <unistd.h>
#include <android/log.h>
#include <sys/user.h>
#include<sys/errno.h>

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
jstring Java_com_example_hellojni_HelloJni_stringFromJNI(JNIEnv* env,
		jobject thiz) {
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a/NEON (hard-float)"
#else
#define ABI "armeabi-v7a/NEON"
#endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

	long ret = execl("/bin/ls", "ls", NULL);
	__android_log_print(ANDROID_LOG_INFO, "JNI Test", "execl returns %ld", ret);
//	pid_t child;
//	long orig_eax;
//	child = fork();
//	if (child == 0) {
//		long val = ptrace(PTRACE_TRACEME, 0, NULL, NULL);
//		execl("/bin/ls", "ls", NULL);
//		__android_log_print(ANDROID_LOG_INFO, "JNI Test", "Child is 0 %ld",
//				val);
//	} else {
//		wait(NULL);
//		long val = ptrace(PTRACE_TRACEME, 0, NULL, NULL);
//		execl("/bin/ls", "ls", NULL);
//		__android_log_print(ANDROID_LOG_INFO, "JNI Test", "Child is not 0 %ld",
//				val);
//	}

	return (*env)->NewStringUTF(env,
			"Hello from JNI !  Compiled with ABI " ABI ".");

}

int Java_com_example_hellojni_HelloJni_startProcess(JNIEnv* env, jobject thiz,
		int pid) {
//	__android_log_print(ANDROID_LOG_INFO, "Start Process",
//			"Starting package: %s", pname);
//	pid_t child;
	struct user_regs regs;
	long val = -2;
	errno = -1;
	int status = 0;
	val = ptrace(PTRACE_ATTACH, pid, 0, 0);
	wait(&status);
	__android_log_print(ANDROID_LOG_INFO, "JNI Test",
			"execl returns %ld %ld %d", val, errno, status);
//	sleep(10);
//	val=-1;
//	errno=-1;
//	val=ptrace(PTRACE_GETFPREGS, pid, NULL, &regs);
//	__android_log_print(ANDROID_LOG_INFO, "JNI Test", "execl returns %ld %ld",
//							val,errno);
//	sleep(10);
//	val=errno=-1;
//	val=ptrace(PTRACE_PEEKUSER, pid, regs.uregs[7], NULL);
//	__android_log_print(ANDROID_LOG_INFO, "JNI Test", "execl returns %ld %ld",
//							val,errno);
//
//	int ret1 =
//				execl(
//						"su", NULL);
//	__android_log_print(ANDROID_LOG_INFO, "JNI Test", "Child is NOT 0 %d", ret1);
//	int ret =
//			execl("./strace -p 32364",
//					(char *) NULL);
//	__android_log_print(ANDROID_LOG_INFO, "JNI Test", "Child is NOT 0 %d", ret);
//	long ret = system("/system/bin/sh -c /test.sh");
//	__android_log_print(ANDROID_LOG_INFO, "JNI Test", "execl returns %ld",
//					ret);
//	if(!fork()){
////		long ret = execl("/system/bin/ls", "ls", NULL);
////		long ret = system("sh /test.sh");
////		__android_log_print(ANDROID_LOG_INFO, "JNI Test", "execl returns %ld",
////						ret);
//	}else{
//
//	}
//	long orig_eax;
//	child = fork();
//	if (child == 0) {
//		long val = ptrace(PTRACE_TRACEME, 0, NULL, NULL);
//		// am start -n com.package.name/com.package.name.ActivityName
//
//		__android_log_print(ANDROID_LOG_INFO, "JNI Test", "Child is 0 %ld",
//				val);
//	} else {
//		long val = ptrace(PTRACE_TRACEME, 0, NULL, NULL);
//		// am start -n com.package.name/com.package.name.ActivityName
//		execl(
//						"./strace am start -a android.intent.action.MAIN -n com.android.settings/.Settings",
//						"am", NULL);
////		execl(
////				"am start -a android.intent.action.MAIN -n com.android.settings/.Settings",
////				"am", NULL);
//		__android_log_print(ANDROID_LOG_INFO, "JNI Test", "Child is NOT 0 %ld",
//				val);
//	}

	return 0;

}

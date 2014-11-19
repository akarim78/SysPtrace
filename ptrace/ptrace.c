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
#include<unistd.h>
#include<sys/ptrace.h>
#include <sys/wait.h>
#include<sys/user.h>
#include<sys/errno.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/stat.h>
#include<sys/types.h>
#include "syscall_names.c"
#define TOTAL_SYS_CALL_COUNT 337
const int long_size = sizeof(long);

void getdata(pid_t child, long addr,
             char *str, int len)
{   char *laddr;
    int i, j;
    union u {
            long val;
            char chars[long_size];
    }data;
    i = 0;
    j = len / long_size;
    laddr = str;
    while(i < j) {
        data.val = ptrace(PTRACE_PEEKDATA,
                          child, addr + i * 4,
                          NULL);
        memcpy(laddr, data.chars, long_size);
        ++i;
        laddr += long_size;
    }
    j = len % long_size;
    if(j != 0) {
        data.val = ptrace(PTRACE_PEEKDATA,
                          child, addr + i * 4,
                          NULL);
        memcpy(laddr, data.chars, j);
    }
    str[len] = '\0';
}

long callPtrace(int pid, char* filename ) {

	int syscall_count[350];
	struct user_regs regs;
	long ins;
	char buf[256];
	char buf2[256];
	memset(buf2, '\0', 256);
	memset(buf,'\0',256);
	struct stat sts;
	sprintf(buf,"/proc/%ld",pid);
	
	ins = ptrace(PTRACE_ATTACH, pid, NULL, NULL);
	int count = 0;
	char* str;

	if(ins==0) {

		int i=0;
		for(i=0;i<=TOTAL_SYS_CALL_COUNT;i++) {
			syscall_count[i]=0;
		}

		sprintf(buf2, "/mnt/sdcard/sysfiles/%s", filename);
		FILE * pFile;
   		int n;
   		char name [100];
		
   		
		while(1){
			int status;

			waitpid(pid,&status,0);
			if(ins >= 0){
				ins=ptrace(PTRACE_GETREGS, pid, NULL, &regs);
				if(errno == 3) break;
				if(regs.uregs[7] <= TOTAL_SYS_CALL_COUNT){
					long sys_call_number= regs.uregs[7];
					syscall_count[sys_call_number]++;
					//fprintf(pFile, "%ld %s\n",sys_call_number, syscall_names[sys_call_number]);
				
				}

	    		}
			ptrace(PTRACE_SYSCALL,pid, NULL, NULL);
	
		}

		pFile = fopen (buf2,"w");
		for(i=0;i<=TOTAL_SYS_CALL_COUNT;i++) {
			if(syscall_count[i]>0)
				fprintf(pFile, "%ld %s %d\n",i, syscall_names[i],syscall_count[i]);
		}

		fclose(pFile);
	}
	return 0;
}

int main(int argc, char** argv) {
	char *cvalue = NULL;
	char *fvalue = NULL;
	int c;

	while ((c = getopt (argc, argv, "p:f:")) != -1) {
	    switch (c){
	      	case 'p':
	    		cvalue = optarg;
	        	//callPtrace(atoi(cvalue));
	        	break;
		case 'f':
			fvalue = optarg;
			break;
		}
	
	}
	printf("%s %s\n", cvalue, fvalue);
	callPtrace(atoi(cvalue), fvalue);

	return 0;
}



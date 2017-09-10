/*
 *     ______  _____  ___  ______  ______  _______  __
 *    / __/ / / / _ \/ _ \/ __/  |/  / _ |/ ___/\ \/ /
 *   _\ \/ /_/ / ___/ , _/ _// /|_/ / __ / /__   \  /
 *  /___/\____/_/  /_/|_/___/_/  /_/_/ |_\___/   /_/.org
 *
 *                 Software Supremacy
 *                 www.supremacy.org
 *
 * Copyright (c) 2017 Supremacy Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

#include <jni.h>
#include <sys/vfs.h>
#include <string.h>
#include <errno.h>

extern "C" {

    JNIEXPORT jobject JNICALL
    Java_org_supremacy_spacewidget_filesystem_FileSystemStatisticsServer_getFilesystemInfo(JNIEnv *env,
                                                                                           jclass type,
                                                                                           jstring path_) {
        const char *path = env->GetStringUTFChars(path_, 0);
        struct statfs stats;

        if (statfs(path, &stats) != 0) {
            jclass exception = env->FindClass("org/supremacy/spacewidget/filesystem/FileSystemStatisticsException");
            env->ThrowNew(exception, strerror(errno));
            return NULL;
        }

        env->ReleaseStringUTFChars(path_, path);

        jclass cls = env->FindClass("org/supremacy/spacewidget/filesystem/FileSystemStatistics");
        jobject ret = env->AllocObject(cls);

        jfieldID  blockSize = env->GetFieldID(cls, "blockSize", "J");
        jfieldID  totalBlocks = env->GetFieldID(cls, "totalBlocks", "J");
        jfieldID  freeBlocks = env->GetFieldID(cls, "freeBlocks", "J");

        env->SetLongField(ret, blockSize, stats.f_bsize);
        env->SetLongField(ret, totalBlocks, stats.f_blocks);
        env->SetLongField(ret, freeBlocks, stats.f_bfree);

        return ret;
    }
}
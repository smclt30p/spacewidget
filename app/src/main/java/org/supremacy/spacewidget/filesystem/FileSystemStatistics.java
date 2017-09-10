package org.supremacy.spacewidget.filesystem;

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

/**
 * This class represents a partial statfs Linux struct
 * that can be found in the sys/vfs.h header file.
 *
 * This class contains basic data about a file system
 * on a given path. This class is not directly written to
 * by Java, but the values are assigned via the C native interface.
 */
public class FileSystemStatistics {

    /**
     * The block size of the underlying filesystem.
     * Usually 512B for FAT and 4096B for EXT filesystems.
     *
     * This value is assigned via JNI
     */
    private long blockSize;

    /**
     * Total number of blocks inside the filesystem.
     *
     * This value is assigned via JNI
     */
    private long totalBlocks;

    /**
     * The number of free storage blocks on the filesystem.
     *
     * This value is assigned via JNI
     */
    private long freeBlocks;

    /**
     * Get the free space on the file system in bytes.
     */
    public long getFreeSpaceBytes() {
        return freeBlocks * blockSize;
    }

    /**
     * Get the total space on the file system in bytes.
     */
    public long getTotalSpaceBytes() {
        return totalBlocks * blockSize;
    }

    /**
     * Gets the percentage ratio between used and free space.
     */
    public int getFreeSpacePercentage() {
        double free = (double) getFreeSpaceBytes();
        double total = (double) getTotalSpaceBytes();
        double ratio = (free / total) * 100;
        return (int) ratio;
    }
}